package logic.serviceimpl;

import java.rmi.RemoteException;
import java.time.LocalDate;

import data.service.IStockDataInterface;
import data.serviceimpl.StockDataController_2;
import exception.NullDateException;
import exception.NullStockIDException;
import logic.service.ForecastRODInterface;
import logic.supportimpl.CalculateValueImpl;
import logic.supportimpl.PredictImpl;
import logic.supportimpl.StatisticImpl;
import logic.supportservice.CalculateValueInterface;
import logic.supportservice.PredictInterface;
import logic.supportservice.StatisticInterface;
import vo.PredictVO;
import vo.SingleStockInfoVO;

/**
 * @Description: 
 * @author: hzp 
 * @date: May 13, 2017
 */
public class ForecastRODImpl implements ForecastRODInterface{
	
	private CalculateValueInterface calValue = new CalculateValueImpl();
	private PredictInterface predict = new PredictImpl();
	private StatisticInterface statistic = new StatisticImpl();
	
	private IStockDataInterface stock = StockDataController_2.getInstance();

	private LocalDate zuizao = LocalDate.of(2005,2,1);

	@Override
	public PredictVO predict( String stockcode, LocalDate date ) throws RemoteException {
		// TODO Auto-generated method stub
		PredictVO predictVO = new PredictVO();
		SingleStockInfoVO ssi = new SingleStockInfoVO();

		// get before 100 days' data
		int vLen = 115;
		double[] closes = new double[vLen];
		LocalDate[] dates = new LocalDate[vLen];
		LocalDate dateT = date.plusDays(1);
		int index = vLen-1;
		double QPrice = 0.0;
		while( index>-2 && dateT.compareTo(zuizao)>0 ){
			try{
				dateT = calValue.getValidBeforeDate( dateT );
				ssi = new SingleStockInfoVO( stock.getSingleStockInfo(stockcode, dateT) );
				if( index>-1 ){
					closes[index] = ssi.getClose();
					dates[index] = ssi.getDate();
				}
				else
					QPrice = ssi.getClose();
				index--;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
			}
		}
		// fill none in array by random
		if( index>-2 ){
			while( index>-1 ){
				int indexT = index + (int)(Math.random()*( vLen - index ));
				if( indexT>vLen-1 )
					indexT = vLen-1;
				if( indexT>index ){
					closes[index] = closes[indexT];
					dates[index] = dates[indexT];
					index--;
				}
			}
			int indexT = index + (int)(Math.random()*( vLen - index ));
			if( indexT>vLen-1 )
				indexT = vLen-1;
			QPrice = closes[indexT];
		}
		
		double[] RODs = new double[vLen];
		for( int i=0; i<vLen; i++ ){
			double ROD = 0.0;
			if( i==0 )
				ROD = ( closes[i] - QPrice ) / QPrice;
			else
				ROD = ( closes[i] - closes[i-1] ) / closes[i-1];
			
			RODs[i] = ROD;
		}
		
		double[] AverageAndVarianceROD = calValue.calTotalityAverageAndVariance( RODs );
		double ave = AverageAndVarianceROD[0];
		double var = AverageAndVarianceROD[1];
		
		predictVO.setMinPrice90ZhiXin(
				statistic.preMinPriceByRODOf90ZhiXin( ave, var, calValue.getNumOfSample(), closes[vLen-1] ) );
		predictVO.setMinPrice99ZhiXin(
				statistic.preMinPriceByRODOf99ZhiXin( ave, var, calValue.getNumOfSample(), closes[vLen-1] ) );
		predictVO.setMaxPrice90ZhiXin(
				statistic.preMaxPriceByRODOf90ZhiXin( ave, var, calValue.getNumOfSample(), closes[vLen-1] ) );
		predictVO.setMaxPrice99ZhiXin(
				statistic.preMaxPriceByRODOf99ZhiXin( ave, var, calValue.getNumOfSample(), closes[vLen-1] ) );
		
		double ZPrice = closes[vLen-1];
		QPrice = closes[vLen-2];
		// length of vector
		int m = 5;
		// 取前 k vectors
		int k = 15;
		double predictPrice = predict.KNNPredictPrice( closes, dates, m, k );
		double predictROD = (predictPrice - ZPrice) / ZPrice;

		if( predictROD>0.1 || predictROD<-0.1 ){
			double predictPriceT = predict.SBPredictPrice( ZPrice, QPrice );
			double predictRODT = (predictPriceT - ZPrice) / ZPrice;
			if( (predictRODT<predictROD && predictROD<-0.1) ||
				(predictRODT>predictROD && predictROD>0.1) );
			else{
				predictPrice = predictPriceT;
				predictROD = predictRODT;
			}
		}
		predictVO.setPredictPrice( predictPrice );
		predictVO.setPredictROD( predictROD );

		return predictVO;
	}
	
}
