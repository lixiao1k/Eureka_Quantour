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
 * 
 * @Description: TODO
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
		int dataNum = numOfDay-1;
		LocalDate date = begindate;
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		while( dataNum>-1 && date.compareTo(zuizao)>0 ){
			try{
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				date = date.minusDays(1);
				closes[dataNum] = ssi.getClose();
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
		int iQROD = ForecastRODImpl.doubletoindex(QROD);
		int iZROD = ForecastRODImpl.doubletoindex(ZROD);
		int avgNum = 2;
		double avg = (closes[0]+closes[1]) / 2;
		double ROD = 0;
		int iROD = 0;
		for( int i=2; i<numOfDay; i++ ){
			ROD = RODs[i];
			iROD = ForecastRODImpl.doubletoindex(ROD);
			srod.firstFloor[iZROD][iROD]++;
			srod.secondFloor[iQROD][iZROD][iROD]++;

			double ZClose = closes[i-1];
			int iYesAvgROD = ForecastRODImpl.doubletoindex( (ZClose-avg)/avg );
			srod.YesAvgROD[iYesAvgROD][iROD]++;

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
		iQROD = ForecastRODImpl.doubletoindex(QROD);
		iZROD = ForecastRODImpl.doubletoindex(ZROD);
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
				iROD = ForecastRODImpl.doubletoindex( ROD );
				srod.wROD[idate][iROD]++;
				
				average = ForecastRODImpl.calAvg( RODs );
				square = ForecastRODImpl.calVariance( RODs );
				int iYesAvgROD = ForecastRODImpl.doubletoindex( (close2-avg)/avg );

				double today = ForecastRODImpl.predictPrice( closes, m, k );
				PreROD = (today-close1) / close1;
				
				ifROE = ForecastRODImpl.preROE( PreROD, square, 1, alpha, ROD);
				if( ifROE )
					srod.zhixin[0]++;
				else
					srod.zhixin[1]++;
				
				// update the srod's content
				srod.firstFloor[iZROD][iROD]++;
				srod.secondFloor[iQROD][iZROD][iROD]++;
				srod.YesAvgROD[iYesAvgROD][iROD]++;

				iQROD = iZROD;
				iZROD = iROD;
				avg = (avg*avgNum+close2)/(avgNum+1);
				avgNum++;
				
				// update the numOfDay-day data in array
				RODs[dayNum] = ROD;
				closes[dayNum] = close2;
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
		double predictPrice = ForecastRODImpl.predictPrice( closes, dates );
		predictVO.setPredictPrice( predictPrice );

		double predictROD = (predictPrice-closes[closes.length-1]) / closes[closes.length-1];
		predictVO.setPredictROD( predictROD );

		return predictVO;
	}
	

/* **************************************************************************************************************** */
	/**
	 * [predictPrice description]
	 * @Author H2P
	 * @Date   2017-06-01
	 * @param  closes     datas of close price
	 * @param  m          length of vector
	 * @param  k          number of relevant character
	 */
	public static double predictPrice( double[] closes, LocalDate[] dates, int m, int k ){
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
	
		double[][] cos = new double[vNum][2];
		for( int i=0; i<vNum; i++ ){
			cos[i][0] = ForecastRODImpl.calCosIncludeAngle( baseVector, vectors[i] );
			cos[i][1] = dates[i];
		}
		double[][] cosSave = new double[vNum][2];
		cosSave = cos;
		
		// bubble sort cos
		for( int i=0; i<vNum-1; i++ ){
			for( int j=0; j<vNum-1-i; j++ ){
				if( cos[j][0]>cos[j+1][0] ){
					double[] temp = new double[2];
					temp = cos[j];
					cos[j] = cos[j+1];
					cos[j+1] = temp;
				}
			}
		}

		// get the first k index in cosSave
		int[] index = new int[k];
		for( int i=0; i<k; i++ ){
			for( int j=0; j<vNum; j++ ){
				if( df.format(cosSave[j]).equals(df.format(cos[i])) ){
					index[j] = i;
					break;
				}
			}
		}



		double sum = 0;
		for( int i=0; i<k; i++ ){
			if( index[i]==(vNum-1) )
				sum += baseVector[vLen-1];
			else
				sum += vectors[index[i]+1][vLen-1];
		}

		result =  sum / k;
		
		return result;
	}
/* **************************************************************************************************************** */


/* **************************************************************************************************************** */
	private static double predictPrice( double[] closes ){
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
	private static int doubletoindex( double ROD){
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
	private static boolean preROE( double average, double square, int num, double alpha, double real){
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
		double vectorMul = ForecastRODImpl.vectorMul( vector1, vector2 );
		double vectorModelMul = ForecastRODImpl.vectorModel( vector1 )*ForecastRODImpl.vectorModel( vector2 );

		return vectorMul / vectorModelMul;
	}

	/**
	 * 
	 * @Description: calculate two vector mul
	 * @author: hzp
	 * @date: May 23, 2017
	 */
	private static double vectorMul( double[] vector1, double[] vector2 ){
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
	 * 
	 * @Description: function : Wi = w1 + (i-1) * d; k Wi's values' sum = 1
	 * @author: hzp
	 * @date: May 24, 2017
	 * @param: @param k : number of factor's weight 
	 * @param: @param i : index of factor
	 */
	private static double getweight( int k, int i ){
		if( i>k )
			return 0;
		double w0 = 0.004;
		double d = 2 * (1-k*w0) / ( k*(k-1) );
		if( i==0 )
			return w0;
		else
			return w0 + (i-1)*d;
	}

}
