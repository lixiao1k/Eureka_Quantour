/**
 * 
 */
package data.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.Eureka.www.AppTest;

import data.serviceimpl.DataInterfaceImpl;
import data.serviceimpl.StockDataController;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import po.SingleStockInfoPO;

/**
 * @author Administrator
 *
 */
public class IDataInterfaceTest extends TestCase {
	private IDataInterface data;
	
	/**
	 * @param name
	 */
	public IDataInterfaceTest(String name) {
		super(name);
		data=new DataInterfaceImpl();
		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( IDataInterfaceTest.class );
    }
//    public void testsignUp(){
//    	assertEquals(false, data.signUpCheck("   lyx", "lyxzzz"));
//    	assertEquals(false, data.signUpCheck("!lyx", "lyxzzz"));
//    	assertEquals(true, data.signUpCheck("lyx", "lyxzzz"));
//    	assertEquals(true, data.signUpCheck("lyx123", "lyxzzz"));
//    	assertEquals(false, data.signUpCheck("lyx", "lyxzzz"));	
//    	assertEquals(true, data.signUpCheck("Lyx", "lyx123456"));
//    }
//    public void testsignIn(){
//    	assertEquals(false, data.signInCheck("lYx", "lyxzzz"));
//    	assertEquals(false, data.signInCheck("ly!x", "ly"));
//    	assertEquals(false, data.signInCheck("lyx", "ly"));
//    	assertEquals(true, data.signInCheck("Lyx", "lyx123456"));
//    	assertEquals(true, data.signInCheck("lyx", "lyxzzz"));
//    	assertEquals(false, data.signInCheck("lyx", "lyxz"));
//    	assertEquals(false, data.signInCheck("lyx", "  lyxz"));
//    	assertEquals(false, data.signInCheck("lyx", "!lyxz"));
//    	assertEquals(false, data.signInCheck("lyx", "lyxzZz"));
//    }
    
    public void testgetMarketByDate(){
    	Calendar day1=Calendar.getInstance();
    	Calendar day2=Calendar.getInstance();
    	Calendar day3=Calendar.getInstance();
    	Calendar day4=Calendar.getInstance();
    	Calendar day5=Calendar.getInstance();
    	Calendar day6=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
    	try {
			day1.setTime(sdf.parse("4/29/14"));
			day2.setTime(sdf.parse("2/1/05"));
	    	day3.setTime(sdf.parse("4/26/14"));
	    	day4.setTime(sdf.parse("4/27/14"));
	    	day5.setTime(sdf.parse("5/31/14"));
	    	day6.setTime(sdf.parse("1/1/01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	assertEquals(data.getSingleStockInfo("1", day3, day4),null);
    	assertEquals(data.getSingleStockInfo("1", day6, day2).size(),1);
    	assertEquals(data.getSingleStockInfo("402", day3, day1).size(),2);
    	assertEquals(data.getSingleStockInfo("402", day1, day5).size(),1);
    	assertEquals(data.getSingleStockInfo("10", day3, day2),null);
//    	assertEquals(10,data.getMarketByDate(day4).size());
//    	assertEquals(10,data.getMarketByDate(day5).size());
//    	data.getSingleStockInfo("1", begin, end);
//    	data.getSingleStockInfo(stockcode, begin, end)
    }
//    public void testmap(){
//		IDataInterface data=new DataInterfaceImpl();
//		try {
//			Thread.sleep(12000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		HashMap<String,Integer> list=new HashMap<String,Integer>();
//		find(list);
//		boolean flag1=true;
//		boolean flag2=true;
//		boolean flag3=true;
//		boolean flag4=true;
//		StockDataController c=StockDataController.getInstance();
//    	HashMap<Calendar,List<SingleStockInfoPO>> map1=c.processmap;
//    	checkmap1(map1,list,flag1);
//    	HashMap<String, List<String>> map2=c.singlesortmap;
//    	HashMap<String, HashMap<Calendar, Integer>> map3=c.sortmap;
//    	checkmap2_3(map2,map3,list,flag2);
//    	HashMap<String, HashMap<Calendar, String>> map4=c.singlestockmap;
//    	checkmap4(map4,list,flag3);
//    	HashMap<String, HashMap<String, String>> map5=c.stockinfo_StringType;
//    	checkmap5(map5,list,flag4);
//    	assertEquals(true,flag1);
//    	assertEquals(true,flag2);
//    	assertEquals(true,flag3);
//    	assertEquals(true,flag4);
//	}
//	private void checkmap1(HashMap<Calendar,List<SingleStockInfoPO>> map,HashMap<String,Integer> list,boolean flag){
//		Iterator<Entry<Calendar, List<SingleStockInfoPO>>> it=map.entrySet().iterator();
//		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
//		int i=0;
//		int count=0;
//		int count1=0;
//		while(it.hasNext()){
//			i++;
//			System.out.println(i+"剩余"+(2393-i));
//			Entry<Calendar, List<SingleStockInfoPO>> entry=it.next();
//			for(SingleStockInfoPO po:entry.getValue()){
//				count++;
//				if(po.getDate().compareTo(entry.getKey())!=0){
//					flag=false;
//					System.out.println("表中日期与列表中的项不符");
//					System.out.println("错误发生在"+po.getCode()+"    "+sdf.format(po.getDate().getTime()));
//					System.out.println("键值为"+sdf.format(entry.getKey().getTime()));
//				}
//				if(!list.containsKey(po.getStr())){
//					count1++;
//					flag=false;
//					System.out.println("表中数据与原数据的项不符");
//					System.out.println("错误发生在"+po.getCode()+"    "+sdf.format(po.getDate().getTime()));
//				}
//			}
//		}
//		System.out.println(count);
//	}
//	private void checkmap2_3(HashMap<String, List<String>> map1,HashMap<String, HashMap<Calendar, Integer>> map2,HashMap<String,Integer> list,boolean flag){
//		Iterator<Entry<String, List<String>>> it=map1.entrySet().iterator();
//		int i=0;
//		int count=0;
//		int count1=0;
//		String str="";
//		String code="";
//		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
//		Calendar cal1=Calendar.getInstance();
//		while(it.hasNext()){
//			i++;
//			Entry<String,List<String>> entry=it.next();
//			code=entry.getKey();
//			count1=0;
//			for(String k:entry.getValue()){
//				count++;
//				Calendar cal=Calendar.getInstance();
//				String[] out=k.split("\t");
//				try {
//					cal.setTime(sdf.parse(out[1]));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if(count1==0){
//					flag=false;
//					cal1.setTime(cal.getTime());
//				}
//				if(!list.containsKey(k)){
//					count1++;
//					flag=false;
//					System.out.println("表中数据与原数据的项不符");
//				}
//				if(!out[8].equals(code)){
//					flag=false;
//					System.out.println("error");
//				}
//				if(count1!=map2.get(code).get(cal)){
//					flag=false;
//					System.out.println("error!!!!");
//				}
//				if(cal.compareTo(cal1)<0){
//					flag=false;
//					System.out.println("时间错误!!!!");
//				}
//				count1++;
//			}
//		}
//		System.out.println(i);
//		System.out.println(count);
//	}
//	private void checkmap5(HashMap<String, HashMap<String, String>> map,HashMap<String,Integer> list,boolean flag){
//		Iterator<Entry<String, HashMap<String, String>>> it=map.entrySet().iterator();
//		int i=0;
//		int count=0;
//		int count1=0;
//		String cal="";
//		String str="";
//		String code="";
//		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
//		while(it.hasNext()){
//			i++;
//			Entry<String, HashMap<String, String>> entry=it.next();
//			cal=entry.getKey();
//			Iterator<Entry<String, String>> iti=entry.getValue().entrySet().iterator();
//			while(iti.hasNext()){
//				count++;
//				Entry<String ,String> e=iti.next();
//				str=e.getValue();
//				code=e.getKey();
//				String[] out=str.split("\t");
//				if((!code.equals(out[8]))||(!cal.equals(out[1]))){
//					System.out.println("error");
//					flag=false;
//				}
//				if(!list.containsKey(str)){
//					System.out.println("error!!!!!");
//					flag=false;
//				}
//			}
//		}
//		System.out.println(count);
//	}
//	private void checkmap4(HashMap<String, HashMap<Calendar, String>> map,HashMap<String,Integer> list,boolean flag){
//		Iterator<Entry<String, HashMap<Calendar, String>>> it=map.entrySet().iterator();
//		int i=0;
//		int count=0;
//		int count1=0;
//		String cal="";
//		String str="";
//		String code="";
//		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
//		while(it.hasNext()){
//			i++;
//			Entry<String, HashMap<Calendar, String>> entry=it.next();
//			code=entry.getKey();
//			Iterator<Entry<Calendar, String>> iti=entry.getValue().entrySet().iterator();
//			while(iti.hasNext()){
//				count++;
//				Entry<Calendar, String> e=iti.next();
//				str=e.getValue();
//				cal=tostring(sdf.format(e.getKey().getTime()));
//				String[] out=str.split("\t");
//				if((!code.equals(out[8]))||(!cal.equals(out[1]))){
//					System.out.println("error");
//					flag=false;
//				}
//				if(!list.containsKey(str)){
//					System.out.println("error!!!!!");
//					flag=false;
//				}
//			}
//		}
//		System.out.println(count);
//	}
//	private String tostring(String str){
//		String[] out=str.split("/");
//		if(out[0].length()==2&&out[0].charAt(0)=='0'){
//			out[0]=out[0].substring(1);
//		}
//		if(out[1].length()==2&&out[1].charAt(0)=='0'){
//			out[1]=out[1].substring(1);
//		}
//		return out[0]+"/"+out[1]+"/"+out[2];
//	}
//	public void find(HashMap<String,Integer> list){
//		try
//		{
//			File stockdata=new File("date.csv");
//			FileReader fr=new FileReader(stockdata);
//			BufferedReader br=new BufferedReader(fr);
//			br.readLine();
//			while(br.ready()){
//				String out=br.readLine();
//				list.put(out, 1);
//			}
//			br.close();
//			fr.close();
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
}
