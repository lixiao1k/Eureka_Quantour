package po;

import java.util.List;

/**
 * Created by huihantao on 2017/5/13.
 */
public class StrategyInfoPO  {
    private String strategTypeNname;
    private boolean publicorprivate;

    private List<Integer> parameter;
    private int purchasenum;

    private int tiaocangqi;

    private String tiaocangjiage;

    public StrategyInfoPO(String strategTypeNname, boolean publicorprivate, List<Integer> parameter, int nums, int tiaocangqi, String tiaocangjiage) {
        this.strategTypeNname = strategTypeNname;
        this.publicorprivate = publicorprivate;
        this.parameter = parameter;
        this.purchasenum = nums;
        this.tiaocangqi = tiaocangqi;
        this.tiaocangjiage = tiaocangjiage;
    }

    public String getStrategTypeNname() {
        return strategTypeNname;
    }

    public void setStrategTypeNname(String strategTypeNname) {
        this.strategTypeNname = strategTypeNname;
    }

    public boolean isPublicorprivate() {
        return publicorprivate;
    }

    public void setPublicorprivate(boolean publicorprivate) {
        this.publicorprivate = publicorprivate;
    }

    public List<Integer> getParameter() {
        return parameter;
    }

    public void setParameter(List<Integer> parameter) {
        this.parameter = parameter;
    }

    public int getPurchasenum() {
        return purchasenum;
    }

    public void setPurchasenum(int purchasenum) {
        this.purchasenum = purchasenum;
    }

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
