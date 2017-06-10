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

    // the date list of the strategy from the begein date to the end date
    // the basic return list 
    // the strategy return list
    private List<LocalDate> datelist;
    private List<Double> jizhunlist;
    private List<Double> celuelist;


// some parameters
    private double alpha;
    private double beta;
    private double sharpe;
    private double jizhunyearreturn;
    private double yearreturn;
    private double zuidahuiche;



    public YieldChartDataVO(List<LocalDate> datelist, List<Double> jizhunlist, List<Double> celuelist, double alpha, double beta, double sharpe, double yearreturn,double jizhunyearreturn) {
        this.datelist = datelist;
        this.jizhunlist = jizhunlist;
        this.celuelist = celuelist;
        this.alpha = alpha;
        this.beta = beta;
        this.sharpe = sharpe;
        this.yearreturn = yearreturn;
        this.jizhunyearreturn=jizhunyearreturn;
    }

    public List<LocalDate> getDatelist() {
        return datelist;
    }

    public void setDatelist(List<LocalDate> datelist) {
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

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getSharpe() {
        return sharpe;
    }

    public void setSharpe(double sharpe) {
        this.sharpe = sharpe;
    }

    public double getYearreturn() {
        return yearreturn;
    }

    public void setYearreturn(double yearreturn) {
        this.yearreturn = yearreturn;
    }

    public double getJizhunyearreturn() {
        return jizhunyearreturn;
    }

    public void setJizhunyearreturn(double jizhunyearreturn) {
        this.jizhunyearreturn = jizhunyearreturn;
    }

    public double getZuidahuiche() {
        return zuidahuiche;
    }

    public void setZuidahuiche(double zuidahuiche) {
        this.zuidahuiche = zuidahuiche;
    }
}
