package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;

import resultmessage.BeginInvalidException;
import resultmessage.DateInvalidException;
import resultmessage.EndInvalidException;
import vo.ComparedInfoVO;
import vo.EMAInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;
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
	public List<SingleStockInfoVO> getSingleStockInfoByTime ( String stockCode, Calendar begin, Calendar end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;
	/**
	 * @Description: to get all stocks' EMA between "begin" and "end",
	 *               invoke getSingleStockInfo() in data layer
	 * @return: Iterator<Double>
	 */
	public List<List<EMAInfoVO>> getEMAInfo ( String stockCode, Calendar begin, Calendar end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;
	/**
	 * @Description: to get stock A and B's compared information between "begin" and "end",
	 *               invoke getSingleStockInfo() in data layer
	 * @return: ComparedInfoVO
	 */
	public ComparedInfoVO getComparedInfo ( String stockCodeA, String stockCodeB, Calendar begin, Calendar end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;
	/**
	 * @Description: to get market's information by date,
	 *               invoke getMarketByDate() in data layer
	 * @return: MarketInfoVO
	 */
	public MarketInfoVO getMarketInfo ( Calendar date )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException;
}
