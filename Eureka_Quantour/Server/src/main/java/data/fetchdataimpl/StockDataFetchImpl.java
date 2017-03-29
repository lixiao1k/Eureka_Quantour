package data.fetchdataimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.fetchdataservice.IStockDataFetch;
import data.parse.ParseStockName;
import resultmessage.InternetdisconnectException;
/**
 * 从网上爬取股票数据
 * @author 刘宇翔
 *
 */
public class StockDataFetchImpl implements IStockDataFetch{
	private String SHA;
	private String SZA;
	private String SHB;
	private String SZB;
	private String CYB;
	private String HS300;
	public static void main(String[] args){
		new StockDataFetchImpl();
	}
	public StockDataFetchImpl(){
		SHA="config/stock/stockset/SHA";
		SHB="config/stock/stockset/SHB";
		SZA="config/stock/stockset/SZA";
		SZB="config/stock/stockset/SZB";
		CYB="config/stock/stockset/CYB";
		HS300="config/stock/stockset/HS300";
		makepath("config/parse");
		makepath("config/backups");
		makepath("config/stock/stockset");
		makepath(SHA);
		makepath(SHB);
		makepath(SZA);
		makepath(SZB);
		makepath(HS300);
		makepath(CYB);
		try {
			getAllStockName();
		} catch (InternetdisconnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 从网上获取所有股票的名字与编号
	 * @return 名字与编号组成的String的列表（格式:"编号名字"）
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	@Override
	public void getAllStockName() throws InternetdisconnectException{
		try{
			File backups=new File("config/backups/StringNameList_Backups");
			if(!backups.exists()){
				backups.createNewFile();
				try {
					testInternet();
					String str=saveToFile("http://quote.stockstar.com/stock/stock_index.htm"
							,"config/backups/StringNameList_Backups");
					dealStringList(match("<li>.*?</li>",str));
					System.out.println("update by web");
				} catch (InternetdisconnectException e) {
					throw e;
				}
			}
			else{
				BufferedReader br=new BufferedReader(new FileReader(backups));
				String total="";
				while(br.ready()){
					total=total+br.readLine();
				}
				br.close();
				dealStringList(match("<li>.*?</li>",total));
				System.out.println("update by backups");
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 判断网络是否可连接
	 * @throws InternetdisconnectException
	 */
	private void testInternet() throws InternetdisconnectException{
		String address ="www.baidu.com";
		BufferedReader br=null;
		try {
			Process process = Runtime.getRuntime().exec("ping " + address+" -n 5");
			br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
			String connectionStr =null;
			String total="";
			while ((connectionStr = br.readLine()) != null) {
				System.out.println(connectionStr);
				total=total+connectionStr;
			}
			br.close();
			if(total.indexOf("TTL")<0){
				throw new InternetdisconnectException();
			}
		} catch (IOException e) {
			System.out.println("链接失败");
			e.printStackTrace();
		}
	}
	/**
	 * 从网站上爬取信息
	 * @param destUrl 网站地址
	 * @param fileName 存储到的文件路径
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
		bis = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(),"GBK"));
		fos = new BufferedWriter(new FileWriter(fileName));
		String total="";
  		while(bis.ready()){
  			String str=bis.readLine();
  			total=total+str;
  			fos.write(str+"\n");
  		}
  		fos.close();
  		bis.close();
  		
  		httpUrl.disconnect();
		return total;
	}
	/**
	 * 用正则表达式匹配string中的某段内容
	 * @param pattern 正则表达式
	 * @param total 内容
	 * @return 匹配到的所有内容
	 */
	private List<String> match(String pattern,String total){
  		Pattern pat=Pattern.compile(pattern);
  		Matcher m=pat.matcher(total);
  		List<String> result=new ArrayList<String>();
  		while(m.find()){
  			String a="";
  			String b="";
  			a=m.group();
  			b=a.substring(40,46);
  			a=a.substring(113,a.length()-9);
  			result.add(b+a);
  		}
  		return result;
	}
	/**
	 * 处理爬取到的股票名字
	 * @param list
	 */
	private void dealStringList(List<String> list){
		try{
			Iterator<String> it=list.iterator();
			String str="";
			String code="";
			String name="";
			File nameTocode=new File("config/parse/nameTocode.properties");
			File codeToname=new File("config/parse/codeToname.properties");
			if(!nameTocode.exists()||!codeToname.exists()){
				nameTocode.createNewFile();
				codeToname.createNewFile();
			}
			else{
				nameTocode.delete();
				codeToname.delete();
			}
			File stockset=new File("config/stock/stockset");
			File[] setlist=stockset.listFiles();
			for(File file:setlist){
				dealdir(file);
			}
			Properties nameTocode_pro=new Properties();
			Properties codeToname_pro=new Properties();
			OutputStream nameTocode_out=new FileOutputStream("config/parse/nameTocode.properties");
			OutputStream codeToname_out=new FileOutputStream("config/parse/codeToname.properties");
			while(it.hasNext()){
				str=it.next();
				code=str.substring(0,6);
				name=ParseStockName.chkHalf(str.substring(6));
				if(name.equals(code)){
					continue;
				}
				generateStockSet(code);
				nameTocode_pro.setProperty(name, code);
				codeToname_pro.setProperty(code, name);
			}
			nameTocode_pro.store(nameTocode_out, "update");
			codeToname_pro.store(codeToname_out, "update");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void dealdir(File file){
		File[] filelist=file.listFiles();
		for(File i:filelist){
			i.delete();
		}
	}
	private void generateStockSet(String code){
		switch(code.charAt(0)){
		case '0':
			addstock(code,SZA);
			break;
		case '2':
			addstock(code,SZB);
			break;
		case '3':
			addstock(code,CYB);
			break;
		case '6':
			addstock(code,SHA);
			break;
		case '9':
			addstock(code,SHB);
			break;
		default:
			System.out.println(code);
		}
	}
	private void makepath(String filepath){
		File file=new File(filepath);
		if(!file.exists()&&!file.isDirectory()){
			file.mkdirs();
		}
	}
	private void addstock(String code,String set){
		File file=new File(set+"/"+code);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
