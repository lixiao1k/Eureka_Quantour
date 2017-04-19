package vo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @Description: gather the data of market in one day
 * @author: hzp
 * @time: 2017年3月6日
 */
public class MarketInfoVO implements Serializable{
	
	private static final long serialVersionUID = -8022819757439228511L;


	private long volume = 0;
	private int zhangting=0;
	private int dieting=0;
	private int tingpai;
	private List<Integer> shanxingtu;
	private List<Integer> diantu;

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public int getZhangting() {
		return zhangting;
	}

	public void setZhangting(int zhangting) {
		this.zhangting = zhangting;
	}

	public int getDieting() {
		return dieting;
	}

	public void setDieting(int dieting) {
		this.dieting = dieting;
	}

	public int getTingpai() {
		return tingpai;
	}

	public void setTingpai(int tingpai) {
		this.tingpai = tingpai;
	}

	public List<Integer> getShanxingtu() {
		return shanxingtu;
	}

	public void setShanxingtu(List<Integer> shanxingtu) {
		this.shanxingtu = shanxingtu;
	}

	public List<Integer> getDiantu() {
		return diantu;
	}

	public void setDiantu(List<Integer> diantu) {
		this.diantu = diantu;
	}

	public MarketInfoVO(long volume, int zhangting, int dieting, int tingpai, List<Integer> shanxingtu, List<Integer> diantu) {
		this.volume = volume;
		this.zhangting = zhangting;
		this.dieting = dieting;
		this.tingpai = tingpai;
		this.shanxingtu = shanxingtu;
		this.diantu = diantu;
	}
}
