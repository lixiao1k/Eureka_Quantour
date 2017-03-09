package vo;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * @Description: save two stocks's information to compare
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
	private Calendar[] dateA ;
	private double lowA = 0.0;
	private double highA = 0.0;
	private double RODA = 0.0;
	private double[] closeA ;
	private double[] logYieldA ;
	private double logYieldVarianceA = 0.0;
	
	private String nameB = "";
	private String codeB = "";
	private Calendar[] dateB ;
	private double lowB = 0.0;
	private double highB = 0.0;
	private double RODB = 0.0;
	private double[] closeB ;
	private double[] logYieldB ;
	private double logYieldVarianceB = 0.0;
	
	public ComparedInfoVO(int n){
		dateA = new Calendar[n];
		dateB = new Calendar[n];
		closeA = new double[n];
		closeB = new double[n];
		logYieldA = new double[n];
		logYieldB = new double[n];
		for(int i=0;i<n;i++){
			closeA[i] = 0.0;
			closeB[i] = 0.0;
			logYieldA[i] = 0.0;
			logYieldB[i] = 0.0;
		}
	}
	
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
	public Calendar[] getDateA() {
		return dateA;
	}
	public void setDateA(Calendar[] dateA) {
		this.dateA = dateA;
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
	public double[] getCloseA() {
		return closeA;
	}
	public void setCloseA(double[] closeA) {
		this.closeA = closeA;
	}
	public double[] getLogYieldA() {
		return logYieldA;
	}
	public void setLogYieldA(double[] logYieldA) {
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
	public Calendar[] getDateB() {
		return dateB;
	}
	public void setDateB(Calendar[] dateB) {
		this.dateB = dateB;
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
	public double[] getCloseB() {
		return closeB;
	}
	public void setCloseB(double[] closeB) {
		this.closeB = closeB;
	}
	public double[] getLogYieldB() {
		return logYieldB;
	}
	public void setLogYieldB(double[] logYieldB) {
		this.logYieldB = logYieldB;
	}
	public double getLogYieldVarianceB() {
		return logYieldVarianceB;
	}
	public void setLogYieldVarianceB(double logYieldVarianceB) {
		this.logYieldVarianceB = logYieldVarianceB;
	}
	
	public ComparedInfoVO initObject(
			String nameA, String codeA, Calendar[] dateA, double lowA, double highA, 
			double RODA, double[] closeA, double[] logYieldA, double logYieldVarianceA,
			String nameB, String codeB, Calendar[] dateB, double lowB, double highB, 
			double RODB, double[] closeB, double[] logYieldB, double logYieldVarianceB, int n)
	{
		ComparedInfoVO ci = new ComparedInfoVO(n);
		ci.setNameA(nameA); ci.setCodeA(codeA); ci.setDateA(dateA); ci.setLowA(lowA); 
		ci.setHighA(highA); ci.setRODA(RODA); ci.setCloseA(closeA); 
		ci.setLogYieldA(logYieldA); ci.setLogYieldVarianceA(logYieldVarianceA);
		
		ci.setNameB(nameB); ci.setCodeB(codeB); ci.setDateB(dateB); ci.setLowB(lowB); 
		ci.setHighB(highB); ci.setRODB(RODB); ci.setCloseB(closeB); 
		ci.setLogYieldB(logYieldB); ci.setLogYieldVarianceB(logYieldVarianceB);
		return ci;
	}
	
}
