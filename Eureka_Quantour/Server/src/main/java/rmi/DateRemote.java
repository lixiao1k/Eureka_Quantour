package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

import exception.*;
import logic.service.ClientLogicInterface;
import logic.service.StockLogicInterface;
import logic.serviceimpl.ClientLogicImpl;
import logic.serviceimpl.StockLogicImpl;
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
		sli = new StockLogicImpl();
	}

	@Override
	public List<SingleStockInfoVO> getSingleStockInfoByTime(String stockCode, LocalDate begin, LocalDate end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {
		// TODO Auto-generated method stub
		return sli.getSingleStockInfoByTime(stockCode, begin, end);
	}

	@Override
	public List<EMAInfoVO> getEMAInfo(String stockCode, LocalDate begin, LocalDate end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException, DateOverException {
		// TODO Auto-generated method stub
		return sli.getEMAInfo(stockCode, begin, end);
	}

	@Override
	public ComparedInfoVO getComparedInfo(String stockCodeA, LocalDate begin, LocalDate end) throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		return sli.getComparedInfo(stockCodeA, begin, end);
	}

	@Override
	public MarketInfoVO getMarketInfo(LocalDate date,String marketname) throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullMarketException {
		return sli.getMarketInfo(date,marketname);
	}


//	@Override
//	public ComparedInfoVO getComparedInfo(String stockCodeA, String stockCodeB, LocalDate begin, LocalDate end)
//			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
//		// TODO Auto-generated method stub
//		return sli.getComparedInfo(stockCodeA, stockCodeB, begin, end);
//	}

//	@Override
//	public MarketInfoVO getMarketInfo(Calendar date)
//			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
//		// TODO Auto-generated method stub
//		return sli.getMarketInfo(date);
//	}

	@Override
	public List<String> getStockSet(String username) throws RemoteException {
		return sli.getStockSet(username);
	}

	@Override
	public List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now, String username) throws RemoteException, NullMarketException {
		return sli.getStockSetSortedInfo(stockSetName,now,username);
	}

//	@Override
//	public List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now) {
//		return sli.getStockSetSortedInfo(stockSetName,now);
//	}

	@Override
	public SingleStockInfoVO getStockBasicInfo(String code, LocalDate now) throws NullStockIDException, NullDateException, RemoteException {
		return sli.getStockBasicInfo(code,now);
	}

	@Override
	public void setStrategy(StrategyConditionVO strategyConditionVO, SaleVO s, LocalDate begin, LocalDate now, String stockSetName, String username) throws RemoteException {
		sli.setStrategy(strategyConditionVO, s, begin, now, stockSetName, username);
	}

//	@Override
//	public List<SingleStockInfoVO> getStockSorted(String stockSetName, LocalDate now) {
//		return sli.getStockSorted(stockSetName, now);
//	}

//	@Override
//	public void setStrategy(StrategyConditionVO sc, SaleVO s, LocalDate begin, LocalDate now, String stockSetName) {
//		sli.setStrategy(sc, s, begin, now, stockSetName);
//	}

	@Override
	public YieldChartDataVO getYieldChartData() throws RemoteException {
		return sli.getYieldChartData();
	}

	@Override
	public YieldDistributionHistogramDataVO getYieldDistributionHistogramData() throws RemoteException {
		return sli.getYieldDistributionHistogramData();
	}

	@Override
	public void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException, RemoteException {
		sli.addStockSet(stockSetName,username);
	}

	@Override
	public void deleteStockSet(String stockSetName, String username) throws RemoteException {
		sli.deleteStockSet(stockSetName,username);
	}

	@Override
	public void addStockToStockSet(String stockName, String stockSetName, String username) throws StockNameRepeatException, RemoteException {
		sli.addStockToStockSet(stockName, stockSetName, username);
	}

	@Override
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username) throws RemoteException {
		sli.deleteStockFromStockSet(stockName, stockSetName, username);
	}

	@Override
	public String nameToCode(String name) throws RemoteException {
		return sli.nameToCode(name);
	}

	@Override
	public void saveStragety(StrategyVO strategyVO, String username) throws RemoteException {

	}

	@Override
	public void deleteStrategy(String createName, String strategyName) throws RemoteException {

	}

	@Override
	public void comment(String Username, String strategyName, String commenterName, LocalDate date, String comment) throws RemoteException {

	}

	@Override
	public StrategyShowVO getStrategy(String createrName, String StrategyName) throws RemoteException {
		return null;
	}

	@Override
	public List<StrategyShowVO> getStrategyList(String createrName) throws RemoteException {
		return null;
	}

	@Override
	public List<StrategyShowVO> getStrategyList() throws RemoteException {
		return null;
	}


	@Override
	public void signUp(String username, char[] password) throws RemoteException, UserNameRepeatException {
		cli.signUp(username,password);
	}

	@Override
	public void signIn(String username, char[] password) throws RemoteException, LogErrorException {
		cli.signIn(username,password);
	}

	@Override
	public void signOut(String username) throws RemoteException {
		cli.signOut(username);
	}

	@Override
	public List<String> getIndustryList() throws RemoteException {
		return sli.getIndustryList();
	}
}
