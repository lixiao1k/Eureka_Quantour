package vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 
 * @Description: save two stocks's information to compare
 * @author: hzp
 * @time: 2017.3.6
 */
public class ComparedInfoVO implements Serializable{
	
	private static final long serialVersionUID = -2038667932682090329L;

	private List<Double> logYieldA ; //dui shu shou yi lv lie biao


	private List<LocalDate> date ; // ri qi lie biao
	private List<Integer> diantu; // dian tu shu zhi

	public List<Double> getLogYieldA() {
		return logYieldA;
	}

	public void setLogYieldA(List<Double> logYieldA) {
		this.logYieldA = logYieldA;
	}

	public List<LocalDate> getDate() {
		return date;
	}

	public void setDate(List<LocalDate> date) {
		this.date = date;
	}

	public List<Integer> getDiantu() {
		return diantu;
	}

	public void setDiantu(List<Integer> diantu) {
		this.diantu = diantu;
	}

	public ComparedInfoVO(List<Double> logYieldA, List<LocalDate> date, List<Integer> diantu) {
		this.logYieldA = logYieldA;
		this.date = date;
		this.diantu = diantu;
	}
}
