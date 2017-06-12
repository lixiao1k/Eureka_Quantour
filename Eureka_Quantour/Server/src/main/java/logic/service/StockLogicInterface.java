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
	 * 添加一系列股票到股票池
	 * @param stockSetName 股票池名字
	 * @param userName 用户名
	 * @param codelist 股票编号的列表
	 */
	public void addStockList_to_StockSet(String userName,String stockSetName,List<String> codelist) throws RemoteException;
	/**
	 * 获取市场指数的两个图
	 * @param name 市场名字
	 * @param begin 开始日期
	 * @param end 结束日期
	 * @return
	 * @throws RemoteException
	 * @throws DateInvalidException
	 * @throws BeginInvalidException
	 * @throws EndInvalidException
	 * @throws NullStockIDException
	 */
	public ExponentChartVO getExponentChart(String name,LocalDate begin,LocalDate end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException;
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
	public List<EMAInfoVO> getExponentEMAInfo( String name, LocalDate begin, LocalDate end )
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



	//get the stock set list name of a user
	List<String> getStockSet(String username) throws  RemoteException;

	//get the stock info of a stock set in a sorted way
	List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now,String username) throws RemoteException, NullMarketException;

	//get single stock info of a day
	SingleStockInfoVO getStockBasicInfo(String code, LocalDate now) throws NullStockIDException, NullDateException, RemoteException;

	// input the parameters and set the strategy
	void setStrategy(StrategyConditionVO strategyConditionVO, SaleVO s, LocalDate begin, LocalDate now, String stockSetName,String username)
		throws  RemoteException;

	//get the yield chart data
	//two list of double meaning data
	//one list of time
	YieldChartDataVO getYieldChartData()throws  RemoteException;;

	// get Yield Distribution data
	YieldDistributionHistogramDataVO getYieldDistributionHistogramData() throws  RemoteException;;

	//add a stock set of a user
	void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException,RemoteException;

	// delete a stock set of a user
	void deleteStockSet(String stockSetName, String username)throws  RemoteException;

	//add a stock into a stockset
	void addStockToStockSet(String stockName, String stockSetName, String username) throws StockNameRepeatException,RemoteException;

	// delete a stock from a specific stockset
	void deleteStockFromStockSet(String stockName, String stockSetName, String username)throws  RemoteException;

	//convert a name to a stock code
	String nameToCode(String name) throws RemoteException;

	// user save a strategy
	void saveStragety(StrategyVO strategyVO, String username) throws RemoteException, StrategyRepeatException;

	// user delete a strategy
	void deleteStrategy ( String createName, String strategyName) throws RemoteException;

	// user comment a strategy
	void comment(String Username, String strategyName, String commenterName, LocalDateTime time, String comment) throws RemoteException;

	// get the entire image of a strategy
	StrategyShowVO getStrategy ( String createrName, String StrategyName ) throws RemoteException;

	// get the user strategy list
	List<StrategyListVO> getStrategyList ( String createrName) throws RemoteException;

	// get all the public strategy list
	List<StrategyListVO> getStrategyList ( )throws RemoteException;

	// conver a strategy to public or private
	void setPublic(String creatroName, String straetgyName,boolean property) throws RemoteException;

	// get the arealist name
	List<String> getAreaList() throws RemoteException;

	// get the getConceptList name
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

	//模糊搜索
	List<String> fuzzySearch(String input) throws  RemoteException;



}
