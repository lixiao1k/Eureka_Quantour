package data.fetchdataimpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import data.common.FileMethod;
import data.common.WebMethod;
import data.datahelperimpl.InitEnvironment;
import exception.InternetdisconnectException;
import exception.NoneMatchedMarketException;

public class StockInfoFetchByWeb {
	private String stockroot;
	private FileMethod filemethod;
	private WebMethod webmethod;
	private SimpleDateFormat sdf;
	private File log;
	private File rightslog;
	private File mainData;
	private File mainPosition;
	private File mainIndex;
	private Properties pro1;
	private Properties pro2;
	private DecimalFormat df1;
	private DecimalFormat df2;
	private long total;
	
	private InitEnvironment ie;
	public StockInfoFetchByWeb(){
		ie=InitEnvironment.getInstance();
		df1 = new DecimalFormat("0.00");
		df2 = new DecimalFormat("0.0000");
		sdf=new SimpleDateFormat("yyyy-MM-dd");
		stockroot=ie.getPath("stockinfo");
		String resources=ie.getPath("resources");
		log=new File("config/stocklog");
		rightslog=new File("config/rightslog");
		mainData=new File(resources+"/mainData");
		mainPosition=new File(resources+"/mainPosition");
		mainIndex=new File(resources+"/mainIndex");
		try {
			log.createNewFile();
			rightslog.createNewFile();
			if(!mainData.exists()){
				mainData.createNewFile();
				mainPosition.createNewFile();
				mainIndex.createNewFile();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		total=mainData.length();
		filemethod=FileMethod.getInstance();
		webmethod=WebMethod.getInstance();
		filemethod.makepath(stockroot);
	}
	/**
	 * 获取股票信息
	 * @throws InternetdisconnectException 网络无法链接时抛出异常
	 */
	public void fetchAllStockInfo() throws InternetdisconnectException{
		webmethod.testInternet();
		File root=new File(stockroot);
		String[] stocklist=root.list();
		int i=stocklist.length;
		int count=0;
		Calendar cal=Calendar.getInstance();
		String enddate=sdf.format(cal.getTime());
		long time=System.currentTimeMillis();
		for(String stock:stocklist){
			count++;
			System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
			fetchdate(stock,enddate);
		}
		long time1=System.currentTimeMillis();
		System.out.println("花费时间：" +(time1-time)+"ms");
	}
	/**
	 * 获取所有股票的复权价格
	 * @throws InternetdisconnectException 网络无法链接时抛出异常
	 */
	public void fetchAllsubscription() throws InternetdisconnectException{
		webmethod.testInternet();
		File root=new File(stockroot);
		String[] stocklist=root.list();
		int i=stocklist.length;
		pro1=new Properties();
		pro2=new Properties();
		try {
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream("config/parse/codeToname.properties"));
			pro1.load(is);
			is.close();
			BufferedInputStream is1 = new BufferedInputStream(
					new FileInputStream("config/parse/nameTocode.properties"));
			pro2.load(is1);
			is1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int count=0;
		Calendar cal=Calendar.getInstance();
		String enddate=sdf.format(cal.getTime());
		for(String stock:stocklist){
			count++;
			System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
			processAdjClose(enddate,stock);
		}
		try {
			OutputStream out=new FileOutputStream("config/parse/codeToname.properties");
			OutputStream out1=new FileOutputStream("config/parse/nameTocode.properties");
			pro1.store(out, "update rights_url");
			pro2.store(out1, "update rights_url");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 汇总所有新添加的数据到主文件中，并且生成索引文件
	 */
	public void indexationAllDate(boolean reset){
		File root=new File(stockroot);
		int count=0;
		String[] list=root.list();
		int i=list.length;
		try{
			BufferedWriter bw_data=new BufferedWriter(new FileWriter(mainData,reset));
			BufferedWriter bw_index=new BufferedWriter(new FileWriter(mainIndex,reset));
			BufferedWriter bw_position=new BufferedWriter(new FileWriter(mainPosition,reset));
			for(String code:list){
				count++;
				System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
				indexationStockInfo(code,bw_data,bw_index,bw_position);
			}
			bw_data.close();
			bw_index.close();
			bw_position.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 汇总某支股票的新增数据到主文件中，并添加索引文件
	 * @param code
	 * @param bw_position 
	 * @param bw_index 
	 * @param bw_data 
	 */
	private void indexationStockInfo(String code, BufferedWriter bw_data, BufferedWriter bw_index, BufferedWriter bw_position){
		Properties pro=new Properties();
		String codepath=stockroot+"/"+code+"/";
		try  {
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream(codepath+"config.properties"));
			pro.load(is);
			is.close();
			if(pro.containsKey("last_day")){
				if(pro.containsKey("last_indexationDate")){
					int dateday=Integer.parseInt(encodeDate(pro.getProperty("last_day")));
					int indexday=Integer.parseInt(encodeDate(pro.getProperty("last_indexationDate")));
					if(dateday>indexday){
						gatherDate(code,indexday,bw_data,bw_index,bw_position);
					}
				}
				else{
					gatherDate(code,-1,bw_data,bw_index,bw_position);
				}
				pro.setProperty("last_indexationDate", pro.getProperty("last_day"));
				OutputStream out=new FileOutputStream(codepath+"config.properties");
				pro.store(out, "update last_indexationDate");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void gatherDate(String code,int startday, BufferedWriter bw_data, BufferedWriter bw_index, BufferedWriter bw_position){
		try{
			String codepath=stockroot+"/"+code+"/";
			BufferedReader br1=new BufferedReader(new FileReader(codepath+"data"));
			BufferedReader br2=new BufferedReader(new FileReader(codepath+"subscription"));
			BufferedReader br3=new BufferedReader(new FileReader(codepath+"afterscription"));
			while(br1.ready()){
				String str=br1.readLine()+","+br2.readLine()+","+br3.readLine();
				String cal=str.substring(0, 10);
				str=str.substring(11);
				int day=Integer.parseInt(encodeDate(cal));
				if(day>startday){
					bw_data.write(str+"\n");
					bw_index.write(cal+","+code+"\n");
					bw_position.write(String.format("%03d",str.length())+","+String.format("%09d", total)+"\n");
					total=total+str.length()+1;
				}
			}
			bw_data.flush();
			bw_index.flush();
			bw_position.flush();
			br1.close();
			br2.close();
			br3.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 处理和复权有关的计算和存储
	 */
	private void processAdjClose(String enddate,String stock){
		Properties pro=new Properties();
		String codepath=stockroot+"/"+stock+"/";
		try  {
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream(codepath+"config.properties"));
			pro.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String last_update=pro.getProperty("lastupdate_subscriptiondate");
		int lastupdate=Integer.parseInt(encodeDate(last_update));
		int end=Integer.parseInt(encodeDate(enddate));
		String last_day=pro.getProperty("last_subscriptiondate");
		if((lastupdate+300)<end){
			fetchrightsurl(stock);
			fetchrights(stock,enddate);
			Properties pro1=new Properties();
			try  {
				BufferedInputStream is = new BufferedInputStream(
						new FileInputStream(codepath+"config.properties"));
				pro1.load(is);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(pro1.getProperty("last_subscriptiondate").equals(last_day)){
				loadsubscription(stock,enddate);
			}
			else{
				calsubscription(stock,true);
			}
		}
		else{
			loadsubscription(stock,enddate);
		}
		String lastCalAfter=pro.getProperty("lastCal_Afterscriptiondate","2005-01-01");
		int lastcal=Integer.parseInt(encodeDate(lastCalAfter));
		end=Integer.parseInt(encodeDate(pro.getProperty("last_day")));
		if(lastcal<end){
			calafterscription(lastcal,stock,true);
			try {
				Properties temppro=new Properties();
				BufferedInputStream tempis = new BufferedInputStream(
						new FileInputStream(codepath+"config.properties"));
				temppro.load(tempis);
				tempis.close();
				temppro.setProperty("lastCal_Afterscriptiondate",temppro.getProperty("last_day"));
				OutputStream tempout = new FileOutputStream(codepath+"config.properties");
				temppro.store(tempout, "update lastCal_Afterscriptiondate");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 不需要进行复权运算时将直接加载复权数据
	 * @param stock 股票编号
	 * @param enddate 终止日期
	 */
	private void loadsubscription(String stock,String enddate){
		String codepath=stockroot+"/"+stock+"/";
		try {
			BufferedReader br=new BufferedReader(new FileReader(codepath+"data"));
			BufferedReader br1=new BufferedReader(new FileReader(codepath+"subscription"));
			while(br1.ready()){
				br1.readLine();
				br.readLine();
			}
			br1.close();
			BufferedWriter bw=new BufferedWriter(new FileWriter(codepath+"subscription",true));
			while(br.ready()){
				String[] out=br.readLine().split(",");
				String tclose=out[4];
				String lclose=out[5];
				String rate=out[6];
				bw.write(tclose+","+lclose+","+rate+"\n");
			}
			bw.close();
			br.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取指定股票复权信息的链接
	 * @param stock 股票编号
	 */
	private void fetchrightsurl(String stock){
		String codepath=stockroot+"/"+stock+"/";
		String url="http://quote.cfi.cn/quote_"+stock+".html";
		String pattern="<DIV\\s+class='b'\\s+id='nodea26'\\s+><NOBR>.*?</NOBR>";
		String rightsurl="";
		try {
			rightsurl=webmethod.fetchandmatch(url, pattern);
			Properties pro=new Properties();
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream(codepath+"config.properties"));
			pro.load(is);
			is.close();
			OutputStream out=new FileOutputStream(codepath+"config.properties");
			rightsurl="quote.cfi.cn"+rightsurl;
			pro.setProperty("rights_url", rightsurl);
			pro.store(out, "update rights_url");
		}
		catch (IllegalStateException e){
			try {
				BufferedWriter bw=new BufferedWriter(new FileWriter(rightslog,true));
				bw.write(stock+"\n");
				bw.close();
				File path=new File(stockroot+"/"+stock);
				filemethod.dealdir(path);
				path.delete();
				delStockSet(stock);
				pro2.remove(pro1.get(stock));
				pro1.remove(stock);			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取指定股票的复权信息
	 * @param stock 股票名称
	 */
	private void fetchrights(String stock,String enddate){
		String codepath=stockroot+"/"+stock+"/";
		String url="http://";
		String pattern="<tr.*?>.*?</tr>";
		File file=new File(codepath+"subscription_record");	
		try {
			file.createNewFile();
			Properties pro=new Properties();
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream(codepath+"config.properties"));
			pro.load(is);
			is.close();
			url=url+pro.getProperty("rights_url");
			webmethod.fetchandmatch(url, codepath+"subscription_record", pattern);
			BufferedReader br=new BufferedReader(new FileReader(codepath+"subscription_record"));
			String date=br.readLine();
			br.close();
			pro.setProperty("lastupdate_subscriptiondate", enddate);
			if(date!=null){
				pro.setProperty("last_subscriptiondate", date.substring(date.length()-11,date.length()-1));	
			}
			else{
				pro.remove("last_subscriptiondate");
			}
			OutputStream out=new FileOutputStream(codepath+"config.properties");
			pro.store(out, "update last_subscriptiondate");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 计算股票后复权
	 * @param stock
	 * @param reset
	 */
	private void calafterscription(int start, String stock,boolean reset){
		String codepath=stockroot+"/"+stock+"/";
		try {
			File data=new File(codepath+"data");
			File adjrecord=new File(codepath+"subscription_record");
			File adjdata=new File(codepath+"afterscription");
			if(!adjdata.exists()){
				adjdata.createNewFile();
			}
			BufferedReader br_adjrecord=new BufferedReader(new FileReader(adjrecord));
			BufferedWriter bw=new BufferedWriter(new FileWriter(adjdata,reset));
			String adj="";
			HashMap<Integer,Double> map_1=new HashMap<Integer,Double>();
			HashMap<Integer,Double> map_2=new HashMap<Integer,Double>();
			File log=new File("config/error_rights");
	  		log.createNewFile();
	  		BufferedWriter bw2=new BufferedWriter(new FileWriter(log,true));
			List<Integer> list=new ArrayList<Integer>();
			while(br_adjrecord.ready()){
				adj=br_adjrecord.readLine();
				String[] record=adj.split(";");
				if(record[3].equals("--")){
					if(record[4].equals("--")){
						bw2.write(stock+"\n");
						continue;
					}
					record[3]=record[4];
				}
				try{
					Double a=parseDouble(record[0])+parseDouble(record[1]);
					map_1.put(Integer.parseInt(encodeDate(record[3])),a);
					map_2.put(Integer.parseInt(encodeDate(record[3])),parseDouble(record[2]));
					list.add(Integer.parseInt(encodeDate(record[3])));
				}catch(Exception e){
					System.out.println(stock);
					e.printStackTrace();
				}
			}
			br_adjrecord.close();
			BufferedReader br_data=new BufferedReader(new FileReader(data));
			while(br_data.ready()){
				String[] record=br_data.readLine().split(",");
				int date=Integer.parseInt(encodeDate(record[0]));
				if(date>start){
					Double tclose=parseDouble(record[4]);
					Double lclose=parseDouble(record[5]);
					Double trate=parseDouble(record[6]);
					int cal=Integer.parseInt(encodeDate(record[0]));
					for(int i=0;i<list.size();i++){
						int key=list.get(i);
						if(cal>=key){
							tclose=calafteradj(tclose,map_1.get(key),map_2.get(key));
							lclose=calafteradj(lclose,map_1.get(key),map_2.get(key));
						}
					}
					Double adjrate=0.0;
					if(df1.format(lclose).equals("0.00")){
						adjrate=trate;
					}
					else{
						adjrate=(tclose-lclose)/lclose*100;
					}
					bw.write(df1.format(tclose)+","+df1.format(lclose)+","+df2.format(adjrate)+"\n");
				}
			}
			bw2.close();
			br_data.close();
			bw.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 计算指定股票的前复权价格
	 * @param stock 股票编号
	 */
	private void calsubscription(String stock,boolean reset){
		String codepath=stockroot+"/"+stock+"/";
		try {
			File data=new File(codepath+"data");
			File adjrecord=new File(codepath+"subscription_record");
			File adjdata=new File(codepath+"subscription");
			BufferedReader br_adjrecord=new BufferedReader(new FileReader(adjrecord));
			BufferedWriter bw=new BufferedWriter(new FileWriter(adjdata));
			String adj="";
			HashMap<Integer,Double> map_1=new HashMap<Integer,Double>();
			HashMap<Integer,Double> map_2=new HashMap<Integer,Double>();
			File log=new File("config/error_rights");
	  		log.createNewFile();
	  		BufferedWriter bw2=new BufferedWriter(new FileWriter(log,reset));
			List<Integer> list=new ArrayList<Integer>();
			while(br_adjrecord.ready()){
				adj=br_adjrecord.readLine();
				String[] record=adj.split(";");
				if(record[3].equals("--")){
					if(record[4].equals("--")){
						bw2.write(stock+"\n");
						continue;
					}
					record[3]=record[4];
				}
				try{
					Double a=parseDouble(record[0])+parseDouble(record[1]);
					map_1.put(Integer.parseInt(encodeDate(record[3])),a);
					map_2.put(Integer.parseInt(encodeDate(record[3])),parseDouble(record[2]));
					list.add(0, Integer.parseInt(encodeDate(record[3])));
				}catch(Exception e){
					System.out.println(stock);
					e.printStackTrace();
				}
			}
			br_adjrecord.close();
			BufferedReader br_data=new BufferedReader(new FileReader(data));
			int rank=0;
			while(br_data.ready()){
				String[] record=br_data.readLine().split(",");
				Double tclose=parseDouble(record[4]);
				Double lclose=parseDouble(record[5]);
				Double trate=parseDouble(record[6]);
				int cal=Integer.parseInt(encodeDate(record[0]));
				for(int i=rank;i<list.size();i++){
					int key=list.get(i);
					if(cal<key){
						tclose=caladj(tclose,map_1.get(key),map_2.get(key));
						lclose=caladj(lclose,map_1.get(key),map_2.get(key));
					}
					else{
						rank++;
					}
				}
				Double adjrate=0.0;
				if(df1.format(lclose).equals("0.00")){
					adjrate=trate;
				}
				else{
					adjrate=(tclose-lclose)/lclose*100;
				}
				bw.write(df1.format(tclose)+","+df1.format(lclose)+","+df2.format(adjrate)+"\n");
			}
			bw2.close();
			br_data.close();
			bw.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 爬取股票信息
	 * @param code 股票编号
	 * @param enddate 结束日期
	 */
	private void fetchdate(String code,String enddate){
		try {
			String codepath=stockroot+"/"+code+"/";
			Properties pro=new Properties();
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream(codepath+"config.properties"));
			pro.load(is);
			is.close();
			String startdate="";
			String tempdate=pro.getProperty("update_day",null);
			if(tempdate==null){
				startdate=pro.getProperty("first_day");
			}
			else{
				Calendar cal=Calendar.getInstance();
				try {
					cal.setTime(sdf.parse(tempdate));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				cal.add(Calendar.DATE, 1);
				startdate=sdf.format(cal.getTime());
			}
			startdate=encodeDate(startdate);
			enddate=encodeDate(enddate);
			String url="";
			try {
				url=generateUrl(code,matchMarketCode(code),startdate,enddate);
			} catch (NoneMatchedMarketException e) {
				System.out.println(e.toString()+" :"+code);
			}
			String resultdate="";
			resultdate=saveToFile(url, codepath+"data");		
			if(!resultdate.equals("")){
				OutputStream out=new FileOutputStream(codepath+"config.properties");
				String[] date=resultdate.split(":");
				if(tempdate==null){
					pro.setProperty("first_day", date[1]);
				}
				BufferedWriter bw=new BufferedWriter(new FileWriter(log));
				bw.write(date[1]+":"+date[0]+"\n");
				bw.close();
				pro.setProperty("last_day", date[0]);
				pro.setProperty("update_day", decodeDate(enddate));
				pro.store(out, "update info up to" + date[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 生成获取指定股票日期内的数据的URL
	 * @param code 股票编号
	 * @param marketcode 市场编号
	 * @param startdate 开始日期,格式为20150509(表示2015年5月9日)
	 * @param enddate 结束日期
	 * @return 生成的URL
	 */
	private String generateUrl(String code,String marketcode,String startdate,String enddate){		
		String result=
				"http://quotes.money.163.com/service/chddata.html?"
				+ "code="+marketcode+code
				+ "&start="+startdate
				+ "&end="+enddate
				+ "&fields=TOPEN;HIGH;LOW;TCLOSE;LCLOSE;PCHG;VOTURNOVER";
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(log));
			bw.write(code+":"+startdate+":"+enddate+"\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 根据股票编号生成市场编号
	 * @param code 股票编好
	 * @return 市场编号
	 * @throws NoneMatchedMarketException 找不到能匹配的市场时抛出该异常
	 */
	private String matchMarketCode(String code) throws NoneMatchedMarketException{
		String marketcode="";
		if(code.charAt(0)=='0'||code.charAt(0)=='2'||code.charAt(0)=='3'){
			marketcode="1";
		}
		else if(code.charAt(0)=='6'||code.charAt(0)=='9'){
			marketcode="0";
		}
		else{
			System.out.println(code);
			throw new NoneMatchedMarketException();
		}
		return marketcode;
	}
	/**
	 * 从网站上爬取股票信息
	 * @param destUrl 获取信息的URL
	 * @param stockpath 股票信息的存储路径
	 * @return 网站上的信息
	 * @throws IOException 
	 */
	private String saveToFile(String destUrl, String fileName) throws IOException {
		BufferedWriter fos = null;
		BufferedReader bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		httpUrl.connect();
		bis = new BufferedReader(new InputStreamReader(httpUrl.getInputStream()));
		fos = new BufferedWriter(new FileWriter(fileName,true));
		String total="";
		int i=0;
		int i_1=0;
		int i_2=0;
		String resultdate="";
		String str="";
		bis.readLine();
		boolean process=true;
		if(bis.ready()){
			str=bis.readLine();
			try{
				while(str.indexOf("None")>=0){
					str=bis.readLine();
				}
			}catch(NullPointerException e){
				process=false;
			}
			if(process){
				i=str.indexOf(",");
				i_1=str.indexOf(",", i+1);
				i_2=str.indexOf(",", i_1+1);
				resultdate=str.substring(0,i)+":";
				total=str.substring(0, i)+str.substring(i_2)+"\n"+total;
				while(bis.ready()){
		  			str=bis.readLine();
		  			if(str.indexOf("None")<0){
		  				i=str.indexOf(",");
			  			i_1=str.indexOf(",", i+1);
			  			i_2=str.indexOf(",", i_1+1);
			  			total=str.substring(0, i)+str.substring(i_2)+"\n"+total;
		  			}
				}
		  		resultdate=resultdate+total.substring(0,i);
			}
		}  	
  		fos.write(total);
  		fos.close();
  		bis.close();
  		httpUrl.disconnect();
  		return resultdate;
	}
	/**
	 * 将XXXX-XX-XX的日期形式转为XXXXXXXX
	 * @param str 原形式日期
	 * @return 转换后的日期
	 */
	private String encodeDate(String str){
		return str.substring(0, 4)+str.substring(5,7)+str.substring(8);
	}
	/**
	 * 将XXXXXXXX的日期形式转为XXXX-XX-XX
	 * @param str 原形式日期
	 * @return 转换后的日期
	 */
	private String decodeDate(String str){
		return str.substring(0, 4)+"-"+str.substring(4,6)+"-"+str.substring(6);
	}
	/**
	 * 在股票池中删除指定股票
	 * @param code 股票编号
	 */
	private void delStockSet(String code){
		switch(code.charAt(0)){
		case '0':
			delstock(code,"config/stock/stockset/SZA");
			break;
		case '2':
			delstock(code,"config/stock/stockset/SZB");
			break;
		case '3':
			delstock(code,"config/stock/stockset/CYB");
			delstock(code,"config/stock/stockset/SZA");
			break;
		case '6':
			delstock(code,"config/stock/stockset/SHA");
			break;
		case '9':
			delstock(code,"config/stock/stockset/SHB");
			break;
		default:
			System.out.println(code);
		}
	}
	/**
	 * 删除指定文件夹
	 * @param code
	 * @param set
	 */
	private void delstock(String code,String set){
		File file=new File(set+"/"+code);
		file.delete();
	}
	/**
	 * 计算复权价格
	 * @param row 原价
	 * @param stock 每股配股
	 * @param dollar 每股分红
	 * @return 前复权价格
	 */
	private Double caladj(Double row,Double stock,Double dollar){
		Double sum=(row-dollar/10)/(1+stock/10);
		return sum;
	}
	/**
	 * 计算后复权价格
	 * @param row 原价
	 * @param stock 每股配股
	 * @param dollar 每股分红
	 * @return 前复权价格
	 */
	private Double calafteradj(Double row,Double stock,Double dollar){
		Double sum=row*(1+stock/10)+dollar/10;
		return sum;
	}
	/**
	 * 转化字符串为double
	 * @param str 源字符串
	 * @return 转换后的数字
	 */
	private Double parseDouble(String str){
		if(str.equals("--")){
			return 0.0;
		}
		else{
			return Double.parseDouble(str);
		}
	}
}
