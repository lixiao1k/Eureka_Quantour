package rmi;
/*
 * @author: lxd
 * @time:2017/3/12
 */
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.jar.Attributes.Name;

import logicservice.ServiceFactory;
import logicserviceimpl.ServiceFactoryImpl;

public class RemoteHelper {
	public RemoteHelper(){
		initService();
	}
    private void initService(){
    	ServiceFactory serviceFactory;
    	try{
    		serviceFactory = ServiceFactoryImpl.getInstance();
    		LocateRegistry.createRegistry(8888);
    		Naming.bind("rmi://localhost:8888/ServiceFactory",serviceFactory);
    		System.out.println("Success");
    	}catch(RemoteException e){
    		e.printStackTrace();
    	}catch (MalformedURLException e) {
			// TODO: handle exception
    		e.printStackTrace();
		}catch (AlreadyBoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    }
}
