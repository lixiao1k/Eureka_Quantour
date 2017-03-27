package vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huihantao on 2017/3/27.
 */
public abstract class StrategyConditionVO implements Serializable {
    private static final long serialVersionUID=3607793986868423023L;
    public String name;
    public void sort(List<SingleStockInfoVO> list, int num){};
}
