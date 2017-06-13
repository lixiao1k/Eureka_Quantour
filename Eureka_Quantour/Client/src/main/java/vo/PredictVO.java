package vo;

import java.io.Serializable;

/**
 * 
 * @Description: save stock's prediction and min price
 * 				 firstMinPrice : 根据90%置信区间计算的价格下限
 * 				 secondMinPrice : 根据99%置信区间计算的价格下限
 * @author: hzp 
 * @date: May 14, 2017
 */
public class PredictVO implements Serializable{

	private static final long serialVersionUID = -8674287798843908253L;

	private double predictPrice;
	
	private double predictROD;
	
	/**
	 * 根据90%置信区间计算的价格下限
	 */
	private double firstMinPrice = 0;
	
	/**
	 * 根据99%置信区间计算的价格下限
	 */
	private double secondMinPrice = 0;
	
	public PredictVO(){
		predictPrice = 0.0;
		predictROD = 0.0;
		firstMinPrice = 0.0;
		secondMinPrice = 0.0;
	}

	public double getPredictPrice() {
		return predictPrice;
	}

	public double getPredictROD() {
		return predictROD;
	}
	
	public double getFirstMinPrice() {
		return firstMinPrice;
	}

	public double getSecondMinPrice() {
		return secondMinPrice;
	}
	
}

