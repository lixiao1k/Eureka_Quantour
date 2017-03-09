package logicserviceimpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import logicservice.StockLogicInterface;
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
	public List<SingleStockInfoVO> getSingleStockInfoByTime(String stockCode, Calendar begin, Calendar end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<EMAInfoVO>> getEMAInfo(String stockCode, Calendar begin, Calendar end ) {
		// TODO Auto-generated method stub
		int methods[] = { 5, 10, 20, 30, 60 };
		// invoke stub to get data
		List<SingleStockInfoVO> lsti1 = slis.getSingleStockInfo(stockCode, begin, end);
		List<List<EMAInfoVO>> llemai = new ArrayList<List<EMAInfoVO>>();
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		
		for( int j=0; j<methods.length; j++){
			int method = methods[j];
			List<EMAInfoVO> lemai = new ArrayList<EMAInfoVO>();
			// 如果数据不足以加权平均，则不处理数据
			if(lsti1.size()<method){
				for(int i=0;i<lsti1.size();i++){
					ssi = lsti1.get(i);
					lemai.add( new EMAInfoVO(ssi.getDate(), ssi.getClose()) );
				}
			}
			else{
				double tempDouble = 0.0;
				// 先处理不需要加权平均的数据
				for(int i=0;i<method-1;i++){
					ssi = lsti1.get(i);
					lemai.add( new EMAInfoVO(ssi.getDate(), ssi.getClose()) );
					tempDouble += lemai.get(i).getEMA();
				}
				// 处理需要加权平均的数据
				for(int i=method-1;i<lsti1.size();i++){
					ssi = lsti1.get(i);
					tempDouble += ssi.getClose();
					lemai.add( new EMAInfoVO(ssi.getDate(), formatDouble(tempDouble/method)) );
					tempDouble -= lemai.get(i-method+1).getEMA();
				}
			}
			llemai.add(lemai);
		}
		return llemai;
	}

	@Override
	public ComparedInfoVO getComparedInfo(String stockCodeA, String stockCodeB, Calendar begin, Calendar end) {
		// TODO Auto-generated method stub
		// invoke stub to get data
		List<SingleStockInfoVO> lstiA = slis.getSingleStockInfo(stockCodeA, begin, end);
		List<SingleStockInfoVO> lstiB = slis.getSingleStockInfo(stockCodeB, begin, end);
		int tempInt = lstiA.size();
		
		ComparedInfoVO ci = new ComparedInfoVO(lstiA.size());
		ci.setNameA(lstiA.get(0).getName());
		ci.setNameB(lstiB.get(0).getName());
		ci.setCodeA(lstiA.get(0).getCode());
		ci.setCodeB(lstiB.get(0).getCode());
		ci.setRODA( (lstiA.get(0).getOpen()-lstiA.get(tempInt).getClose()) 
				/ lstiA.get(0).getOpen());
		ci.setRODB( (lstiB.get(0).getOpen()-lstiB.get(tempInt).getClose()) 
				/ lstiB.get(0).getOpen());
		
		double maxA = 0, minA = 0, maxB = 0, minB = 0;
		double open = 0.0, close = 0.0;
		double[] logYieldA = new double[tempInt];
		double[] logYieldB = new double[tempInt];
		double[] closeA = new double[tempInt];
		double[] closeB = new double[tempInt];
		Calendar[] dateA = new Calendar[tempInt];
		Calendar[] dateB = new Calendar[tempInt];
		SingleStockInfoVO ssiA = new SingleStockInfoVO();
		SingleStockInfoVO ssiB = new SingleStockInfoVO();	
		for( int i=0 ; i<tempInt ; i++ ){
			ssiA = lstiA.get(i);
			ssiB = lstiB.get(i);
			dateA[i] = ssiA.getDate();
			dateB[i] = ssiB.getDate();
			closeA[i] = ssiA.getClose();
			closeB[i] = ssiB.getClose();
					
			// 获取最高（低）值
			if( lstiA.get(i).getHigh()>maxA )
				maxA = ssiA.getHigh();
			if( lstiB.get(i).getHigh()>maxB )
				maxB = ssiB.getHigh();
			if( lstiA.get(i).getLow()<minA )
				minA = ssiA.getLow();
			if( lstiB.get(i).getLow()<minB )
				minB = ssiB.getLow();
			
			open = ssiA.getOpen(); close = ssiA.getClose();
			logYieldA[i] = Math.log(close/open);
			open = ssiB.getOpen(); close = ssiB.getClose();
			logYieldB[i] = Math.log(close/open);
		}
		
		ci.setCloseA(closeA);
		ci.setCloseB(closeB);
		ci.setLogYieldA(logYieldA);
		ci.setLogYieldB(logYieldB);
		ci.setLogYieldVarianceA(calVariance(logYieldA));
		ci.setLogYieldVarianceB(calVariance(logYieldB));
		
		return null;
	}

	@Override
	public MarketInfoVO getMarketInfo(Calendar date) {
		// TODO Auto-generated method stub
		MarketInfoVO mi = new MarketInfoVO();
		List<SingleStockInfoVO> lsti = slis.getMarketInfo(date);
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		
		// 用来存储上一个交易日的数据
		SingleStockInfoVO ssiTemp = lsti.get(0);
		int volume = 0;
		int riseStop = 0, dropStop = 0, riseEFP = 0, stopEFP = 0;
		int OMCEFP = 0, OMCLTFP = 0;
		for( int i=1; i<lsti.size(); i++ ){
			ssi = lsti.get(i);
			double close = ssi.getClose();
			double open = ssi.getOpen();
			
			volume += ssi.getVolume();
			if(open>0){
				if( ifDoubleEqual((ssi.getHigh()-open)/open,  0.1) )
					riseStop++;
				if( ifDoubleEqual((open-ssi.getLow())/open,  0.1) )
					dropStop++;
				if( (close-open)/open > 0.05 )
					riseEFP++;
				if( (open-close)/open > 0.05 )
					stopEFP++;
			}
			if( (open-close) > 0 ){
				if( (open-close) > (0.05*ssiTemp.getClose()) )
					OMCEFP++;
			}
			else{
				if( (open-close) < (-0.05*ssiTemp.getClose()) )
					OMCLTFP++;
			}
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
		result = (tempD1-tempD2/length)/length;
		return result;
	}
	
	private boolean ifDoubleEqual(double d1, double d2){
		String s1 = String.valueOf(formatDouble(d1));
		String s2 = String.valueOf(formatDouble(d2));
		return s1.equals(s2);
	}
}
