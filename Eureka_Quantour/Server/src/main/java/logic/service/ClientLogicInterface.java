package logic.service;


import exception.DisConnectedException;
import exception.LogErrorException;
import exception.SqlNotConnectedException;
import exception.UserNameRepeatException;

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
	public void signUp ( String username, char[] password ) throws RemoteException, UserNameRepeatException;
	/**
	 * @Description: to sign in, invoke signIn() indata
	 * @return: boolean
	 * @throws SqlNotConnectedException 
	 */
	public void signIn ( String username, char[] password ) throws RemoteException, LogErrorException, SqlNotConnectedException;

	//用户退出

	public void signOut(String username) throws RemoteException;
	/**
	 * 和服务器获得连接
	 * @param userName 用户名
	 * @throws DisConnectedException
	 */
	public void getConn(String userName)  throws RemoteException;

}
