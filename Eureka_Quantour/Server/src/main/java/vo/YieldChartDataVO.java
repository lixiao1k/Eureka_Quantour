package vo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by huihantao on 2017/3/27.
 */
public class YieldChartDataVO implements Serializable{

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
