package logic.supportservice;

public interface BPNetSupportInterface {

	double[] EMV( double[] highPrice, double[] lowPrice, double[] volume, 
			double[][] QMaxNumDayData );
	
	/**
	 * 
	 * @Description: 5日指数平均指数
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param closePrice
	 * @param QMaxNumDayData
	 */
	double[] EMA5( double[] closePrice, double[][] QMaxNumDayData );
	
	/**
	 * 
	 * @Description: 60日指数平均指数
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param closePrice
	 * @param QMaxNumDayData
	 */
	double[] EMA60( double[] closePrice, double[][] QMaxNumDayData );
	
	/**
	 * 
	 * @Description: 5日平均指数
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param closePrice
	 * @param QMaxNumDayData
	 */
	double[] MA5( double[] closePrice, double[][] QMaxNumDayData );
	
	/**
	 * 
	 * @Description: 60日平均指数
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param closePrice
	 * @param QMaxNumDayData
	 */
	double[] MA60( double[] closePrice, double[][] QMaxNumDayData );
	
	/**
	 * 
	 * @Description: calculate 动量指标
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param closePrice
	 * @param QMaxNumDayData
	 */
	double[] MTM( double[] closePrice, double[][] QMaxNumDayData );
	
	/**
	 * 
	 * @Description: calculate 量能指标
	 * @author: hzp
	 * @date: 2017年6月10日
	 * @param volume
	 * @param QMaxNumDayData
	 */
	double[] MACD( double[] volume, double[][] QMaxNumDayData );
	
	/**
	 * 
	 * @Description: calculate 能量指标
	 * @author: hzp
	 * @date: 2017年6月10日
	 * @param closePrice
	 * @param highPrice
	 * @param lowPrice
	 * @param openPrice
	 * @param QMaxNumDayData
	 */
	double[] CR5( double[] closePrice, double[] highPrice, double[] lowPrice, 
			double[] openPrice, double[][] QMaxNumDayData );
	
	/**
	 * 
	 * @Description: 初始化以上参数
	 * @author: hzp
	 * @date: 2017年6月10日
	 * @param overPrice
	 * @param highPrice
	 * @param lowPrice
	 * @param openPrice
	 * @param vol
	 * @param QMaxNumDayData
	 */
	double[][] bpTrain( double[] overPrice, double[] highPrice, double[] lowPrice,
			double[] openPrice, double[] vol, double[][] QMaxNumDayData );
}
