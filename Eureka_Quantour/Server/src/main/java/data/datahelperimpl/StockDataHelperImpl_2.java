package data.datahelperimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import data.datahelperservice.IStockDataHelper;
import data.fetchdataimpl.StockDataFetchImpl;
import data.fetchdataservice.IStockDataFetch;
import exception.InternetdisconnectException;

public class StockDataHelperImpl_2 implements IStockDataHelper{
	private File infopath;
	private IStockDataFetch fetch;
	public StockDataHelperImpl_2(){
		fetch=StockDataFetchImpl.getInstance();
		infopath=new File("config/stock/info");
		if(!infopath.exists()&&infopath.isDirectory()){
			try {
				fetch.fetchAllStockSet();
				fetch.fetchAllStockInfo();
			} catch (InternetdisconnectException e) {
				System.out.println(e.toString());
			}
			
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
