package logic.supportimpl;

import java.time.LocalDate;

import data.service.IStockDataInterface;
import data.serviceimpl.StockDataController_2;
import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import logic.supportservice.CalculateValueInterface;
import logic.supportservice.PredictInterface;
import logic.supportservice.SortArrayInterface;
import logic.supportservice.StatisticInterface;
import po.SingleStockInfoPO;
import vo.SingleStockInfoVO;

public class PredictImpl implements PredictInterface{
	
	private CalculateValueInterface calValue = new CalculateValueImpl();
	private SortArrayInterface sortArray = new SortArrayImpl();
	private StatisticInterface statistic = new StatisticImpl();
	
	private IStockDataInterface stock = StockDataController_2.getInstance();

	private LocalDate zuizao = LocalDate.of(2005,2,1);

	@Override
	public double KNNPredictPrice(double[] closes, LocalDate[] dates, int m, int k) {
		// TODO Auto-generated method stub
		int n = closes.length;
		
		if( n<m || closes.length!=dates.length )
			return 0.0;

		// length of vector
		int vLen = m;
		// number of vector
		int vNum = n-m;

		double[] baseVector = new double[vLen];
		for( int i=0; i<vLen; i++ )
			baseVector[i] = closes[i+vNum];
	
		double[][] vectors = new double[vNum][vLen];
		for( int i=0; i<vNum; i++ )
			for( int j=0; j<vLen; j++ )
				vectors[i][j] = closes[i+j];
	
		// calcuate cos two vectors' include angle
		double[] cos = new double[vNum];
		double[] dateD = new double[vNum];
		double[][] closeD = new double[vNum][2];
		for( int i=0; i<vNum; i++ ){
			cos[i] = calValue.calCosIncludeAngle( baseVector, vectors[i] );
			LocalDate dateT = dates[i+vLen];
			dateD[i] = dateT.getYear() + dateT.getMonthValue() + dateT.getDayOfMonth();
			closeD[i][0] = closes[i+vLen-1];
			closeD[i][1] = closes[i+vLen];
		}

		// sort value of cos from max to min
		int[] cosSortIndex = sortArray.getSortIndexMaxToMin( cos );
		// select k max value of cos
		double[] KCos = new double[k];
		double[] KDateSave = new double[k];
		// save every vector's last close price and latter close price
		double[][] KCloseSave = new double[k][2];
		for( int i=0; i<k; i++ ){
			for( int j=0; j<vNum; j++ ){
				if( cosSortIndex[j]==i ){
					KCos[i] = cos[j];
					KDateSave[i] = dateD[j];
					KCloseSave[i][0] = closeD[j][0];
					KCloseSave[i][1] = closeD[j][1];
				}
			}
		}

		// sort date from 近 to 远
		int[] dateSortIndex = sortArray.getSortIndexMinToMax( KDateSave );
		for( int i=0; i<k; i++ )
			dateSortIndex[i] += i;

		// sort date and value of cos
		int[] sortIndex = new int[k];
		for( int i=0; i<k; i++ ){
			int minValue = 9000000;
			int index = 0;
			for( int j=0; j<k; j++ ){
				if( minValue>dateSortIndex[j] ){
					minValue = dateSortIndex[j];
					index = j;
				}
			}
			sortIndex[index] = i;
			dateSortIndex[index] = 10000000;
		}

		// calculate result
		double result = 0;
		for( int i=0; i<k; i++ )
			for( int j=0; j<k; j++ )
				if( sortIndex[j]==i ){
					result += (KCloseSave[j][1] - KCloseSave[j][0]) / KCloseSave[j][0] * 100.0 * statistic.getWeight( k, k-1-i );
				}
		result = 1 + result / 100.0;
		result = result * closes[n-1];
		return result;
	}

	
	/**
	 * @Description:
	 * @author: 	 hzp
	 * @date:        2017-06-03
	 */
	@Override
	public double SBPredictPrice(double ZPrice, double QPrice) {
		// TODO Auto-generated method stub
		double result = ZPrice + (ZPrice - QPrice)/2;
		if( result<0 )
			return 0;
		return result;
	}


	@Override
	public double KNNPredictPriceForStrategy(String stockcode, LocalDate date, int len, int m, int k) {
		// TODO Auto-generated method stub
		SingleStockInfoPO ssi = new SingleStockInfoPO();

		// get before 100 days' data
		int vLen = 115;
		if( len>5 && len<1000 )
			vLen = len;
		double[] closes = new double[vLen];
		LocalDate[] dates = new LocalDate[vLen];
		LocalDate dateT=zuizao;
		try {
			dateT = stock.addDays(date, -1);
		} catch (DateOverException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int index = vLen-1;
		while( index>-1 && dateT.compareTo(zuizao)>0 ){
			try{
				dateT = calValue.getValidBeforeDate( dateT );
				ssi = stock.getSingleStockInfo(stockcode, dateT);
				closes[index] = ssi.getAftClose();
				dates[index] = ssi.getDate();
				index--;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
			}
		}
		// fill none in array by random
		if( index>-1 ){
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
		}
		
		double predictPrice = KNNPredictPrice( closes, dates, m, k );
		
		return predictPrice;
		
	}


	@Override
	public double KNNPredictRODForStrategy(String stockcode, LocalDate date, int len, int m, int k) {
		// TODO Auto-generated method stub
		SingleStockInfoPO ssi = new SingleStockInfoPO();
		
		double QPrice = 0;
		LocalDate dateT = date.plusDays(1);
		while( QPrice==0 && dateT.compareTo(zuizao)>0 ){
			try{
				dateT = calValue.getValidBeforeDate( dateT );
				ssi = stock.getSingleStockInfo(stockcode, dateT);
				QPrice = ssi.getAftClose();
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
			}
		}
		
		double predictPrice = KNNPredictPriceForStrategy( stockcode, date, len, m, k);
		
		double ROD = ( predictPrice - QPrice ) / QPrice;
		
		return ROD;
	}
}
