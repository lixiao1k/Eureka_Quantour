package dataController;

import java.util.HashMap;
import java.util.Map;

/**
 * 界面之间传值
 */
public class DataContorller {
    private Map<String,Object> map = new HashMap<>();
    private static DataContorller instance;
    public static DataContorller getInstance(){
    	if(instance==null){
    		instance = new DataContorller();
    		return instance;
    	}else{
    		return instance;
    	}
    }
    
    public void put(String name, Object o){
    	map.put(name, o);
    }
    public Object get(String name){
    	return map.get(name);
    }
    public void remove(String name){
    	map.remove(name);
    }
    public void upDate(String name,Object o){
    	if(map.containsKey(name)){
    		map.remove(name);
    		map.put(name, o);
    	}else{
    		map.put(name, o);
    	}
    }
}
