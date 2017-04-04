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
import exception.InternetdisconnectException;

public class StockDataHelperImpl_2 implements IStockDataHelper{
	private File infopath;
	private IStockDataFetch fetch;
	public static void main(String[] args){
		new StockDataHelperImpl_2();
	}
	public StockDataHelperImpl_2(){
		fetch=StockDataFetchImpl.getInstance();
		infopath=new File("config/stock/info");
		
//		if(!infopath.exists()&&!infopath.isDirectory()){
//			try {
//				fetch.fetchAllStockSet();
//				fetch.fetchAllStockInfo();
//			} catch (InternetdisconnectException e) {
//				System.out.println(e.toString());
//			}
//			
//		}
		long t1=System.currentTimeMillis();
		HashMap<String, HashMap<String, String>> map=new HashMap<String, HashMap<String, String>>();
		map=getAllStock();
		long t2=System.currentTimeMillis();
		System.out.println("读取时间"+(t2-t1));
		Scanner sc=new Scanner(System.in);
		String str="";
		while(!(str=sc.nextLine()).equals("esc")){
			String cal=str.substring(0,10);
			String code=str.substring(11);
			long tt1=System.currentTimeMillis();
		
			if(map.containsKey(cal)){
				if(map.get(cal).containsKey(code)){
					System.out.println(map.get(cal).get(code));
				}
				else{
					System.out.println("当天没有该股票");
				}
			}
			else{
				System.out.println("没有当天信息");
			}
			long tt2=System.currentTimeMillis();
			System.out.println("读取时间"+(tt2-tt1));
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
				BufferedReader br=new BufferedReader(new FileReader("config/stock/info/"+stock+"/data"));
				while(br.ready()){
					str=br.readLine();
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
}
