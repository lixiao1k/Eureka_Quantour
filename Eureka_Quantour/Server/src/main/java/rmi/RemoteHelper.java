package rmi;
/**
 * @author: lxd
 * @time:2017/3/12
 */
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.Connection;
import java.sql.SQLException;

import data.common.WebMethod;
import data.database.DataBaseOperation;
//import data.service.DataThread;
import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.InternetdisconnectException;

public class RemoteHelper {
	public RemoteHelper(){
		initService();
	}
    private void initService(){
    	try {
			WebMethod.getInstance().testInternet();
		} catch (InternetdisconnectException e1) {
			System.out.println("please check your internet");
			System.exit(0);
		}
    	Connection conn=null;
		conn=DataBaseOperation.getInstance().getConn(conn);
		boolean flag=true;
		if(conn==null)
		{
			flag=false;
		}
		if(!flag)
		{
			System.out.println("连接不上数据库，请检查网络后再试");
			System.exit(0);
		}
		try {
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
    	DateRemote dateRemote;
    	try{
    		IDataInterface data=new DataInterfaceImpl();
    		dateRemote = new DateRemote();
    		LocateRegistry.createRegistry(8888);
            //System.setProperty("java.rmi.server.hostname","123.206.212.13");
    		Naming.rebind("rmi://localhost:8888/DateRemote",dateRemote);
    		System.out.println("Success");
    	}catch(RemoteException e){
    		e.printStackTrace();
    	}catch (MalformedURLException e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    }
}
