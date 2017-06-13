package logic.supportservice;

import java.time.LocalDate;

/**
 *
 * @author hzp
 * @Date   2017-6.3
 */
public interface CalculateValueInterface{
	
	/**
	 * @Description: get number of 随机抽样
	 */
	int getNumOfSample();

	/**
	 * @Description: calculate average of data in array    
	 */
	double calAverage( double[] data );

	/**
	 * @Description: calculate variance of data in array     
	 */
	double calVariance( double[] data );

	/**
	 * @Description: 根据样本均值和方差估计总体均值和方差
	 * @date: 2017年6月8日
	 */
	double[] calTotalityAverageAndVariance( double[] data );
	
	/**
	 * 
	 * @Description: calculate cos ( two vector's include angle )
	 * @author: hzp
	 * @date: May 23, 2017
	 */
	double calCosIncludeAngle( double[] vector1, double[] vector2 );

	/**
	 * 
	 * @Description: calculate two vectors' multiply-product
	 * @author: hzp
	 * @date: May 23, 2017
	 */
	double vectorMultiply( double[] vector1, double[] vector2 );

	/**
	 * 
	 * @Description: calculate vector's model
	 * @author: hzp
	 * @date: May 23, 2017
	 */
	double vectorModel( double[] vector );
	
	/**
	 * @Description: calculate len-day EMA
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param Len
	 */
	double calEMA( double[] closes, int len );

	/**
	 * 
	 * @Description: calculate 差离值
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param closesShort
	 * @param closesLong
	 */
	double calDIF( double[] closesShort, double[] closesLong );

	/**
	 * 
	 * @Description: calculate DEA
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param DIFs
	 * @param len
	 */
	double calDEA( double[] DIFs, int len );
	
	/**
	 * 
	 * @Description: 获得改日期后面一个工作日
	 * @author: hzp
	 */
	LocalDate getValidLatterDate( LocalDate date );
	
	/**
	 * 
	 * @Description: 获得改日期前面一个工作日
	 * @author: hzp
	 */
	LocalDate getValidBeforeDate( LocalDate date );
	
}