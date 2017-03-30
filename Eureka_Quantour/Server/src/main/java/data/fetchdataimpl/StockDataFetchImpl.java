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

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import data.fetchdataservice.IStockDataFetch;
import data.parse.ParseStockName;
import exception.InternetdisconnectException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
/**
 * 为软件扩充股票池和股票
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
	private String ArtificialSet;
	public static void main(String[] args){
		new StockDataFetchImpl();
	}
	public StockDataFetchImpl(){
		SHA="config/stock/stockset/SHA";
		SHB="config/stock/stockset/SHB";
		SZA="config/stock/stockset/SZA";
		SZB="config/stock/stockset/SZB";
		CYB="config/stock/stockset/CYB";
		ArtificialSet="extendsdata/set";
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
		makepath(ArtificialSet);
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
			File backups_HS300=new File("config/backups/HS300List");
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
			if(!backups_HS300.exists()){
				backups_HS300.createNewFile();
				try {
					testInternet();
					saveToFile("115.29.204.48","webdata","000300cons.xls","config/backups/HS300List.xls");
					System.out.println("update by web");
				} catch (InternetdisconnectException e) {
					throw e;
				}
			}
			else{
				BufferedReader br=new BufferedReader(new FileReader(backups_HS300));
				dealdir(new File(HS300));
				while(br.ready()){
					addstock(br.readLine().substring(0, 6),HS300);
				}
				br.close();
				System.out.println("update by backups");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取手工添加的股票池
	 * @return 名字与编号组成的String的列表（格式:"编号名字"）
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	public void getArtificialSet(){
		File file=new File(ArtificialSet);
		File[] filelist=file.listFiles();
		for(File set:filelist){
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
	 * 从ftp上下载文件
	 * @param ftppath ftp地址
	 * @param absolutepath ftp服务器上的路径
	 * @param fileName 文件名
	 * @param localpath 需要存储的路径
	 * @return 文件内容
	 * @throws IOException
	 */
	private String saveToFile(String ftppath, String absolutepath,
			String fileName,String localpath) throws IOException {
		FTPClient ftp=new FTPClient();
		ftp.connect(ftppath);
		ftp.login("anonymous", "");
		int reply = ftp.getReplyCode();    
        if(!FTPReply.isPositiveCompletion(reply)) {    
             ftp.disconnect();
        }   
		ftp.changeWorkingDirectory(absolutepath);
		FTPFile[] fs = ftp.listFiles();  
		for(FTPFile ff:fs){ 
			if(ff.getName().equals("000300cons.xls")){  
				File localFile = new File(localpath);    
                OutputStream is = new FileOutputStream(localFile);     
                ftp.retrieveFile(ff.getName(), is);  
                is.close();    
                break;
			}    
        }  
		String buffer = "";
        try {  
               File file = new File(localpath);
               // 设置读文件编码
               WorkbookSettings setEncode = new WorkbookSettings();
               setEncode.setEncoding("GB2312");
               // 从文件流中获取Excel工作区对象（WorkBook）
               Workbook wb = Workbook.getWorkbook(file,setEncode); 
               Sheet sheet = wb.getSheet(0); 
               for (int i = 1; i < 301; i++) {  
            		 Cell cell = sheet.getCell(0, i);   
            		 buffer += cell.getContents(); 
            		 Cell cell1 = sheet.getCell(1, i); 
            		 buffer += ParseStockName.chkHalf(cell1.getContents());
            		 buffer +="\n";
            		 addstock(cell.getContents(),HS300);
               }  
        } catch (BiffException e) {  
        	e.printStackTrace();  
        } catch (IOException e) {  
        	e.printStackTrace();  
        }   
        //write the string into the file
        String savePath = localpath.substring(0,localpath.length()-4);
        File saveCSV = new File(savePath);
        try {   
        	if(!saveCSV.exists())
        		saveCSV.createNewFile();
        	BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
        	writer.write(buffer);
        	writer.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }        
  		ftp.disconnect();
  		return buffer;
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
