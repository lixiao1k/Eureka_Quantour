package data.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import data.serviceimpl.DataInterfaceImpl;
import exception.DateOverException;
import exception.LogErrorException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.StockHaltingException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;
import exception.StrategyRepeatException;
import exception.UserNameRepeatException;
import po.SingleStockInfoPO;
import po.StockSetInfoPO;
import po.StrategyInfoPO;

public class DataDriver {
	public static void main(String[] args){
		new DataDriver();
	}
	public DataDriver(){
		dotest();
	}
	public void dotest(){
		IDataInterface data=new DataInterfaceImpl();
		Scanner sc=new Scanner(System.in);
		String mode;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		while(!(mode=sc.nextLine()).equals("esc")){
			long inputtime = 0;
			if(mode.equals("create user")){
				String userName=sc.nextLine();
				String passWord=sc.nextLine();
				inputtime=System.currentTimeMillis();
				try {
					data.signUpCheck(userName, passWord);
				} catch (UserNameRepeatException e) {
					System.out.println(e.toString());
				}
			}
			else if(mode.equals("login user")){
				String userName=sc.nextLine();
				String passWord=sc.nextLine();
				inputtime=System.currentTimeMillis();
				try {
					data.signInCheck(userName, passWord);
				} catch (LogErrorException e) {
					System.out.println(e.toString());
				}
			}
			else if(mode.equals("logout user")){
				String userName=sc.nextLine();
				inputtime=System.currentTimeMillis();
				data.logout(userName);
			}
			else if(mode.equals("get user stock set")){
				String userName=sc.nextLine();
				inputtime=System.currentTimeMillis();
				try{
					for(String name:data.getStockSet(userName)){
						System.out.println(name);
					}
				}catch(NullPointerException e){
					
				}
				
			}
			else if(mode.equals("add stock set")){
				String username=sc.nextLine();
				String stockSetName=sc.nextLine();
				inputtime=System.currentTimeMillis();
				try {
					data.addStockSet(stockSetName, username);
				} catch (StockSetNameRepeatException e) {
					System.out.println(e.toString());
				}
			}
			else if(mode.equals("delete stock set")){
				String username=sc.nextLine();
				String stockSetName=sc.nextLine();
				inputtime=System.currentTimeMillis();
				data.deleteStockSet(stockSetName, username);
			}
			else if(mode.equals("add stock to stockset")){
				String username=sc.nextLine();
				String stockSetName=sc.nextLine();
				String stockName=sc.nextLine();
				inputtime=System.currentTimeMillis();
				try {
					data.addStockToStockSet(stockName, stockSetName, username);
				} catch (StockNameRepeatException e) {
					System.out.println(e.toString());
				}
			}
			else if(mode.equals("del stock from stockset")){
				String username=sc.nextLine();
				String stockSetName=sc.nextLine();
				String stockName=sc.nextLine();
				inputtime=System.currentTimeMillis();
				data.deleteStockFromStockSet(stockName, stockSetName, username);
			}
			else if(mode.equals("get stockset info")){
				String username=sc.nextLine();
				String stockSetName=sc.nextLine();
				inputtime=System.currentTimeMillis();
				if(username.equals("-1")){
					for(String name:data.getStockSetInfo(stockSetName)){
						System.out.println(name);
					}
				}
				else{
					for(String name:data.getStockSetInfo(stockSetName,username)){
						System.out.println(name);
					}
				}
			}
			else if(mode.equals("translate1")){
				String stockSetName=sc.nextLine();
				System.out.println(data.codeToname(stockSetName));
			}
			else if(mode.equals("translate2")){
				String stockSetName=sc.nextLine();
				System.out.println(data.nameTocode(stockSetName));
			}
			else if(mode.equals("get single stock")){
				String stockcode=sc.nextLine();
				String cal=sc.nextLine();
				LocalDate date=LocalDate.parse(cal);
				inputtime=System.currentTimeMillis();
				try {
					System.out.println(data.getSingleStockInfo(stockcode, date).toString());
					System.out.println(data.getSingleStockInfo(stockcode, date).getAftavg_5());
				} catch (NullStockIDException e) {
					System.out.println(e.toString());
				} catch (NullDateException e) {
					System.out.println(e.toString());
				}
			}
			else if(mode.equals("add")){
				String cal=sc.nextLine();
				LocalDate date=LocalDate.parse(cal);
				String lasti=sc.nextLine();
				int last=Integer.valueOf(lasti);
				inputtime=System.currentTimeMillis();
				try {
					System.out.println(data.addDays(date, last));
				} catch (DateOverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(mode.equals("save strategy")){
				try {
					data.saveStrategy(new StrategyInfoPO("动量", false, new ArrayList<Integer>(), 0, 0, mode), "my first", "Lyx");
				} catch (StrategyRepeatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(mode.equals("comment")){
				data.comment("Lyx", "my first", "Lyx", LocalDateTime.now(), "haa");
			}
//			else if(mode.equals("judge market day")){
//				String cal=sc.nextLine();
//				Calendar day=Calendar.getInstance();
//				try {
//					day.setTime(sdf.parse(cal));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				inputtime=System.currentTimeMillis();
//				System.out.println(data.isMarketDay(day));
//			}
//			else if(mode.equals("get min day")){
//				String stockcode=sc.nextLine();
//				inputtime=System.currentTimeMillis();
//				try {
//					System.out.println(data.getMinDay(stockcode));
//				} catch (NullStockIDException e) {
//					System.out.println(e.toString());
//				}
//				
//			}
//			else if(mode.equals("get max day")){
//				String stockcode=sc.nextLine();
//				inputtime=System.currentTimeMillis();
//				try {
//					System.out.println(data.getMaxDay(stockcode));
//				} catch (NullStockIDException e) {
//					System.out.println(e.toString());
//				}
//			}
//			else if(mode.equals("get singleinfo by end")){
//				String stockcode=sc.nextLine();
//				String cal1=sc.nextLine();
//				String cal2=sc.nextLine();
//				Calendar day1=Calendar.getInstance();
//				Calendar day2=Calendar.getInstance();
//				try {
//					day1.setTime(sdf.parse(cal1));
//					day2.setTime(sdf.parse(cal2));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				inputtime=System.currentTimeMillis();
//				try {
//					for(SingleStockInfoPO po:data.getSingleStockInfo_byEnd(stockcode, day1, day2)){
//						System.out.println(po.toString());
//					}
//				} catch (NullStockIDException e) {
//					System.out.println(e.toString());
//				}catch (NullPointerException e) {
//					System.out.println("null");
//				}
//			}
//			else if(mode.equals("get singleinfo by last")){
//				String stockcode=sc.nextLine();
//				String cal1=sc.nextLine();
//				Calendar day1=Calendar.getInstance();
//				int last=sc.nextInt();
//				try {
//					day1.setTime(sdf.parse(cal1));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				inputtime=System.currentTimeMillis();
//				try {
//					for(SingleStockInfoPO po:data.getSingleStockInfo_byLast(stockcode, day1, last)){
//						System.out.println(po.toString());
//					}
//				} catch (NullStockIDException e) {
//					System.out.println(e.toString());
//				}
//			}
//			else if(mode.equals("get setinfo by end")){
//				String stockcode=sc.nextLine();
//				String cal1=sc.nextLine();
//				String cal2=sc.nextLine();
//				Calendar day1=Calendar.getInstance();
//				Calendar day2=Calendar.getInstance();
//				try {
//					day1.setTime(sdf.parse(cal1));
//					day2.setTime(sdf.parse(cal2));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				inputtime=System.currentTimeMillis();
//				try{
//					for(StockSetInfoPO po:data.getStockInfoinSet_StopByEnd(stockcode, day1, day2)){
//						System.out.println(po.toString());
//					}
//				}catch (NullPointerException e) {
//					System.out.println("null");
//				}
//			}
//			else if(mode.equals("get setinfo by last")){
//				String stockcode=sc.nextLine();
//				String cal1=sc.nextLine();
//				Calendar day1=Calendar.getInstance();
//				int last=sc.nextInt();
//				try {
//					day1.setTime(sdf.parse(cal1));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				inputtime=System.currentTimeMillis();
//				try {
//					for(StockSetInfoPO po:data.getStockInfoinSet_StopByLast(stockcode, day1, last)){
//						System.out.println(po.toString());
//					}
//				} catch (NullDateException e) {
//					System.out.println(e.toString());
//				}
//			}
//			else if(mode.equals("get setinfo")){
//				String stockcode=sc.nextLine();
//				String cal1=sc.nextLine();
//				Calendar day1=Calendar.getInstance();
//				try {
//					day1.setTime(sdf.parse(cal1));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				inputtime=System.currentTimeMillis();
//				try {
//					System.out.println(data.getStockInfoinSet(stockcode, day1).toString());
//				} catch (NullDateException e) {
//					System.out.println(e.toString());
//				}
//			}
//			else if(mode.equals("get setinfo forward by last")){
//				String stockcode=sc.nextLine();
//				String cal1=sc.nextLine();
//				Calendar day1=Calendar.getInstance();
//				int last=sc.nextInt();
//				try {
//					day1.setTime(sdf.parse(cal1));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				inputtime=System.currentTimeMillis();
//				try {
//					for(StockSetInfoPO po:data.getStockInfoinSet_forwardByLast(stockcode, day1, last)){
//						System.out.println(po.toString());
//					}
//				} catch (NullDateException e) {
//					System.out.println(e.toString());
//				}
//			}
			long endtime=System.currentTimeMillis();
			System.out.println("花费时间:" + (endtime-inputtime)+"ms");
			System.out.println("success");
		}
		sc.close();
	}
}
