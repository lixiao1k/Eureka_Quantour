package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import exception.*;
import logic.service.ClientLogicInterface;
import logic.service.ForecastRODInterface;
import logic.service.StockLogicInterface;
import logic.serviceimpl.ClientLogicImpl;
import logic.serviceimpl.ForecastRODImpl;
import logic.serviceimpl.StockLogicImpl;
import vo.*;

import javax.swing.plaf.synth.SynthEditorPaneUI;

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月13日
 */
public class DateRemote extends UnicastRemoteObject implements ClientLogicInterface, StockLogicInterface, ForecastRODInterface{

	private static final long serialVersionUID = 6589478936265429621L;

	private ClientLogicInterface cli;
	private StockLogicInterface sli;
	private ForecastRODInterface frodi;
	protected DateRemote() throws RemoteException {
		cli = new ClientLogicImpl();
		sli = new StockLogicImpl();
		frodi = new ForecastRODImpl();
	}

	public List<SingleStockInfoVO> getExponentInfoByTime (String stockCode, LocalDate begin, LocalDate end )
			 throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException{
		return sli.getExponentInfoByTime(stockCode, begin, end);
	}
	@Override
	public List<SingleStockInfoVO> getSingleStockInfoByTime(String stockCode, LocalDate begin, LocalDate end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {
		return sli.getSingleStockInfoByTime(stockCode, begin, end);
	}

	@Override
	public List<EMAInfoVO> getEMAInfo(String stockCode, LocalDate begin, LocalDate end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException, DateOverException {
		return sli.getEMAInfo(stockCode, begin, end);
	}

	@Override
	public ComparedInfoVO getComparedInfo(String stockCodeA, LocalDate begin, LocalDate end) throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {
		return sli.getComparedInfo(stockCodeA, begin, end);
	}

	@Override
	public MarketInfoVO getMarketInfo(LocalDate date,String marketname) throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullMarketException {
		return sli.getMarketInfo(date,marketname);
	}

	@Override
	public List<String> getStockSet(String username) throws RemoteException {
		return sli.getStockSet(username);
	}

	@Override
	public List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now, String username) throws RemoteException, NullMarketException {
		return sli.getStockSetSortedInfo(stockSetName,now,username);
	}

	@Override
	public SingleStockInfoVO getStockBasicInfo(String code, LocalDate now) throws NullStockIDException, NullDateException, RemoteException {
		return sli.getStockBasicInfo(code,now);
	}

	@Override
	public void setStrategy(StrategyConditionVO strategyConditionVO, SaleVO s, LocalDate begin, LocalDate now, String stockSetName, String username) throws RemoteException {
		sli.setStrategy(strategyConditionVO, s, begin, now, stockSetName, username);
	}

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
	public void saveStragety(StrategyVO strategyVO, String username) throws RemoteException, StrategyRepeatException {
		sli.saveStragety(strategyVO, username);
	}

	@Override
	public void deleteStrategy(String createName, String strategyName) throws RemoteException {
		sli.deleteStrategy(createName, strategyName);
	}

	@Override
	public void comment(String Username, String strategyName, String commenterName, LocalDateTime time, String comment) throws RemoteException {
		sli.comment(Username, strategyName, commenterName, time, comment);
	}

	@Override
	public StrategyShowVO getStrategy(String createrName, String StrategyName) throws RemoteException {
		return sli.getStrategy(createrName, StrategyName);
	}

	@Override
	public List<StrategyListVO> getStrategyList(String createrName) throws RemoteException {
		return sli.getStrategyList(createrName);
	}

	@Override
	public List<StrategyListVO> getStrategyList() throws RemoteException {
		return sli.getStrategyList();
	}

	@Override
	public void setPublic(String creatroName, String straetgyName, boolean property) throws RemoteException {
		sli.setPublic(creatroName, straetgyName, property);
	}

	@Override
	public List<String> getAreaList() throws RemoteException {
		return sli.getAreaList();
	}

	@Override
	public List<String> getConceptList() throws RemoteException {
		return sli.getConceptList();
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

	@Override
	public StockRODVO getStockROD( String stockcode, LocalDate begindate, LocalDate enddate, int numOfDay, double alpha, int m, int k )
			throws RemoteException{
		return frodi.getStockROD( stockcode, begindate, enddate, numOfDay, alpha, m, k);
	}

	@Override
	public PredictVO predict(String stockcode, LocalDate date) throws RemoteException {
		return frodi.predict(stockcode, date);
	}

	@Override
	public List<Double> getTimeSharingData(String code, LocalDate date)
			throws TimeShraingLackException, NullStockIDException, RemoteException {
		return sli.getTimeSharingData(code, date);
	}

	@Override
	public List<String> fuzzySearch(String input) throws RemoteException{
		return sli.fuzzySearch(input);
	}
}
