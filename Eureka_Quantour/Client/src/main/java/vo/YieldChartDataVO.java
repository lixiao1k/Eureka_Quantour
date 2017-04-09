package vo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by huihantao on 2017/3/27.
 */
public class YieldChartDataVO implements Serializable{

    public List<Calendar> getDatelist() {
		return datelist;
	}

	public void setDatelist(List<Calendar> datelist) {
		this.datelist = datelist;
	}

	public List<Double> getJizhunlist() {
		return jizhunlist;
	}

	public void setJizhunlist(List<Double> jizhunlist) {
		this.jizhunlist = jizhunlist;
	}

	public List<Double> getCeluelist() {
		return celuelist;
	}

	public void setCeluelist(List<Double> celuelist) {
		this.celuelist = celuelist;
	}

	private static final long serialVersionUID=3607793986865623023L;
    private List<Calendar> datelist;
    private List<Double> jizhunlist;
    private List<Double> celuelist;

    public YieldChartDataVO(List<Calendar> datelist, List<Double> jizhunlist, List<Double> celuelist) {
        this.datelist = datelist;
        this.jizhunlist = jizhunlist;
        this.celuelist = celuelist;
    }
}
