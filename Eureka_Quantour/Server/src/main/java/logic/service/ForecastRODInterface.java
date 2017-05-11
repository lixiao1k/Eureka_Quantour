package logic.service;

import java.time.LocalDate;

import vo.StockRODVO;

public interface ForecastRODInterface {
	
	public StockRODVO getStockROD( String stockcode, LocalDate begindate, LocalDate enddate);
	
}
