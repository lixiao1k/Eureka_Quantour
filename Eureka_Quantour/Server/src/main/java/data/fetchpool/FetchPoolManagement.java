package data.fetchpool;

import java.util.Hashtable;

public class FetchPoolManagement {
	private static FetchPoolManagement instance;
    // 连接池存放  
    public Hashtable<String,WebConnectionPool> pools = new Hashtable<String, WebConnectionPool>();  
      
    // 初始化  
    private FetchPoolManagement(){  
        init();  
    }  
    // 单例实现  
    public static FetchPoolManagement getInstance(){  
        if(instance==null) instance=new FetchPoolManagement();
    	return instance;  
    }  
      
      
    // 初始化所有的连接池  
    public void init(){  
        WebConnectionPool pool=new WebConnectionPool();
        pools.put("web", pool);
    }  
      
    // 获得连接,根据连接池名字 获得连接  
    public int  getConnection(String poolName,String url,String path,String type){   
        int index=0;
    	if(pools.size()>0 && pools.containsKey(poolName)){  
            index=getPool(poolName).getConnection(url, path, type);;  
        }else{  
            System.out.println("Error:Can't find this connecion pool ->"+poolName);  
        }   
    	return index;
    }  
    public void startConn(String poolName,int index){
    	if(pools.size()>0 && pools.containsKey(poolName)){  
    		getPool(poolName).startConnection(index);  
        }else{  
            System.out.println("Error:Can't find this connecion pool ->"+poolName);  
        }   
    }
     
    // 关闭，回收连接  
    public void close(String poolName,int index){  
            WebConnectionPool pool = getPool(poolName);    
                if(pool != null){  
                    pool.releaseConn(index);  
                }
    }  
      
      
    // 获得连接池  
    public WebConnectionPool getPool(String poolName){  
        WebConnectionPool pool = null;  
        if(pools.size() > 0){  
             pool = pools.get(poolName);  
        }  
        return pool;  
    }  
}
