package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class DBUtil {
	public static final String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=UTC";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	static Connection conn;
	
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		try{
		    conn = DriverManager.getConnection(URL,USER,PASSWORD);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		conn.close();
	}
}