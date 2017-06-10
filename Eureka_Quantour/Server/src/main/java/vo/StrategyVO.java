package vo;

import java.io.Serializable;

/**
 * Created by huihantao on 2017/5/11.
 */
public class StrategyVO implements Serializable {

    private static final long serialVersionUID = -4028276186904973508L;

    private String name;// strategy name
    private StrategyConditionVO strategyConditionVO;// condition of the strategy 
    // for more details 
    // see in the strategyConditionVO
    private SaleVO saleVO;
    // the sale condition of the strategy

    // whether public or not of the strategy
    private boolean publicorprivate;

    public StrategyVO(String name, StrategyConditionVO strategyConditionVO, SaleVO saleVO, boolean publicorprivate) {
        this.name = name;
        this.strategyConditionVO = strategyConditionVO;
        this.saleVO = saleVO;
        this.publicorprivate = publicorprivate;
    }


    public StrategyVO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StrategyConditionVO getStrategyConditionVO() {
        return strategyConditionVO;
    }

    public void setStrategyConditionVO(StrategyConditionVO strategyConditionVO) {
        this.strategyConditionVO = strategyConditionVO;
    }

    public SaleVO getSaleVO() {
        return saleVO;
    }

    public void setSaleVO(SaleVO saleVO) {
        this.saleVO = saleVO;
    }

    public boolean isPublicorprivate() {
        return publicorprivate;
    }

    public void setPublicorprivate(boolean publicorprivate) {
        this.publicorprivate = publicorprivate;
    }
}
