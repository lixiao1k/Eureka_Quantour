package po;

import java.util.List;

/**
 * Created by huihantao on 2017/5/13.
 */
public class StrategyInfoPO  {
    private String strategyname;
    private boolean publicorprivate;

    private String name;
    private List<Integer> parameter;
    private int purchasenum;

    private int tiaocangqi;

    private String tiaocangjiage;

    public StrategyInfoPO(String strategyname, boolean publicorprivate, String name, List<Integer> parameter, int nums, int tiaocangqi, String tiaocangjiage) {
        this.strategyname = strategyname;
        this.publicorprivate = publicorprivate;
        this.name = name;
        this.parameter = parameter;
        this.purchasenum = nums;
        this.tiaocangqi = tiaocangqi;
        this.tiaocangjiage = tiaocangjiage;
    }

    public String getStrategyname() {
        return strategyname;
    }

    public void setStrategyname(String strategyname) {
        this.strategyname = strategyname;
    }

    public boolean isPublicorprivate() {
        return publicorprivate;
    }

    public void setPublicorprivate(boolean publicorprivate) {
        this.publicorprivate = publicorprivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
