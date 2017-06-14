package vo;

import java.io.Serializable;

/**
 * 
 * @Description: save stock's prediction and min price，
 * 				 MinPrice90ZhiXin : 根据90%置信区间计算的价格下限，
 * 				 MinPrice99ZhiXin : 根据99%置信区间计算的价格下限，
 * 				 MaxPrice90ZhiXin : 根据90%置信区间计算的价格上限，
 * 				 MaxPrice90ZhiXin : 根据90%置信区间计算的价格上限，
 * @author: hzp 
 * @date: May 14, 2017
 */
public class PredictVO implements Serializable{

	private static final long serialVersionUID = -8674287798843908253L;

	private double predictPrice;
	
	private double predictROD;
	
	private double MinPrice90ZhiXin = 0;
	
	private double MinPrice99ZhiXin = 0;
	
	private double MaxPrice90ZhiXin = 0;
	
	private double MaxPrice99ZhiXin = 0;
	
	public PredictVO(){
		predictPrice = 0.0;
		predictROD = 0.0;
		MinPrice90ZhiXin = 0.0;
		MinPrice99ZhiXin = 0.0;
		MaxPrice90ZhiXin = 0.0;
		MaxPrice99ZhiXin = 0.0;
	}

	public double getPredictPrice() {
		return predictPrice;
	}

	public void setPredictPrice(double predictPrice) {
		this.predictPrice = predictPrice;
	}

	public double getPredictROD() {
		return predictROD;
	}

	public void setPredictROD(double predictROD) {
		this.predictROD = predictROD;
	}
	
	public double getMinPrice90ZhiXin() {
		return MinPrice90ZhiXin;
	}

	public void setMinPrice90ZhiXin(double firstMinPrice) {
		this.MinPrice90ZhiXin = firstMinPrice;
	}

	public double getMinPrice99ZhiXin() {
		return MinPrice99ZhiXin;
	}

	public void setMinPrice99ZhiXin(double MinPrice99ZhiXin) {
		this.MinPrice99ZhiXin = MinPrice99ZhiXin;
	}
	
	public double getMaxPrice90ZhiXin() {
		return MaxPrice90ZhiXin;
	}

	public void setMaxPrice90ZhiXin(double maxPrice90ZhiXin) {
		MaxPrice90ZhiXin = maxPrice90ZhiXin;
	}

	public double getMaxPrice99ZhiXin() {
		return MaxPrice99ZhiXin;
	}

	public void setMaxPrice99ZhiXin(double maxPrice99ZhiXin) {
		MaxPrice99ZhiXin = maxPrice99ZhiXin;
	}
	
}
