package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.List;

import logic.service.ClientLogicInterface;
import logic.service.StockLogicInterface;
import logic.serviceimpl.ClientLogicImpl;
import logic.serviceimpl.StockLogicImpl;
import resultmessage.BeginInvalidException;
import resultmessage.DateInvalidException;
import resultmessage.EndInvalidException;
import vo.ComparedInfoVO;
import vo.EMAInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;
/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月13日
 */
public class DateRemote extends UnicastRemoteObject implements ClientLogicInterface, StockLogicInterface{

	private static final long serialVersionUID = 6589478936265429621L;

	private ClientLogicInterface cli;
	private StockLogicInterface sli;
	protected DateRemote() throws RemoteException {
		cli = new ClientLogicImpl();
		sli = new StockLogicImpl();
	}

	@Override
	public List<SingleStockInfoVO> getSingleStockInfoByTime(String stockCode, Calendar begin, Calendar end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		// TODO Auto-generated method stub
		return sli.getSingleStockInfoByTime(stockCode, begin, end);
	}

	@Override
	public List<List<EMAInfoVO>> getEMAInfo(String stockCode, Calendar begin, Calendar end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		// TODO Auto-generated method stub
		return sli.getEMAInfo(stockCode, begin, end);
	}

	@Override
	public ComparedInfoVO getComparedInfo(String stockCodeA, String stockCodeB, Calendar begin, Calendar end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		// TODO Auto-generated method stub
		return sli.getComparedInfo(stockCodeA, stockCodeB, begin, end);
	}

	@Override
	public MarketInfoVO getMarketInfo(Calendar date) 
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		// TODO Auto-generated method stub
		return sli.getMarketInfo(date);
	}

	@Override
	public boolean signUp(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return cli.signUp(username, password);
	}

	@Override
	public boolean signIn(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return cli.signIn(username, password);
	}

}
