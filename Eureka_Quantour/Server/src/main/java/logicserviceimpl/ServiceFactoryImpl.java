package logicserviceimpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import logicservice.ClientLogicInterface;
import logicservice.ServiceFactory;
import logicservice.StockLogicInterface;

public class ServiceFactoryImpl extends UnicastRemoteObject implements ServiceFactory{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ClientLogicInterface clientLogicInterface;
	private StockLogicInterface stockLogicInterface;
	private static ServiceFactoryImpl instance;
	public static ServiceFactoryImpl getInstance() throws RemoteException{
		if(instance==null){
			instance = new ServiceFactoryImpl();
		}
		return instance;
	}
	protected ServiceFactoryImpl()throws RemoteException{
		super();
	}
	@Override
	public ClientLogicInterface getClientLogicInterface() throws RemoteException {
		// TODO Auto-generated method stub
		if(clientLogicInterface==null)
			clientLogicInterface = new ClientLogicImpl();
		return clientLogicInterface;
	}
	@Override
	public StockLogicInterface getStockLogicInterface() throws RemoteException {
		// TODO Auto-generated method stub
		if(stockLogicInterface==null)
			stockLogicInterface = new StockLogicImpl();
		return stockLogicInterface;
	}
	

}
