package exception;

/**
 * Created by huihantao on 2017/4/6.
 */
public class PriceTypeException extends Exception{
    public PriceTypeException(){
        super();
    }

    public String toString(){
        return "调仓价格种类错误";
    }
}
