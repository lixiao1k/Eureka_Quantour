package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

import vo.PredictVO;

public interface ForecastRODInterface extends Remote {
	
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
