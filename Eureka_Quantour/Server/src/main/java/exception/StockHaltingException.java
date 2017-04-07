package exception;
/**
 * 股票停牌时抛出该异常（数据层不判断是否是星期天）
 * @author 刘宇翔
 *
 */
public class StockHaltingException extends Exception{
	private int calendar;//停牌日期，2017-03-28则为20170328
	private int stock;//停牌股票编号，如1
	private static final long serialVersionUID = 1298600769065877840L;
	public StockHaltingException(int _cal,int _stock){
		super();
		this.calendar=_cal;
		this.stock=_stock;
	}
	public StockHaltingException(int _stock){
		super();
		this.stock=_stock;
		calendar=0;
	}
	public String toString(){
		return stock+"股票在"+calendar+"天停牌";
	}
}
