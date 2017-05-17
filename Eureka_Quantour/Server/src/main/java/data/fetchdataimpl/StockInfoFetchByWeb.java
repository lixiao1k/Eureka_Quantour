package data.fetchdataimpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.impl.xb.ltgfmt.Code;

import com.mysql.cj.jdbc.PreparedStatement;

import data.common.DateLeaf;
import data.common.DateTrie;
import data.common.FileMethod;
import data.common.WebMethod;
import data.database.ConnectionPoolManager;
import data.datahelperimpl.InitEnvironment;
import data.fetchpool.FetchPoolManagement;
import data.parse.Parse;
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
		if(cal.get(Calendar.HOUR_OF_DAY)<18){
			cal.set(Calendar.DATE, cal.get(Calendar.DATE)-1);
		}
		String enddate=sdf.format(cal.getTime());
		System.out.println(enddate);
		long time=System.currentTimeMillis();
		try{
			Properties pro=new Properties();
			File profile=new File("config/stock/dataconfig.properties");
			if(!profile.exists()){
				profile.createNewFile();
			}
			InputStream is=new FileInputStream("config/stock/dataconfig.properties");
			pro.load(is);
			is.close();
			String lastday=pro.getProperty("lastday");
			int date1=Parse.getInstance().getIntDate(lastday);
		boolean flag=true;
		for(String stock:stocklist){
			count++;
			System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
			String date2=fetchdate(stock,enddate);
			if(flag){
				if(date2!=null){
					int tempdate=Parse.getInstance().getIntDate(date2);
					if(tempdate>date1){
						date1=tempdate;
						lastday=date2;
						if(date2.equals(enddate)){
							flag=false;
						}
					}
				}
			}
		}
		OutputStream os=new FileOutputStream("config/stock/dataconfig.properties");
		if(flag){
			pro.setProperty("lastday", lastday);
		}
		else{	
			pro.setProperty("lastday", enddate);
		}
		pro.setProperty("lastUpdateDay", enddate);
		pro.store(os, "update personalSize");
		}catch(IOException e){
			e.printStackTrace();
		}
		long time1=System.currentTimeMillis();
		System.out.println("花费时间：" +(time1-time)+"ms");
	}
	/**
	 * 获取股票信息
	 * @throws InternetdisconnectException 网络无法链接时抛出异常
	 */
	public void fetchAllStockInfo2() throws InternetdisconnectException{
		webmethod.testInternet();
		File root=new File(stockroot);
		String[] stocklist=root.list();
		int i=stocklist.length;
		int count=0;
		Calendar cal=Calendar.getInstance();
		if(cal.get(Calendar.HOUR_OF_DAY)<18){
			cal.set(Calendar.DATE, cal.get(Calendar.DATE)-1);
		}
		String enddate=sdf.format(cal.getTime());
		long time=System.currentTimeMillis();
		try{
			Properties pro=new Properties();
			File profile=new File("config/stock/dataconfig.properties");
			if(!profile.exists()){
				profile.createNewFile();
			}
			InputStream is=new FileInputStream("config/stock/dataconfig.properties");
			pro.load(is);
			is.close();
			String lastday=pro.getProperty("lastday");
			int date1=Parse.getInstance().getIntDate(lastday);
		boolean flag=true;
		for(String stock:stocklist){
			count++;
			System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
			String date2=fetchdate2(stock,enddate);
			if(flag){
				if(date2!=null){
					int tempdate=Parse.getInstance().getIntDate(date2);
					if(tempdate>date1){
						date1=tempdate;
						lastday=date2;
						if(date2.equals(enddate)){
							flag=false;
						}
					}
				}
			}
		}
		OutputStream os=new FileOutputStream("config/stock/dataconfig.properties");
		if(flag){
			pro.setProperty("lastday", lastday);
		}
		else{	
			pro.setProperty("lastday", enddate);
		}
		pro.setProperty("lastUpdateDay", enddate);
		pro.store(os, "update personalSize");
		}catch(IOException e){
			e.printStackTrace();
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
		if(cal.get(Calendar.HOUR_OF_DAY)<18){
			cal.set(Calendar.DATE, cal.get(Calendar.DATE)-1);
		}
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
	
	
	
	
	
	
	public void fetchCompany() throws InternetdisconnectException{
		webmethod.testInternet();
		File root=new File("config/stock/info");
		String[] stocklist=root.list();
		int i=stocklist.length;
		int count=0;
		LocalDate ld=LocalDate.of(2005, 2, 1);
		boolean flag=false;
		for(String stock:stocklist){
			count++;
			System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
			try {
				if(stock.equals("600876")){
					flag=true;
				}
				if(flag){
					fetchSingleCompany(ld,stock);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void fetchSingleCompany(LocalDate ld,String code) throws IOException{
		String codepath="config/stock/info/"+code+"/";
		Properties pro=new Properties();
		BufferedInputStream is = new BufferedInputStream(
				new FileInputStream(codepath+"config.properties"));
		pro.load(is);
		is.close();
		String url=pro.getProperty("rights_url","null");
		String index=url.substring(18,url.length()-12);
		for(int i=ld.getYear();i<=LocalDate.now().getYear();i++){
			String url1="http://quote.cfi.cn/quote.aspx?contenttype=cwzbMgfxzb&stockid="
					+ index
					+ "&jzrq="
					+ i;
			String url2="http://quote.cfi.cn/quote.aspx?contenttype=gbjg&stockid="
					+ index
					+ "&jzrq="
					+ i;
			boolean isA=true;
			if(code.charAt(0)=='2'||code.charAt(0)=='9'){
				isA=false;
			}
			fetchAndmatch1(url1, code);
			fetchAndmatch2(url2,code,isA);
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
	public void fetchExponent(){
		FileMethod.getInstance().makepath("config/exponent");
		Properties pro=new Properties();
		try {
			InputStream is=new FileInputStream("config/stock/dataconfig.properties");
			pro.load(is);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String endday=pro.getProperty("lastday");
		String startday=pro.getProperty("updateExponentday","2005-02-01");
		File file=new File("config/resources/date/totalCalendar");
		List<Integer> date=new ArrayList<Integer>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			while(br.ready()){
				String t=br.readLine().substring(0, 8);
				date.add(Integer.valueOf(t));
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select code from stockinfo where code like 'zs%'";
		PreparedStatement pstmt=null;
		List<String> fetchList=new ArrayList<String>();
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				String temp=rs.getString(1).substring(2);
				if(temp.charAt(0)=='3'){
					temp='1'+temp;
				}
				else{
					temp='0'+temp;
				}
				fetchList.add(temp);
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		int start=Parse.getInstance().getIntDate(startday);
		int end=Parse.getInstance().getIntDate(endday);
		for(int j=0;j<fetchList.size();j++){
			String url="";
			url="http://quotes.money.163.com/service/chddata.html?"
					+ "code="+fetchList.get(j)
					+ "&start="+start
					+ "&end="+end
					+ "&fields=TOPEN;HIGH;LOW;TCLOSE;LCLOSE;PCHG;VOTURNOVER";
			filemethod.makepath("config/exponent/zs"+fetchList.get(j).substring(1));
			try {
				String resultdate=saveToFile(url, "config/exponent/zs"+fetchList.get(j).substring(1)+"/data");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File temp=new File("config/exponent/zs"+fetchList.get(j).substring(1)+"/data");
			try {
				BufferedReader br=new BufferedReader(new FileReader(temp));
				conn=ConnectionPoolManager.getInstance().getConnection("quantour");
				sql="insert into exponent values(?,?,?,?,?,?,?,?,?)";
				pstmt=null;
				pstmt = (PreparedStatement)conn.prepareStatement(sql);
				while(br.ready()){
					String[] result=br.readLine().split(",");
					try {
						pstmt.setString(1,"zs"+fetchList.get(j).substring(1));
						pstmt.setString(2,result[0]);
						pstmt.setString(3, result[1]);
						pstmt.setString(4, result[4]);
						pstmt.setString(5, result[2]);
						pstmt.setString(6, result[3]);
						pstmt.setString(7, result[5]);
						pstmt.setString(8, result[6]);
						pstmt.setString(9, result[7]);
						pstmt.executeUpdate();
					}catch (SQLException e) {
						System.out.println(result[7]);
						e.printStackTrace();
						System.exit(0);
					}
				}
				br.close();
				pstmt.close();
				ConnectionPoolManager.getInstance().close("quantour", conn);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println("complete");
	}
	public void fetchStockInfo_Minutes(){
		File root=new File(stockroot);
		int count=0;
		String[] list=root.list();
		int i=list.length;
		LocalDateTime lt=LocalDateTime.now();
		File index=new File("config/resources/date/totalCalendar");
		List<String> date=new ArrayList<String>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(index));
			while(br.ready()){
				String r=br.readLine().substring(0, 8);
				date.add(r);
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try{
			boolean flag=false;
			int min=180;
			for(String code:list){
				count++;
				if(code.equals("000001")){
					flag=true;
				}
				if(flag){
					System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
					//dealSingleInfo_Minutes(code,lt,date);
					//check_Minutes(code,date);
					//check_Minutes(code,date);
					getMinInterval(code,date);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void getMinInterval(String code,List<String> date)throws IOException{
		String path=stockroot+"/"+code+"/Minutes";
		File file=new File(path);
		File[] list=file.listFiles();
		int min=180;
		for(File temp:list){
			BufferedReader br=new BufferedReader(new FileReader(temp));
			boolean none=false;
			while(br.ready()){
				String[] sum=br.readLine().split("\t");
				if(sum[0].indexOf("close")>=0){
					none=true;
				}
			}
			br.close();
			if(!none){
				br=new BufferedReader(new FileReader(temp));
				br.readLine();
				LocalTime last=LocalTime.now();
				while(br.ready()){
					try{
					String[] r=br.readLine().split("\t");
					r[5].equals("买盘");
					String[] m=r[0].split(":");
					if(Integer.valueOf(m[2])%3!=0){
					}
					}catch(Exception e){
						String symbol="sh";
						if(code.charAt(0)=='0'||code.charAt(0)=='2'||code.charAt(0)=='3'){
							symbol="sz";
						}
						String url="http://market.finance.sina.com.cn/downxls.php?date="
								+ decodeDate(date.get(Integer.valueOf(temp.getName())))
								+ "&symbol="
								+ symbol
								+ code;
						System.out.println(decodeDate(date.get(Integer.valueOf(temp.getName())))+"---------"+code);
						int in=FetchPoolManagement.getInstance().getConnection("web", url, path+"/"+temp.getName(), "InputStream");
						FetchPoolManagement.getInstance().startConn("web", in);
					}
				}
				br.close();
			}
			
		}
	}
	private void check_Minutes(String code,List<String> date)throws IOException{
		String path=stockroot+"/"+code+"/Minutes";
		File file=new File(path);
		File[] list=file.listFiles();
		BufferedWriter bw=new BufferedWriter(new FileWriter("config/log_min",true));
		for(File temp:list){
			BufferedReader br=new BufferedReader(new FileReader(temp));
			boolean aft=false;
			boolean good=false;
			boolean none=false;
			while(br.ready()){
				String[] sum=br.readLine().split("\t");
				if(sum[0].indexOf("15:")>=0||sum[0].indexOf("14:")>=0){
					aft=true;
				}
				if(sum[0].indexOf("09:")>=0||sum[0].indexOf("10:")>=0){
					good=true;
				}
				if(sum[0].indexOf("close")>=0){
					none=true;
				}
			}
			if(aft&&good||none){
				
			}
			else{
				System.out.println(code+":"+temp.getName());
				bw.write(code+":"+temp.getName()+"\n");
				String symbol="sh";
				if(code.charAt(0)=='0'||code.charAt(0)=='2'||code.charAt(0)=='3'){
					symbol="sz";
				}
				String url="http://market.finance.sina.com.cn/downxls.php?date="
						+ decodeDate(date.get(Integer.valueOf(temp.getName())))
						+ "&symbol="
						+ symbol
						+ code;
				System.out.println(decodeDate(date.get(Integer.valueOf(temp.getName())))+"---------"+code);
				int in=FetchPoolManagement.getInstance().getConnection("web", url, path+"/"+temp.getName(), "InputStream");
				FetchPoolManagement.getInstance().startConn("web", in);
			}
			br.close();
		}
		bw.close();
	}
	private void dealSingleInfo_Minutes(String code,LocalDateTime lt,List<String> datesort) throws IOException{
		String path=stockroot+"/"+code+"/";
		Properties pro=new Properties();
		BufferedInputStream is = new BufferedInputStream(
				new FileInputStream(path+"config.properties"));
		pro.load(is);
		is.close();
		String lastupdate=pro.getProperty("lastUpdate_Minutes", "2005-02-01 00:00:00");
		LocalDateTime old=LocalDateTime.parse(lastupdate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		File index=new File(path+"psIndex");
		List<String> date=new ArrayList<String>();
		List<Integer> sort=new ArrayList<Integer>();
		BufferedReader br=new BufferedReader(new FileReader(index));
		while(br.ready()){
			int r=Integer.valueOf(br.readLine());
			sort.add(r);
			date.add(datesort.get(r));
		}
		FileMethod.getInstance().dealdir(new File(path+"Minutes"));
		FileMethod.getInstance().makepath(path+"Minutes");
		br.close();
		if(lt.compareTo(old)>0){
			lastupdate=lt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			pro.setProperty("lastUpdate_Minutes", lastupdate);
			OutputStream out=new FileOutputStream(path+"config.properties");
			pro.store(out, "update lastUpdate_Minutes");
			out.close();
			for(int i=date.size()>=30?date.size()-5:0;i<date.size();i++){
				String symbol="sh";
				if(code.charAt(0)=='0'||code.charAt(0)=='2'||code.charAt(0)=='3'){
					symbol="sz";
				}
				String url="http://market.finance.sina.com.cn/downxls.php?date="
						+ decodeDate(date.get(i))
						+ "&symbol="
						+ symbol
						+ code;
				System.out.println(decodeDate(date.get(i))+"---------"+code);
				int in=FetchPoolManagement.getInstance().getConnection("web", url, path+"Minutes/"+String.format("%04d", sort.get(i)), "InputStream");
				FetchPoolManagement.getInstance().startConn("web", in);
			}
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
//				if(pro.containsKey("last_indexationDate")){
//					int dateday=Integer.parseInt(encodeDate(pro.getProperty("last_day")));
//					int indexday=Integer.parseInt(encodeDate(pro.getProperty("last_indexationDate")));
//					if(dateday>indexday){
//						gatherDate(code,indexday,bw_data,bw_index,bw_position);
//					}
//				}
				if(false){
					
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
	public void indexationStockDate(boolean isAddition){
		try{
			File root=new File(stockroot);
			int count=0;
			String[] list=root.list();
			int i=list.length;
			Properties pro=new Properties();
			File profile=new File("config/stock/dataconfig.properties");
			if(!profile.exists()){
				profile.createNewFile();
			}
			InputStream is=new FileInputStream("config/stock/dataconfig.properties");
			pro.load(is);
			is.close();
			boolean update=true;
			DateTrie trie=new DateTrie();
			int lastday=Parse.getInstance().getIntDate(pro.getProperty("lastday","2005-02-01"));
			if(!pro.containsKey("lastIndexationDay")){
				DateWriter dw=new DateWriter(isAddition);
				for(String code:list){
					count++;
					System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
					gatherDate(code,-1,trie,isAddition,dw);
				}
				dw.close();
			}
			else{
				int indexDay=Parse.getInstance().getIntDate(pro.getProperty("lastIndexationDay"));
				if(indexDay<lastday){
					DateWriter dw=new DateWriter(isAddition);
					for(String code:list){
						count++;
						System.out.println("正在处理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
						gatherDate(code,indexDay,trie,isAddition,dw);
					}
					dw.close();
				}
				else{
					update=false;
				}
			}
			if(update){
				int presize=Integer.valueOf(pro.getProperty("daynumber", "0"));
				int pretotal=Integer.valueOf(pro.getProperty("nowrow", "0"));
				int[] receive=generateCalendarIndex(presize,pretotal,trie,isAddition);	
				OutputStream os=new FileOutputStream("config/stock/dataconfig.properties");
				pro.setProperty("daynumber", String.valueOf(receive[1]));
				pro.setProperty("nowrow", String.valueOf(receive[0]));
				pro.setProperty("lastIndexationDay", Parse.getInstance().intTocal(trie.getMax().getCal()));
				pro.setProperty("lastday", Parse.getInstance().intTocal(trie.getMax().getCal()));
				pro.store(os, "update personalSize");
				count=0;
				for(String code:list){
					count++;
					System.out.println("正在处 理第"+count+"个，总共"+i+"个"+"剩余"+(i-count)+"个。");
					generateStockIndex(presize,code,trie,isAddition);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void gatherDate(String code,int startday,DateTrie trie,boolean isAddition,DateWriter dw){
		try{
			String codepath=stockroot+"/"+code+"/";
			BufferedReader br1=new BufferedReader(new FileReader(codepath+"data"));
			BufferedReader br2=new BufferedReader(new FileReader(codepath+"subscription"));
			BufferedReader br3=new BufferedReader(new FileReader(codepath+"afterscription"));
			AverageStackFactory asf=new AverageStackFactory(codepath,true);
			asf.addstack(5);
			asf.close();
			asf.openReader();
			while(br1.ready()){
				String str=br1.readLine()+","+br2.readLine()+","+br3.readLine()+asf.readline();//
				String cal=str.substring(0, 10);
				str=str.substring(11);
				int day=Integer.parseInt(encodeDate(cal));
				if(day>startday){
					File file=new File("config/resources/date/calendarDate/");
					if(!file.exists()&&!file.isDirectory()){
						file.mkdir();
					}
					trie.add(day, Integer.valueOf(code));
					dw.writer(day, str+"\n");
				}
			}
			br1.close();
			br2.close();
			br3.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private int[] generateCalendarIndex(int presize,int pretotal,DateTrie trie,boolean isAddition){
		try{
			String codepath=ie.getPath("resources")+"/date/";
			BufferedWriter bw_ps=new BufferedWriter(new FileWriter(codepath+"mainPosition",isAddition));
			BufferedWriter bw_tc=new BufferedWriter(new FileWriter(codepath+"totalCalendar",isAddition));
			BufferedWriter bw=new BufferedWriter(new FileWriter("config/resources/mainData",isAddition));
			DateLeaf p;
			int total=pretotal;
			int size=presize;
			p=trie.getMin();
			while(p!=null){
				bw_tc.write(p.getCal()+""+total+"\n");
				BufferedReader br1=new BufferedReader(new FileReader("config/resources/date/calendarDate/"+p.getCal()));
				int temptotal=0;
				for(int i=0;i<p.getNamelist().size();i++){
					String str=br1.readLine();
					bw_ps.write(p.getNamelist().get(i)+String.format("%03d",str.length())+String.format("%06d", temptotal)+"\n");
					temptotal=temptotal+str.length()+1;
				}
				br1.close();
				total=total+p.getNamelist().size();
				size=size+1;
				p=(DateLeaf) p.getNext();
			}
			bw.close();
			bw_ps.close();
			bw_tc.close();
			int[] info=new int[2];
			info[0]=total;
			info[1]=size;
			return info;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	private void generateStockIndex(int premainsize,String code,DateTrie trie,boolean isAddition){
		try{
			String codepath=stockroot+"/"+code+"/";
			File psIndex=new File(codepath+"psIndex");
			if(!psIndex.exists()){
				psIndex.createNewFile();
			}
			File file=new File(codepath+"calendar");
			file.delete();
			File mainIndex=new File(codepath+"mainIndex");
			if(!mainIndex.exists()){
				mainIndex.createNewFile();
			}
			int size=premainsize;
			BufferedWriter bw_main=new BufferedWriter(new FileWriter(codepath+"mainIndex",isAddition));
			BufferedWriter bw_ps=new BufferedWriter(new FileWriter(codepath+"psIndex",isAddition));
			Properties pro=new Properties();
			InputStream is=new FileInputStream(codepath+"config.properties");
			pro.load(is);
			is.close();
			int prepssize=Integer.valueOf(pro.getProperty("personalSize", "0"));
			
			DateLeaf p=trie.getMin();
			int code1=Integer.valueOf(code);
			while(p!=null){
				int i=p.getDateinfo().getOrDefault(code1, 9999);
				bw_main.write(String.format("%04d", i));
				if(i!=9999){
					bw_ps.write(String.format("%04d", size)+"\n");
					bw_main.write(String.format("%04d", prepssize));
					prepssize++;
				}
				else{
					bw_main.write("9999");
				}
				bw_main.write("\n");
				size++;
				p=(DateLeaf) p.getNext();
			}
			OutputStream os=new FileOutputStream(codepath+"config.properties");
			pro.setProperty("personalSize", String.valueOf(prepssize));
			pro.store(os, "update personalSize");
			bw_main.close();
			bw_ps.close();
			os.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void gatherDate(String code,int startday, BufferedWriter bw_data, BufferedWriter bw_index, BufferedWriter bw_position){
		try{
			String codepath=stockroot+"/"+code+"/";
			BufferedReader br1=new BufferedReader(new FileReader(codepath+"data"));
			BufferedReader br2=new BufferedReader(new FileReader(codepath+"subscription"));
			BufferedReader br3=new BufferedReader(new FileReader(codepath+"afterscription"));
//			AverageStackFactory asf=new AverageStackFactory(codepath,true);
//			asf.addstack(5);
//			asf.addstack(10);
//			asf.addstack(20);
//			asf.addstack(30);
//			asf.addstack(60);
//			asf.close();
//			asf.openReader();
			while(br1.ready()){
				String str=br1.readLine()+","+br2.readLine()+","+br3.readLine();//+asf.readline()
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
//			asf.closeReader();
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
	private String fetchdate2(String code,String enddate){
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
				url=generateUrl2(code,matchMarketCode(code),startdate,enddate);
			} catch (NoneMatchedMarketException e) {
				System.out.println(e.toString()+" :"+code);
			}
			String resultdate="";
			resultdate=saveToFile2(url, codepath+"data");		
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
				return date[0];
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 爬取股票信息
	 * @param code 股票编号
	 * @param enddate 结束日期
	 */
	private String fetchdate(String code,String enddate){
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
				return date[0];
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
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
	 * 生成获取指定股票日期内的数据的URL
	 * @param code 股票编号
	 * @param marketcode 市场编号
	 * @param startdate 开始日期,格式为20150509(表示2015年5月9日)
	 * @param enddate 结束日期
	 * @return 生成的URL
	 */
	private String generateUrl2(String code,String marketcode,String startdate,String enddate){		
		String result=
				"http://q.stock.sohu.com/hisHq?code=cn_"
				+ code
				+ "&start="
				+ startdate
				+ "&end="
				+ enddate
				+ "&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
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
	 * 从网站上爬取股票信息
	 * @param destUrl 获取信息的URL
	 * @param stockpath 股票信息的存储路径
	 * @return 网站上的信息
	 * @throws IOException 
	 */
	private String saveToFile2(String destUrl, String fileName) throws IOException {
		BufferedWriter fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		httpUrl.connect();
		bis = new BufferedInputStream(httpUrl.getInputStream());
		fos = new BufferedWriter(new FileWriter(fileName,true));
		String total="";
		String resultdate="";
		int BUFFER_SIZE = 8096;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		while ((size = bis.read(buf)) != -1){
			total=total+new String(buf);
		}
		total=total.substring(40);
		boolean has=false;
		String p="\\[.*?\\]";
		 Pattern pat=Pattern.compile(p);
		 Matcher m=pat.matcher(total);
		 String date="";
		 String beforedate="";
		 String lastday="";
		 String result="";
		 DecimalFormat   df=new   DecimalFormat("0.00"); 
		 while(m.find()){	
		  	String column=m.group();
		  	String p1="\".*?\"";
			Pattern pat1=Pattern.compile(p1);
			Matcher m1=pat1.matcher(column);
			m1.find();
			beforedate=date;
			date=m1.group();
			date=date.substring(1,date.length()-1);
			if(date.length()>8){
				if(!has){
					lastday=date;
				}
				has=true;
				m1.find();
				String open=m1.group().substring(1,m1.group().length()-1);
				m1.find();
				String close=m1.group().substring(1,m1.group().length()-1);
				m1.find();
				String before=m1.group().substring(1,m1.group().length()-1);
				m1.find();
				String rate=m1.group().substring(1,m1.group().length()-2);
				m1.find();
				String low=m1.group().substring(1,m1.group().length()-1);
				m1.find();
				String high=m1.group().substring(1,m1.group().length()-1);
				m1.find();
				String volume=m1.group().substring(1,m1.group().length()-1);
				
				String tclose=df.format(Double.parseDouble(close)+Double.parseDouble(before));
				result=date+","+open+","+high+","+low+","+close+","+tclose+","+rate+","+volume+"00"+"\n"+result;
			}
		 }
		String startday=beforedate;
  		if(has){
  			fos.write(result);
  			resultdate=lastday+":"+startday;
  		}
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
	private void fetchAndmatch1(String destUrl,String code) throws IOException{
		int BUFFER_SIZE = 8096;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0; 
		url = new URL(destUrl);   
		httpUrl = (HttpURLConnection) url.openConnection();
//		httpUrl.setRequestProperty( "User-agent", "Mozilla/9.0 (compatible; MSIE 10.0; Windows NT 8.1; .NET CLR 2.0.50727)" );
		try{
			httpUrl.connect();
		}catch(Exception e){
			System.out.println(destUrl);
			httpUrl.connect();
		}
		bis = new BufferedInputStream(httpUrl.getInputStream());
		String total="";
		while ((size = bis.read(buf)) != -1){
			total=total+new String(buf,0,size);
		}
		bis.close();
		if(total.indexOf(("禁止访问"))>=0){
			System.out.println("ip is banned");
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fetchAndmatch1(destUrl,code);
		}
  		String pattern="<tr.*?>.*?</tr>";
  		List<String> date=new ArrayList<String>();
  		List<Double> basic=new ArrayList<Double>();
  		List<Double> netasset=new ArrayList<Double>();
  		int count=0;
  		for(String row:match(total,pattern)){
  			count++;
  			if(count==2){
  				for(String day:match(row,"[0-9]{4}-[0-9]{2}-[0-9]{2}")){
  					date.add(day);
  				}
  			}
  			if(count==4){
  				for(String day:match(row,"-?[0-9]+\\.[0-9]+|--")){
  					if(day.equals("--")){
  						basic.add(0.0);
  					}
  					else{
  						basic.add(Double.valueOf(day));
  					}
  				}
  			}
  			if(count==8){
  				for(String day:match(row,"[0-9]+\\.[0-9]+|--")){
  					if(day.equals("--")){
  						netasset.add(0.0);
  					}
  					else{
  						netasset.add(Double.valueOf(day));
  					}
  				}
  			}
  		}
  		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="insert into companyquota values(?,?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			for(int i=0;i<date.size();i++){
				pstmt.setString(1, code);
				pstmt.setString(2, date.get(i));
				pstmt.setDouble(3, basic.get(i));
				pstmt.setDouble(4, netasset.get(i));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}catch(IndexOutOfBoundsException e){
			System.out.println(code+destUrl);
			System.out.println(date.size()+":"+basic.size()+":"+netasset.size());
			System.out.println(total);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	private void fetchAndmatch2(String destUrl,String code,boolean isA) throws IOException{
		int BUFFER_SIZE = 8096;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0; 
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
	//	httpUrl.setRequestProperty( "User-agent", "Mozilla/9.0 (compatible; MSIE 10.0; Windows NT 8.1; .NET CLR 2.0.50727)" );
		try{
			httpUrl.connect();
		}catch(Exception e){
			System.out.println(destUrl);
			httpUrl.connect();
		}
		bis = new BufferedInputStream(httpUrl.getInputStream());
		String total="";
		while ((size = bis.read(buf)) != -1){
			total=total+new String(buf,0,size);
		}
		bis.close();
		if(total.indexOf(("禁止访问"))>=0){
			System.out.println("ip is banned");
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fetchAndmatch2(destUrl,code,isA);
		}
  		String pattern="<tr.*?>.*?</tr>";
  		List<String> date=new ArrayList<String>();
  		List<Long> basic=new ArrayList<Long>();
  		List<Long> flu=new ArrayList<Long>();
  		int count=0;
  		for(String row:match(total,pattern)){
  			count++;
  			if(count==2){
  				for(String day:match(row,"[0-9]{4}-[0-9]{2}-[0-9]{2}")){
  					date.add(day);
  				}
  			}
  			if(count==4){
  				for(String day:match(row,"[0-9]+\\.[0]{2}|--")){
  					if(day.equals("--")){
  						basic.add((long) 0);
  					}
  					else{
  						basic.add(Long.valueOf(day.substring(0, day.length()-3)));
  					}
  				}
  			}
  			if(count==6&&isA){
  				for(String day:match(row,"[0-9]+\\.[0]{2}|--")){
  					if(day.equals("--")){
  						flu.add((long) 0);
  					}
  					else{
  						flu.add(Long.valueOf(day.substring(0, day.length()-3)));
  					}
  				}
  			}
  			if(count==11&&!isA){
  				for(String day:match(row,"[0-9]+\\.[0]{2}|--")){
  					if(day.equals("--")){
  						flu.add((long) 0);
  					}
  					else{
  						flu.add(Long.valueOf(day.substring(0, day.length()-3)));
  					}
  				}
  			}
  		}
  		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="insert into companycaptial values(?,?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			for(int i=0;i<date.size();i++){
				pstmt.setString(1, code);
				pstmt.setString(2, date.get(i));
				pstmt.setLong(3, basic.get(i));
				pstmt.setLong(4, flu.get(i));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}catch(IndexOutOfBoundsException e){
			System.out.println(code+destUrl);
			System.out.println(date.size()+":"+basic.size()+":"+flu.size());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	private List<String> match(String total,String pattern){
		List<String> date=new ArrayList<String>();
		String p=pattern;
  		Pattern pat=Pattern.compile(p);
  		Matcher m=pat.matcher(total);
  		while(m.find()){
  			date.add(m.group());
  		}
  		return date;
	}
}
class DateWriter{
	private HashMap<Integer,Integer> index;
	private List<BufferedWriter> writer;
	private boolean isAddition;
	private String path;
	private int now;
	public DateWriter(boolean _isAddition){
		index=new HashMap<Integer,Integer>();
		writer=new ArrayList<BufferedWriter>();
		isAddition=_isAddition;
		path="config/resources/date/calendarDate/";
		now = 0;
	}
	public void writer(int day,String info) throws IOException{
		int index=this.index.getOrDefault(day, -1);
		if(index<0){
			File file=new File(path+day);
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter temp=new BufferedWriter(new FileWriter(path+day,isAddition));
			temp.write(info);
			this.index.put(day, now);
			writer.add(temp);
			now++;
		}
		else{
			writer.get(index).write(info);
		}
	}
	public void close() throws IOException{
		for(int i=0;i<writer.size();i++){
			writer.get(i).close();
		}
	}
}
