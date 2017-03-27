package logic.serviceimpl;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import logic.service.ClientLogicInterface;

import java.rmi.RemoteException;

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月6日
 */
public class ClientLogicImpl implements ClientLogicInterface{

	private IDataInterface cli = new DataInterfaceImpl();


	@Override
	public boolean signUp(String username, char[] password) throws RemoteException {
		return false;
	}

	@Override
	public boolean signIn(String username, char[] password) throws RemoteException {
		return false;
	}
}
