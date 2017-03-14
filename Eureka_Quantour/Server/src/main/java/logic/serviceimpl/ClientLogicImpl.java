package logic.serviceimpl;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import logic.service.ClientLogicInterface;

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月6日
 */
public class ClientLogicImpl implements ClientLogicInterface{

	private IDataInterface cli = new DataInterfaceImpl();
	@Override
	public boolean signUp(String username, String password) {
		// TODO Auto-generated method stub
		return cli.signUpCheck(username, password);
	}

	@Override
	public boolean signIn(String username, String password) {
		// TODO Auto-generated method stub
		return cli.signInCheck(username, password);
	}

}
