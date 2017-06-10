package data.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
		File file=new File("config/abcde.txt");
		String driver;
	    String url;
	    String username;
	    String password;
        try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			driver="com.mysql.cj.jdbc.Driver";  
			url=br.readLine()+"&maxReconnects=1";  
        	username=br.readLine();  
        	password=br.readLine(); 
        	br.close();
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
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	    return null;
	}
	
}
