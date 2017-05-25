package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

import vo.PredictVO;
import vo.StockRODVO;

public interface ForecastRODInterface extends Remote {
	
	/**
	 * 
	 * @Description: TODO
	 * @author: hzp
	 * @date: May 23, 2017
	 * @param: @param stockcode
	 * @param: @param begindate
	 * @param: @param enddate
	 * @param: @param numOfDay
	 * @param: @param alpha
	 * @param: @param m : number of vector's character
	 * @param: @param k : number of KNN data
	 * @param: @return
	 * @param: @throws RemoteException 
	 * @return: StockRODVO
	 */
	public StockRODVO getStockROD( String stockcode, LocalDate begindate, LocalDate enddate, int numOfDay, double alpha, int m, int k )
			throws RemoteException;
	
	/**
	 * 
	 * @Description: TODO
	 * @author: hzp
	 * @date: May 14, 2017
	 * @param: stockcode : the code of stock
	 * @param: date : the newest time of stock
	 * @return: PredictVO
	 */
	public PredictVO predict( String stockcode, LocalDate date ) throws RemoteException;
}
