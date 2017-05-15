package data.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class DBInitInfo {
	public  static List<DataBasePool>  beans = null;  
    static{  
        beans = new ArrayList<DataBasePool>();  
        DataBasePool beanOracle = new DataBasePool();  
        File file=new File("config/abcde.txt");
        try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			beanOracle.setDriverName("com.mysql.jdbc.Driver");  
			beanOracle.setUrl(br.readLine());  
        	beanOracle.setUserName(br.readLine());  
        	beanOracle.setPassword(br.readLine()); 
        	br.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
        beanOracle.setMinConnections(5);  
        beanOracle.setMaxConnections(100);  
        beanOracle.setPoolName("quantour");  
        beans.add(beanOracle);  
    }  
}
