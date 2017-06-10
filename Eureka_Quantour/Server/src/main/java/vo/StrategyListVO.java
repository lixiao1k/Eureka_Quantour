package vo;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by huihantao on 2017/5/15.
 */
public class StrategyListVO implements Comparable<StrategyListVO>, Serializable{
    private static final long serialVersionUID = 2444861004516374102L;
    //查看策略列表时候所显示的简单信息

    private String createrName;// the name of the creator
    private String strategyName; //the name of the strategy
    private double strategyYearReturn; // the year return of the strategy



    public StrategyListVO(String createrName, String strategyName, double strategyYearReturn) {
        this.createrName = createrName;
        this.strategyName = strategyName;
        this.strategyYearReturn = strategyYearReturn;
        double d = 0.200;
        DecimalFormat df = new DecimalFormat("0.00");
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public double getStrategyYearReturn() {
        return strategyYearReturn;
    }

    public void setStrategyYearReturn(double strategyYearReturn) {
        this.strategyYearReturn = strategyYearReturn;
    }

    @Override
    public int compareTo(StrategyListVO o) {
        return (int) -(this.getStrategyYearReturn()*100-100*o.getStrategyYearReturn());
    }


}
