package logic.supportservice;

/**
 * @Description: 
 * @author: hzp
 * @date: 2017年6月3日
 * @version: V1.0
 */
public interface StatisticInterface {

	/**
	 * @Description: judge if predict value is in the zhi xin qu jian calculated by history datas
	 * @author: hzp
	 * @date: May 17, 2017
	 * @param: @param num 	: chou yang ci shu
	 * @param: @param alpha : 误差允许大小
	 */
	boolean predictROE( double average, double variance, int num, double alpha, double preValue);
	
	
	/**
	 * @Description: 根据90%置信区间的 下限 来预测最低价格
	 * @author: hzp
	 * @date: 2017年6月13日
	 * @param average
	 * @param variance
	 * @param num
	 * @param todayPrice
	 */
	double preMinPriceByRODOf90ZhiXin( double average, double variance, int num, double todayPrice );
	
	
	/**
	 * @Description: 根据99%置信区间的 下限 来预测最低价格
	 * @author: hzp
	 * @date: 2017年6月13日
	 * @param average
	 * @param variance
	 * @param num
	 * @param todayPrice
	 */
	double preMinPriceByRODOf99ZhiXin( double average, double variance, int num, double todayPrice );
	
	
	/**
	 * @Description: 根据90%置信区间的 上限 来预测最低价格
	 * @author: hzp
	 * @date: 2017年6月13日
	 * @param average
	 * @param variance
	 * @param num
	 * @param todayPrice
	 */
	double preMaxPriceByRODOf90ZhiXin( double average, double variance, int num, double todayPrice );
	
	
	/**
	 * @Description: 根据99%置信区间的 上限 来预测最低价格
	 * @author: hzp
	 * @date: 2017年6月13日
	 * @param average
	 * @param variance
	 * @param num
	 * @param todayPrice
	 */
	double preMaxPriceByRODOf99ZhiXin( double average, double variance, int num, double todayPrice );
	
	
	/**
	 * 经过测试，结果和为 1 
	 * @Description: function : Wi = w0 + i * d; k Wi's values' sum = 1
	 * @author: hzp
	 * @date: May 24, 2017
	 * @param: @param k : number of factor's weight 
	 * @param: @param i : index of factor
	 */
	double getWeight( int k, int i );
}
