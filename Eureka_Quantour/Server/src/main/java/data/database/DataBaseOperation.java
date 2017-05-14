package data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseOperation {
	private static DataBaseOperation dbo;
	private DataBaseOperation(){
		
	}
	public static DataBaseOperation getInstance(){
		if(dbo==null) dbo=new DataBaseOperation();
		return dbo;
	}
	public Connection getConn(Connection conn){
		String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://localhost:3306/quantour?useSSL=true&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
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
	
}
