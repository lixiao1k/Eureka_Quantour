package vo;

import java.io.Serializable;
import java.util.List;

public class ExponentChartVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -129078576494399448L;
	private List<EMAInfoVO> ema;
	private List<SingleStockInfoVO> list;
	public List<EMAInfoVO> getEma() {
		return ema;
	}
	public ExponentChartVO(List<EMAInfoVO> ema, List<SingleStockInfoVO> list) {
		super();
		this.ema = ema;
		this.list = list;
	}
	public void setEma(List<EMAInfoVO> ema) {
		this.ema = ema;
	}
	public List<SingleStockInfoVO> getList() {
		return list;
	}
	public void setList(List<SingleStockInfoVO> list) {
		this.list = list;
	}
}
