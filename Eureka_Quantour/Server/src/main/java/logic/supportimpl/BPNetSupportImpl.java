package logic.supportimpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import logic.supportservice.BPNetSupportInterface;
import logic.supportservice.CalculateValueInterface;

public class BPNetSupportImpl implements BPNetSupportInterface{
	
	private CalculateValueInterface calValue = new CalculateValueImpl();

	private DecimalFormat df = new DecimalFormat("#0.000"); 
	private final int maxNum = 61;
	
	private final int openIndex = 0;
	private final int highIndex = 1;
	private final int closeIndex = 2;
	private final int lowIndex = 3;
	private final int volumeIndex = 4;

    private final int numOfInput = 4;
	
	public int getMaxNum(){
		return maxNum;
	}
	
	public int getNumOfInput(){
		return numOfInput;
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

	
	@Override
	public double[] EMA5( double[] closePrice, double[][] QMaxNumDayData ) {
		int cha = 5; 
        int QLen = QMaxNumDayData.length;

        double[] EMA5 = new double[closePrice.length];
        double[] closes = new double[cha];
        for( int i=0; i<closePrice.length; i++ ){
        	if( i<cha-1 ){
                for( int k=0; k<cha-i; k++ )
                    closes[k] = QMaxNumDayData[QLen-1-k][closeIndex];
                int j = 0;
                for( int k=cha-i; k<cha; k++, j++ )
                    closes[k] = closePrice[j];
            }
            else
               for( int k=0; k<cha; k++ )
                    closes[k] = closePrice[i-k];
                
        	EMA5[i] = calValue.calEMA( closes, cha );
        }
        return EMA5;
	}

	
	@Override
	public double[] EMA60( double[] closePrice, double[][] QMaxNumDayData) {
		int cha = 60; 
        int QLen = QMaxNumDayData.length;

        double[] EMA60 = new double[closePrice.length];
        double[] closes = new double[cha];
        for( int i=0; i<closePrice.length; i++ ){
            if( i<cha-1 ){
                for( int k=0; k<cha-i; k++ )
                    closes[k] = QMaxNumDayData[QLen-1-k][closeIndex];
                int j = 0;
                for( int k=cha-i; k<cha; k++, j++ )
                    closes[k] = closePrice[j];
            }
            else
               for( int k=0; k<cha; k++ )
                    closes[k] = closePrice[i-k];
                
            EMA60[i] = calValue.calEMA( closes, cha );
        }
        return EMA60;
	}

	
	@Override
	public double[] MA5( double[] closePrice, double[][] QMaxNumDayData ) {
		int cha = 5; 
        int QLen = QMaxNumDayData.length;

        double[] MA5 = new double[closePrice.length];
        double sum = 0;
        for( int i=0; i<closePrice.length; i++ ){
            sum = 0;
            if( i<cha-1 ){
                for( int k=0; k<cha-i; k++ )
                    sum += QMaxNumDayData[QLen-1-k][closeIndex];
                int j = 0;
                for( int k=cha-i; k<cha; k++, j++ )
                    sum += closePrice[j];
            }
            else
               for( int k=0; k<cha; k++ )
                    sum += closePrice[i-k];
                
            MA5[i] = sum / cha;
        }
        return MA5;
	}

	
	@Override
	public double[] MA60( double[] closePrice, double[][] QMaxNumDayData) {
		int cha = 60; 
        int QLen = QMaxNumDayData.length;

        double[] MA60 = new double[closePrice.length];
        double sum = 0;
        for( int i=0; i<closePrice.length; i++ ){
            sum = 0;
            if( i<cha-1 ){
                for( int k=0; k<cha-i; k++ )
                    sum += QMaxNumDayData[QLen-1-k][closeIndex];
                int j = 0;
                for( int k=cha-i; k<cha; k++, j++ )
                    sum += closePrice[j];
            }
            else
               for( int k=0; k<cha; k++ )
                    sum += closePrice[i-k];
                
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
		double[] EMA5 = EMA5( closePrice, QMaxNumDayData );
		double[] EMA60 = EMA60( closePrice, QMaxNumDayData );
		double[] MTM = MTM( closePrice, QMaxNumDayData );
		double[] CR5 = CR5( closePrice, highPrice, lowPrice, openPrice, QMaxNumDayData );
		
		int length = EMA60.length;
        
        double[] closes = new double[length];
        for( int i=0; i<length; i++ ){
            if( i==0 )
                closes[i] = QMaxNumDayData[QMaxNumDayData.length-1][closeIndex];
            else
                closes[i] = closePrice[i-1];
        }
		
		List<ArrayList<Double>> datalist = new ArrayList<ArrayList<Double>>();
		for( int i=0; i<length; i++ ){
			ArrayList<Double> list = new ArrayList<Double>();
			list.add( EMA5[i] );
			list.add( EMA60[i] );
			list.add( MTM[i] );
			// list.add( CR5[i] );
            list.add( closes[i] ); 
			datalist.add( list );
		}
		double[][] data = new double[datalist.size()][numOfInput];
		for( int i=0; i<datalist.size(); i++ ){
			for( int j=0; j<numOfInput; j++ ){
				data[i][j] = datalist.get(i).get(j);
				// System.out.print( df.format(data[i][j])+"  " );
			}
			// System.out.println();
		}
		return data;
	}	
}
