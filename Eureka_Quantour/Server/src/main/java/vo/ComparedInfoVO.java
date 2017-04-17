package vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

/**
 * 
 * @Description: save two stocks's information to compare
 * @author: hzp
 * @time: 2017.3.6
 */
public class ComparedInfoVO implements Serializable{
	
	private static final long serialVersionUID = -2038667932682090329L;
	
	/**
	 * ROD: rise or drop percent
	 * logYieldA: 对数收益率
	 * logYieldVarianceA: 对数收益率方差
	 */
	private String nameA = "";
	private String codeA = "";
	private double lowA = 0.0;
	private double highA = 0.0;
	private double RODA = 0.0;
	private List<Double> closeA ;
	private List<Double> logYieldA ;
	private double logYieldVarianceA = 0.0;
	

	private List<LocalDate> date ;
	

	
	public String getNameA() {
		return nameA;
	}
	public void setNameA(String nameA) {
		this.nameA = nameA;
	}
	public String getCodeA() {
		return codeA;
	}
	public void setCodeA(String codeA) {
		this.codeA = codeA;
	}
	public double getLowA() {
		return lowA;
	}
	public void setLowA(double lowA) {
		this.lowA = lowA;
	}
	public double getHighA() {
		return highA;
	}
	public void setHighA(double highA) {
		this.highA = highA;
	}
	public double getRODA() {
		return RODA;
	}
	public void setRODA(double rODA) {
		RODA = rODA;
	}
	public List<Double> getCloseA() {
		return closeA;
	}
	public void setCloseA(List<Double> closeA) {
		this.closeA = closeA;
	}
	public List<Double> getLogYieldA() {
		return logYieldA;
	}
	public void setLogYieldA(List<Double> logYieldA) {
		this.logYieldA = logYieldA;
	}
	public double getLogYieldVarianceA() {
		return logYieldVarianceA;
	}
	public void setLogYieldVarianceA(double logYieldVarianceA) {
		this.logYieldVarianceA = logYieldVarianceA;
	}


	public List<LocalDate> getDate() {
		return date;
	}
	public void setDate(List<LocalDate> date) {
		this.date = date;
	}
	




	public ComparedInfoVO(String nameA, String codeA, double lowA, double highA, double RODA, List<Double> closeA, List<Double> logYieldA, double logYieldVarianceA, List<LocalDate> date) {
		this.nameA = nameA;
		this.codeA = codeA;
		this.lowA = lowA;
		this.highA = highA;
		this.RODA = RODA;
		this.closeA = closeA;
		this.logYieldA = logYieldA;
		this.logYieldVarianceA = logYieldVarianceA;
		this.date = date;
	}
}
