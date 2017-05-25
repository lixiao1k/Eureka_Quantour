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

	private double predictPrice;
	
	private double predictROD;
	
	public PredictVO(){
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
