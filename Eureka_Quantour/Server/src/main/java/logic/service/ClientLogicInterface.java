package logic.service;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月6日
 */
public interface ClientLogicInterface extends Remote{
	/**
	 * @Description: to sign up, invoke signUp() in data
	 * @return: boolean
	 */
	public void signUp ( String username, char[] password )throws RemoteException;
	/**
	 * @Description: to sign in, invoke signIn() indata
	 * @return: boolean
	 */
	public void signIn ( String username, char[] password )throws RemoteException;

	public void signOut(String username) throws RemoteException;

}
