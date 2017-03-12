package logic.serviceimpl;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import logic.service.StockLogicInterface;
import vo.ComparedInfoVO;
import vo.EMAInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;
/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月8日
 */
public class StockLogicImpl implements StockLogicInterface{

	private StockLogicImplStub slis = new StockLogicImplStub();   
	
	@Override
	public List<SingleStockInfoVO> getSingleStockInfoByTime( String stockCode, Calendar begin, Calendar end )throws RemoteException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<EMAInfoVO>> getEMAInfo( String stockCode, Calendar begin, Calendar end )throws RemoteException {
		// TODO Auto-generated method stub
		int methods[] = { 5, 10, 20, 30, 60 };
		// invoke stub to get data
		List<SingleStockInfoVO> lsti1 = slis.getSingleStockInfo(stockCode, begin, end);
		List<List<EMAInfoVO>> llemai = new ArrayList<List<EMAInfoVO>>();
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		
		for( int j=0; j<methods.length; j++){
			int method = methods[j];
			int methodTemp = method;
			double close = 0.0;
			List<EMAInfoVO> lemai = new ArrayList<EMAInfoVO>();

			double tempDouble = 0.0;
			for( int i=0; i<lsti1.size(); i++){
				ssi = lsti1.get(i);
				close = ssi.getClose();
				// 数据丢失
				if( close==0 ){
					methodTemp++;
					continue;
				}
				// 先处理不需要加权平均的数据
				if( i<(methodTemp-1) ){
					lemai.add( new EMAInfoVO( ssi.getDate(), close) );
					tempDouble += close;			
				}
				// 处理需要加权平均的数据
				else{
					tempDouble += close;
					lemai.add( new EMAInfoVO(ssi.getDate(), formatDouble(tempDouble/method)) );
					tempDouble -= lemai.get(i-method+1).getEMA();
				}
			}
			llemai.add(lemai);
		}
		return llemai;
	}

	@Override
	public ComparedInfoVO getComparedInfo(String stockCodeA, String stockCodeB, Calendar begin, Calendar end)throws RemoteException {
		// TODO Auto-generated method stub
		// invoke stub to get data
		List<SingleStockInfoVO> lstiA = slis.getSingleStockInfo(stockCodeA, begin, end);
		List<SingleStockInfoVO> lstiB = slis.getSingleStockInfo(stockCodeB, begin, end);
		SingleStockInfoVO ssiA = new SingleStockInfoVO();
		SingleStockInfoVO ssiB = new SingleStockInfoVO();
		int tempInt = lstiA.size();
		ComparedInfoVO ci = new ComparedInfoVO(tempInt);
		
		// 获取前一天的数据
		double closeA1 = 0.0, closeB1 = 0.0;
		Calendar tempCal = begin;
		int getCount = 5;
		for( int i=0; i<getCount; i++ ){
			tempCal = calendarAdvance(tempCal);
			if( ssiA.getCode().equals("") )
				ssiA = getSingleStockInfoByTime(stockCodeA, tempCal, tempCal).get(0);
			if( ssiB.getCode().equals("") )
				ssiB = getSingleStockInfoByTime(stockCodeB, tempCal, tempCal).get(0);
			if( !ssiA.getCode().equals("") && !ssiB.getCode().equals(""))
				break;
		}
		closeA1 = ssiA.getClose();
		closeB1 = ssiB.getClose();
		
		ci.setNameA(lstiA.get(0).getName());
		ci.setNameB(lstiB.get(0).getName());
		ci.setCodeA(stockCodeA);
		ci.setCodeB(stockCodeB);
		// 涨幅为正，跌幅为负
		int iOpen = 0, iClose = tempInt-1;
		double dOpen =0.0, dClose = 0.0;
		while( lstiA.get(iOpen).getOpen()==0 && iOpen<(tempInt-1) )
			iOpen++;
		dOpen = lstiA.get(iOpen).getOpen();
		if( dOpen!=0 ){
			while( lstiA.get(iClose).getClose()==0 && iClose>-1 )
				iClose--;
			dClose = lstiA.get(iClose).getClose();
			ci.setRODA( formatDouble( dClose-dOpen ) / dOpen );
		}
		iOpen = 0; iClose = tempInt-1;
		dOpen =0.0; dClose = 0.0;
		while( lstiB.get(iOpen).getOpen()==0 && iOpen<(tempInt-1) )
			iOpen++;
		dOpen = lstiB.get(iOpen).getOpen();
		if( dOpen!=0 ){
			while( lstiB.get(iClose).getClose()==0 && iClose>-1 )
				iClose--;
			dClose = lstiB.get(iClose).getClose();
			ci.setRODB( formatDouble( dClose-dOpen ) / dOpen );
		}
		
		double maxA = 0, minA = 0, maxB = 0, minB = 0;
		double closeA2 = 0.0, closeB2 = 0.0;
		double[] logYieldA = new double[tempInt];
		double[] logYieldB = new double[tempInt];
		double[] closeA = new double[tempInt];
		double[] closeB = new double[tempInt];
		Calendar[] date = new Calendar[tempInt];
		ssiA = new SingleStockInfoVO();
		ssiB = new SingleStockInfoVO();	
		for( int i=0 ; i<tempInt ; i++ ){
			ssiA = lstiA.get(i);
			ssiB = lstiB.get(i);
			closeA[i] = ssiA.getClose();
			closeB[i] = ssiB.getClose();
			date[i] = ssiA.getDate();
			// 获取最高（低）值
			if( lstiA.get(i).getHigh()>maxA )
				maxA = ssiA.getHigh();
			if( lstiB.get(i).getHigh()>maxB )
				maxB = ssiB.getHigh();
			if( lstiA.get(i).getLow()<minA )
				minA = ssiA.getLow();
			if( lstiB.get(i).getLow()<minB )
				minB = ssiB.getLow();
			
			closeA2 = ssiA.getClose();
			if( closeA1!=0 )
				logYieldA[i] = formatDouble( Math.log(closeA2/closeA1) );
			closeA1 = closeA2;
			closeB2 = ssiB.getClose();
			if( closeB1!=0 )
				logYieldB[i] = formatDouble( Math.log(closeB2/closeB1) );
			closeB1 = closeB2;
		}
		
		ci.setCloseA(closeA);
		ci.setCloseB(closeB);
		ci.setLogYieldA(logYieldA);
		ci.setLogYieldB(logYieldB);
		ci.setLogYieldVarianceA(calVariance(logYieldA));
		ci.setLogYieldVarianceB(calVariance(logYieldB));
		ci.setDate(date);
		return ci;
	}

	@Override
	public MarketInfoVO getMarketInfo(Calendar date)throws RemoteException {
		// TODO Auto-generated method stub
		MarketInfoVO mi = new MarketInfoVO();
		List<SingleStockInfoVO> lsti = slis.getMarketInfo(date);
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		
		// 用来存储上一个交易日的数据
		Calendar begin = date;
		SingleStockInfoVO ssiTemp = new SingleStockInfoVO();
		for( int i=0; i<5; i++ ){
			begin = calendarAdvance(begin);
			ssiTemp = new SingleStockInfoVO();
			if( !ssiTemp.getCode().equals("") )
				break;
		}
		
		int volume = 0;
		int riseStop = 0, dropStop = 0, riseEFP = 0, stopEFP = 0;
		int OMCEFP = 0, OMCLTFP = 0;
		for( int i=0; i<lsti.size(); i++ ){
			ssi = lsti.get(i);
			double close = ssi.getClose();
			double open = ssi.getOpen();
			
			volume += ssi.getVolume();
			if(open>0){
				if( ifDoubleEqual((ssi.getHigh()-open)/open,  0.10) )
					riseStop++;
				if( ifDoubleEqual((open-ssi.getLow())/open,  0.10) )
					dropStop++;
				if( (close-open)/open > 0.05 )
					riseEFP++;
				if( (open-close)/open > 0.05 )
					stopEFP++;
			}
			if( (open-close) > (0.05*ssiTemp.getClose()) )
				OMCEFP++;
			else if( (open-close) < (-0.05*ssiTemp.getClose()) )
				OMCLTFP++;
		}
		
		mi.setVolume(volume);
		mi.setNumOfRiseStop(riseStop);
		mi.setNumOfDropStop(dropStop);
		mi.setNumOfRiseEFP(riseEFP);
		mi.setNumOfDropEFP(stopEFP);
		mi.setNumOfOMCEFP(OMCEFP);
		mi.setNumOfOMCLTFP(OMCLTFP);
		
		return mi;
	}

	private double formatDouble(double d){
		DecimalFormat df = new DecimalFormat("#0.00");
		return Double.parseDouble(df.format(d));
	}
	
	private double calVariance(double[] num){
		int length = num.length;
		double result = 0.0;
		double tempD1 = 0.0;
		double tempD2 = 0.0;
		for(int i=0;i<length;i++){
			tempD1 += Math.pow(num[i], 2);
			tempD2 += num[i];
		}
		tempD2 = Math.pow(tempD2, 2);
		result = (tempD1 - tempD2/length) / length;
		return formatDouble( result );
	}
	
	public boolean ifDoubleEqual(double d1, double d2){
		String s1 = String.valueOf(formatDouble(d1));
		String s2 = String.valueOf(formatDouble(d2));
		return s1.equals(s2);
	}
	
	public Calendar calendarAdvance(Calendar cal){
		if( cal.get(Calendar.DAY_OF_WEEK) == 2 )
			cal.add(Calendar.DAY_OF_MONTH, -3);
		else if( cal.get(Calendar.DAY_OF_WEEK) == 1 )
			cal.add(Calendar.DAY_OF_MONTH, -2);
		else
			cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal;
	}
}
