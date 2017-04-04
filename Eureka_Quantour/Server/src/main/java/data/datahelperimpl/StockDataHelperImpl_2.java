package data.datahelperimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
	public static void main(String[] args){
		new StockDataHelperImpl_2();
	}
	public StockDataHelperImpl_2(){
		t=Translate.getInstance();
		fetch=StockDataFetchImpl.getInstance();
		infopath=new File("config/stock/info");
		if(!infopath.exists()&&!infopath.isDirectory()){
			try {
				fetch.fetchAllStockSet();
				fetch.fetchAllStockInfo();
			} catch (InternetdisconnectException e) {
				System.out.println(e.toString());
			}
			
		}
		long t1=System.currentTimeMillis();
		HashMap<String, HashMap<String, String>> map2;
		map2=getAllStock();
		long t2=System.currentTimeMillis();
		System.out.println("读取时间"+(t2-t1));
		Scanner sc=new Scanner(System.in);
		String str="";
		while(!(str=sc.nextLine()).equals("esc")){
			String cal=str.substring(0,10);
			String code=str.substring(11);
			long ttt1=System.currentTimeMillis();
			System.out.println(map2.get(cal).get(code));
			long ttt2=System.currentTimeMillis();
			System.out.println("读取时间"+(ttt2-ttt1));
		}
	}
	@Override
	public HashMap<String, HashMap<String, String>> getAllStock() {
		try {
			HashMap<String, HashMap<String, String>> map=new HashMap<String, HashMap<String, String>>();
			String[] filelist=infopath.list();
			String str="";
			String cal="";
			for(String stock:filelist){		
				String stockname=t.trans_codeToname(stock);
				BufferedReader br=new BufferedReader(new FileReader("config/stock/info/"+stock+"/data"));
				BufferedReader br_adj=new BufferedReader(new FileReader("config/stock/info/"+stock+"/subscription"));
				while(br.ready()){
					str=br.readLine()+","+br_adj.readLine()+","+stockname;
					cal=str.substring(0, 10);
					if(map.containsKey(cal)){
						map.get(cal).put(stock, str);
					}
					else{
						HashMap<String,String> tempmap=new HashMap<String,String>();
						tempmap.put(stock, str);
						map.put(cal, tempmap);
					}
				}
				br_adj.close();
				br.close();
			}
			return map;
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
//	public BPlusTree getTreeStock() {
//		try {
//			String[] filelist=infopath.list();
//			String str="";
//			String cal="";
//			int count=0;
//			BPlusTree tree=new BPlusTree(15);
//			for(String stock:filelist){		
//				BufferedReader br=new BufferedReader(new FileReader("config/stock/info/"+stock+"/data"));
//				while(br.ready()){
//					str=br.readLine();
//					cal=str.substring(0, 10);
//					tree.insertOrUpdate(Integer.parseInt(encodeDate(cal)), 
//							new AbstractMap.SimpleEntry<String,Integer>(stock,count));
//					count++;
//				}
//				br.close();
//				count=0;
//			}
//			return tree;
//		} catch (IOException e) {
//		// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public HashMap<String, HashMap<String, Integer>> getAllCalendar() {
//		try {
//			HashMap<String, HashMap<String, Integer>> map=new HashMap<String, HashMap<String, Integer>>();
//			String[] filelist=infopath.list();
//			String cal="";
//			int count=0;
//			for(String stock:filelist){		
//				BufferedReader br=new BufferedReader(new FileReader("config/stock/info/"+stock+"/calendar"));
//				while(br.ready()){
//					cal=br.readLine();
//					if(map.containsKey(cal)){
//						map.get(cal).put(stock, count);
//					}
//					else{
//						HashMap<String,Integer> tempmap=new HashMap<String,Integer>();
//						tempmap.put(stock, count);
//						map.put(cal, tempmap);
//					}
//					count++;
//				}
//				br.close();
//				count=0;
//			}
//			return map;
//		} catch (IOException e) {
//		// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public String read(String code,int line){
//		try {
//			BufferedReader br=new BufferedReader(new FileReader("config/stock/info/"+code+"/data"));
//			int count=0;
//			while(line!=count){
//				br.readLine();
//				count++;
//			}
//			return br.readLine();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
}
