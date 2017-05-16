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
	
	@Override
	public StockRODVO getStockROD( String stockcode, LocalDate begindate, LocalDate enddate, int numOfDay, double alpha )
			throws RemoteException{
		// TODO Auto-generated method stub
		StockRODVO srod = new StockRODVO();
		LocalDate zuizao = LocalDate.of(2005,2,1);

		// save qian numOfDay day stock's close
		if( numOfDay<100 || numOfDay>300 )
			numOfDay = 100;
		double[] closes = new double[numOfDay];
		int dataNum = 0;
		LocalDate date = begindate;
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		while( dataNum<numOfDay && date.compareTo(zuizao)>0 ){
			try{
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, date) );
				date = date.minusDays(1);
				closes[dataNum] = ssi.getClose();
				dataNum++;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
				date = date.plusDays(1);
			}
		}
		if( dataNum<(numOfDay-1) ){
			for( ; dataNum<numOfDay; dataNum++ ){
				int index = (int)(Math.random()*dataNum);
				if( index>dataNum-1 )
					index = dataNum-1;
				closes[dataNum] = closes[index];
			}
		}
		
		
		//the number of day owning data
		int dayNum = 0;
		
		// the true ROD of one day
		double ROD = 0;

		/*
		* get the index of array in StockRODVO
		* the initialzaion of idate is -1 which show the day is weekend
		*/
		int idate = -1;
		int iROD = 0;
		
		// save two days' close
		double close1 = 0;
		double close2 = 0;
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
			double[] AvgAndFangcha = new double[2];
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
				
				AvgAndFangcha = ForecastRODImpl.calAvgAndFangcha( closes );
				ifROE = ForecastRODImpl.preROE( AvgAndFangcha[0], AvgAndFangcha[1], 1, alpha, ROD);

				// update the numOfDay-day data in array
				closes[dayNum] = close1;
				dayNum++;
				if( dayNum==numOfDay )
					dayNum = 0;


				

			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){
				srod.nodata[idate][1]++;
			}
			
		}
		return srod;
	}
	
	@Override
	public PredictVO predict(String stockcode, LocalDate date) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
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
	* return the distribution of ROD in array
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

	/**
	 * 
	 * @Description: judge if real value in the zhi xin qu jian
	 * @author: hzp
	 * @date: May 17, 2017
	 * @param: @param aveg
	 * @param: @param fangcha
	 * @param: @param num : chou yang ci shu
	 * @param: @param alpha : ji suan zhi xin qu jian de α
	 * @param: @param ROD
	 */
	private static boolean preROE( double aveg, double fangcha, int num, double alpha, double ROD){
		double zalpha = 0.0;
		if( alpha==0.1 )
			zalpha = 1.65;
		else if( alpha==0.05 )
			zalpha = 1.96;
		else if( alpha==0.01 )
			zalpha = 2.58;
		double lower = aveg - fangcha*zalpha/Math.sqrt(num);
		double upper = aveg + fangcha*zalpha/Math.sqrt(num);
		if( ROD>=lower && ROD<=upper )
			return true;
		else
			return false;

	}

	private static double[] calAvgAndFangcha( double[] closes ){
		// select stock's numOfDay-day data random, calculate yangben avg and fangcha
		int numOfDay = closes.length;

		double[] yangbenAvg = new double[30];
		double avg = 0.0;
		double fangcha = 0.0;
		for( int i=0; i<30; i++ ){
			double RODSum = 0;
			for( int j=0; i<20; j++ ){
				double random = Math.random();
				int index = (int)(random*numOfDay);
				if( index>(numOfDay-1) )
					index = numOfDay-1;
				RODSum += closes[index];
			}
			yangbenAvg[i] = RODSum / 20;
		}
		for( int i=0; i<yangbenAvg.length; i++ ){
			avg += yangbenAvg[i];
			fangcha += Math.pow( yangbenAvg[i], 2 );
		}
		avg = avg / yangbenAvg.length;
		fangcha -= Math.pow( avg, 2 );

		double AvgAndFangcha[] = new double[2];
		AvgAndFangcha[0] = avg;
		AvgAndFangcha[1] = fangcha;

		return AvgAndFangcha;
	}

}
