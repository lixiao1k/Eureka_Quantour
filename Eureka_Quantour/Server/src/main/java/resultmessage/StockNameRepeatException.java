package resultmessage;

public class StockNameRepeatException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9129627084744097615L;
	public StockNameRepeatException(){
		super();
	}
	
	public String toString(){
		return "该股票池中已存在该股票";
	}
}
