package data.fetchpool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.cj.jdbc.PreparedStatement;

import data.common.WebMethod;
import data.database.ConnectionPoolManager;

public class WebConnectionPool {
	 // 连接池配置属性  
    private List<FetchConnection> fc;  
    private List<Integer> blank;
    private boolean isActive = false; // 连接池活动状态  
    private int contActive = 0;// 记录创建的总的连接数  
      
    private int minConnections = 1; // 空闲池，最小连接数  
    private int maxConnections = 10; // 空闲池，最大连接数  
      
    private int initConnections = 30;// 初始化连接数  
      
    private long connTimeOut = 1000;// 重复获得连接的频率  
      
    private int maxActiveConnections = 100;// 最大允许的连接数，和数据库对应  
      
    private long connectionTimeOut = 1000*60*20;// 连接超时时间，默认20分钟  
    
    private long lazyCheck = 1000*30;// 延迟多少时间后开始 检查  
    private long periodCheck = 1000*10;// 检查频率  
    
    public WebConnectionPool() {  
        super();  
        this.fc=new ArrayList<FetchConnection>();
        this.blank=new ArrayList<Integer>();
        init();   
        cheackPool();
    }  
    // 初始化  
    public void init() {  
    }  
  
    // 获得连接  
    public synchronized int getConnection(String url,String path,String type) {   
            // 判断是否超过最大连接数限制  
             this.fc.add(new FetchConnection(url,type,path,this.fc.size(),true));
             return fc.size()-1;
    }  
    // 获得连接  
    public synchronized int getConnection(String url,String path,String type,String code,boolean isA,String url2) {   
            // 判断是否超过最大连接数限制  
             this.fc.add(new FetchConnection(url,type,path,this.fc.size(),true,code,isA,url2));
             return fc.size()-1;
    }  
  
    public void startConnection(int index){
    	if(contActive<maxActiveConnections){
    		fetchThread t=new fetchThread(this.fc.get(index));
        	Thread tt=new Thread(t);
        	tt.start();
        	contActive++;
    	}
    	else{
    		blank.add(index);
    	}
    }
    // 释放连接  
    public synchronized void releaseConn(int index) {  
        	this.fc.get(index).setStatus(false);
        	contActive--;
        	if(blank.size()>0){
        		int i=blank.get(0);
        		blank.remove(0);
        		startConnection(i);
        	}
    }  
    
    
    
    // 连接池状态  
    public boolean isActive() {  
        return isActive;  
    }
    public void cheackPool() {   
            new Timer().schedule(new TimerTask() {  
            @Override  
            public void run() {  
            // 1.对线程里面的连接状态  
            // 2.连接池最小 最大连接数  
            // 3.其他状态进行检查，因为这里还需要写几个线程管理的类，暂时就不添加了
            	int count=0;
//            	for(int i=0;i<fc.size();i++){
//            		if(fc.get(i).isStatus()){
//            			System.out.println(fc.get(i).getCode());
//            		}
//            	}
            System.out.println("空线池连接数："+blank.size()); 
            System.out.println("关闭的连接数"+count);
            System.out.println("活动连接数：："+contActive);  
            System.out.println("总的连接数："+fc.size());  
            System.out.println("--------------------------------------------------------");
                }  
            },lazyCheck,periodCheck);   
    }  
    
    class fetchThread implements Runnable{
    	FetchConnection tfc;
		public fetchThread(FetchConnection fc){
			this.tfc=fc;
		}
    	@Override
		public void run() {
			String type=tfc.getType();
			if(type.equals("InputStream")){
				try {
					try{
						WebMethod.getInstance().saveToFile_ByInputStream(tfc.getUrl(), tfc.getTargetpath());
					}catch(SocketException e){
						e.printStackTrace();
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
					releaseConn(tfc.getIndex());
			}
			else if(type.equals("DataBase")){
				try {
					fetchAndmatch1(tfc.getUrl(), tfc.getCode());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						fetchAndmatch1(tfc.getUrl(), tfc.getCode());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				try {
					fetchAndmatch2(tfc.getUrl2(),tfc.getCode(),tfc.isA());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						fetchAndmatch2(tfc.getUrl2(),tfc.getCode(),tfc.isA());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				releaseConn(tfc.getIndex());
			}
			else{
				try {
					WebMethod.getInstance().saveToFile_ByBufferedReader(tfc.getUrl(), tfc.getTargetpath());
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
					releaseConn(tfc.getIndex());


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
      		String pattern="<tr.*?>.*?</tr>";
      		List<String> date=new ArrayList<String>();
      		List<Double> basic=new ArrayList<Double>();
      		List<Double> netasset=new ArrayList<Double>();
      		for(String row:match(total,pattern)){
      			if(row.indexOf("截止日期")>=0){
      				for(String day:match(row,"[0-9]{4}-[0-9]{2}-[0-9]{2}")){
      					date.add(day);
      				}
      			}
      			if(row.indexOf("基本每股收益")>=0){
      				for(String day:match(row,"[0-9]+\\.[0-9]+|--")){
      					if(day.equals("--")){
      						basic.add(0.0);
      					}
      					else{
      						basic.add(Double.valueOf(day));
      					}
      				}
      			}
      			if(row.indexOf("每股净资产")>=0){
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
    			System.out.println(code+tfc.getUrl());
    			System.out.println(date.size()+":"+basic.size()+":"+netasset.size());
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
      		String pattern="<tr.*?>.*?</tr>";
      		List<String> date=new ArrayList<String>();
      		List<Long> basic=new ArrayList<Long>();
      		List<Long> flu=new ArrayList<Long>();
      		for(String row:match(total,pattern)){
      			if(row.indexOf("截止日期")>=0){
      				for(String day:match(row,"[0-9]{4}-[0-9]{2}-[0-9]{2}")){
      					date.add(day);
      				}
      			}
      			if(row.indexOf("总股本")>=0){
      				for(String day:match(row,"[0-9]+\\.[0]{2}|--")){
      					if(day.equals("--")){
      						basic.add((long) 0);
      					}
      					else{
      						basic.add(Long.valueOf(day.substring(0, day.length()-3)));
      					}
      				}
      			}
      			if(row.indexOf("1)流通A股")>=0&&isA){
      				for(String day:match(row,"[0-9]+\\.[0]{2}|--")){
      					if(day.equals("--")){
      						flu.add((long) 0);
      					}
      					else{
      						flu.add(Long.valueOf(day.substring(0, day.length()-3)));
      					}
      				}
      			}
      			if(row.indexOf("1)流通B股")>=0&&!isA){
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
    			System.out.println(code+tfc.getUrl2());
    			System.out.println(date.size()+":"+basic.size()+":"+flu.size());
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
}
