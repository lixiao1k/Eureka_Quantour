package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import exception.*;
import vo.*;

/**
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月6日
 */
public interface StockLogicInterface extends Remote {
	/**
	* @Description: to get all stocks' information between "begin" and "end", 
	*               invoke getSingleStockInfo() in data layer
	* @return: Iterator<SingleStockInfoVO>
	*/
	List<SingleStockInfoVO> getSingleStockInfoByTime (String stockCode, LocalDate begin, LocalDate end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException,RemoteException;
	/**
	* @Description: to get all stocks' EMA between "begin" and "end",
	*               invoke getSingleStockInfo() in data layer
	* @return: Iterator<Double>
	*/
	List<EMAInfoVO> getEMAInfo ( String stockCode, LocalDate begin, LocalDate end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException, DateOverException,RemoteException;
	/**
	* @Description: to get stock A and B's compared information between "begin" and "end",
	*               invoke getSingleStockInfo() in data layer
	* @return: ComparedInfoVO
	*/
	ComparedInfoVO getComparedInfo ( String stockCodeA, LocalDate begin, LocalDate end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;
	/**
	* @Description: to get market's information by date,
	*               invoke getMarketByDate() in data layer
	* @return: MarketInfoVO
	*/
	MarketInfoVO getMarketInfo ( LocalDate date,String marketname )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException,NullMarketException;

	/*
	* @Description: to get the name list of Industry
	* @author:LXD
	*/
	List<String> getIndustryList() throws RemoteException;
	
	List<String> getStockSet(String username)throws RemoteException;

	List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now,String username)throws RemoteException, NullMarketException;

	SingleStockInfoVO getStockBasicInfo(String code, LocalDate now) throws NullStockIDException, NullDateException,RemoteException;

	void setStrategy(StrategyConditionVO strategyConditionVO, SaleVO s, LocalDate begin, LocalDate now, String stockSetName,String username)throws RemoteException;

	YieldChartDataVO getYieldChartData()throws RemoteException;

	YieldDistributionHistogramDataVO getYieldDistributionHistogramData()throws RemoteException;

	void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException,RemoteException;

	void deleteStockSet(String stockSetName, String username)throws RemoteException;

	void addStockToStockSet(String stockName, String stockSetName, String username) throws StockNameRepeatException,RemoteException;

	void deleteStockFromStockSet(String stockName, String stockSetName, String username)throws RemoteException;
	
	String nameToCode(String name) throws RemoteException;

	//按照用户名保存策略
	void saveStragety(StrategyVO strategyVO, String username) throws RemoteException;

	//按照用户名和策略名称删除策略
	void deleteStrategy ( String createName, String strategyName) throws RemoteException;

	//评论策略
	void comment(String Username, String strategyName, String commenterName, LocalDate time, String comment) throws RemoteException;

	//StrategyShowVO getStrategy ( String createrName, String StrategyName ) throws RemoteException;

	//List<StrategyShowVO> getStrategyList ( String createrName) throws RemoteException;

	//List<StrategyShowVO> getStrategyList ( )throws RemoteException;

	// 修改策略公开或私有属性
	void setPublic(String creatroName, String straetgyName,boolean property) throws RemoteException;





}
