package logic.serviceimpl;

import data.service.ICompanyDataInterface;
import data.service.IDataInterface;
import data.service.IStockDataInterface;
import data.service.IStockSetInterface;
import data.service.IStrategyDataInterface;
import data.service.IUserDataInterface;
import data.serviceimpl.CompanyDataController;
import data.serviceimpl.DataInterfaceImpl;
import data.serviceimpl.StockDataController_2;
import data.serviceimpl.StockSetDataController;
import data.serviceimpl.StrategyDataController;
import data.serviceimpl.UserDataController;
import exception.DisConnectedException;
import exception.LogErrorException;
import exception.SqlNotConnectedException;
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

	private IUserDataInterface cli = UserDataController.getInstance();


	@Override
	public void signUp(String username, char[] password) throws RemoteException, UserNameRepeatException {
		cli.signUpCheck(username,String.valueOf(password));
	}

	@Override
	public void signIn(String username, char[] password) throws RemoteException, LogErrorException, SqlNotConnectedException {
		cli.signInCheck(username,String.valueOf(password));
	}

	@Override
	public void signOut(String username) throws RemoteException {
		cli.logout(username);
	}
	/**
	 * 和服务器获得连接
	 * @param userName 用户名
	 * @throws DisConnectedException
	 */
	public void getConn(String userName)  throws RemoteException
	{
		try {
			cli.getConn(userName);
		} catch (DisConnectedException e) {
			System.out.println(e.toString());
			throw new RemoteException();
		}
	}
}
