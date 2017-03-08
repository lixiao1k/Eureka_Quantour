package vo;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017.3.6
 */
public class ComparedInfoVO implements Serializable{
	/**
	 * ROD: rise or drop percent
	 * logYieldA: 对数收益率
	 * logYieldVarianceA: 对数收益率方差
	 */
	private String nameA = "";
	private String codeA = "";
	private Calendar beginA ;
	private Calendar endA ;
	private double lowA = 0;
	private double highA = 0;
	private double RODA = 0;
	private double closeA = 0;
	private double logYieldA = 0;
	private double logYieldVarianceA = 0;
	
	private String nameB = "";
	private String codeB = "";
	private Calendar beginB ;
	private Calendar endB ;
	private double lowB = 0;
	private double highB = 0;
	private double RODB = 0;
	private double closeB = 0;
	private double logYieldB = 0;
	private double logYieldVarianceB = 0;
	
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
	public Calendar getBeginA() {
		return beginA;
	}
	public void setBeginA(Calendar beginA) {
		this.beginA = beginA;
	}
	public Calendar getEndA() {
		return endA;
	}
	public void setEndA(Calendar endA) {
		this.endA = endA;
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
	public double getCloseA() {
		return closeA;
	}
	public void setCloseA(double closeA) {
		this.closeA = closeA;
	}
	public double getLogYieldA() {
		return logYieldA;
	}
	public void setLogYieldA(double logYieldA) {
		this.logYieldA = logYieldA;
	}
	public double getLogYieldVarianceA() {
		return logYieldVarianceA;
	}
	public void setLogYieldVarianceA(double logYieldVarianceA) {
		this.logYieldVarianceA = logYieldVarianceA;
	}
	public String getNameB() {
		return nameB;
	}
	public void setNameB(String nameB) {
		this.nameB = nameB;
	}
	public String getCodeB() {
		return codeB;
	}
	public void setCodeB(String codeB) {
		this.codeB = codeB;
	}
	public Calendar getBeginB() {
		return beginB;
	}
	public void setBeginB(Calendar beginB) {
		this.beginB = beginB;
	}
	public Calendar getEndB() {
		return endB;
	}
	public void setEndB(Calendar endB) {
		this.endB = endB;
	}
	public double getLowB() {
		return lowB;
	}
	public void setLowB(double lowB) {
		this.lowB = lowB;
	}
	public double getHighB() {
		return highB;
	}
	public void setHighB(double highB) {
		this.highB = highB;
	}
	public double getRODB() {
		return RODB;
	}
	public void setRODB(double rODB) {
		RODB = rODB;
	}
	public double getCloseB() {
		return closeB;
	}
	public void setCloseB(double closeB) {
		this.closeB = closeB;
	}
	public double getLogYieldB() {
		return logYieldB;
	}
	public void setLogYieldB(double logYieldB) {
		this.logYieldB = logYieldB;
	}
	public double getLogYieldVarianceB() {
		return logYieldVarianceB;
	}
	public void setLogYieldVarianceB(double logYieldVarianceB) {
		this.logYieldVarianceB = logYieldVarianceB;
	}
	
	public ComparedInfoVO initObject(String nameA, String codeA, Calendar beginA, Calendar endA, double lowA,
			double highA, double RODA, double closeA, double logYieldA, double logYieldVarianceA,
			String nameB, String codeB, Calendar beginB, Calendar endB, double lowB,
			double highB, double RODB, double closeB, double logYieldB, double logYieldVarianceB){
		ComparedInfoVO ci = new ComparedInfoVO();
		ci.setNameA(nameA); ci.setCodeA(codeA); ci.setBeginA(beginA); ci.setCloseA(closeA);
		ci.setEndA(endA); ci.setLowA(lowA); ci.setHighA(highA); ci.setRODA(RODA);
		ci.setCloseA(closeA); ci.setLogYieldA(logYieldA); ci.setLogYieldVarianceA(logYieldVarianceA);
		ci.setNameA(nameB); ci.setCodeA(codeB); ci.setBeginA(beginB); ci.setCloseA(closeB);
		ci.setEndA(endB); ci.setLowA(lowB); ci.setHighA(highB); ci.setRODA(RODB);
		ci.setCloseA(closeB); ci.setLogYieldA(logYieldB); ci.setLogYieldVarianceA(logYieldVarianceB);
		return ci;
	}
	
}
