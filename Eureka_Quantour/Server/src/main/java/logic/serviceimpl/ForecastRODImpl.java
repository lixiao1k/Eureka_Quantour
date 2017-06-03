package logic.serviceimpl;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
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

	private static DecimalFormat df = new DecimalFormat("0.0000");
	
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

/* **************************************************************************************************************** */
		// save qian numOfDay day stock's close
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
				date = date.minusDays(1);
				dataNum--;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
				date = date.minusDays(1);
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


/* **************************************************************************************************************** */		
		// calculate numOfDay-day's ROD
		double[] RODs = new double[numOfDay];

		ssi = new SingleStockInfoVO();
		ssi.setClose(-1);
		while( ssi.getClose()==-1 && date.compareTo(zuizao)>0 ){
			try{
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				date = date.minusDays(1);
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
				date = date.minusDays(1);
			}
		}

		double closeT = ssi.getClose();
		if( closeT==-1 ){
			int index = (int)(Math.random()*numOfDay);
			if( index>numOfDay-1)
				index = numOfDay-1;
			closeT = closes[index];
		}

		for( int i=0; i<numOfDay; i++ ){
			double RODT = (closes[i]-closeT)/closeT;
			closeT = closes[i];
			RODs[i] = RODT;
		}
/* **************************************************************************************************************** */


/* **************************************************************************************************************** */
		/*
		* begin fill in firstFloor and secondFloor
		* Q : qian tian
		* Z : zuo tian
		* i : index
		*/
		double QROD = RODs[0];
		double ZROD = RODs[1];
		int iQROD = doubleToIndex(QROD);
		int iZROD = doubleToIndex(ZROD);
		int avgNum = 2;
		double avg = (closes[0]+closes[1]) / 2;
		double ROD = 0;
		int iROD = 0;
		for( int i=2; i<numOfDay; i++ ){
			ROD = RODs[i];
			iROD = doubleToIndex(ROD);

			double ZClose = closes[i-1];
			int iYesAvgROD = doubleToIndex( (ZClose-avg)/avg );
			srod.YesterdayAvgROD[iYesAvgROD][iROD]++;

			QROD = ZROD;
			ZROD = ROD;
			iQROD = iZROD;
			iZROD = iROD;
			avg = (avg*avgNum+closes[i])/(avgNum+1);
			avgNum++;
		}
/* **************************************************************************************************************** */


/* **************************************************************************************************************** */
		//the number of day owning data
		int dayNum = 0;
		
		double close1 = 0;
		double close2 = 0;

		QROD = RODs[RODs.length-2];
		ZROD = RODs[RODs.length-1];
		ROD = 0;

		/*
		* get the index of array in StockRODVO
		* the initialzaion of idate is -1 which show the day is weekend
		*/
		int idate = -1;
		iQROD = doubleToIndex(QROD);
		iZROD = doubleToIndex(ZROD);
		iROD = 0;
		
		// save two days' close
		ssi = new SingleStockInfoVO();
		ssi.setClose(-1);
		date = begindate;

		// get the first data
		while( ssi.getClose()==-1 ){
			try{
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				close2 = ssi.getClose();
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
				date = date.plusDays(1);
			}
		}
		
		while( date.compareTo(enddate)<=0 ){
			double PreROD = 0;
			double average = 0.0;
			double square = 0.0;
			boolean ifROE = false;
			try{
				close1 = close2;

				// get next day's data
				date = date.plusDays(1);
				while( (idate = getDayOfWeek(date)) == -1 ){
					date = date.plusDays(1);
				}
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				close2 = ssi.getClose();

				// the number of data existing add one
				srod.nodata[idate][0]++;

				ROD = (close2-close1)/close1;		
				iROD = doubleToIndex( ROD );
				srod.wROD[idate][iROD]++;
				
				average = calValue.calAvg( RODs );
				square = calValue.calVariance( RODs );
				int iYesAvgROD = doubleToIndex( (close2-average)/average );

				double today = KNNPredictPrice( closes, dates, m, k );
				PreROD = (today-close1) / close1;
				
				ifROE = statistic.predictROE( PreROD, square, 1, alpha, ROD);
				if( ifROE )
					srod.zhixin[0]++;
				else
					srod.zhixin[1]++;
				
				// update the srod's content
				srod.YesterdayAvgROD[iYesAvgROD][iROD]++;

				iQROD = iZROD;
				iZROD = iROD;
				avg = (avg*avgNum+close2)/(avgNum+1);
				avgNum++;
				
				// update the numOfDay-day data in array
				RODs[dayNum] = ROD;
				closes[dayNum] = close2;
				dates[dayNum] = date;
				dayNum++;
				if( dayNum==numOfDay-1 )
					dayNum = 0;

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
			}catch ( NullDateException e){
				srod.nodata[idate][1]++;
			}
		}
/* **************************************************************************************************************** */

		return srod;
	}
	
	@Override
	public PredictVO predict(String stockcode, LocalDate date) throws RemoteException {
		// TODO Auto-generated method stub
		PredictVO predictVO = new PredictVO();
		SingleStockInfoVO ssi = new SingleStockInfoVO();

		double[] closes = new double[100];
		LocalDate[] dates = new LocalDate[100];
		LocalDate dateT = date;
		int index = closes.length-1;
		while( index>-1 && dateT.compareTo(zuizao)>0 ){
			try{
				dateT = dateT.minusDays(1);
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, dateT) );
				closes[index] = ssi.getClose();
				dates[index] = ssi.getDate();
				index--;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
			}
		}

		if( index>-1 ){
			while( index>-1 ){
				int indexT = index + (int)(Math.random()*( closes.length - index ));
				if( indexT>closes.length-1 )
					indexT = closes.length-1;
				if( indexT>index ){
					closes[index] = closes[indexT];
					dates[index] = dates[indexT];
					index--;
				}
			}
		}

		// double predictPrice = SBPredictPrice( closes[closes.length-1], closes[closes.length-2] );
		double predictPrice = KNNPredictPrice( closes, dates, 5, 25 );
		predictVO.setPredictPrice( predictPrice );

		double predictROD = (predictPrice-closes[closes.length-1]) / closes[closes.length-1];
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
		double result = 0;
		int n = closes.length;
	
		if( n<m )
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
		double[] closeD = new double[vNum];
		for( int i=0; i<vNum; i++ ){
			cos[i] = calValue.calCosIncludeAngle( baseVector, vectors[i] );
			LocalDate dateT = dates[i+vLen];
			dateD[i] = dateT.getYear() + dateT.getMonthValue() + dateT.getDayOfMonth();
			closeD[i] = closes[i+vLen];
		}

		// sort value of cos from max to min
		int[] cosSortIndex = sortArray.getSortIndexMaxToMin( cos );
		// select k max value of cos
		double[] KCos = new double[k];
		double[] KDateSave = new double[k];
		double[] KCloseSave = new double[k];
		for( int i=0; i<k; i++ ){
			for( int j=0; j<vNum; j++ ){
				if( cosSortIndex[j]==i ){
					KCos[i] = cos[j];
					KDateSave[i] = dateD[j];
					KCloseSave[i] = closeD[j];
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
		for( int i=0; i<k; i++ )
			for( int j=0; j<k; j++ )
				if( sortIndex[j]==i ){
					result += KCloseSave[j] * statistic.getWeight( k, k-1-i );
				}
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
	
	/*
	* return the index of distribution of ROD in array
	*/
	private int doubleToIndex( double ROD){
		String str = df.format(ROD);
		double d = Double.valueOf(str);
		
		if( d<-0.1)
			return 0;
		else if( d>=-0.1 && d<=0 ){
			int result = (int)((d+0.1) / 0.01);
			result += 1;
			return result;
		}
		else if( d>0 && d<0.1 ){
			int result = (int)((d+0.1) / 0.01);
			result += 2;
			return result;
		}
		else if( d==0.1 )
			return 21;
		else
			return 22;
	}
	
}
