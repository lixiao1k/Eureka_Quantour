package logic.serviceimpl;

import java.rmi.RemoteException;
import java.time.LocalDate;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.NullDateException;
import exception.NullStockIDException;
import logic.service.ForecastRODInterface;
import logic.supportimpl.CalculateValueImpl;
import logic.supportimpl.SortArrayImpl;
import logic.supportimpl.StatisticImpl;
import logic.supportservice.CalculateValueInterface;
import logic.supportservice.SortArrayInterface;
import logic.supportservice.StatisticInterface;
import vo.PredictVO;
import vo.SingleStockInfoVO;
import vo.StockRODVO;

/**
 * @Description: 
 * @author: hzp 
 * @date: May 13, 2017
 */
public class ForecastRODImpl implements ForecastRODInterface{
	
	private CalculateValueInterface calValue = new CalculateValueImpl();
	private SortArrayInterface sortArray = new SortArrayImpl();
	private StatisticInterface statistic = new StatisticImpl();
	
	private IDataInterface idata = new DataInterfaceImpl();

	private LocalDate zuizao = LocalDate.of(2005,2,1);
	
	@Override
	public StockRODVO getStockROD( String stockcode, LocalDate begindate, LocalDate enddate, int numOfDay, double alpha, int m, int k )
			throws RemoteException{
		// TODO Auto-generated method stub
		StockRODVO srod = new StockRODVO();

/* save qian numOfDay-day stock's close
 **************************************************************************************************************** */
		if( numOfDay<100 )
			numOfDay = 100;
		double[] closes = new double[numOfDay];
		LocalDate[] dates = new LocalDate[numOfDay];
		int dataNum = numOfDay-1;
		LocalDate date = begindate;
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		while( dataNum>-1 && date.compareTo(zuizao)>0 ){
			try{
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				dates[dataNum] = date;
				closes[dataNum] = ssi.getClose();
				date = getValidBeforeDate( date );
				dataNum--;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
				date = getValidBeforeDate( date );
			}
		}
		if( dataNum>-1 ){
			for( ; dataNum>-1; dataNum-- ){
				int index = (int)( Math.random()*(numOfDay-dataNum) );
				if( index<=dataNum )
					index = dataNum+1;
				else if( index>=numOfDay-1 )
					index = numOfDay-1;
				closes[dataNum] = closes[index];
				dates[dataNum] = dates[index];
			}
		}
/* **************************************************************************************************************** */


/* calculate numOfDay-day's ROD
 **************************************************************************************************************** */		
		double[] RODs = new double[numOfDay];

		// get pro day's close price
		ssi = new SingleStockInfoVO();
		ssi.setClose(-1);
		while( ssi.getClose()==-1 && date.compareTo(zuizao)>0 ){
			try{
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				date = getValidBeforeDate( date );
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
				date = getValidBeforeDate( date );
			}
		}

		double closeT = ssi.getClose();
		if( closeT==-1 ){
			int index = (int)(Math.random()*numOfDay);
			if( index>numOfDay-1)
				index = numOfDay-1;
			closeT = closes[index];
		}

		// calculate every day's ROD
		for( int i=0; i<numOfDay; i++ ){
			double RODT = (closes[i]-closeT)/closeT;
			closeT = closes[i];
			RODs[i] = RODT;
		}
/* **************************************************************************************************************** */


/* **************************************************************************************************************** */
		// zuo tian price
		double ZPrice = 0;
		// jin tian price
		double JPrice = closes[numOfDay-1];
		double ROD = 0;
		
		ssi = new SingleStockInfoVO();
		date = begindate;
		
		while( date.compareTo(enddate)<=0 ){
			double PreROD = 0;
			boolean ifROE = false;
			try{
				ZPrice = JPrice;

				// get next day's data
				date = getValidLatterDate( date );
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				JPrice = ssi.getClose();

				ROD = (JPrice - ZPrice) / ZPrice;		

				double todayKNN = KNNPredictPrice( closes, dates, m, k );
				double todaySB = SBPredictPrice( ZPrice, closes[numOfDay-2] );
				double RODKNN = (todayKNN - ZPrice) / ZPrice;
				double RODSB = (todaySB - ZPrice) / ZPrice;
				if( RODKNN<-0.1 || RODKNN>0.1 ){
					if( (RODKNN<-0.1 && RODSB<RODKNN) ||
						(RODKNN>0.1 && RODSB>RODKNN) )
						PreROD = RODKNN;
					else
						PreROD = RODSB;
				}
				else
					PreROD = RODKNN;
				
				double[] AveAndVar = new double[2];
				AveAndVar = calValue.calTotalityAverageAndVariance( RODs );
				
				ifROE = statistic.predictROE( AveAndVar[0], AveAndVar[1], calValue.getNumOfSample(), alpha, PreROD );
				if( ifROE )
					srod.zhixin[0]++;
				else
					srod.zhixin[1]++;
				
				// update the numOfDay-day data in array
				for( int i=0; i<numOfDay-1; i++ ){
					RODs[i] = RODs[i+1];
					closes[i] = closes[i+1];
					dates[i] = dates[i+1];
				}
				RODs[numOfDay-1] = ROD;
				closes[numOfDay-1] = JPrice;
				dates[numOfDay-1] = date;

				if( PreROD>0 ){
					if( ROD>0 )
						srod.Pos[0]++;
					else
						srod.Pos[1]++;
				}
				else if( PreROD<0 ){
					if( ROD<0 )
						srod.Neg[0]++;
					else
						srod.Neg[1]++;
				}
				
				
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){}
		}
/* **************************************************************************************************************** */

		return srod;
	}
	
	@Override
	public PredictVO predict(String stockcode, LocalDate date) throws RemoteException {
		// TODO Auto-generated method stub
		PredictVO predictVO = new PredictVO();
		SingleStockInfoVO ssi = new SingleStockInfoVO();

		// get before 100 days' data
		int vLen = 100;
		double[] closes = new double[vLen];
		LocalDate[] dates = new LocalDate[vLen];
		LocalDate dateT = date.plusDays(1);
		int index = vLen-1;
		while( index>-1 && dateT.compareTo(zuizao)>0 ){
			try{
				dateT = getValidBeforeDate( date );
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, dateT) );
				closes[index] = ssi.getClose();
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

		double ZPrice = closes[vLen-1];
		double QPrice = closes[vLen-2];
		double predictPrice = KNNPredictPrice( closes, dates, 5, 15 );
		double predictROD = (predictPrice - ZPrice) / ZPrice;

		if( predictROD>0.1 || predictROD<-0.1 ){
			double predictPriceT = SBPredictPrice( ZPrice, QPrice );
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
	

	/**
	 * @author H2P
	 * @date   2017-06-01
	 * @param  closes     datas of close price
	 * @param  m          length of vector
	 * @param  k          number of relevant character
	 */
	public double KNNPredictPrice( double[] closes, LocalDate[] dates, int m, int k ){
		// KNN algorithm
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
	 * @param        closes     [description]
	 * @return                  [description]
	 */
	private double SBPredictPrice( double ZPrice, double QPrice ){
		double result = ZPrice + (ZPrice - QPrice)/2;
		if( result<0 )
			return 0;
		return result;
	}

	/**
	 * @Description: 根据日期返回 星期 的代号，如果是-1表示不是交易日
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param date
	 */
	private int getDayOfWeek( LocalDate date){
		String dow = date.getDayOfWeek().toString();
		if( dow.equals("MONDAY"))
			return 0;
		else if( dow.equals("TUESDAY") )
			return 1;
		else if( dow.equals("WEDNESDAY") )
			return 2;
		else if( dow.equals("THURSDAY") )
			return 3;
		else if( dow.equals("FRIDAY"))
			return 4;
		else
			return -1;
	}

	private LocalDate getValidLatterDate( LocalDate date ){
		date = date.plusDays(1);
		while( getDayOfWeek(date)==-1 )
			date = date.plusDays(1);
		return date;
	}

	private LocalDate getValidBeforeDate( LocalDate date ){
		date = date.minusDays(1);
		while( getDayOfWeek(date)==-1 )
			date = date.minusDays(1);
		return date;
	}
	
}
