package vo;

import java.io.Serializable;

/**
 * Created by huihantao on 2017/3/27.
 */
public class SaleVO implements Serializable {
    private static final long serialVersionUID = -8022219757439228511L;

    private int tiaocangqi=10;

    private String tiaocangjiage="收盘价";

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
}
