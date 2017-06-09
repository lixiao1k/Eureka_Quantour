package logic.supportimpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import logic.supportservice.BPNetSupportInterface;

public class BPNetSupportImpl implements BPNetSupportInterface{

	private DecimalFormat df = new DecimalFormat("#0.000"); 
	
	/* 
     * A = ( JHigh + JLow ) / 2;
     * B = ( QHigh + QLow ) / 2;
     * C = ( JHigh - JLow );
     * EM = ( A - B ) * C / JVolume;
     * EMV = N日内EM的累和;
     * MAEMV = EMV的M日简单移动平均.
     * 
     * N=14 , M=9
     */
	@Override
	public List<Double> EMV( List<Double> highPrice, List<Double> lowPrice, List<Double> vol ) {
		List<Double> EM = new ArrayList<Double>();
        for( int i=2; i<highPrice.size(); i++ ){
        	double JHigh = highPrice.get(i);
        	double JLow = lowPrice.get(i);
        	double QHigh = highPrice.get(i-2);
        	double QLow = lowPrice.get(i-2);
        	
            double A = ( JHigh + JLow ) / 2;
            double B = ( QHigh + QLow ) / 2;
            double C = JHigh - JLow;
            
            double temp = ( A - B ) * C;
            EM.add( temp / vol.get(i) );
        }

        /* 
         * 取N为14，即14日的EM值之和；M为9，即9日的移动平均
         */
        int N = 14;
        int M = 9;

        List<Double> EMV = new ArrayList<Double>();
        for( int i=N; i<EM.size()+1; i++ ){
            // 14日累和
            double sum = 0;
            for( int j=i-N; j<i; j++ ){
                sum += EM.get(j);
            }
            EMV.add(sum);
        }

        List<Double> MAEMV = new ArrayList<Double>();
        for( int i=M; i<EMV.size()+1; i++){
            // 9日移动平均
            double sum = 0;
            for( int j=i-M; j<i; j++ ){
                sum += EMV.get(j);
            }
            sum = sum / M;
            MAEMV.add(sum);
        }
        return MAEMV;
	}

	
	 /* 
     * EMA = (当日或当期close - 上一日或上期EXPMA) / Ｎ + 上一日或上期EXPMA
     * 首次上期EXPMA值为上一期收盘价，Ｎ为天数。 
     */ 
	@Override
	public List<Double> EMA5(List<Double> overPrice) {

        List<Double> EMA5 = new ArrayList<Double>();
        for( int i=0; i<5; i++){
            EMA5.add(overPrice.get(i));
        }
        for( int i=5; i<overPrice.size(); i++ ){
            double temp = ( overPrice.get(i) - EMA5.get(i-5) ) / 5;
            EMA5.add( temp + EMA5.get(i-5) );
        }
        return EMA5;
	}

	
	@Override
	public List<Double> EMA60(List<Double> overPrice) {

        List<Double> EMA60 = new ArrayList<Double>();
        for( int i=0; i<60; i++ ){
            EMA60.add(overPrice.get(i));
        }
        for( int i=60; i<overPrice.size(); i++ ){
            double temp = ( overPrice.get(i) - EMA60.get(i-60) ) /60;
            EMA60.add( temp + EMA60.get(i-60) );
        }
        return EMA60;
	}

	
	@Override
	public List<Double> MA5(List<Double> overPrice) {
		List<Double> MA5 = new ArrayList<Double>();
        for( int i=5; i<overPrice.size()+1; i++ ){
            double sum = 0;
            for( int j=i-1; j>=i-5; j-- ){
                sum += overPrice.get(j);
            }
            sum = sum / 5;
            MA5.add(sum);
        }
        return MA5;
	}

	
	@Override
	public List<Double> MA60(List<Double> overPrice) {
		List<Double> MA60 = new ArrayList<Double>();
        for( int i=60; i<overPrice.size()+1; i++ ){
            double sum = 0;
            for( int j=i-1; j>=i-60; j-- ){
                sum += overPrice.get(j);
            }
            sum = sum / 60;
            MA60.add(sum);
        }
        return MA60;
	}

	
	/* 
     * 动量指标MTM
     * 1. MTM = JClose - N日前close
     * 2. MTMMA = MTM的M日移动平均
     * 3. 参数N一般设置为12日
     *    参数M一般设置为6
     *    表中当动量值减低或反转增加时，应为买进或卖出时机
     */
	@Override
	public List<Double> MTM(List<Double> overPrice) {
		
		List<Double> MTM = new ArrayList<Double>();
        List<Double> MTMlist = new ArrayList<Double>();
        int N = 12;
        int M = 6;
        for( int i=N; i<overPrice.size(); i++ ){
            MTM.add( overPrice.get(i) - overPrice.get(i-N) );
        }
        
        //移动平均参数为6
        for( int i=M; i<MTM.size()+1; i++ ){
            double sum = 0;
            for( int j=i-1; j>=i-M; j-- ){
                sum += MTM.get(j);
            }
            sum = sum / M;
            MTMlist.add(sum);
        }
        return MTMlist;
	}

	
	// 量能指标就是通过动态分析成交量的变化
	@Override
	public List<Double> MACD(List<Double> vol) {
		
		int shortN = 12;
        List<Double> Short = new ArrayList<Double>();
        for( int i=shortN; i<vol.size()+1; i++ ){
            Short.add( 2*vol.get(i-1) + (shortN-1)*vol.get(i-shortN) );
        }
        int longN = 26;
        List<Double> Long = new ArrayList<Double>();
        for( int i=longN; i<vol.size()+1; i++ ){
            Long.add( 2*vol.get(i-1) + (longN-1)*vol.get(i-longN) );
        }
        
        // 取两个序列中较短序列的长度
        int length = 0;
        if( Short.size()>Long.size() ){
            length = Long.size();
        }
        else{
            length = Short.size();
        }
        
        List<Double> DIFF1 = new ArrayList<Double>();
        for( int i=length-1; i>=0; i-- ){
            DIFF1.add( Short.get(i) - Long.get(i) );
        }

        List<Double> DIFF = new ArrayList<Double>();
        for( int i=0; i<DIFF1.size(); i++ ){
            DIFF.add( DIFF1.get( DIFF1.size()-i-1 ) );
        }

        List<Double> DEA = new ArrayList<Double>();
        for( int i=0; i<DIFF.size()-1; i++ ){
            DEA.add( 2*DIFF.get(i+1) + (9-1)*DIFF.get(i) );
        }
        
        List<Double> MACD = new ArrayList<Double>();
        for( int i=1; i<DIFF.size(); i++ ){
            MACD.add( DIFF.get(i)-DEA.get(i-1) );
        }
        return MACD;
	}

	
	// 能量指标：CR
	@Override
	public List<Double> CR5(List<Double> overPrice, List<Double> highPrice, List<Double> lowPrice,
			List<Double> openPrice) {
		
		List<Double> YM = new ArrayList<Double>();
        for( int i=0; i<overPrice.size(); i++ ){
            YM.add( (highPrice.get(i) + overPrice.get(i) + lowPrice.get(i) + openPrice.get(i)) / 4 );
        }
        
        // p1表示5日以来多方力量总和
        List<Double> HYM = new ArrayList<Double>();
        for( int i=6; i<highPrice.size()+1; i++ ){
            double sum = 0;
            for( int j=i-1; j>=i-5; j-- ){
                sum += highPrice.get(j) - YM.get(j-1);
            }
            HYM.add(sum);
        }
        
        // p2表示5日以来空方力量总和
        List<Double> YML = new ArrayList<Double>();
        for( int i=6; i<lowPrice.size()+1; i++ ){
            double sum = 0;
            for( int j=i-1; j>=i-5; j-- ){
                sum += YM.get(j-1) - lowPrice.get(j);
            }
            YML.add(sum);
        }

        List<Double> CR = new ArrayList<Double>();
        for( int i=0; i<YML.size(); i++ ){
            double temp = -1;
            if( YML.get(i)!=0 )
                temp = HYM.get(i) / YML.get(i);
            
            if( temp<0 )
                CR.add( 0.0 );
            else
                CR.add( temp );
        }
        return CR;
	}

	
	@Override
	public double[][] bpTrain( List<Double> closePrice, List<Double> highPrice, List<Double> lowPrice,
			List<Double> openPrice, List<Double> vol ){
//		List<Double> EMV = EMV(highPrice, lowPrice, vol);
		List<Double> EMA5 = EMA5( closePrice );
		List<Double> EMA60 = EMA60( closePrice );
		List<Double> MA5 = MA5( closePrice );
		List<Double> MA60 = MA60( closePrice );
		List<Double> MTM = MTM( closePrice );
//		List<Double> MACD = MACD(vol);
		List<Double> CR5 = CR5( closePrice, highPrice, lowPrice, openPrice );
		
		int length = 0;
		if( EMA60.size() > MA60.size() )
			length = MA60.size();
	    else
	    	length = EMA60.size();
		
		List<ArrayList<Double>> datalist = new ArrayList<ArrayList<Double>>();
		for( int i=0; i<length; i++ ){
			ArrayList<Double> list = new ArrayList<Double>();
//			list.add(EMV.get(EMV.size()-length+i));
			list.add( EMA5.get(EMA5.size()-length+i) );
			list.add( EMA60.get(EMA60.size()-length+i) );
			list.add( MA5.get(MA5.size()-length+i) );
			list.add( MA60.get(MA60.size()-length+i) );
			list.add( MTM.get(MTM.size()-length+i) );
//			list.add(MACD.get(MACD.size()-length+i));
			list.add( CR5.get(CR5.size()-length+i) );
			datalist.add( list );
		}
		double[][] data = new double[datalist.size()][6];
//		for( int i=0; i<datalist.size(); i++ ){
//			for( int j=0; j<6; j++ ){
//				data[i][j] = datalist.get(i).get(j);
//				System.out.print( df.format(data[i][j])+"  " );
//			}
//			System.out.println();
//		}
		return data;
	}	
}
