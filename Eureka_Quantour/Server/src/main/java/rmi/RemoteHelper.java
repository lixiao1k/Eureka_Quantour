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

//import data.service.DataThread;
import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;

public class RemoteHelper {
	public RemoteHelper(){
		initService();
	}
    private void initService(){
    	DateRemote dateRemote;
    	try{
    		IDataInterface data=new DataInterfaceImpl();
    		dateRemote = new DateRemote();
    		LocateRegistry.createRegistry(8888);

    		Naming.bind("rmi://172.25.169.209:8888/DateRemote",dateRemote);
//    		Naming.bind("rmi://localhost:8888/DateRemote", dateRemote);

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
