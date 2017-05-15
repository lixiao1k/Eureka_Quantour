package vo;

import java.io.Serializable;

/**
 * Created by huihantao on 2017/5/15.
 */
public class StrategyListVO implements Comparable<StrategyListVO>, Serializable{
    private static final long serialVersionUID = 2444861004516374102L;
    //查看策略列表时候所显示的简单信息
    private String createrName;
    private String strategyName;
    private double strategyYearReturn;
    private boolean publicorPrivate;

    public boolean isPublicorPrivate() {
        return publicorPrivate;
    }

    public void setPublicorPrivate(boolean publicorPrivate) {
        this.publicorPrivate = publicorPrivate;
    }

    public StrategyListVO(String createrName, String strategyName, double strategyYearReturn, boolean publicorPrivate) {
        this.createrName = createrName;
        this.strategyName = strategyName;
        this.strategyYearReturn = strategyYearReturn;
        this.publicorPrivate = publicorPrivate;
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
        return (int) (this.getStrategyYearReturn()*100-100*o.getStrategyYearReturn());
    }


}
