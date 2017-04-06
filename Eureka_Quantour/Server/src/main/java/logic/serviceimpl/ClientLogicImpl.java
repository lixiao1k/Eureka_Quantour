package logic.serviceimpl;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.LogErrorException;
import exception.UserNameRepeatException;
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
	public void signUp(String username, char[] password) throws RemoteException, UserNameRepeatException {
		cli.signUpCheck(username,String.valueOf(password));
	}

	@Override
	public void signIn(String username, char[] password) throws RemoteException, LogErrorException {
		cli.signInCheck(username,String.valueOf(password));
	}

	@Override
	public void signOut(String username) throws RemoteException {
		cli.logout(username);
	}
}
