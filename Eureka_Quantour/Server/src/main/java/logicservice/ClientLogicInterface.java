package logicservice;

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
	public boolean signUp ( String username, String password )throws RemoteException;
	/**
	 * @Description: to sign in, invoke signIn() indata
	 * @return: boolean
	 */
	public boolean signIn ( String username, String password )throws RemoteException;

}
