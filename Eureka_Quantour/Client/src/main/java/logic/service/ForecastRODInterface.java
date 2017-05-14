package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

import vo.StockRODVO;

public interface ForecastRODInterface extends Remote {
	
	public StockRODVO getStockROD( String stockcode, LocalDate begindate, LocalDate enddate) throws RemoteException;
	
}
