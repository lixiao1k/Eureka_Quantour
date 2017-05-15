package po;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StrategyShowPO {
	private String createrName;
	private String strategyName;
    private List<LocalDate> TimeList;
    private List<Double> BasicReturn;
    private List<Double> StrategyReturn;
    private Double Alpha;
    private Double Beta;
    private Double Sharp;
    private Double zuidahuiche;
    private Double StrategyYearReturn;

    
    public StrategyShowPO(String createrName, String strategyName, 
    		Double alpha, Double beta, Double sharp, Double zuidahuiche,
			Double strategyYearReturn,int length) {
		super();
		this.createrName = createrName;
		this.strategyName = strategyName;
		List<Double> list1=new ArrayList<Double>();
    	List<Double> list2=new ArrayList<Double>();
    	List<LocalDate> list3=new ArrayList<LocalDate>();
    	LocalDate temp=LocalDate.now();
    	for(int i=0;i<length;i++){
    		list1.add(0.0);
    		list2.add(0.0);
    		list3.add(temp);
    	}
    	setTimeList(list3);
    	setBasicReturn(list2);
    	setStrategyReturn(list1);
		Alpha = alpha;
		Beta = beta;
		Sharp = sharp;
		this.zuidahuiche = zuidahuiche;
		StrategyYearReturn = strategyYearReturn;
	}
	public void add(double br,double sr,LocalDate time,int index){
    	TimeList.set(index, time);
        BasicReturn.set(index, br);
        StrategyReturn.set(index, sr);
    }
	/**
	 * @return the timeList
	 */
	public List<LocalDate> getTimeList() {
		return TimeList;
	}
	/**
	 * @param timeList the timeList to set
	 */
	public void setTimeList(List<LocalDate> timeList) {
		TimeList = timeList;
	}
	/**
	 * @return the basicReturn
	 */
	public List<Double> getBasicReturn() {
		return BasicReturn;
	}
	/**
	 * @param basicReturn the basicReturn to set
	 */
	public void setBasicReturn(List<Double> basicReturn) {
		BasicReturn = basicReturn;
	}
	/**
	 * @return the strategyReturn
	 */
	public List<Double> getStrategyReturn() {
		return StrategyReturn;
	}
	/**
	 * @param strategyReturn the strategyReturn to set
	 */
	public void setStrategyReturn(List<Double> strategyReturn) {
		StrategyReturn = strategyReturn;
	}
	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		return Alpha;
	}
	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(Double alpha) {
		Alpha = alpha;
	}
	/**
	 * @return the beta
	 */
	public Double getBeta() {
		return Beta;
	}
	/**
	 * @param beta the beta to set
	 */
	public void setBeta(Double beta) {
		Beta = beta;
	}
	/**
	 * @return the sharp
	 */
	public Double getSharp() {
		return Sharp;
	}
	/**
	 * @param sharp the sharp to set
	 */
	public void setSharp(Double sharp) {
		Sharp = sharp;
	}
	/**
	 * @return the zuidahuiche
	 */
	public Double getZuidahuiche() {
		return zuidahuiche;
	}
	/**
	 * @param zuidahuiche the zuidahuiche to set
	 */
	public void setZuidahuiche(Double zuidahuiche) {
		this.zuidahuiche = zuidahuiche;
	}
	/**
	 * @return the strategyYearReturn
	 */
	public Double getStrategyYearReturn() {
		return StrategyYearReturn;
	}
	/**
	 * @param strategyYearReturn the strategyYearReturn to set
	 */
	public void setStrategyYearReturn(Double strategyYearReturn) {
		StrategyYearReturn = strategyYearReturn;
	}

	/**
	 * @return the createrName
	 */
	public String getCreaterName() {
		return createrName;
	}

	/**
	 * @param createrName the createrName to set
	 */
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	/**
	 * @return the strategyName
	 */
	public String getStrategyName() {
		return strategyName;
	}

	/**
	 * @param strategyName the strategyName to set
	 */
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
}
