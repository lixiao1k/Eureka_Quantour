package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtil {
	public static final String URL = "jdbc:mysql://localhost:3306/quantour?useUnicode=true&characterEncoding=utf8&useSSL=true";
	public static final String USER = "root";
	public static final String PASSWORD = "lixiaodong1996";
	
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		try{
		Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
}