package vo;

import java.io.Serializable;

/**
 * Created by huihantao on 2017/3/27.
 */
public class SaleVO implements Serializable {
    private static final long serialVersionUID = -8022219757439228511L;

    private int tiaocangqi=10; // the days for another sale

    private String tiaocangjiage="收盘价";// the price type for sale

    private int num;// abandoned

    public int getTiaocangqi() {
        return tiaocangqi;
    }

    public void setTiaocangqi(int tiaocangqi) {
        this.tiaocangqi = tiaocangqi;
    }

    public String getTiaocangjiage() {
        return tiaocangjiage;
    }

    public void setTiaocangjiage(String tiaocangjiage) {
        this.tiaocangjiage = tiaocangjiage;
    }

    public SaleVO(int tiaocangqi, String tiaocangjiage) {
        this.tiaocangqi = tiaocangqi;
        this.tiaocangjiage = tiaocangjiage;
    }



    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public SaleVO(int tiaocangqi, String tiaocangjiage, int num) {
        this.tiaocangqi = tiaocangqi;
        this.tiaocangjiage = tiaocangjiage;
        this.num = num;
    }

    public SaleVO() {
    }
}
