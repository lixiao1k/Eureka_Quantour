package data.datahelperimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import data.datahelperservice.IStockDataHelper;
import data.fetchdataimpl.StockDataFetchImpl;
import data.fetchdataservice.IStockDataFetch;
import data.parse.Translate;
import exception.InternetdisconnectException;

public class StockDataHelperImpl_2 implements IStockDataHelper{
	private File infopath;
	private IStockDataFetch fetch;
	private Translate t;
	private MappedByteBuffer mbb;
	private MappedByteBuffer mbb2;
	private HashMap<String,HashMap<String,Integer>> map;
	public static void main(String[] args){
		new StockDataHelperImpl_2();
	}
	public StockDataHelperImpl_2(){
		t=Translate.getInstance();
		fetch=StockDataFetchImpl.getInstance();
		infopath=new File("config/stock/info");
		map=new HashMap<String,HashMap<String,Integer>> ();
		if(!infopath.exists()&&!infopath.isDirectory()){
			try {
				fetch.fetchAllStockSet();
				fetch.fetchAllStockInfo();
			} catch (InternetdisconnectException e) {
				System.out.println(e.toString());
			}
			
		}
		long t1=System.currentTimeMillis();
		HashMap<Integer, Entry<Long,Integer>> map2;
		loadDate();
		//getAllStock();
		//check();
		System.out.println("success");
		long t2=System.currentTimeMillis();
		System.out.println("映射到内存的时间"+(t2-t1));
		
		long z1=System.currentTimeMillis();
		//check2();
		System.out.println("success");
		long z2=System.currentTimeMillis();
		System.out.println("测试的时间"+(z2-z1));
		Scanner sc=new Scanner(System.in);
		String str="";
		while(!(str=sc.nextLine()).equals("esc")){
			String[] out=str.split(",");
			long ttt1=System.currentTimeMillis();
			Random random=new Random();
			for(int i=0;i<10000;i++) {
				String date="2017-03-28";
				if(map.containsKey(date)){
					Iterator<Integer> it=map.get(date).values().iterator();
					while(it.hasNext()){
						int entry=it.next();
						readPosition(entry);
					}
				}
			}
//			System.out.println(readPosition(5638085));
			long ttt2=System.currentTimeMillis();
			System.out.println("读取时间"+(ttt2-ttt1));
		}
	}
	@Override
	public HashMap<String, HashMap<String, String>> getAllStock() {
		try {
			HashMap<String, HashMap<String, Entry<Long,Integer>>> map=new HashMap<String, HashMap<String, Entry<Long,Integer>>>();
			String[] filelist=infopath.list();
			String str="";
			String cal="";
			File data=new File("config/resources/mainData");
			File position=new File("config/resources/mainPosition");
			File index=new File("config/resources/mainIndex");
			if(true){
				data.createNewFile();
				index.createNewFile();
				position.createNewFile();
				BufferedWriter bw1=new BufferedWriter(new FileWriter(data));
				BufferedWriter bw2=new BufferedWriter(new FileWriter(index));
				BufferedWriter bw3=new BufferedWriter(new FileWriter(position));
				int count=0;
				long total=0;
				for(String stock:filelist){		
					count++;
					System.out.println(3269-count);
					BufferedReader br=new BufferedReader(new FileReader("config/stock/info/"+stock+"/data"));
					BufferedReader br_adj=new BufferedReader(new FileReader("config/stock/info/"+stock+"/subscription"));		
					int size=0;
					while(br.ready()){
						str=br.readLine()+","+br_adj.readLine()+"\n";
						bw1.write(str);
						size=str.length();
						cal=str.substring(0, 10);
						bw2.write(cal+","+stock+"\n");
						bw3.write(String.format("%02d", size-1)+","+String.format("%09d", total)+"\n");
						total=total+size;
					}
					
					br_adj.close();
					br.close();
				}
				System.out.println(total);
				bw1.close();
				bw2.close();
				bw3.close();
			}
			else{
				
			}
			return null;
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public String getNameToCode(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 将XXXX-XX-XX的日期形式转为XXXXXXXX
	 * @param str 原形式日期
	 * @return 转换后的日期
	 */
	private String encodeDate(String str){
		return str.substring(0, 4)+str.substring(5,7)+str.substring(8);
	}
	private void loadDate(){
		try {
			long ttt1=System.currentTimeMillis();	
			FileInputStream is=new FileInputStream("config/resources/mainData");
			FileChannel fc=is.getChannel();
			System.out.println(fc.size());
			mbb=fc.map(MapMode.READ_ONLY, 0, fc.size());
			long ttt2=System.currentTimeMillis();
			System.out.println("获取数据时间"+(ttt2-ttt1));	
			is.close();
			FileInputStream is2=new FileInputStream("config/resources/mainPosition");
			FileChannel fc2=is2.getChannel();
			System.out.println(fc2.size());
			mbb2=fc2.map(MapMode.READ_ONLY, 0, fc2.size());
			long tttt2=System.currentTimeMillis();
			System.out.println("获取数据时间"+(tttt2-ttt1));	
			is2.close();
			BufferedReader br=new BufferedReader(new FileReader("config/resources/mainIndex"));
			int count=0;
			while(br.ready()){
				String[] out=br.readLine().split(",");
				String cal=out[0];
				String code=out[1];
				if(map.containsKey(cal)){
					map.get(cal).put(code, count);
				}
				else{
					HashMap<String,Integer> temp=new HashMap<String,Integer>();
					temp.put(code,count);
					map.put(cal, temp);
				}
				count++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	private String readPosition(int a){
		int position=a*14;
		byte[] dst1=new byte[3];
		byte[] dst2=new byte[9];
		mbb2.position(position);
		mbb2.get(dst1);
		mbb2.position(position+4);
		mbb2.get(dst2);
		String b=new String(dst1);
		String c=new String(dst2);
		return read(Integer.parseInt(c),Integer.parseInt(b));
	}
	private String read(int a,int b){
		byte[] dst=new byte [b];
		mbb.position(a);
		mbb.get(dst);
		String t=new String(dst);
		return t;
	}
	public String randomDateBetweenMinAndMax(){  
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	       Calendar calendar = Calendar.getInstance();  
	       //注意月份要减去1  
	       calendar.set(2015,1,1);  
	       calendar.getTime().getTime();  
	       //根据需求，这里要将时分秒设置为0  
	       calendar.set(Calendar.HOUR_OF_DAY, 0);  
	       calendar.set(Calendar.MINUTE, 0);  
	       calendar.set(Calendar.SECOND,0);  
	       long min = calendar.getTime().getTime();;  
	       calendar.set(2017,2,29);  
	       calendar.set(Calendar.HOUR_OF_DAY,0);  
	       calendar.set(Calendar.MINUTE,0);  
	       calendar.set(Calendar.SECOND,0);  
	       calendar.getTime().getTime();  
	       long max = calendar.getTime().getTime();  
	       //得到大于等于min小于max的double值  
	       double randomDate = Math.random()*(max-min)+min;  
	       //将double值舍入为整数，转化成long类型  
	       calendar.setTimeInMillis(Math.round(randomDate));  
	       return sdf.format(calendar.getTime());  
	}  
	private void check(){
		try{
			BufferedReader br1=new BufferedReader(new FileReader("config/resources/mainData"));
			BufferedReader br2=new BufferedReader(new FileReader("config/resources/mainPosition"));
			int count =0;
			int total = 0;
			while(br1.ready()){
				count++;
				String t1=br1.readLine();
				int size1=t1.length();
				String[] t2=br2.readLine().split(",");
				int size2=Integer.parseInt(t2[0]);
				int t=Integer.parseInt(t2[1]);
				if(size1!=size2){
					System.out.println(count+":   size1="+size1+",size2="+(size2)+"\n"+t1);
					System.exit(0);
				}
				if(t!=total){
					System.out.println(count+"::"+total+t1);
					System.exit(0);
				}
				if(!read(total,size2).equals(t1)){
					System.out.println(count+":"+total+t1);
					System.exit(0);
				}
				total=total+size1+1;
			}
			br1.close();
			br2.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void check2(){
		try {
			String[] filelist=infopath.list();
			String str="";
			String cal="";
			if(true){
				int count=0;
				long total=0;
				for(String stock:filelist){		
					count++;
					System.out.println(3269-count);
					BufferedReader br=new BufferedReader(new FileReader("config/stock/info/"+stock+"/data"));
					BufferedReader br_adj=new BufferedReader(new FileReader("config/stock/info/"+stock+"/subscription"));	
					BufferedReader br_adj1=new BufferedReader(new FileReader("config/stock/info/"+stock+"/afterscription"));	
					while(br.ready()){
						str=br.readLine()+","+br_adj.readLine()+","+br_adj1.readLine();
						cal=str.substring(0, 10);
						str=str.substring(11);
						int row=0;
						try{
							row=map.get(cal).get(stock);
							String str1=readPosition(row);
							if(str1==null){
								System.out.println(cal+":"+stock+":"+row+"\nnull!!!!!");
								System.exit(0);
							}
							if(!str1.equals(str)){
								System.out.println(cal+":"+stock+":"+row);
								System.exit(0);
							}
						}catch(Exception e){
								System.out.println(cal+":"+stock+":"+row+"\nnull!!!!!");
								System.exit(0);
							
						}
						
						
					}	
					br_adj.close();
					br.close();
					br_adj1.close();
				}
				System.out.println(total);
			}
			else{
				
			}
			return;
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
}
