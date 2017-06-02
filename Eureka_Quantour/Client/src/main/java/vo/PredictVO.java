package vo;

import java.io.Serializable;

/**
 * 
 * @Description: TODO
 * @author: hzp 
 * @date: May 14, 2017
 */
public class PredictVO implements Serializable{

	private static final long serialVersionUID = -8674287798843908253L;

	// 预测的价格
	private double predictPrice;
	
	// 预测的价格所对应的涨跌幅
	private double predictROD;
	
	PredictVO(){
		predictPrice = 0.0;
		predictROD = 0.0;
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
	
	
}
