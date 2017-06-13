package data.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
		URL url = null;
		try {
		url = new URL("http://baicu.com/");
		InputStream in = url.openStream();//打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream
			System.out.println("连接正常");
		in.close();//关闭此输入流并释放与该流关联的所有系统资源。
		} catch (IOException e) {
			throw new InternetdisconnectException();
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
		 httpUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		 httpUrl.connect();
		 bis = new BufferedInputStream(httpUrl.getInputStream());
		 File file=new File(fileName);
		 if(!file.exists()){
			file.createNewFile();
		 }
		 BufferedReader br=new BufferedReader(new InputStreamReader(bis));
		 while (!br.ready())    
         {    
              try {
				Thread.sleep(1000*30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // wait for stream to be ready.    
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
	/**
	 * 从网站上爬取信息
	 * @param destUrl 网站地址
	 * @param fileName 存储到的文件路径
	 * @return 网站上的信息
	 * @throws IOException 
	 */
	public String fetchandmatch(String destUrl,String pattern) throws IOException, IllegalStateException{
		BufferedReader bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;	
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		httpUrl.connect();
		bis = new BufferedReader(new InputStreamReader(httpUrl.getInputStream()));
		String total="";
  		while(bis.ready()){
  			String str=bis.readLine();
  			total=total+str;
  		}
  		Pattern pat=Pattern.compile(pattern);
  		Matcher m=pat.matcher(total);
  		m.find();
  		String result=m.group();
  		result=result.substring(result.indexOf("href='")+6,result.length()-18);
  		bis.close();
  		httpUrl.disconnect();
		return result;
	}
	/**
	 * 从网站上爬取信息
	 * @param destUrl 网站地址
	 * @param fileName 存储到的文件路径
	 * @return 网站上的信息
	 * @throws IOException 
	 */
	public List<String> fetchandmatch(String destUrl,String filepath,String pattern) throws IOException, IllegalStateException{
		BufferedReader bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;	
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		httpUrl.connect();
		bis = new BufferedReader(new InputStreamReader(httpUrl.getInputStream()));
		String total="";
  		while(bis.ready()){
  			String str=bis.readLine();
  			total=total+str;
  		}
		String p=pattern;
  		Pattern pat=Pattern.compile(p);
  		Matcher m=pat.matcher(total);
   	
  		m.find();
  		m.find();
  		String right="<td>方案实施</td>";
  		String date="</td><td>";
  		File file=new File(filepath);
  		if(!file.exists()){
  			file.createNewFile();
  		}
  		BufferedWriter bw=new BufferedWriter(new FileWriter(file));
  		List<String> list=new ArrayList<String>();
  		String row="";
  		while(m.find()){
  			String result=m.group();
  			boolean f1=false;
  			boolean f2=false;
  			if(result.indexOf(right)>=0){
  				int z=Integer.parseInt(result.substring(result.indexOf(date)+date.length(), result.indexOf(date)+date.length()+4));
  				if(z<2005){
  					continue;
  				}
  				result=result.substring(result.indexOf(right)+right.length());
  				String pa="<td>.*?</td>";
  		  		Pattern p2=Pattern.compile(pa);
  		  		Matcher mt=p2.matcher(result);
  		  		int count=0;
  		  		while(mt.find()){
  		  			count++;
  		  			String column=mt.group();
  		  			if(count==4){
  		  			}
  		  			else{
  		  				if(count==5&&column.substring(4,column.length()-5).equals("--")){
  		  					f1=true;
  		  				}
  		  				if(count==6&&column.substring(4,column.length()-5).equals("--")){
  		  					f2=true;
  		  				}	
  		  				row=row+column.substring(4,column.length()-5)+";";
  		  			}
  		  		}
  		  		if(!f1||!f2){
  		  			bw.write(row+"\n");
  		  			list.add(row);
  		  		}
  	  	  		row="";
  			}
  		}
  		bw.close();
  		bis.close();
  		httpUrl.disconnect();
		return list;
	}
	public HashMap<String,List<String>> matchIndustry(String path) throws IOException{
		HashMap<String,List<String>> result=new HashMap<String,List<String>>();
		File file=new File(path);
		BufferedReader bis = null;
		bis = new BufferedReader(new FileReader(file));
		String total="";
		while(bis.ready()){
			total=total+bis.readLine();
		}
		String p="<tr>[\\s\\S]*?</tr>";
  		Pattern pat=Pattern.compile(p);
  		Matcher m=pat.matcher(total);
  		String nowIndustry="";
  		while(m.find()){
  			String match=m.group();
  			String p1="[0-9]{6}";
  			String p2="<a\\starget=_blank\\shref=\\'http://quote.cfi.cn/quotelist\\.aspx.*?style.*?>.*?</a>";
  			Pattern pat2=Pattern.compile(p2);
  			Matcher m2=pat2.matcher(match);
  			if(m2.find()){
  				String p3=">.*?<";
  	  			Pattern pat3=Pattern.compile(p3);
  	  			Matcher m3=pat3.matcher(m2.group());
  	  			m3.find();
  	  			List<String> temp=new ArrayList<String>();
  	  			nowIndustry=m3.group().substring(1,m3.group().length()-1);
  				result.put(m3.group().substring(1,m3.group().length()-1), temp);
  				
  			}
  			else{
  				Pattern pat1=Pattern.compile(p1);
  	  			Matcher m1=pat1.matcher(match);
  	  			while(m1.find()){
  	  				String stt=m1.group();
  	  				result.get(nowIndustry).add(stt);
  	  			}
  			}
  		}
  		bis.close();
  		return result;
	}
}
