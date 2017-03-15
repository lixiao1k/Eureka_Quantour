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
	private IStockDataHelper stockdatahelper;//获取datahelper层的服务
	private static StockDataController stockdata;//单例模式的对象

	private SimpleDateFormat sdf;//日期的转化格式
	
	private HashMap<String,HashMap<String,String>> stockinfo_StringType;//服务器初始启动时提供的map表
	private HashMap<Calendar,List<String>> processmap;//经过处理后为getMarket方法提供的map表
	private HashMap<String,HashMap<Calendar,String>> singlestockmap;//数据初次处理后为getSingleStock方法提供的map表
	private HashMap<String,List<String>> singlesortmap;//数据二次处理后为getSingleStock方法提供的map表
	private HashMap<String,HashMap<Calendar,Integer>> sortmap;//数据二次处理后为getSingleStock方法提供的顺序表
	
	private boolean process_data;//判断为getMarket方法提供的map表是否就绪的boolean变量
	private boolean single_data;//判断为getSingleStock方法提供的map表初次处理是否完成的boolean变量
	private boolean sort_data;//判断为getSingleStock方法提供的map表二次处理是否完成的boolean变量
	
	/**
	 * 类的初始化
	 */
	private StockDataController(){
		//获得服务
		stockdatahelper=StockDataHelperImp.getInstance();
		System.out.println("开始取数据");
		long start_time=System.currentTimeMillis();
		stockinfo_StringType=stockdatahelper.getAllStock();
		long end_time=System.currentTimeMillis();
		System.out.println("花费在取数据上的时间为: "+(-start_time+end_time)+" ms");
		
		//初始化相关变量
		processmap=new HashMap<Calendar,List<String>>();
		singlestockmap=new HashMap<String,HashMap<Calendar,String>>();
		process_data=false;
		single_data=false;
		sort_data=false;
		singlesortmap=new HashMap<String,List<String>>();
		sortmap=new HashMap<String,HashMap<Calendar,Integer>>();
		sdf=new SimpleDateFormat("MM/dd/yy");


		
		//打开对于getsingle处理的线程
		single_thread tt1=new single_thread();
		Thread t1=new Thread(tt1);
		t1.start();
	}
	
	public static StockDataController getInstance(){
		if(stockdata==null) stockdata=new StockDataController();
		return stockdata;
	}
	
	/**
	 * 获取某只股票一段时间内的所有信息
	 * @param stockcode	股票代码
	 * @param begin 起始日期
	 * @param end	终止日期
	 * @return List<SingleStockInfoPO>一系列股票对象
	 */
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode, Calendar begin, Calendar end){
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
	
	/**
	 * singlethread未处理完成时，getMarket调用的方法
	 * @param stockcode	股票代码
	 * @param begin 起始日期
	 * @param end	终止日期
	 * @return List<SingleStockInfoPO>一系列股票对象
	 */
	private List<SingleStockInfoPO> getSingleStockInfo_unprocess(String stockcode, Calendar begin, Calendar end) {
		
		//如果开始时间小于最小日期或终止时间大于最大日期，将最小(最大)日期代替开始(终止日期)
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
		
		//如果开始时间大于终止时间，直接返回null
		if(begin.compareTo(end)>0){
			return null;
		}
		
		Calendar i=begin;
		List<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		Boolean hascalendar=false;
		Boolean hascode=false;
		
		//循环判断日期是否在表中，是则取出
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
		if(list.size()==0){
			return null;
		}
		return list;
	}
	
	/**
	 * sortthread未处理完成时，getMarket调用的方法
	 * @param stockcode	股票代码
	 * @param begin 起始日期
	 * @param end	终止日期
	 * @return List<SingleStockInfoPO>一系列股票对象
	 */
	private List<SingleStockInfoPO> getSingle(String stockcode, Calendar begin, Calendar end) {
		if(!singlestockmap.containsKey(stockcode)){
			return null;
		}
		if(begin.compareTo(end)>0){
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
		HashMap<Calendar,String> map=singlestockmap.get(stockcode);
		Calendar i=begin;
		List<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		while(i.compareTo(end)<=0){
			if(map.containsKey(i)){
				list.add(new SingleStockInfoPO(map.get(i)));
			}
			i.set(Calendar.DATE, i.get(Calendar.DATE)+1);
		}
		if(list.size()==0){
			return null;
		}
		return list;
	}
	
	/**
	 * sortthread处理完成后，getMarket调用的方法
	 * @param stockcode	股票代码
	 * @param begin 起始日期
	 * @param end	终止日期
	 * @return List<SingleStockInfoPO>一系列股票对象
	 */
	private List<SingleStockInfoPO> getSingleAftersort(String code, Calendar begin, Calendar end) {
		
		//如果不存在该支股票放回null
		if(!sortmap.containsKey(code)){
			return null;
		}
		
		//将开头日期和结束日期规范
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
		
		//如果开始时间大于结束时间，返回null
		if(begin.compareTo(end)>0){
			return null;
		}
		
		
		List<String> list=singlesortmap.get(code);
		HashMap<Calendar,Integer> map=sortmap.get(code);
		
		//找到begin存在的日期
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
		
		//找到end存在的日期
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
		
		//获取列表
		for(int k=i;k<=j;k++){
			list1.add(new SingleStockInfoPO(list.get(k)));
		}
		return list1;
	}
	
	/**
	 * 根据日期获得市场信息
	 * @param date 日期
	 * @return List<SingleStockInfoPO>，当天所有股票信息
	 */
	public List<SingleStockInfoPO> getMarketByDate(Calendar date){
		if(process_data){
			if(!processmap.containsKey(date)){
				System.out.println("error");
				return null;
			}
			else{
				List<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
				for(String str:processmap.get(date)){
					list.add(new SingleStockInfoPO(str,1,1));
				}
				return list;
			}
			
		}
		else{
			return processMarketByDate(date);
		}
	}
	/**
	 * processthread未处理完成时，getMarket调用的方法
	 * @param date，所选的时间
	 * @return List<SingleStockInfoPO>一系列股票对象
	 */
	private List<SingleStockInfoPO> processMarketByDate(Calendar date) {
		String string_date=tostring(sdf.format(date.getTime()));
		if(!stockinfo_StringType.containsKey(string_date)){
			return null;
		}
		HashMap<String,String> datamap=stockinfo_StringType.get(string_date);
		Iterator<Entry<String, String>> it=datamap.entrySet().iterator();
		ArrayList<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		while(it.hasNext()){
			Entry<String,String> e=it.next();
			String info=e.getValue();
			String code=e.getKey();
			list.add(new SingleStockInfoPO(info,getLastClose(string_date,code)));
		}
		return list;
	}
	private double getLastClose(String calendar,String code){
		try {
			Calendar date=Calendar.getInstance();
			date.setTime(sdf.parse(calendar));
			int i=0;
			while(i<7){
				i++;
				date.set(Calendar.DATE, date.get(Calendar.DATE)-1);
				String cal=tostring(sdf.format(date.getTime()));
				if(stockinfo_StringType.containsKey(cal)){
					if(stockinfo_StringType.get(cal).containsKey(code)){
						return Double.parseDouble(stockinfo_StringType.get(cal).get(code).split("\t")[5]);
					}
				}
			}
			return 0;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	private List<String> processMarketByDate_String(Calendar date) {
		String string_date=tostring(sdf.format(date.getTime()));
		if(!stockinfo_StringType.containsKey(string_date)){
			return null;
		}
		HashMap<String,String> datamap=stockinfo_StringType.get(string_date);
		Iterator<Entry<String, String>> it=datamap.entrySet().iterator();
		ArrayList<String> list=new ArrayList<String>();
		while(it.hasNext()){
			Entry<String,String> e=it.next();
			String info=e.getValue();
			String code=e.getKey();
			int sort=sortmap.get(code).get(date);
			if(sort==0){
				list.add(info+"\t"+0);
			}
			else{
				list.add(info+"\t"+singlesortmap.get(code).get(sort-1).split("\t")[5]);
			}
		}
		return list;
	}
	
	//对于初始map表进行便于getMarket方法查找的简化
	class process_thread implements Runnable{
		@Override
		public void run() {
			System.out.println("开始整理按天数获得市场数据");
			long start_time=System.currentTimeMillis();
			go();
			long end_time=System.currentTimeMillis();
			System.out.println("花费在整理按天数获得市场股票上的时间为: "+(-start_time+end_time)+" ms");
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
				processmap.put(cal, processMarketByDate_String(cal));
			}
			process_data=true;
			System.out.println("处理完成");
		}
	}
	
	//对于初始map表进行便于getsinglestock方法查找的初步简化
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
	
	//对于初始map表进行便于getsinglestock方法查找的排序简化
	class sort_thread implements Runnable{
		@Override
		public void run() {
			System.out.println("开始对股票数据进行排序");
			long start_time=System.currentTimeMillis();
			go();
			long end_time=System.currentTimeMillis();
			System.out.println("花费在排序股票上的时间为: "+(-start_time+end_time)+" ms");
			
			//打开对于getmarket处理的线程
			process_thread tt=new process_thread();
			Thread t=new Thread(tt);
			t.start();
		}
		private void go(){
			Iterator<Entry<String, HashMap<Calendar, String>>> it1=singlestockmap.entrySet().iterator();
			while(it1.hasNext()){
				Entry<String, HashMap<Calendar, String>> e=it1.next();
				HashMap<Calendar, String> stock_i=e.getValue();	
				Set<Entry<Calendar,String>> set=stock_i.entrySet();
				List<Entry<Calendar,String>> aList = new LinkedList<Entry<Calendar,String>>(set);     
		        //对于calendar表数据进行排序
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
	
	/**
	 * 将日期转换为数据来源的形式
	 * @param str	正规的日期
	 * @return 数据来源形式的日期
	 */
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
}
