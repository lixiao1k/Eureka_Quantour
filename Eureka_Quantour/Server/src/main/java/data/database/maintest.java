package data.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.mysql.cj.jdbc.PreparedStatement;

import data.datahelperimpl.StockDataHelperImpl_2;
import data.parse.Parse;
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
		List<Integer> date=sdhi.datesort;
		for(int i:date)
		{
			getDate(Parse.getInstance().getlocalDate(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		}
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
			long t2=System.currentTimeMillis();
			total=total+(t2-t1);
//			while(rs.next()){
//				for(int i=0;i<15;i++)
//				{
//					rs.getString(i);
//				}				
//			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			
		}
	}
	private void getDate(String date)
	{
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from stockdata where date = '"+date+"'";
		PreparedStatement pstmt=null;

		try {
			long t1=System.currentTimeMillis();
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			long t2=System.currentTimeMillis();
			total=total+(t2-t1);
//			while(rs.next()){
//				for(int i=0;i<15;i++)
//				{
//					rs.getString(i);
//				}				
//			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			
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
