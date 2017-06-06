package exception;

/**
 * Created by huihantao on 2017/4/17.
 */
public class NullMarketException extends  Exception {
    public  NullMarketException(){
        super();
    }
    public String toString(){
        return "今日市场无数据";
    }
}
