package data.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import com.mysql.cj.jdbc.PreparedStatement;

import data.datahelperimpl.StockDataHelperImpl_2;
import data.parse.Parse;
import data.service.IStockDataInterface;
import data.service.IStockSetInterface;
import data.serviceimpl.StockDataController_2;
import data.serviceimpl.StockSetDataController;
import exception.NullDateException;
import exception.StockHaltingException;
import po.SingleStockInfoPO;

public class maintest {
	Connection conn;
	StockDataHelperImpl_2 sdhi;
	long total;
	public static void main(String[] args){
		new maintest();
	}
	private maintest(){
		total=0;
		sdhi=new StockDataHelperImpl_2();
		IStockDataInterface isd=StockDataController_2.getInstance();
		IStockSetInterface iss=StockSetDataController.getInstance();
		long t1=System.currentTimeMillis();
		List<String> list1=iss.getStockSetInfo("SZA");
		
		HashMap<String,String> temp=new HashMap<String,String>();
		for(int i=0;i<list1.size();i++)
		{
			temp.put(list1.get(i), "");
		}
		List<Integer> date=sdhi.datesort;
		File file=new File("config/stock/info");
		
		for(int i:sdhi.datesort)
		{
			//System.out.println(i);
			HashMap<String,String> map1=new HashMap<String,String>();
			HashMap<String,String> map2=new HashMap<String,String>();
				try {
					map1=isd.getOneDay_Date(Parse.getInstance().getlocalDate(i),temp);
				} catch (NullDateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int count1=0;
				int count2=0;
				for(String name:list1)
				{
					try {
						map2.put(name, sdhi.getSingleInfo(i, name));
					} catch (StockHaltingException e) {
						continue;
					}
					catch(NullDateException e)
					{
						continue;
					}
					count1++;
				}
					
				Iterator<Entry<String, String>> it=map1.entrySet().iterator();
				while(it.hasNext())
				{
					Entry<String, String> code=it.next();
					if(!map2.containsKey(code.getKey()))
					{
						System.out.println("error1:"+i);
						System.exit(0);
					}
					if(!map1.get(code.getKey()).equals(code.getValue()))
					{
						System.out.println("error2:"+i);
						System.exit(0);
					}
					count2++;
				}
				if(count1!=count2)
				{
					System.out.println("error3:"+i+":"+count1+"-"+count2);
					System.exit(0);
				}
			
		}
		long t2=System.currentTimeMillis();
		total=t2-t1;
		System.out.println(total);
	}
	private void getAll()
	{
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from stockdata";
		PreparedStatement pstmt=null;

		try {
			long t1=System.currentTimeMillis();
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				for(int i=0;i<15;i++)
				{
				
					rs.getString(i);
				}				
			}
			long t2=System.currentTimeMillis();
			total=total+(t2-t1);
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			
		}
	}
	private HashMap<String,Double> getDate(String date)
	{
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select code,aftclose,close from stockdata where date = '"+date+"'";
		PreparedStatement pstmt=null;
		HashMap<String,Double> map=new HashMap<String,Double>();
		try {
			long t1=System.currentTimeMillis();
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();

			while(rs.next()){
				for(int i=0;i<1;i++)
				{
					//System.out.println(map);
					map.put(rs.getString(1), rs.getDouble(2));
				}				
			}
			long t2=System.currentTimeMillis();
			total=total+(t2-t1);
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return map;
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return null;
		}
	}
	public Connection getConn(){
		String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://localhost:3306/quantour?useSSL=true&serverTimezone=UTC";
	    String username = "root";
	    String password = "yxliu97";
	    conn = null;
	    try {
	    	Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	public boolean find(int cal){
		String sql = "select info from stock where date='"+cal+"'";
		//System.out.println(sql);
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        if(rs.next()){
	        	return true;
	        }
	        else{
	        	return false;
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean find(int cal,int code){
		String sql = "select info from stock where date>'"+cal+"' and date < 20170419 and code='"+code+"'";
		//System.out.println(sql);
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        if(rs.next()){
	        	System.out.println(rs.getString(1));
	        	while(rs.next())
	        		System.out.println(rs.getString(1));
	        	return true;
	        }
	        else{
	        	return false;
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public void insert(){
		conn=getConn();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		File file=new File("config/stock/info");
		String[] list=file.list();
		int count = 0;
		String sql="insert into stock values(?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		for(int i:sdhi.datesort){
			System.out.println(i);
			if(i<20170315){
				continue;
			}
			for(String j:list){
				int code=Parse.getInstance().strToint(j);
				if(i==20170315&&find(i,code)){
					continue;
				}
				String str;
				try {
					str=sdhi.getSingleInfo(i, j);
				} catch (StockHaltingException | NullDateException e1) {
					continue;
				}
				try{
					pstmt.setInt(1, i);
					pstmt.setInt(2, code);
					pstmt.setString(3, str);
					count++;
					pstmt.addBatch();
					
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			pstmt.executeBatch();
			conn.commit();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
