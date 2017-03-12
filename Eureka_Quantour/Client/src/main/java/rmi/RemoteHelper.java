package rmi;
/*
 * @author:lxd
 * @time:2017/3/12
 */
import java.rmi.Remote;

import logic.service.ServiceFactory;

public class RemoteHelper {
    private Remote remote;
    private static RemoteHelper remoteHelper = new RemoteHelper();
    public static RemoteHelper getInstance(){
    	return remoteHelper;
    }
    private RemoteHelper(){
    	
    }
    public void setRemote(Remote remote){
    	this.remote = remote;
    }
    public ServiceFactory getServiceFactory(){
    	return (ServiceFactory)remote;
    }
}
