package vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

/**
 * Created by huihantao on 2017/3/27.
 */
public class YieldChartDataVO implements Serializable{

    private static final long serialVersionUID=3607793986865623023L;
    private List<LocalDate> datelist;
    private List<Double> jizhunlist;
    private List<Double> celuelist;

    private double alpha;
    private double beta;
    private double sharpe;
    private double yearreturn;

    public YieldChartDataVO(List<LocalDate> datelist, List<Double> jizhunlist, List<Double> celuelist) {
        this.datelist = datelist;
        this.jizhunlist = jizhunlist;
        this.celuelist = celuelist;
    }
}
