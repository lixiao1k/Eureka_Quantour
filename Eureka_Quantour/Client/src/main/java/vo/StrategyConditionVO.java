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
    // KNN
    private List<Integer> parameter;//动量策略、均值策略
    // 动量策略：
    // 数组只有一个元素，即形成期
    // 均值策略
    // 数组只有一个元素，即几日均值
    // KNN
    // 数组有两个值 第一个为k，第二个为m


    private int nums;
    // 一定策略下，所筛选的股票个数
    // 比如按照动量策略取每次涨幅的前10只股票
    public StrategyConditionVO(String name, List<Integer> extra, int nums) {
        this.name = name;
        this.parameter = extra;
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
        return parameter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtra(List<Integer> extra) {
        this.parameter = extra;
    }

}
