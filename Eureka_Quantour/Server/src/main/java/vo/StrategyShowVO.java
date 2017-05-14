package vo;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by huihantao on 2017/5/13.
 */
public class StrategyShowVO implements Comparable<StrategyShowVO>{
    private List<LocalDate> TimeList;
    private List<Double> BasicReturn;
    private List<Double> StrategyReturn;
    private double Alpha;
    private double Beta;
    private double Sharp;
    private double zuidahuiche;
    private double StrategyYearReturn;


    public StrategyShowVO() {
    }

    public StrategyShowVO(List<LocalDate> timeList, List<Double> basicReturn, List<Double> strategyReturn, double alpha, double beta, double sharp, double zuidahuiche, double strategyYearReturn) {
        TimeList = timeList;
        BasicReturn = basicReturn;
        StrategyReturn = strategyReturn;
        Alpha = alpha;
        Beta = beta;
        Sharp = sharp;
        this.zuidahuiche = zuidahuiche;
        StrategyYearReturn = strategyYearReturn;
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

    @Override
    public int compareTo(StrategyShowVO o) {
        int a= (int) (100*StrategyYearReturn);
        int b= (int) (100*o.StrategyYearReturn);
        return a-b;
    }
}
