package vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huihantao on 2017/4/6.
 */
public class StrategyConditionVO implements Serializable{
    private static final long serialVersionUID = 3441219408241335848L;

    private String name;
    private List<Integer> parameter;
    private int nums;

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getExtra() {
        return parameter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtra(List<Integer> extra) {
        this.parameter = extra;
    }

    public StrategyConditionVO(String name, List<Integer> extra, int nums) {
        this.name = name;
        this.parameter = extra;
        this.nums = nums;
    }

    public StrategyConditionVO(String name, List<Integer> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public StrategyConditionVO( ) {

    }
}
