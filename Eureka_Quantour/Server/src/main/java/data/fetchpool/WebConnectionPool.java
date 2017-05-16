package data.fetchpool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import data.common.WebMethod;

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
      
    private int maxActiveConnections = 400;// 最大允许的连接数，和数据库对应  
      
    private long connectionTimeOut = 1000*60*20;// 连接超时时间，默认20分钟  
    
    private long lazyCheck = 1000*60*1;// 延迟多少时间后开始 检查  
    private long periodCheck = 1000*30;// 检查频率  
    
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
            for(int i=0;i<fc.size();i++){
            	if(!fc.get(i).isStatus()){
            		System.out.println(fc.get(i).getUrl());
            		count++;
            	}
            }
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
    }
}
