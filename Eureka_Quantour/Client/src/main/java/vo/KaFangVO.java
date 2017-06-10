package vo;

import java.io.Serializable;

public class KaFangVO implements Serializable{

	private static final long serialVersionUID = -4072788349127134725L;

	// 理想的卡方临界值
	private double idealValue;
	// 真实计算的卡方值
	private double realValue;
	// true表示符合， false表示不符合
	private boolean isNormalDistribution;
	
	public KaFangVO(){
		idealValue = 0;
		realValue = 0;
		isNormalDistribution = false;
	}
	
	public double getIdealValue() {
		return idealValue;
	}

	public void setIdealValue(double idealValue) {
		this.idealValue = idealValue;
	}

	public double getRealValue() {
		return realValue;
	}

	public void setRealValue(double realValue) {
		this.realValue = realValue;
	}

	public boolean isNormalDistribution() {
		return isNormalDistribution;
	}

	public void setNormalDistribution(boolean isNormalDistribution) {
		this.isNormalDistribution = isNormalDistribution;
	}
}
