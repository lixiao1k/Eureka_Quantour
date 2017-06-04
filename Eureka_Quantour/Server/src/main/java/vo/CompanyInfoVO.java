package vo;

import java.io.Serializable;
import java.time.LocalDate;

public class CompanyInfoVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2907112713693781792L;
	private String code;
	private LocalDate date;//截止日期
	private double basicIncome;//基本每股收益
	private double netAsset;//每股净资产
	private long totalCapitalization;//总股本
	private long fluCapitalization;//流通股本
	public double getShiYing() {
		return shiYing;
	}
	public void setShiYing(double shiYing) {
		this.shiYing = shiYing;
	}
	public double getShiJing() {
		return shiJing;
	}
	public void setShiJing(double shiJing) {
		this.shiJing = shiJing;
	}
	public double getHuanShou() {
		return huanShou;
	}
	public void setHuanShou(double huanShou) {
		this.huanShou = huanShou;
	}
	private double shiYing;//市盈率
	@Override
	public String toString() {
		return "CompanyInfoVO [code=" + code + ", date=" + date + ", basicIncome=" + basicIncome + ", netAsset="
				+ netAsset + ", totalCapitalization=" + totalCapitalization + ", fluCapitalization=" + fluCapitalization
				+ ", shiYing=" + shiYing + ", shiJing=" + shiJing + ", huanShou=" + huanShou + "]";
	}
	private double shiJing;//市净率
	private double huanShou;//换手率
	public CompanyInfoVO(String code, LocalDate date, double basicIncome, double netAsset, long totalCapitalization,
			long fluCapitalization,double shiYing,double shiJing,double huanShou) {
		super();
		this.code = code;
		this.date = date;
		this.basicIncome = basicIncome;
		this.netAsset = netAsset;
		this.totalCapitalization = totalCapitalization;
		this.fluCapitalization = fluCapitalization;
		this.shiYing=shiYing;
		this.shiJing=shiJing;
		this.huanShou=huanShou;
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
