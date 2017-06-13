package logic.supportimpl;

import java.time.LocalDate;

import logic.supportservice.CalculateValueInterface;

public class CalculateValueImpl implements CalculateValueInterface{

	private final int numOfSample = 30;
	private final int lenOfSample = 10;
	
	@Override
	public int getNumOfSample(){
		return numOfSample;
	}

	@Override
	public double calAverage(double[] data) {
		// TODO Auto-generated method stub
		double sum = 0.0;
		for( int i=0; i<data.length; i++ )
			sum += data[i];
		return sum / data.length;
	}

	@Override
	public double calVariance(double[] data) {
		// TODO Auto-generated method stub
		double avg = calAverage( data );
		double avgSquare = Math.pow( avg, 2 );

		double[] data2 = new double[data.length];
		for( int i=0; i<data.length; i++ )
			data2[i] = Math.pow( data[i], 2 );
		double squareAvg = calAverage( data2 );

		return (squareAvg - avgSquare);
	}

	@Override
	public double[] calTotalityAverageAndVariance( double[] data ){
		double[] AverageAndVariance = new double[2];

		double[] sampleAverage = new double[numOfSample];
		for( int i=0; i<numOfSample; i++ ){
			double[] closesSave = new double[lenOfSample];
			for( int j=0; j<lenOfSample; j++ ){
				int index = (int)( Math.random()*(data.length-1) );
				closesSave[j] = data[index];
			}
			sampleAverage[i] = calAverage( closesSave );
		}

		AverageAndVariance[0] = calAverage( sampleAverage );
		AverageAndVariance[1] = calVariance( sampleAverage ) * numOfSample;
		return AverageAndVariance;
	}

	@Override
	public double calCosIncludeAngle(double[] vector1, double[] vector2) {
		// TODO Auto-generated method stub
		if( vector1.length!=vector2.length )
			return 0;
		double vectorMul = vectorMultiply( vector1, vector2 );
		double vectorModelMul = vectorModel( vector1 )*vectorModel( vector2 );

		return vectorMul / vectorModelMul;
	}

	@Override
	public double vectorMultiply(double[] vector1, double[] vector2) {
		// TODO Auto-generated method stub
		if( vector1.length!=vector1.length )
			return 0;

		double sum = 0;
		for( int i=0; i<vector1.length; i++ )
			sum += vector1[i]*vector2[i];
		return sum;
	}

	@Override
	public double vectorModel(double[] vector) {
		// TODO Auto-generated method stub
		double sum = 0;
		for( int i=0; i<vector.length; i++ )
			sum += Math.pow( vector[i], 2 );
		sum = Math.sqrt( sum );
		return sum;
	}

	@Override
	public double calEMA(double[] closes, int len) {
		// TODO Auto-generated method stub
		if( len!= closes.length )
			return 0;
		double a = 2 / ( len + 1 );
		// 分子
		double numerator = 0;
		// 分母
		double denominator = 0;
		for( int i=0; i<len; i++ ){
			numerator += closes[len-1-i] * Math.pow( 1-a, i );
			denominator = Math.pow( 1-a, i );
		}
		return numerator / denominator;
	}

	@Override
	public double calDIF( double[] closesShort, double[] closesLong ){
		double DIFShort = calEMA( closesShort, closesShort.length );
		double DIFLong = calEMA( closesLong, closesLong.length );
		if( closesShort.length < closesLong.length )	
			return DIFShort - DIFLong;
		else
			return DIFLong - DIFShort;
	}

	@Override
	public double calDEA( double[] DIFs, int len ){
		if( DIFs.length!=len )
			return 0.0;
		return calEMA( DIFs, len );
	}

	@Override
	public LocalDate getValidLatterDate(LocalDate date) {
		// TODO Auto-generated method stub
		date = date.plusDays(1);
		while( getDayOfWeek(date)==-1 )
			date = date.plusDays(1);
		return date;
	}

	@Override
	public LocalDate getValidBeforeDate(LocalDate date) {
		// TODO Auto-generated method stub
		date = date.minusDays(1);
		while( getDayOfWeek(date)==-1 )
			date = date.minusDays(1);
		return date;
	}
	
	/**
	 * @Description: 根据日期返回 星期 的代号，如果是-1表示不是交易日
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param date
	 */
	private int getDayOfWeek( LocalDate date){
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
    
}
