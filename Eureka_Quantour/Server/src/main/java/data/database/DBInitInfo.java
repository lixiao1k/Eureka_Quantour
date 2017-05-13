package data.database;

import java.util.ArrayList;
import java.util.List;

public class DBInitInfo {
	public  static List<DataBasePool>  beans = null;  
    static{  
        beans = new ArrayList<DataBasePool>();  
        DataBasePool beanOracle = new DataBasePool();  
        beanOracle.setDriverName("com.mysql.jdbc.Driver");  
        beanOracle.setUrl("jdbc:mysql://localhost:3306/quantour?useSSL=true&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&maxReconnects=10");  
        beanOracle.setUserName("root");  
        beanOracle.setPassword("yxliu97");  
        beanOracle.setMinConnections(5);  
        beanOracle.setMaxConnections(100);  
        beanOracle.setPoolName("quantour");  
        beans.add(beanOracle);  
    }  
}
