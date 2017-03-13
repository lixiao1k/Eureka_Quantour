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

public class RemoteHelper {
	public RemoteHelper(){
		initService();
	}
    private void initService(){
    	DateRemote dateRemote;
    	try{
    		dateRemote = new DateRemote();
    		LocateRegistry.createRegistry(8888);
    		Naming.bind("rmi://localhost:8888/DateRemote",dateRemote);
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
