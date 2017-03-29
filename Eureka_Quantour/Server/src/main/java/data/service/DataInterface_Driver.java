package data.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import data.serviceimpl.DataInterfaceImpl;
import exception.LogErrorException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;
import exception.UserNameRepeatException;
import po.SingleStockInfoPO;

public class DataInterface_Driver {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		long start_time=System.currentTimeMillis();
		System.out.println(start_time);	
		IDataInterface data=new DataInterfaceImpl();
		long end_time=System.currentTimeMillis();
		System.out.println(end_time);
		System.out.println("runtime: "+(-start_time+end_time)+" ms");
		String input="";
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		while(!(input=in.nextLine()).equals("esc")){
			if(input.charAt(0)=='1'){
				input=input.substring(2);
				String[] three=input.split(",");
				Calendar cal1=Calendar.getInstance();
				Calendar cal2=Calendar.getInstance();
				try {
					cal1.setTime(sdf.parse(three[1]));
					cal2.setTime(sdf.parse(three[2]));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long start_time1=System.currentTimeMillis();
				int i=0;
				System.out.println(three[0]+" "+three[1]+ " "+three[2]);
				if(data.getSingleStockInfo(three[0],cal1,cal2)==null){
					System.out.println("bucunzai");
				}
				else{
				for(SingleStockInfoPO po:data.getSingleStockInfo(three[0],cal1,cal2)){
					i++;
					System.out.println(po.toString());
				}
//					data.getSingleStockInfo(three[0],cal1,cal2);
				}
				System.out.println(i);
				long end_time1=System.currentTimeMillis();
				System.out.println(end_time1);
				System.out.println("runtime: "+(-start_time1+end_time1)+" ms");
			}
			else if(input.charAt(0)=='2'){
				input=input.substring(2);
				Calendar cal=Calendar.getInstance();
				try {
					cal.setTime(sdf.parse(input));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(sdf.format(cal.getTime()));
				long start_time1=System.currentTimeMillis();
				for(SingleStockInfoPO po:data.getMarketByDate(cal)){
//					if(po.getLast_adjclose()!=po.getLast_close()) 
						System.out.println(po.toString());
				}
//				data.getMarketByDate(cal);
				long end_time1=System.currentTimeMillis();
				System.out.println(end_time1);
				System.out.println("runtime: "+(-start_time1+end_time1)+" ms");
			}
			else if(input.charAt(0)=='3'){
				input=input.substring(2);
				String[] info=input.split(",");
				try {
					data.signUpCheck(info[0], info[1]);
				} catch (UserNameRepeatException e) {
					e.toString();
				}
			}
			else if(input.charAt(0)=='4'){
				input=input.substring(2);
				String[] info=input.split(",");
				try {
					data.signInCheck(info[0], info[1]);
				} catch (LogErrorException e) {
					e.toString();
				}
			}
			else if(input.charAt(0)=='5'){
				input=input.substring(2);
				data.logout(input);
			}
			else if(input.charAt(0)=='6'){
				input=input.substring(2);
				data.getStockSet(input);
			}
			else if(input.charAt(0)=='7'){
				input=input.substring(2);
				String[] out=input.split(",");
				try {
					data.addStockSet(out[0], out[1]);
				} catch (StockSetNameRepeatException e) {
					System.out.println(e.toString());
				}
			}
			else if(input.charAt(0)=='8'){
				input=input.substring(2);
				String[] out=input.split(",");
				data.deleteStockSet(out[0], out[1]);;
			}
			else if(input.charAt(0)=='9'){
				input=input.substring(2);
				String[] out=input.split(",");
				try {
					data.addStockToStockSet(out[0], out[1], out[2]);
				} catch (StockNameRepeatException e) {
					System.out.println(e.toString());
				}
			}
			else if(input.charAt(0)=='a'){
				input=input.substring(2);
				String[] out=input.split(",");
				data.deleteStockFromStockSet(out[0], out[1], out[2]);
			}
		}
	}
}
