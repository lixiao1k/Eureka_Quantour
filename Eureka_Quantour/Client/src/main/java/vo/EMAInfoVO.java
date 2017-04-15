package vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 
 * @Description: to save information used to draw JunXianTu
 * @author: hzp
 * @time: 2017年3月8日
 */
public class EMAInfoVO implements Serializable{

	private static final long serialVersionUID = -3607793986868403023L;

	private List<LocalDate> date;
	private List<Double> EMA;
	private int tianshu;

	public EMAInfoVO(List<LocalDate> date, List<Double> EMA, int tianshu) {
		this.date = date;
		this.EMA = EMA;
		this.tianshu = tianshu;
	}

	public void setDate(List<LocalDate> date) {
		this.date = date;
	}

	public void setEMA(List<Double> EMA) {
		this.EMA = EMA;
	}

	public int getTianshu() {
		return tianshu;
	}

	public void setTianshu(int tianshu) {
		this.tianshu = tianshu;
	}

	public List<LocalDate> getDate() {
		return date;
	}

	public List<Double> getEMA() {
		return EMA;
	}
}
