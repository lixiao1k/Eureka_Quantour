package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;
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
	 List<SingleStockInfoVO> getSingleStockInfoByTime ( String stockCode, Calendar begin, Calendar end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;
	/**
	 * @Description: to get all stocks' EMA between "begin" and "end",
	 *               invoke getSingleStockInfo() in data layer
	 * @return: Iterator<Double>
	 */
	 List<List<EMAInfoVO>> getEMAInfo ( String stockCode, Calendar begin, Calendar end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;
	/**
	 * @Description: to get stock A and B's compared information between "begin" and "end",
	 *               invoke getSingleStockInfo() in data layer
	 * @return: ComparedInfoVO
	 */
	 ComparedInfoVO getComparedInfo ( String stockCodeA, String stockCodeB, Calendar begin, Calendar end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;
	/**
	 * @Description: to get market's information by date,
	 *               invoke getMarketByDate() in data layer
	 * @return: MarketInfoVO
	 */
	 MarketInfoVO getMarketInfo ( Calendar date )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;


	 List<String> getStockSet(String username);

	 List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, Calendar now);

	 SingleStockInfoVO getStockBasicInfo(String code, Calendar now);

	 List<SingleStockInfoVO> getStockSorted(String stockSetName, Calendar now);


	 void setStrategy(StrategyConditionVO sc, SaleVO s, Calendar begin, Calendar now, String
			stockSetName);

	 YieldChartDataVO getYieldChartData();

	 YieldDistributionHistogramDataVO getYieldDistributionHistogramData();

	 void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException;

	 void deleteStockSet(String stockSetName, String username);

	 void addStockToStockSet(String stockName, String stockSetName, String username) throws StockNameRepeatException;

	 void deleteStockFromStockSet(String stockName, String stockSetName, String username);

}
