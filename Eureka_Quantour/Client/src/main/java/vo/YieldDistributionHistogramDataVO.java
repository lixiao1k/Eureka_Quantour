package vo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by huihantao on 2017/3/27.
 */
public class YieldDistributionHistogramDataVO implements Serializable{

    private static final long serialVersionUID=5907793986868423023L;


    private List<Double> yieldlist;
    private List<Integer> minuslist;
    private List<Integer> pluslist;

    public YieldDistributionHistogramDataVO(List<Double> yieldlist, List<Integer> minuslist, List<Integer> pluslist) {
        this.yieldlist = yieldlist;
        this.minuslist = minuslist;
        this.pluslist = pluslist;
    }
}
