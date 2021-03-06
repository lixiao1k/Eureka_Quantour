package vo;

import java.io.Serializable;

/**
 * Created by huihantao on 2017/3/27.
 */
public class SaleVO implements Serializable {
    private static final long serialVersionUID = -8022219757439228511L;

    private int tiaocangqi=10;
    // 调仓期
    //knn策略下应该将调仓期改为1，且恒定不变

    private String tiaocangjiage="收盘价";
    // 调仓价格

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

    public SaleVO() {
    }
}
