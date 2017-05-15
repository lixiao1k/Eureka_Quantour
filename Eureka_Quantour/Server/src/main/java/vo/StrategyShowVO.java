package vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by huihantao on 2017/5/13.
 */
public class StrategyShowVO implements Serializable{

    //点开策略后显示的超级详细的信息
    //含评价，图表，计算结果参数，输入参数

    //计算结果
    private List<LocalDate> TimeList;
    private List<Double> BasicReturn;
    private List<Double> StrategyReturn;
    private double Alpha;
    private double Beta;
    private double Sharp;
    private double zuidahuiche;
    private double StrategyYearReturn;
    //评价
    private List<CommentVO> comments;
    //输入参数

    private StrategyConditionVO strategyConditionVO;
    private SaleVO saleVO;




    public StrategyShowVO() {
    }

    public List<LocalDate> getTimeList() {
        return TimeList;
    }

    public void setTimeList(List<LocalDate> timeList) {
        TimeList = timeList;
    }

    public List<Double> getBasicReturn() {
        return BasicReturn;
    }

    public void setBasicReturn(List<Double> basicReturn) {
        BasicReturn = basicReturn;
    }

    public List<Double> getStrategyReturn() {
        return StrategyReturn;
    }

    public void setStrategyReturn(List<Double> strategyReturn) {
        StrategyReturn = strategyReturn;
    }

    public double getAlpha() {
        return Alpha;
    }

    public void setAlpha(double alpha) {
        Alpha = alpha;
    }

    public double getBeta() {
        return Beta;
    }

    public void setBeta(double beta) {
        Beta = beta;
    }

    public double getSharp() {
        return Sharp;
    }

    public void setSharp(double sharp) {
        Sharp = sharp;
    }

    public double getZuidahuiche() {
        return zuidahuiche;
    }

    public void setZuidahuiche(double zuidahuiche) {
        this.zuidahuiche = zuidahuiche;
    }

    public double getStrategyYearReturn() {
        return StrategyYearReturn;
    }

    public void setStrategyYearReturn(double strategyYearReturn) {
        StrategyYearReturn = strategyYearReturn;
    }


}
