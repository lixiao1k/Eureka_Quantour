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

public class ForecastRODImpl implements ForecastRODInterface{

	private static DecimalFormat df = new DecimalFormat("0.0000");
	
	private IDataInterface idata = new DataInterfaceImpl();
	
	@Override
	public StockRODVO getStockROD( String stockcode, LocalDate begindate, LocalDate enddate, double step, int numOfDay ) throws RemoteException{
		// TODO Auto-generated method stub
		StockRODVO srod = new StockRODVO();

		LocalDate date = begindate;
		
		//the number of day owning data
		int dayNum = 0;
		double RODT = 0;
		// the sum of rise percent
		double RODP = 0;
		// the sum of down percent
		double RODN = 0;

		// the number of rise appears
		double PNum = 0;
		// the number of down appears
		double NNum = 0;
		
		double weightP = 0.5;
		double weightN = 0.5;

		// the number of predicting rise wrongly
		int pError = 0;
		// the number of predicting down wrongly
		int nError = 0;
		
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
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		ssi.setClose(-1);

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
				srod.RODw[iROD][idate]++;
				
				if( dayNum>=numOfDay ){
					RODT = weightP*RODP + weightN*RODN;
					RODT /= dayNum;

					if( RODT>0 ){
						if( ROD<0 ){
							pError++;
							nError = 0;
							srod.Pos[1]++;
						}
						else{
							pError = 0;
							nError = 0;
							srod.Pos[0]++;
						}
					}
					else if( RODT<0 ){
						if( ROD>0 ){
							pError = 0;
							nError ++;
							srod.Neg[1]++;
						}
						else{
							pError = 0;
							nError = 0;
							srod.Neg[0]++;
						}					
					}

					RODT -= ROD;
					srod.ROETimes[preROE(RODT)]++;
				}
				
				if( ROD>0 ){
					PNum ++;
					if( ROD<0.1 )
						RODP += ROD;
					else
						RODP += 0.1;
				}
				else{
					NNum ++;
					if( ROD>-0.1 )
						RODN += ROD;
					else
						RODN += -0.1;
				}
				dayNum++;
				
				if( pError>0 ){
					weightP = 1 - ForecastRODImpl.FuncG( pError*step );
					weightN = 1 - weightP;
				}
				else if( nError>0 ){
					weightN = 1 - ForecastRODImpl.FuncG( nError*step );
					weightP = 1 - weightN;
				}
				else{
					weightP = 0.5;
					weightN = 0.5;
				}

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
	

	/*
	* judge if the forecast is right
	* if dt >= 0.01, the forcast is wrong
	*/
	private static int preROE( double d ){
		double dt = Math.abs(d);
		if( dt<0.01 )
			return (int)(dt/0.001);
		else
			return 10;
	}
	
	/*
	* G(index)
	*/
	private static double FuncG( double index ){
		double denominator = 1.0 + Math.pow( Math.E , index);
		return 1.0/denominator;
	}

}
