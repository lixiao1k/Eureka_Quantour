package vo;

import java.io.Serializable;
import java.util.Calendar;
/**
 * 
 * @Description: to save information used to draw JunXianTu
 * @author: hzp
 * @time: 2017年3月8日
 */
public class EMAInfoVO implements Serializable{

	private static final long serialVersionUID = -3607793986868403023L;
	
	private Calendar date;
	private double EMA;
	
	public EMAInfoVO(){
		this.date = Calendar.getInstance();
		this.EMA = 0.0;
	}
	
	public EMAInfoVO(Calendar date, double EMA){
		this.date = date;
		this.EMA = EMA;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public double getEMA() {
		return EMA;
	}

	public void setEMA(double EMA) {
		this.EMA = EMA;
	}
	
}
