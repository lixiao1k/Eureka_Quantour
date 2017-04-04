package exception;

public class NoneMatchedMarketException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9114563060257630286L;
	public NoneMatchedMarketException(){
		super();
	}
	
	public String toString(){
		return "没有匹配该股票编号的市场";
	}
}
