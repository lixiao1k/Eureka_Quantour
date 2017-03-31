package data.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import exception.InternetdisconnectException;
/**
 * 涉及到网络抓取的公用方法
 * @author 刘宇翔
 *
 */
public class WebMethod {
	private static WebMethod p;
	private WebMethod(){
	}
	public static WebMethod getInstance(){
		if(p==null) p=new WebMethod();
		return p;
	}
	/**
	 * 判断网络是否可连接
	 * @throws InternetdisconnectException
	 */
	public  void testInternet() throws InternetdisconnectException{
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
	public String saveToFile_ByBufferedReader(String destUrl, String fileName) throws IOException {
		BufferedWriter fos = null;
		BufferedReader bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		httpUrl.connect();
		bis = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(),"GBK"));
		File file=new File(fileName);
		 if(!file.exists()){
			file.createNewFile();
		 }
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
	public void saveToFile_ByInputStream(String destUrl, String fileName) throws IOException {
		 int BUFFER_SIZE = 8096;
		 FileOutputStream fos = null;
		 BufferedInputStream bis = null;
		 HttpURLConnection httpUrl = null;
		 URL url = null;
		 byte[] buf = new byte[BUFFER_SIZE];
		 int size = 0;
		  
		 url = new URL(destUrl);
		 httpUrl = (HttpURLConnection) url.openConnection();
		 httpUrl.connect();
		 bis = new BufferedInputStream(httpUrl.getInputStream());
		 File file=new File(fileName);
		 if(!file.exists()){
			file.createNewFile();
		 }
		 fos = new FileOutputStream(fileName);
		 while ((size = bis.read(buf)) != -1){
			 fos.write(buf, 0, size);
		 }
		   
		 fos.close();
		 bis.close();
		 httpUrl.disconnect();
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
	public void saveToFile(String ftppath, String absolutepath,
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
			if(ff.getName().equals(fileName)){  
				File localFile = new File(localpath);   
				if(!localFile.exists()){
					localFile.createNewFile();
				}
                OutputStream is = new FileOutputStream(localFile);     
                ftp.retrieveFile(ff.getName(), is);  
                is.close();    
                break;
			}    
        }   
  		ftp.disconnect();
	}
	
	/**
	 * 用正则表达式匹配string中的某段内容
	 * @param pattern 正则表达式
	 * @param total 内容
	 * @return 匹配到的所有内容
	 */
	public List<String> match(String pattern,String total){
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
}
