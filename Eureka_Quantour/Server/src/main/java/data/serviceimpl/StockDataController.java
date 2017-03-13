package data.serviceimpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import data.datahelperimpl.StockDataHelperImp;
import data.datahelperservice.IStockDataHelper;
import po.SingleStockInfoPO;

/**
 * 股票模块方法的实现
 * @author 刘宇翔
 *
 */
public class StockDataController {
	private IStockDataHelper stockdatahelper;
	private static StockDataController stockdata;
	public HashMap<String,HashMap<String,String>> stockinfo_StringType;
	public HashMap<Calendar,List<SingleStockInfoPO>> processmap;
	private SimpleDateFormat sdf;
	private boolean process_data;
	public HashMap<String,HashMap<Calendar,String>> singlestockmap;
	private boolean single_data;
	public HashMap<String,List<String>> singlesortmap;
	public HashMap<String,HashMap<Calendar,Integer>> sortmap;
	private boolean sort_data;
	private StockDataController(){
		stockdatahelper=StockDataHelperImp.getInstance();
		stockinfo_StringType=stockdatahelper.getAllStock();
		processmap=new HashMap<Calendar,List<SingleStockInfoPO>>();
		singlestockmap=new HashMap<String,HashMap<Calendar,String>>();
		process_data=false;
		single_data=false;
		sort_data=false;
		singlesortmap=new HashMap<String,List<String>>();
		sortmap=new HashMap<String,HashMap<Calendar,Integer>>();
		sdf=new SimpleDateFormat("MM/dd/yy");
		process_thread tt=new process_thread();
		Thread t=new Thread(tt);
		t.start();
		single_thread tt1=new single_thread();
		Thread t1=new Thread(tt1);
		t1.start();
	}
	public static StockDataController getInstance(){
		if(stockdata==null) stockdata=new StockDataController();
		return stockdata;
	}
	public List<SingleStockInfoPO> getSingleStockInfo_unprocess(String stockcode, Calendar begin, Calendar end) {
		Calendar i=begin;
		List<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		Boolean hascalendar=false;
		Boolean hascode=false;
		while(i.compareTo(end)<=0){
			hascalendar=stockinfo_StringType.containsKey(tostring(sdf.format(i.getTime())));
			if(hascalendar){
				hascode=stockinfo_StringType.get(tostring(sdf.format(i.getTime()))).containsKey(stockcode);
				if(hascode){
					list.add(new SingleStockInfoPO
							(stockinfo_StringType.get(
									tostring(sdf.format(i.getTime()))).get(stockcode)));
				}
			}
			i.set(Calendar.DATE, i.get(Calendar.DATE)+1);
		}
		return list;
	}
	private String tostring(String str){
		String[] out=str.split("/");
		if(out[0].length()==2&&out[0].charAt(0)=='0'){
			out[0]=out[0].substring(1);
		}
		if(out[1].length()==2&&out[1].charAt(0)=='0'){
			out[1]=out[1].substring(1);
		}
		return out[0]+"/"+out[1]+"/"+out[2];
	}
	public List<SingleStockInfoPO> getMarketByDate(Calendar date){
		Iterator<Entry<Calendar, List<SingleStockInfoPO>>> it=processmap.entrySet().iterator();
		while(it.hasNext()){
			System.out.println(sdf.format(it.next().getKey().getTime()));
		}
		if(process_data){
			if(!processmap.containsKey(date)){
				System.out.println("error");
				return null;
			}
			for(SingleStockInfoPO po:processmap.get(date)) System.out.println(po.toString());
			return processmap.get(date);
		}
		else{
			return processMarketByDate(date);
		}
	}
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode, Calendar begin, Calendar end){
		System.out.println(processmap.size());
		if(sort_data){
			return getSingleAftersort(stockcode, begin, end);
		}
		else if(single_data){
			return getSingle(stockcode, begin, end);
		}
		else{
			return getSingleStockInfo_unprocess(stockcode, begin, end);
		}
	}
	public List<SingleStockInfoPO> getSingle(String stockcode, Calendar begin, Calendar end) {
		if(!singlestockmap.containsKey(stockcode)){
			return null;
		}
		HashMap<Calendar,String> map=singlestockmap.get(stockcode);
		Calendar i=begin;
		List<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		while(i.compareTo(end)<=0){
			if(map.containsKey(i)){
				list.add(new SingleStockInfoPO(map.get(i)));
			}
			i.set(Calendar.DATE, i.get(Calendar.DATE)+1);
		}
		return list;
	}
	public List<SingleStockInfoPO> getSingleAftersort(String code, Calendar begin, Calendar end) {
		if(!sortmap.containsKey(code)){
			return null;
		}
		try {
			Calendar head=Calendar.getInstance();
			head.setTime(sdf.parse("2/1/05"));
			Calendar tail=Calendar.getInstance();
			tail.setTime(sdf.parse("4/29/14"));
			if(begin.compareTo(head)<0){
				begin.setTime(head.getTime());
			}
			
			if(end.compareTo(tail)>0){
				end.setTime(tail.getTime());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(begin.compareTo(end)>0){
			return null;
		}
		List<String> list=singlesortmap.get(code);
		HashMap<Calendar,Integer> map=sortmap.get(code);
		while(begin.compareTo(end)<=0){
			if(map.containsKey(begin)){
				break;
			}
			else{
				begin.set(Calendar.DATE, begin.get(Calendar.DATE)+1);
			}
		}
		int i=0;
		int j=0;
		if(map.containsKey(begin)){
			i=sortmap.get(code).get(begin);
		}
		else{
			return null;
		}
		while(end.compareTo(begin)>=0){
			if(map.containsKey(end)){
				break;
			}
			else{
				end.set(Calendar.DATE, end.get(Calendar.DATE)-1);
			}
		}
		if(map.containsKey(end)){
			j=sortmap.get(code).get(end);
		}
		else{
			return null;
		}
		List<SingleStockInfoPO> list1=new ArrayList<SingleStockInfoPO>();
		for(int k=i;k<=j;k++){
			list1.add(new SingleStockInfoPO(list.get(k)));
		}
		return list1;
	}
	public List<SingleStockInfoPO> processMarketByDate(Calendar date) {
		String string_date=tostring(sdf.format(date.getTime()));
		if(!stockinfo_StringType.containsKey(string_date)){
			return null;
		}
		HashMap<String,String> datamap=stockinfo_StringType.get(string_date);
		Iterator<Entry<String, String>> it=datamap.entrySet().iterator();
		ArrayList<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		while(it.hasNext()){
			String info=it.next().getValue();
			list.add(new SingleStockInfoPO(info));
		}
		return list;
	}
	class process_thread implements Runnable{
		@Override
		public void run() {
			go();
		}
		private void go(){
			Iterator<String> it=stockinfo_StringType.keySet().iterator();
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
			while(it.hasNext()){
				String entry=it.next();
				Calendar cal=Calendar.getInstance();
				try {
					cal.setTime(sdf.parse(entry));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				processmap.put(cal, processMarketByDate(cal));
			}
			process_data=true;
			System.out.println("处理完成");
		}
	}
	class single_thread implements Runnable{
		@Override
		public void run() {
			System.out.println("开始整理按单支股票获得数据");
			long start_time=System.currentTimeMillis();
			go();
			long end_time=System.currentTimeMillis();
			System.out.println("花费在整理单支股票上的时间为: "+(-start_time+end_time)+" ms");
			sort_thread tt1=new sort_thread();
			Thread t1=new Thread(tt1);
			t1.start();
		}
		private void go(){
			Iterator<Entry<String, HashMap<String, String>>> it1=stockinfo_StringType.entrySet().iterator();
			while(it1.hasNext()){
				Entry<String, HashMap<String, String>> entry=it1.next();
				Iterator<Entry<String, String>> it=entry.getValue().entrySet().iterator();
				SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
				Calendar cal=Calendar.getInstance();
				try {
					cal.setTime(sdf.parse(entry.getKey()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String code="";
				String str="";
				while(it.hasNext()){
					Entry<String, String> e=it.next();
					code=e.getKey();
					str=e.getValue();
					if(singlestockmap.containsKey(code)){
						singlestockmap.get(code).put(cal, str);
					}
					else{
						HashMap<Calendar,String> m=new HashMap<Calendar,String>();
						m.put(cal, str);
						singlestockmap.put(code, m);
					}
				}
			}
			single_data=true;
		}
	}
	class sort_thread implements Runnable{
		@Override
		public void run() {
			System.out.println("开始对股票数据进行排序");
			long start_time=System.currentTimeMillis();
			go();
			long end_time=System.currentTimeMillis();
			System.out.println("花费在排序股票上的时间为: "+(-start_time+end_time)+" ms");
		}
		private void go(){
			Iterator<Entry<String, HashMap<Calendar, String>>> it1=singlestockmap.entrySet().iterator();
			while(it1.hasNext()){
				Entry<String, HashMap<Calendar, String>> e=it1.next();
				HashMap<Calendar, String> stock_i=e.getValue();	
				Set<Entry<Calendar,String>> set=stock_i.entrySet();
				List<Entry<Calendar,String>> aList = new LinkedList<Entry<Calendar,String>>(set);     
		        Collections.sort(aList, new Comparator<Entry<Calendar,String>>(){  
		            @Override   
		            public int compare(Entry<Calendar,String> ele1,Entry<Calendar,String> ele2){  
		            	return ele1.getKey().compareTo(ele2.getKey());   
		           }  
		        });   
//		        HashMap<Calendar,String> aMap2 = new LinkedHashMap<Calendar,String>();  
		        List<String> list=new ArrayList<String>();
		        HashMap<Calendar,Integer> aMap1 = new LinkedHashMap<Calendar,Integer>();
		        int i=0;
		        for(Entry<Calendar,String> entry: aList){  
		            list.add(entry.getValue());
		            aMap1.put(entry.getKey(), i);
		            i++;
		        }
				singlesortmap.put(e.getKey(), list);
				sortmap.put(e.getKey(), aMap1);
			}
			sort_data=true;
			System.out.println(single_data);
		}
	}
}
