package logic.supportimpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import logic.supportservice.BPNetSupportInterface;

public class BPNetSupportImpl implements BPNetSupportInterface{

	private DecimalFormat df = new DecimalFormat("#0.000"); 
	private final int maxNum = 61;
	
	private final int openIndex = 0;
	private final int highIndex = 1;
	private final int closeIndex = 2;
	private final int lowIndex = 3;
	private final int volumeIndex = 4;
	
	public int getMaxNUm(){
		return maxNum;
	}
	
	public int getOpenIndex(){
		return openIndex;
	}
	public int getHighIndex(){
		return highIndex;
	}
	public int getCloseIndex(){
		return closeIndex;
	}
	public int getLowIndex(){
		return lowIndex;
	}
	public int getVolumeIndex(){
		return volumeIndex;
	}
	
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
	public double[] EMV( double[] highPrice, double[] lowPrice, double[] volume, 
			double[][] QMaxNumDayData ) {
		
        int QLen = QMaxNumDayData.length;
		/* 
         * 取N为14，即14日的EM值之和；M为9，即9日的移动平均
         */
        int N = 14;
        int M = 9;
        int NAddM = N + M -2;
        
		double[] EM = new double[highPrice.length+NAddM];
        for( int i=0; i<EM.length; i++ ){
        	double JHigh = 0;
        	double JLow = 0;
        	double QHigh = 0;
        	double QLow = 0;
        	if( i<NAddM ){
        		JHigh = QMaxNumDayData[QLen+i-NAddM][highIndex];
        		JLow = QMaxNumDayData[QLen+i-NAddM][lowIndex];
        	}
        	else{
        		JHigh = highPrice[i-NAddM];
        		JLow = lowPrice[i-NAddM];
        	}
        	if( i-2<NAddM ){
        		QHigh = QMaxNumDayData[QLen+(i-2)-NAddM][highIndex];
        		QLow = QMaxNumDayData[QLen+(i-2)-NAddM][lowIndex];
        	}
        	else{
        		QHigh = highPrice[i-2-NAddM];
        		QLow = lowPrice[i-2-NAddM];
        	}
        	
            double A = ( JHigh + JLow ) / 2;
            double B = ( QHigh + QLow ) / 2;
            double C = JHigh - JLow;
            
            double temp = ( A - B ) * C;
            EM[i] = temp / volume[i];
        }

        
        double[] EMV = new double[highPrice.length+M-1];
        for( int i=EMV.length-1; i>-1; i-- ){
            // 14日累和
            double sum = 0;
            for( int j=0; j<N; j++ ){
                sum += EM[i+N-j-1];
            }
            EMV[i] = sum;
        }

        double[] MAEMV = new double[highPrice.length];
        for( int i=MAEMV.length-1; i>-1; i-- ){
            // 9日移动平均
            double sum = 0;
            for( int j=0; j<M; j++ ){
                sum += EMV[i+M-j-1];
            }
            sum = sum / M;
            MAEMV[i] = sum;
        }
        return MAEMV;
	}

	
	 /* 
     * EMA = (当日或当期close - 上一日或上期EXPMA) / Ｎ + 上一日或上期EXPMA
     * 首次上期EXPMA值为上一期收盘价，Ｎ为天数。 
     */ 
	@Override
	public double[] EMA5( double[] closePrice, double[][] QMaxNumDayData ) {
		int cha = 5; 
        int QLen = QMaxNumDayData.length;

        double[] EMA5 = new double[closePrice.length];
        for( int i=0; i<closePrice.length; i++ ){
        	double temp = 0;
        	if( i<cha ){
        		temp = ( closePrice[i] - QMaxNumDayData[QLen-1+i-cha][closeIndex] ) / cha;
        		EMA5[i] = temp + QMaxNumDayData[QLen-1+i-cha][closeIndex];
        	}
        	else{
        		temp = ( closePrice[i] - EMA5[i-cha] ) / cha;
        		EMA5[i] = temp + EMA5[i-cha];
        	}
        }
        return EMA5;
	}

	
	@Override
	public double[] EMA60( double[] closePrice, double[][] QMaxNumDayData) {
		int cha = 60;
        int QLen = QMaxNumDayData.length;

        double[] EMA60 = new double[closePrice.length];
        for( int i=0; i<closePrice.length; i++ ){
        	double temp = 0;
        	if( i<cha ){
        		temp = ( closePrice[i] - QMaxNumDayData[QLen-1+i-cha][closeIndex] ) / cha;
        		EMA60[i] = temp + QMaxNumDayData[QLen-1+i-cha][closeIndex];
        	}
        	else{
        		temp = ( closePrice[i] - EMA60[i-cha] ) / cha;
        		EMA60[i] = temp + EMA60[i-cha];
        	}
        }
        return EMA60;
	}

	
	@Override
	public double[] MA5( double[] closePrice, double[][] QMaxNumDayData ) {
		int cha = 5;
        int QLen = QMaxNumDayData.length;
		
		double[] MA5 = new double[closePrice.length];
		for( int i=0; i<closePrice.length; i++ ){
            double sum = 0;
            if( i<cha ){
                for( int j=0; j<cha-i; j++ )
                    sum += QMaxNumDayData[QLen-1-j][closeIndex];
                for( int j=0; j<i; j++ )
                    sum += closePrice[j];
            }
            else
                for( int j=0; j<cha; j++ )
                    sum += closePrice[i+j-cha];
			MA5[i] = sum / cha;
		}
		
        return MA5;
	}

	
	@Override
	public double[] MA60( double[] closePrice, double[][] QMaxNumDayData) {
		int cha = 60;
        int QLen = QMaxNumDayData.length;
		
		double[] MA60 = new double[closePrice.length];
		for( int i=0; i<closePrice.length; i++ ){
            double sum = 0;
            if( i<cha ){
                for( int j=0; j<cha-i; j++ )
                    sum += QMaxNumDayData[QLen-1-j][closeIndex];
                for( int j=0; j<i; j++ )
                    sum += closePrice[j];
            }
            else
                for( int j=0; j<cha; j++ )
                    sum += closePrice[i+j-cha];
			MA60[i] = sum / cha;
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
	public double[] MTM( double[] closePrice, double[][] QMaxNumDayData ) {
		int N = 12;
        int M = 6;
        int QLen = QMaxNumDayData.length;
		
		double[] MTM = new double[closePrice.length+M-1];
        for( int i=0; i<MTM.length; i++ ){
        	if( i<(M-1) )
                MTM[i] = QMaxNumDayData[QLen-(M-1)+i][closeIndex] - QMaxNumDayData[QLen-(M-1)+i-N][closeIndex];
            else if( i<N+(M-1) )
                MTM[i] = closePrice[i-(M-1)] - QMaxNumDayData[QLen-(M-1)+i-N][closeIndex];
            else
                MTM[i] = closePrice[i-(M-1)] - closePrice[i-(M-1)-N];
        }
        
        double[] MTMlist = new double[closePrice.length];
        //移动平均参数为6
        for( int i=0; i<MTMlist.length; i++ ){
            double sum = 0;
            for( int j=0; j<M; j++ ){
                sum += MTM[i+j];
            }
            sum = sum / M;
            MTMlist[i] = sum;
        }
        return MTMlist;
	}

	
	// 量能指标就是通过动态分析成交量的变化
	@Override
	public double[] MACD( double[] volume, double[][] QMaxNumDayData ) {
        int QLen = QMaxNumDayData.length;
        int length = volume.length+1;
		
		int shortN = 12;
        double[] Short = new double[length];
        Short[0] = 2 * QMaxNumDayData[QLen-1][volumeIndex] + (shortN-1) * QMaxNumDayData[QLen-1-shortN][volumeIndex];
        for( int i=1; i<length; i++ ){
            if( i<shortN+1 )
                Short[i] = 2 * volume[i] + (shortN-1) * QMaxNumDayData[QLen-shortN+i-1][volumeIndex];
            else
                Short[i] = 2 * volume[i] + (shortN-1) * volume[i-shortN];
        }

        int longN = 26;
        double[] Long = new double[length];
        Long[0] = 2 * QMaxNumDayData[QLen-1][volumeIndex] + (longN-1) * QMaxNumDayData[QLen-1-longN][volumeIndex];
        for( int i=0; i<length; i++ ){
            if( i<longN+1 )
                Long[i] = 2 * volume[i] + (longN-1) * QMaxNumDayData[QLen-longN+i-1][volumeIndex];
            else
                Long[i] = 2 * volume[i] + (longN-1) * volume[i-longN];
        }

        double[] DIFF = new double[length];
        for( int i=0; i<length; i++ )
            DIFF[i] = Short[i] - Long[i];

        double[] DEA = new double[length-1];
        for( int i=0; i<length-1; i++ )
            DEA[i] = 2*DIFF[i+1] + (9-1)*DIFF[i];
        
        double[] MACD = new double[length-1];
        for( int i=1; i<length-1; i++ )
            MACD[i] = DIFF[i]-DEA[i-1];

        return MACD;
	}

	
	// 能量指标：CR
	@Override
	public double[] CR5( double[] closePrice, double[] highPrice, double[] lowPrice,
			double[] openPrice, double[][] QMaxNumDayData ) {
        int QLen = QMaxNumDayData.length;
        int length = closePrice.length;
		
        int addNum = 5;
		double[] YM = new double[length+addNum];
        for( int i=0; i<length; i++ ){
            if( i<addNum )
                YM[i] = ( QMaxNumDayData[QLen-addNum+i][highIndex] + QMaxNumDayData[QLen-addNum+i][lowIndex]
                    + QMaxNumDayData[QLen-addNum+i][openIndex] + QMaxNumDayData[QLen-addNum+i][closeIndex] ) / 4;
            else
                YM[i] = ( highPrice[i-addNum] + lowPrice[i-addNum]
                 + openPrice[i-addNum] + closePrice[i-addNum] ) / 4;
        }

        // p1表示5日以来多方力量总和
        double[] HYM = new double[length];
        for( int i=0; i<length; i++ ){
            double sum = 0;
            for( int j=0; j<5; j++ ){
            	if( i+j<5 )
            		sum += QMaxNumDayData[QLen-5+i+j][highIndex] - YM[i+j];
            	else
                    sum += highPrice[i+j-5] - YM[i+j];
            }
            HYM[i] = sum;
        }
        
        // p2表示5日以来空方力量总和
        double[] YML = new double[length];
        for( int i=0; i<length; i++ ){
            double sum = 0;
            for( int j=0; j<5; j++ ){
                if( i+j<5 )
                    sum += YM[i+j] - QMaxNumDayData[QLen-5+i+j][lowIndex];
                else
                    sum += YM[i+j] - lowPrice[i+j-5];
            }
            YML[i] = sum;
        }

        double[] CR = new double[length];
        for( int i=0; i<length; i++ ){
            double temp = -1.0;
            if( YML[i]!=0 )
                temp = HYM[i] / YML[i];
            
            if( temp<0 )
                CR[i] = 0.0;
            else
                CR[i] = temp;

        }
        return CR;
	}

	
	@Override
	public double[][] bpTrain( double[] closePrice, double[] highPrice, double[] lowPrice,
			double[] openPrice, double[] vol, double[][] QMaxNumDayData ){
//		List<Double> EMV = EMV(highPrice, lowPrice, vol);
		double[] EMA5 = EMA5( closePrice, QMaxNumDayData );
		double[] EMA60 = EMA60( closePrice, QMaxNumDayData );
		double[] MA5 = MA5( closePrice, QMaxNumDayData );
		double[] MA60 = MA60( closePrice, QMaxNumDayData );
		double[] MTM = MTM( closePrice, QMaxNumDayData );
//		List<Double> MACD = MACD(vol);
		double[] CR5 = CR5( closePrice, highPrice, lowPrice, openPrice, QMaxNumDayData );
		
		int length = EMA60.length;
		
		List<ArrayList<Double>> datalist = new ArrayList<ArrayList<Double>>();
		for( int i=0; i<length; i++ ){
			ArrayList<Double> list = new ArrayList<Double>();
//			list.add( EMV[i] );
			list.add( EMA5[i] );
			list.add( EMA60[i] );
			list.add( MA5[i] );
			list.add( MA60[i] );
			list.add( MTM[i] );
//			list.add( MACD[i] );
			list.add( CR5[i] );
			datalist.add( list );
		}
		double[][] data = new double[datalist.size()][6];
		for( int i=0; i<datalist.size(); i++ ){
			for( int j=0; j<6; j++ ){
				data[i][j] = datalist.get(i).get(j);
				// System.out.print( df.format(data[i][j])+"  " );
			}
			// System.out.println();
		}
		return data;
	}	
}
