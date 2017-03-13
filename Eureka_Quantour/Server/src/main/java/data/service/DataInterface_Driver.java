package data.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import data.serviceimpl.DataInterfaceImpl;
import data.serviceimpl.StockDataController;
import po.SingleStockInfoPO;

public class DataInterface_Driver {
	public static void main(String[] args){
		
		
    	new DataInterface_Driver();
//		Scanner in=new Scanner(System.in);
//		long start_time=System.currentTimeMillis();
//		System.out.println(start_time);	
//		IDataInterface data=new DataInterfaceImpl();
//		long end_time=System.currentTimeMillis();
//		System.out.println(end_time);
//		System.out.println("runtime: "+(-start_time+end_time)+" ms");
//		String input="";
//		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
//		while(!(input=in.nextLine()).equals("esc")){
//			if(input.charAt(0)=='1'){
//				input=input.substring(2);
//				String[] three=input.split(",");
//				Calendar cal1=Calendar.getInstance();
//				Calendar cal2=Calendar.getInstance();
//				try {
//					cal1.setTime(sdf.parse(three[1]));
//					cal2.setTime(sdf.parse(three[2]));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				long start_time1=System.currentTimeMillis();
//				data.getSingleStockInfo(three[0],cal1,cal2);
//				long end_time1=System.currentTimeMillis();
//				System.out.println(end_time1);
//				System.out.println("runtime: "+(-start_time1+end_time1)+" ms");
//			}
//			else if(input.charAt(0)=='2'){
//				input=input.substring(2);
//				Calendar cal=Calendar.getInstance();
//				try {
//					cal.setTime(sdf.parse(input));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println(sdf.format(cal.getTime()));
//				long start_time1=System.currentTimeMillis();
//				data.getMarketByDate(cal);
//				long end_time1=System.currentTimeMillis();
//				System.out.println(end_time1);
//				System.out.println("runtime: "+(-start_time1+end_time1)+" ms");
//			}
//			else if(input.charAt(0)=='3'){
//				input=input.substring(2);
//				String[] info=input.split(",");
//				data.signUpCheck(info[0], info[1]);
//			}
//			else if(input.charAt(0)=='4'){
//				input=input.substring(2);
//				String[] info=input.split(",");
//				data.signInCheck(info[0], info[1]);
//			}
//			else if(input.charAt(0)=='5'){
//				input=input.substring(2);
//				data.logout(input);
//			}
//		}
	}
	public DataInterface_Driver(){
		IDataInterface data=new DataInterfaceImpl();
		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String,Integer> list=new HashMap<String,Integer>();
		find(list);
		StockDataController c=StockDataController.getInstance();
//    	HashMap<Calendar,List<SingleStockInfoPO>> map1=c.processmap;
//    	checkmap1(map1,list);
    	HashMap<String, List<String>> map2=c.singlesortmap;
    	HashMap<String, HashMap<Calendar, Integer>> map3=c.sortmap;
    	checkmap2_3(map2,map3,list);
//    	HashMap<String, HashMap<Calendar, String>> map4=c.singlestockmap;
//    	checkmap4(map4,list);
//    	HashMap<String, HashMap<String, String>> map5=c.stockinfo_StringType;
//    	checkmap5(map5,list);
	}
	private void checkmap1(HashMap<Calendar,List<SingleStockInfoPO>> map,HashMap<String,Integer> list){
		Iterator<Entry<Calendar, List<SingleStockInfoPO>>> it=map.entrySet().iterator();
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		int i=0;
		int count=0;
		int count1=0;
		while(it.hasNext()){
			i++;
			System.out.println(i+"剩余"+(2393-i));
			Entry<Calendar, List<SingleStockInfoPO>> entry=it.next();
			for(SingleStockInfoPO po:entry.getValue()){
				count++;
				if(po.getDate().compareTo(entry.getKey())!=0){
					System.out.println("表中日期与列表中的项不符");
					System.out.println("错误发生在"+po.getCode()+"    "+sdf.format(po.getDate().getTime()));
					System.out.println("键值为"+sdf.format(entry.getKey().getTime()));
				}
				if(!list.containsKey(po.getStr())){
					count1++;
					System.out.println("表中数据与原数据的项不符");
					System.out.println("错误发生在"+po.getCode()+"    "+sdf.format(po.getDate().getTime()));
				}
			}
		}
		System.out.println(count);
	}
	private void checkmap2_3(HashMap<String, List<String>> map1,HashMap<String, HashMap<Calendar, Integer>> map2,HashMap<String,Integer> list){
		Iterator<Entry<String, List<String>>> it=map1.entrySet().iterator();
		int i=0;
		int count=0;
		int count1=0;
		String str="";
		String code="";
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		Calendar cal1=Calendar.getInstance();
		while(it.hasNext()){
			i++;
			Entry<String,List<String>> entry=it.next();
			code=entry.getKey();
			count1=0;
			for(String k:entry.getValue()){
				count++;
				Calendar cal=Calendar.getInstance();
				String[] out=k.split("\t");
				try {
					cal.setTime(sdf.parse(out[1]));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(count1==0){
					cal1.setTime(cal.getTime());
				}
				if(!list.containsKey(k)){
					count1++;
					System.out.println("表中数据与原数据的项不符");
				}
				if(!out[8].equals(code)){
					System.out.println("error");
				}
				if(count1!=map2.get(code).get(cal)){
					System.out.println("error!!!!");
				}
				if(cal.compareTo(cal1)<0){
					System.out.println("时间错误!!!!");
				}
				count1++;
			}
		}
		System.out.println(i);
		System.out.println(count);
	}
	private void checkmap5(HashMap<String, HashMap<String, String>> map,HashMap<String,Integer> list){
		Iterator<Entry<String, HashMap<String, String>>> it=map.entrySet().iterator();
		int i=0;
		int count=0;
		int count1=0;
		String cal="";
		String str="";
		String code="";
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		while(it.hasNext()){
			i++;
			Entry<String, HashMap<String, String>> entry=it.next();
			cal=entry.getKey();
			Iterator<Entry<String, String>> iti=entry.getValue().entrySet().iterator();
			while(iti.hasNext()){
				count++;
				Entry<String ,String> e=iti.next();
				str=e.getValue();
				code=e.getKey();
				String[] out=str.split("\t");
				if((!code.equals(out[8]))||(!cal.equals(out[1]))){
					System.out.println("error");
				}
				if(!list.containsKey(str)){
					System.out.println("error!!!!!");
				}
			}
		}
		System.out.println(count);
	}
	private void checkmap4(HashMap<String, HashMap<Calendar, String>> map,HashMap<String,Integer> list){
		Iterator<Entry<String, HashMap<Calendar, String>>> it=map.entrySet().iterator();
		int i=0;
		int count=0;
		int count1=0;
		String cal="";
		String str="";
		String code="";
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		while(it.hasNext()){
			i++;
			Entry<String, HashMap<Calendar, String>> entry=it.next();
			code=entry.getKey();
			Iterator<Entry<Calendar, String>> iti=entry.getValue().entrySet().iterator();
			while(iti.hasNext()){
				count++;
				Entry<Calendar, String> e=iti.next();
				str=e.getValue();
				cal=tostring(sdf.format(e.getKey().getTime()));
				String[] out=str.split("\t");
				if((!code.equals(out[8]))||(!cal.equals(out[1]))){
					System.out.println("error");
				}
				if(!list.containsKey(str)){
					System.out.println("error!!!!!");
				}
			}
		}
		System.out.println(count);
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
	public void find(HashMap<String,Integer> list){
		try
		{
			File stockdata=new File("F://date.csv");
			FileReader fr=new FileReader(stockdata);
			BufferedReader br=new BufferedReader(fr);
			br.readLine();
			while(br.ready()){
				String out=br.readLine();
				list.put(out, 1);
			}
			br.close();
			fr.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
