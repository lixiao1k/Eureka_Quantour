package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

import vo.PredictVO;
import vo.StockRODVO;

public interface ForecastRODInterface extends Remote {
	
	public StockRODVO getStockROD( String stockcode, LocalDate begindate, LocalDate enddate, double step, int numOfDay )
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