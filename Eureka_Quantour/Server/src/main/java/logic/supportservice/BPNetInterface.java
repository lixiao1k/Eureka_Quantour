package logic.supportservice;

public interface BPNetInterface {
	
	/**
	 * 
	 * @Description: calculate every point's value according to input
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param in
	 */
	double[] computeOut( double[] in );
	
	/**
	 * 
	 * @Description: correct weight according to every point's value and ideal value
	 * @author: hzp
	 * @date: 2017年6月8日
	 * @param tar 
	 */
	void updateWeight( double tar );
	
	/**
	 * 
	 * @Description: train
	 * @author: hzp
	 * @date: 2017年6月10日
	 * @param in
	 * @param tar 
	 */
	void train( double[] in, double tar );
}
