package logic.serviceimpl;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.LocalDate;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.NullDateException;
import exception.NullStockIDException;
import logic.service.ForecastRODInterface;
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
		int iQROD = ForecastRODImpl.doubleToIndex(QROD);
		int iZROD = ForecastRODImpl.doubleToIndex(ZROD);
		int avgNum = 2;
		double avg = (closes[0]+closes[1]) / 2;
		double ROD = 0;
		int iROD = 0;
		for( int i=2; i<numOfDay; i++ ){
			ROD = RODs[i];
			iROD = ForecastRODImpl.doubleToIndex(ROD);

			double ZClose = closes[i-1];
			int iYesAvgROD = ForecastRODImpl.doubleToIndex( (ZClose-avg)/avg );
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
		iQROD = ForecastRODImpl.doubleToIndex(QROD);
		iZROD = ForecastRODImpl.doubleToIndex(ZROD);
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
				while( (idate = ForecastRODImpl.getDayOfWeek(date)) == -1 ){
					date = date.plusDays(1);
				}
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				close2 = ssi.getClose();

				// the number of data existing add one
				srod.nodata[idate][0]++;

				ROD = (close2-close1)/close1;		
				iROD = ForecastRODImpl.doubleToIndex( ROD );
				srod.wROD[idate][iROD]++;
				
				average = ForecastRODImpl.calAvg( RODs );
				square = ForecastRODImpl.calVariance( RODs );
				int iYesAvgROD = ForecastRODImpl.doubleToIndex( (close2-average)/average );

				double today = ForecastRODImpl.KNNPredictPrice( closes, dates, m, k );
				PreROD = (today-close1) / close1;
				
				ifROE = ForecastRODImpl.predictROE( PreROD, square, 1, alpha, ROD);
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

		// double predictPrice = ForecastRODImpl.predictPrice( closes, 5, 25);
		double predictPrice = ForecastRODImpl.KNNPredictPrice( closes, dates, 5, 25 );
		predictVO.setPredictPrice( predictPrice );

		double predictROD = (predictPrice-closes[closes.length-1]) / closes[closes.length-1];
		predictVO.setPredictROD( predictROD );

		return predictVO;
	}
	

/* **************************************************************************************************************** */
	/**
	 * @author H2P
	 * @date   2017-06-01
	 * @param  closes     datas of close price
	 * @param  m          length of vector
	 * @param  k          number of relevant character
	 */
	public static double KNNPredictPrice( double[] closes, LocalDate[] dates, int m, int k ){
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
			cos[i] = ForecastRODImpl.calCosIncludeAngle( baseVector, vectors[i] );
			LocalDate dateT = dates[i+vLen];
			dateD[i] = dateT.getYear() + dateT.getMonthValue() + dateT.getDayOfMonth();
			closeD[i] = closes[i+vLen];
		}

		// sort value of cos from max to min
		int[] cosSortIndex = ForecastRODImpl.getSortIndexMaxToMin( cos );
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
		int[] dateSortIndex = ForecastRODImpl.getSortIndexMinToMax( KDateSave );
		for( int i=0; i<k; i++ )
			dateSortIndex[i] += i;

		// sort date and value of cos
		int[] sortIndex = new int[k];
		for( int i=0; i<k; i++ ){
			int minValue = -100;
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
				if( sortIndex[j]==i )
					result += KCloseSave[j] * ForecastRODImpl.getWeight( k, k-1-i );

		return result;
	}
/* **************************************************************************************************************** */


/* **************************************************************************************************************** */
	private static double SBPredictPrice( double[] closes ){
		if( closes.length<2 )
			return 0;
		double ZPrice = closes[closes.length-1];
		double QPrice = closes[closes.length-2];
		double result = ZPrice + (ZPrice - QPrice)/2;
		return result;
	}
/* **************************************************************************************************************** */


	private static int getDayOfWeek( LocalDate date){
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
	private static int doubleToIndex( double ROD){
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

	private static double calAvg( double[] data ){
		double sum = 0.0;
		for( int i=0; i<data.length; i++ )
			sum += data[i];
		return sum / data.length;
	}

	private static double calVariance( double[] data ){
		double avg = ForecastRODImpl.calAvg( data );
		double avgSquare = Math.pow( avg, 2 );

		double[] data2 = new double[data.length];
		for( int i=0; i<data.length; i++ )
			data2[i] = Math.pow( data[i], 2 );
		double squareAvg = ForecastRODImpl.calAvg( data2 );

		return (squareAvg - avgSquare);
	}

	/**
	 * 
	 * @Description: judge if real value in the zhi xin qu jian
	 * @author: hzp
	 * @date: May 17, 2017
	 * @param: @param num : chou yang ci shu
	 * @param: @param alpha : ji suan zhi xin qu jian de α
	 * @param: @param real
	 */
	private static boolean predictROE( double average, double square, int num, double alpha, double real){
		double zalpha = 0.0;
		if( alpha==0.1 )
			zalpha = 1.65;
		else if( alpha==0.05 )
			zalpha = 1.96;
		else if( alpha==0.01 )
			zalpha = 2.58;
		double lower = average - Math.sqrt(square)*zalpha/Math.sqrt(num);
		double upper = average + Math.sqrt(square)*zalpha/Math.sqrt(num);

		if( real>=lower && real<=upper ){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * 
	 * @Description: calculate cos ( two vector's include angle )
	 * @author: hzp
	 * @date: May 23, 2017
	 */
	private static double calCosIncludeAngle( double[] vector1, double[] vector2 ){
		if( vector1.length!=vector2.length )
			return 0;
		double vectorMul = ForecastRODImpl.vectorMultiply( vector1, vector2 );
		double vectorModelMul = ForecastRODImpl.vectorModel( vector1 )*ForecastRODImpl.vectorModel( vector2 );

		return vectorMul / vectorModelMul;
	}

	/**
	 * 
	 * @Description: calculate two vectors' multiply-product
	 * @author: hzp
	 * @date: May 23, 2017
	 */
	private static double vectorMultiply( double[] vector1, double[] vector2 ){
		if( vector1.length!=vector1.length )
			return 0;

		double sum = 0;
		for( int i=0; i<vector1.length; i++ )
			sum += vector1[i]*vector2[i];
		return sum;
	}

	/**
	 * 
	 * @Description: calculate vector's model
	 * @author: hzp
	 * @date: May 23, 2017
	 */
	private static double vectorModel( double[] vector ){
		double sum = 0;
		for( int i=0; i<vector.length; i++ )
			sum += Math.pow( vector[i], 2 );
		sum = Math.sqrt( sum );
		return sum;
	}

	/**
	 * 进过测试，结果和为 1 
	 * @Description: function : Wi = w0 + i * d; k Wi's values' sum = 1
	 * @author: hzp
	 * @date: May 24, 2017
	 * @param: @param k : number of factor's weight 
	 * @param: @param i : index of factor
	 */
	private static double getWeight( int k, int i ){
		if( i>k )
			return 0;
		double w0 = 0.004;
		double d = 2.0 * (1.0-k*w0) / ( k*(k-1.0) );

		return w0 + i*d;
	}

	/**
	 * @Description:
	 * @author: 	 hzp
	 * @date:        2017-06-01
	 * @param        nums       sort nums and return every num's order
	 */
	private static int[] getSortIndexMinToMax( double[] nums ){
		int len = nums.length;

		double[][] temp = new double[len][2];
		for( int i=0; i<len; i++ ){
			temp[i][0] = nums[i];
			temp[i][1] = i;
		}

		for( int k=0; k<len-1; k++ ){
			for( int i=0; i<len-1-k; i++ ){
				if( temp[i][0]>temp[i+1][0] ){
					double tempD = temp[i][0];
					double tempIndex = temp[i][1];

					temp[i][0] = temp[i+1][0];
					temp[i][1] = temp[i+1][1];

					temp[i+1][0] = tempD;
					temp[i+1][1] = tempIndex;
				}
			}
		}

		int[] index = new int[len];
		for( int i=0; i<len; i++ )
			index[(int)temp[i][1]] = i;

		return index; 
	}

	private static int[] getSortIndexMaxToMin( double[] nums ){
		int[] indexMinToMax = ForecastRODImpl.getSortIndexMinToMax( nums );
		return ForecastRODImpl.arrayConverse( indexMinToMax );
	}

	private static int[] arrayConverse( int[] index ){
		int len = index.length;

		int[] result = new int[len];
		for( int i=0; i<len; i++ )
			result[i] = index[len-1-i];

		return result;
	}
	
}
