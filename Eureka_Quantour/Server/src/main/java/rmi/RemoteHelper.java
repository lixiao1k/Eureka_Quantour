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

import data.common.WebMethod;
//import data.service.DataThread;
import data.common.WebMethod;
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
    	DateRemote dateRemote;
		try {
			WebMethod.getInstance().testInternet();
		} catch (InternetdisconnectException e) {
			System.exit(0);
		}

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
