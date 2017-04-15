package vo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huihantao on 2017/3/27.
 */
public class YieldDistributionHistogramDataVO implements Serializable{

    private static final long serialVersionUID=5907793986868423023L;


    private Map<Double,List<Integer>> zuhe;

    public YieldDistributionHistogramDataVO(Map<Double, List<Integer>> zuhe) {
        this.zuhe = zuhe;
    }

    public Map<Double, List<Integer>> getZuhe() {
        return zuhe;
    }

    public void setZuhe(Map<Double, List<Integer>> zuhe) {
        this.zuhe = zuhe;
    }
}
