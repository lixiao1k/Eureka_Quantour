package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
 * @author: lxd
 * @time:2017/3/12
 */

/*
 * RMI远程工厂对象，只在客户端需要用到某服务时才创建，节省服务器资源
 */
public interface ServiceFactory extends Remote{
    public ClientLogicInterface getClientLogicInterface() throws RemoteException;
    public StockLogicInterface getStockLogicInterface() throws RemoteException;
}
