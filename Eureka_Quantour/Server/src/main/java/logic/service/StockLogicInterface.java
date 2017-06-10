package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import exception.*;
import po.CompanyInfoPO;
import vo.*;

/**
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月6日
 */
public interface StockLogicInterface extends Remote {
	
	/**
	 * 获取市场的均线图
	 * @param name 市场名
	 * @param begin 开始日期
	 * @param end 结束日期
	 * @return
	 * @throws RemoteException
	 * @throws DateInvalidException
	 * @throws BeginInvalidException
	 * @throws EndInvalidException
	 * @throws NullStockIDException
	 */
	public List<EMAInfoVO> getExponentEMAInfo( String stockCode, LocalDate begin, LocalDate end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException;
	
	public CompanyInfoVO getLatestCommpanyInfo(LocalDate time,String code) throws NullStockIDException, NullDateException, RemoteException;
	
	List<SingleStockInfoVO> getExponentInfoByTime (String name, LocalDate begin, LocalDate end )
			 throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException;
	
	
	/**
	 * @Description: to get all stocks' information between "begin" and "end", 
	 *               invoke getSingleStockInfo() in data layer
	 * @return: Iterator<SingleStockInfoVO>
	 */
	List<SingleStockInfoVO> getSingleStockInfoByTime (String stockCode, LocalDate begin, LocalDate end )
			 throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException;
	/**
	 * @Description: to get all stocks' EMA between "begin" and "end",
	 *               invoke getSingleStockInfo() in data layer
	 * @return: Iterator<Double>
	 */
	List<EMAInfoVO> getEMAInfo ( String stockCode, LocalDate begin, LocalDate end )
			 throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException, DateOverException;
	/**
	 * @Description: to get stock A and B's compared information between "begin" and "end",
	 *               invoke getSingleStockInfo() in data layer
	 * @return: ComparedInfoVO
	 */
	ComparedInfoVO getComparedInfo ( String stockCodeA, LocalDate begin, LocalDate end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException;
	/**
	 * @Description: to get market's information by date,
	 *               invoke getMarketByDate() in data layer
	 * @return: MarketInfoVO
	 */
	MarketInfoVO getMarketInfo ( LocalDate date,String marketname )
            throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullMarketException;
	
	/*
	 * @Description: to get the name list of Industry
	 * @author:LXD
	 */
	List<String> getIndustryList() throws RemoteException;


	List<String> getStockSet(String username) throws  RemoteException;

	List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now,String username) throws RemoteException, NullMarketException;

	SingleStockInfoVO getStockBasicInfo(String code, LocalDate now) throws NullStockIDException, NullDateException, RemoteException;

	void setStrategy(StrategyConditionVO strategyConditionVO, SaleVO s, LocalDate begin, LocalDate now, String stockSetName,String username)
		throws  RemoteException;

	YieldChartDataVO getYieldChartData()throws  RemoteException;;

	YieldDistributionHistogramDataVO getYieldDistributionHistogramData() throws  RemoteException;;

	void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException,RemoteException;

	void deleteStockSet(String stockSetName, String username)throws  RemoteException;

	void addStockToStockSet(String stockName, String stockSetName, String username) throws StockNameRepeatException,RemoteException;

	void deleteStockFromStockSet(String stockName, String stockSetName, String username)throws  RemoteException;

	String nameToCode(String name) throws RemoteException;

	void saveStragety(StrategyVO strategyVO, String username) throws RemoteException, StrategyRepeatException;

	void deleteStrategy ( String createName, String strategyName) throws RemoteException;

	void comment(String Username, String strategyName, String commenterName, LocalDateTime time, String comment) throws RemoteException;

	StrategyShowVO getStrategy ( String createrName, String StrategyName ) throws RemoteException;

	List<StrategyListVO> getStrategyList ( String createrName) throws RemoteException;

	List<StrategyListVO> getStrategyList ( )throws RemoteException;

	void setPublic(String creatroName, String straetgyName,boolean property) throws RemoteException;

	List<String> getAreaList() throws RemoteException;

	List<String> getConceptList() throws RemoteException;

	/**
	 * 获取股票分时线
	 * @param code
	 * @param date
	 * @return 从9点到11点半，再从13点到15点的以1秒为单位的double数组
	 * @throws TimeShraingLackException
	 * @throws NullStockIDException
	 * @throws RemoteException 
	 */
	TimeSharingVO getTimeSharingData(String code,LocalDate date)throws TimeShraingLackException,NullStockIDException, RemoteException;

	List<String> fuzzySearch(String input) throws  RemoteException;



}
