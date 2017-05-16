package vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huihantao on 2017/4/6.
 */
public class StrategyConditionVO implements Serializable{
    private static final long serialVersionUID = 3441219408241335848L;

    private String name;
    // 策略名称（目前只有2个） 
    // 动量策略
    // 均值策略
    private List<Integer> extra;//动量策略、均值策略
    // 动量策略：
    // 数组只有一个元素，即形成期
    // 均值策略
    // 数组只有一个元素，即几日均值


    private int nums;
    // 一定策略下，所筛选的股票个数
    // 比如按照动量策略取每次涨幅的前10只股票

    public StrategyConditionVO(String name, List<Integer> extra, int nums) {
        this.name = name;
        this.extra = extra;
        this.nums = nums;
    }

    public StrategyConditionVO( ) {

    }
    

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
        return extra;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtra(List<Integer> extra) {
        this.extra = extra;
    }

}
