package logic.supportservice;

/**
 *
 * @author hzp
 * @Date   2017-6.3
 */
public interface CalculateValueInterface{

	/**
	 * @Description: calculate average of data in array    
	 */
	double calAvg( double[] data );

	/**
	 * @Description: calculate variance of data in array     
	 */
	double calVariance( double[] data );
	
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
	
}