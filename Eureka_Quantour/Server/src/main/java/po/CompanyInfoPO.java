package po;

import java.time.LocalDate;

public class CompanyInfoPO {
	private String code;
	private LocalDate date;//截止日期
	private double basicIncome;//基本每股收益
	private double netAsset;//每股净资产
	private long totalCapitalization;//总股本
	private long fluCapitalization;//流通股本
	public CompanyInfoPO(String code, LocalDate date, double basicIncome, double netAsset, long totalCapitalization,
			long fluCapitalization) {
		super();
		this.code = code;
		this.date = date;
		this.basicIncome = basicIncome;
		this.netAsset = netAsset;
		this.totalCapitalization = totalCapitalization;
		this.fluCapitalization = fluCapitalization;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public double getBasicIncome() {
		return basicIncome;
	}
	public void setBasicIncome(double basicIncome) {
		this.basicIncome = basicIncome;
	}
	public double getNetAsset() {
		return netAsset;
	}
	public void setNetAsset(double netAsset) {
		this.netAsset = netAsset;
	}
	public long getTotalCapitalization() {
		return totalCapitalization;
	}
	public void setTotalCapitalization(long totalCapitalization) {
		this.totalCapitalization = totalCapitalization;
	}
	public long getFluCapitalization() {
		return fluCapitalization;
	}
	public void setFluCapitalization(long fluCapitalization) {
		this.fluCapitalization = fluCapitalization;
	}
	
}
