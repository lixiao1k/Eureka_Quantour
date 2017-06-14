package logic.serviceimpl;

import java.rmi.RemoteException;
import java.time.LocalDate;

import data.service.IStockDataInterface;
import data.serviceimpl.StockDataController_2;
import exception.NullDateException;
import exception.NullStockIDException;
import logic.service.ForecastRODInterface;
import logic.supportimpl.BPNetImpl;
import logic.supportimpl.BPNetSupportImpl;
import logic.supportimpl.CalculateValueImpl;
import logic.supportimpl.PredictImpl;
import logic.supportimpl.StatisticImpl;
import logic.supportservice.BPNetInterface;
import logic.supportservice.BPNetSupportInterface;
import logic.supportservice.CalculateValueInterface;
import logic.supportservice.PredictInterface;
import logic.supportservice.StatisticInterface;
import po.SingleStockInfoPO;
import vo.PredictVO;

/**
 * @Description: 用于预测股票明天价格
 * @author: hzp 
 * @date: May 13, 2017
 */
public class ForecastRODImpl implements ForecastRODInterface{
	
	private CalculateValueInterface calValue = new CalculateValueImpl();
	private PredictInterface predict = new PredictImpl();
	private StatisticInterface statistic = new StatisticImpl();
	
	private IStockDataInterface stock = StockDataController_2.getInstance();

	// 所有股票有数据的最早的日期
	private LocalDate zuizao = LocalDate.of(2005,2,1);
	// 需要记录的内容个数：开、收、高、低、交易量
	private static final int characterNum = 5;
	
	@Override
	public PredictVO predict( String stockcode, LocalDate date ) throws RemoteException {
		// TODO Auto-generated method stub
		PredictVO predictVO = new PredictVO();
		SingleStockInfoPO ssi = new SingleStockInfoPO();
		BPNetSupportInterface bps = new BPNetSupportImpl();
		
		//学习系数
	    double rate = 0.15;
	    //动量系数
	    double mobp = 0.8;
		BPNetInterface bp = new BPNetImpl( bps.getNumOfInput(), rate, mobp );
		
		
/*********************************************************************************************************/

		
		// get before 100 + 1 days' data
		int vLen = 100 + 1;
		double[] openPrice = new double[vLen];
		double[] highPrice = new double[vLen];
		double[] closePrice = new double[vLen];
		double[] lowPrice = new double[vLen];
		double[] vol = new double[vLen];
		
		// bagin to get data
		LocalDate dateT = date.plusDays(1);

		int index = vLen-1;
		while( index>-1 && dateT.compareTo(zuizao)>0 ){
			try{
				dateT = calValue.getValidBeforeDate( dateT );
				ssi = stock.getSingleStockInfo(stockcode, dateT);
				
				openPrice[index] 	=  ssi.getOpen() ;
				highPrice[index] 	=  ssi.getHigh();
				closePrice[index] 	=  ssi.getClose();
				lowPrice[index] 	=  ssi.getLow();
				vol[index] 			=  (double)ssi.getVolume();
				
				index--;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){}
		}
		
		// 如果没有数据，直接返回
		if( index==vLen-1 )
			return predictVO;
		
		// 随意摘取数据补全101
		while( index>-1 ){
			int indexT = (int)( Math.random()*(vLen-1-index) ) + index;
			if( indexT>vLen-1 )
				indexT = vLen - 1;
			else if( indexT<=index )
				indexT = index + 1;
				 
			openPrice[index] 	=  openPrice[indexT];
			highPrice[index] 	=  highPrice[indexT];
			closePrice[index] 	=  closePrice[indexT];
			lowPrice[index] 	=  lowPrice[indexT];
			vol[index] 			=  vol[indexT];
		}
		
/******************************************************/	
		// get vLen-day data before 101 days
		vLen = bps.getMaxNum();
		double[][] QMaxNumDayData = new double[vLen][characterNum];
		index = vLen-1;
		while( index>-1 && dateT.compareTo(zuizao)>0 ){
			try{
				dateT = calValue.getValidBeforeDate( dateT );
				ssi = stock.getSingleStockInfo(stockcode, dateT);
				
				QMaxNumDayData[index][bps.getOpenIndex()] 	=  ssi.getOpen();
				QMaxNumDayData[index][bps.getHighIndex()] 	=  ssi.getHigh();
				QMaxNumDayData[index][bps.getCloseIndex()] 	=  ssi.getClose();
				QMaxNumDayData[index][bps.getLowIndex()] 	=  ssi.getLow();
				QMaxNumDayData[index][bps.getVolumeIndex()] =  (double)ssi.getVolume();
				
				index--;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){}
		}
		
		while( index>-1 ){
			int indexT = (int)( Math.random()*(vLen-1-index) ) + index;
			if( indexT>vLen-1 )
				indexT = vLen - 1;
			else if( indexT<=index )
				indexT = index + 1;
				 
			QMaxNumDayData[index][bps.getOpenIndex()] 	=  QMaxNumDayData[indexT][bps.getOpenIndex()];
			QMaxNumDayData[index][bps.getHighIndex()] 	=  QMaxNumDayData[indexT][bps.getHighIndex()];
			QMaxNumDayData[index][bps.getCloseIndex()] 	=  QMaxNumDayData[indexT][bps.getCloseIndex()];
			QMaxNumDayData[index][bps.getLowIndex()] 	=  QMaxNumDayData[indexT][bps.getLowIndex()];
			QMaxNumDayData[index][bps.getVolumeIndex()]	=  QMaxNumDayData[indexT][bps.getVolumeIndex()];
		}
		
/******************************************************/			
		// 初始化bp神经网络的输入
		double[][] dataset = bps.bpTrain( closePrice, highPrice, lowPrice, openPrice, vol, QMaxNumDayData );
	    
		// 开始训练和学习
	    for( int n=0; n<5000; n++ )
            for( int i=0; i<vLen-1; i++ )
                bp.train( dataset[i], closePrice[i+1] );
	    // 预测价格
	    double[] a = bp.computeOut( dataset[vLen-1] );
	    double predictPrice = - Math.log( 1/a[0] - 1 ) * 100;

/*********************************************************************************************************/
		
		double[] RODs = new double[vLen-1];
		for( int i=0; i<vLen-1; i++ ){
			double ROD = 0.0;
			ROD = ( closePrice[i+1] - closePrice[i] ) / closePrice[i];
			
			RODs[i] = ROD;
		}
		
		double[] AverageAndVarianceROD = calValue.calTotalityAverageAndVariance( RODs );
		double ave = AverageAndVarianceROD[0];
		double var = AverageAndVarianceROD[1];
		
		double ZPrice = closePrice[closePrice.length-1];
		
		// 计算 90 / 99 置信区间的上下限
		predictVO.setMinPrice90ZhiXin(
				statistic.preMinPriceByRODOf90ZhiXin( ave, var, calValue.getNumOfSample(), ZPrice ) );
		predictVO.setMinPrice99ZhiXin(
				statistic.preMinPriceByRODOf99ZhiXin( ave, var, calValue.getNumOfSample(), ZPrice ) );
		predictVO.setMaxPrice90ZhiXin(
				statistic.preMaxPriceByRODOf90ZhiXin( ave, var, calValue.getNumOfSample(), ZPrice ) );
		predictVO.setMaxPrice99ZhiXin(
				statistic.preMaxPriceByRODOf99ZhiXin( ave, var, calValue.getNumOfSample(), ZPrice ) );

		double predictROD = (predictPrice - ZPrice) / ZPrice;
		
		// 如果涨跌幅超过10%，则修改
		if( predictROD>0.1 || predictROD<-0.1 ){
			double QPrice = closePrice[closePrice.length-2];
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
	
//	@Override
//	public PredictVO predict( String stockcode, LocalDate date ) throws RemoteException {
//		// TODO Auto-generated method stub
//		PredictVO predictVO = new PredictVO();
//		SingleStockInfoPO ssi = new SingleStockInfoPO();
//
//		// get before 115 days' data
//		int vLen = 115;
//		
//		List<Double> closes = new ArrayList<>();
//		Double[] closesA = new Double[vLen];
//		List<LocalDate> dates = new ArrayList<>();
//		
//		LocalDate dateT = date.plusDays(1);
//		int index = vLen-1;
//		double QPrice = 0.0;
//		while( index>-2 && dateT.compareTo(zuizao)>0 ){
//			try{
//				dateT = calValue.getValidBeforeDate( dateT );
//				ssi = stock.getSingleStockInfo(stockcode, dateT);
//				if( index>-1 ){
//					closes.add( ssi.getClose() );
//					closesA[index] = ssi.getClose();
//					dates.add( ssi.getDate() );
//				}
//				else
//					QPrice = ssi.getClose();
//				index--;
//			}catch ( NullStockIDException e ){
//				e.printStackTrace();
//			}catch ( NullDateException e){
//			}
//		}
//		// fill none in array by random
//		if( index>-2 ){
//			while( index>-1 ){
//				int indexT = index + (int)(Math.random()*( vLen - index ));
//				if( indexT>vLen-1 )
//					indexT = vLen-1;
//				if( indexT>index ){
//					closes.add( closesA[indexT] );
//					closesA[index] = closesA[indexT];
//					dates.add( dates.get( indexT ) );
//					index--;
//				}
//			}
//			int indexT = index + (int)(Math.random()*( vLen - index ));
//			if( indexT>vLen-1 )
//				indexT = vLen-1;
//			QPrice = closesA[indexT];
//		}
//		
//		double[] RODs = new double[vLen];
//		for( int i=0; i<vLen; i++ ){
//			double ROD = 0.0;
//			if( i==0 )
//				ROD = ( closesA[i] - QPrice ) / QPrice;
//			else
//				ROD = ( closesA[i] - closesA[i-1] ) / closesA[i-1];
//			
//			RODs[i] = ROD;
//		}
//		
//		double[] AverageAndVarianceROD = calValue.calTotalityAverageAndVariance( RODs );
//		double ave = AverageAndVarianceROD[0];
//		double var = AverageAndVarianceROD[1];
//		
//		predictVO.setMinPrice90ZhiXin(
//				statistic.preMinPriceByRODOf90ZhiXin( ave, var, calValue.getNumOfSample(), closesA[vLen-1] ) );
//		predictVO.setMinPrice99ZhiXin(
//				statistic.preMinPriceByRODOf99ZhiXin( ave, var, calValue.getNumOfSample(), closesA[vLen-1] ) );
//		predictVO.setMaxPrice90ZhiXin(
//				statistic.preMaxPriceByRODOf90ZhiXin( ave, var, calValue.getNumOfSample(), closesA[vLen-1] ) );
//		predictVO.setMaxPrice99ZhiXin(
//				statistic.preMaxPriceByRODOf99ZhiXin( ave, var, calValue.getNumOfSample(), closesA[vLen-1] ) );
//		
//		double ZPrice = closesA[vLen-1];
//		QPrice = closesA[vLen-2];
//		// length of vector
//		int m = 5;
//		// 取前 k vectors
//		int k = 15;
//		double predictPrice = predict.KNNPredictPrice( closes, dates, m, k );
//		double predictROD = (predictPrice - ZPrice) / ZPrice;
//
//		if( predictROD>0.1 || predictROD<-0.1 ){
//			double predictPriceT = predict.SBPredictPrice( ZPrice, QPrice );
//			double predictRODT = (predictPriceT - ZPrice) / ZPrice;
//			if( (predictRODT<predictROD && predictROD<-0.1) ||
//				(predictRODT>predictROD && predictROD>0.1) );
//			else{
//				predictPrice = predictPriceT;
//				predictROD = predictRODT;
//			}
//		}
//		predictVO.setPredictPrice( predictPrice );
//		predictVO.setPredictROD( predictROD );
//
//		return predictVO;
//	}
}
