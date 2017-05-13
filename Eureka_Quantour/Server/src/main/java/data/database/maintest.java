package data.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.cj.jdbc.PreparedStatement;

import data.datahelperimpl.StockDataHelperImpl_2;
import data.parse.Parse;
import exception.NullDateException;
import exception.StockHaltingException;

public class maintest {
	Connection conn;
	StockDataHelperImpl_2 sdhi;
	public static void main(String[] args){
		new maintest();
	}
	private maintest(){
		sdhi=new StockDataHelperImpl_2();
		conn=getConn();
		File file=new File("config/stock/info");
		String[] list=file.list();
		long total=0;
		String str=null;
		long t1=System.currentTimeMillis();
	
			int i=20050201;
			for(;i<20170420;i++){
				try {
				
				sdhi.getSingleInfo(i, "000001");
			} catch (StockHaltingException | NullDateException e) {
			}
			}
	
//		find(20160315,1);
		long t2=System.currentTimeMillis();
		System.out.println(t2-t1);
		Scanner sc=new Scanner(System.in);
		sc.nextLine();
//		for(String i:list){
//			int code=Parse.getInstance().strToint(i);
//			long t1=System.currentTimeMillis();
//			try {
//				str=sdhi.getSingleInfo(20170315,i);
//			} catch (StockHaltingException | NullDateException e) {
//			}
//			long t2=System.currentTimeMillis();
//			total=total+t2-t1;
//		}
//		System.out.println(total+"\n"+str);
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
