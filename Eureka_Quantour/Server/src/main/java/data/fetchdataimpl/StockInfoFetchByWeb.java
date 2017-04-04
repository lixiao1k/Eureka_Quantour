package data.fetchdataimpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import data.common.FileMethod;
import data.common.WebMethod;
import exception.InternetdisconnectException;
import exception.NoneMatchedMarketException;

public class StockInfoFetchByWeb {
	private String stockroot;
	private FileMethod filemethod;
	private WebMethod webmethod;
	private SimpleDateFormat sdf;
	private File log;
	public static void main(String[] args){
		new StockInfoFetchByWeb();
	}
	public StockInfoFetchByWeb(){
		sdf=new SimpleDateFormat("yyyy-MM-dd");
		stockroot="config/stock/info";
		log=new File("config/stocklog");
		try {
			log.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		filemethod=FileMethod.getInstance();
		webmethod=WebMethod.getInstance();
		filemethod.makepath(stockroot);
		try {
			webmethod.testInternet();
			fetchAllStockInfo();
		} catch (InternetdisconnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			readtxt();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
//	private void readtxt() throws IOException{
//		int BUFFER_SIZE=(row_length+1)*12;
//		long t=System.currentTimeMillis();
//  		RandomAccessFile a = new RandomAccessFile("config/stock/info/000001/data", "rw");
//  		FileChannel fc=a.getChannel();
//  		MappedByteBuffer mb=fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
//  		byte[] buf=new byte[BUFFER_SIZE];
//  		byte[] buf1 = new byte[(int) (fc.size()%BUFFER_SIZE)];
//  		int count=0;
//  		a.seek(BUFFER_SIZE);
//  		System.out.println(a.readLine());
//  		long t1=System.currentTimeMillis();
////  		for(int i=0;i<fc.size();i+=BUFFER_SIZE){
////  			if(fc.size()>i+BUFFER_SIZE){
////  				mb.get(buf);
////  				byte[] newbuf=new byte[101];
////  				break;
////  			}
////  			else{
////  				mb.get(buf1);
////  			}
////  		}
//  		
//		 FileReader in = new FileReader("config/stock/info/000001/data");
//		 BufferedReader reader = new BufferedReader(in);
//		 reader.skip(BUFFER_SIZE);
//		 System.out.println(reader.readLine());
//		 System.out.println(reader.readLine());
//		 reader.close();
//		 long t2=System.currentTimeMillis();
//		 System.out.println("方法1时间为："+(t1-t));
//		 System.out.println("方法2时间为："+(t2-t1));
//	}
	public void fetchAllStockInfo(){
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
		if(bis.ready()){
			str=bis.readLine();
			i=str.indexOf(",");
			i_1=str.indexOf(",", i+1);
			i_2=str.indexOf(",", i_1+1);
			resultdate=str.substring(0,i)+":";
			total=str.substring(0, i)+str.substring(i_2)+"\n"+total;
			while(bis.ready()){
	  			str=bis.readLine();
	  			i=str.indexOf(",");
	  			i_1=str.indexOf(",", i+1);
	  			i_2=str.indexOf(",", i_1+1);
	  			total=str.substring(0, i)+str.substring(i_2)+"\n"+total;
			}
	  		resultdate=resultdate+str.substring(0,i);
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
}
