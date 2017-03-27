package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.List;

import logic.service.ClientLogicInterface;
import logic.service.StockLogicInterface;
import logic.serviceimpl.ClientLogicImpl;
import logic.serviceimpl.StockLogicImpl;
import logic.serviceimpl.StockLogicImplstub;
import resultmessage.BeginInvalidException;
import resultmessage.DateInvalidException;
import resultmessage.EndInvalidException;
import vo.*;

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
		sli = new StockLogicImplstub();
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
	public List<String> getStockSet(String username) {
		return sli.getStockSet(username);
	}

	@Override
	public List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, Calendar now) {
		return sli.getStockSetSortedInfo(stockSetName,now);
	}

	@Override
	public SingleStockInfoVO getStockBasicInfo(String code, Calendar now) {
		return sli.getStockBasicInfo(code,now);
	}

	@Override
	public List<SingleStockInfoVO> getStockSorted(String stockSetName, Calendar now) {
		return sli.getStockSorted(stockSetName, now);
	}

	@Override
	public void setStrategy(StrategyConditionVO sc, SaleVO s, Calendar begin, Calendar now, String stockSetName) {
		sli.setStrategy(sc, s, begin, now, stockSetName);
	}

	@Override
	public YieldChartDataVO getYieldChartData() {
		return sli.getYieldChartData();
	}

	@Override
	public YieldDistributionHistogramDataVO getYieldDistributionHistogramData() {
		return sli.getYieldDistributionHistogramData();
	}

	@Override
	public void addStockSet(String stockSetName, String username) {
		sli.addStockSet(stockSetName,username);
	}

	@Override
	public void deleteStockSet(String stockSetName, String username) {
		sli.deleteStockSet(stockSetName,username);
	}

	@Override
	public void addStockToStockSet(String stockName, String stockSetName, String username) {
		sli.addStockToStockSet(stockName, stockSetName, username);
	}

	@Override
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username) {
		sli.deleteStockFromStockSet(stockName, stockSetName, username);
	}

	@Override
	public boolean signUp(String username, char[] password) throws RemoteException {
		return true;
	}

	@Override
	public boolean signIn(String username, char[] password) throws RemoteException {
		return true;
	}
}
