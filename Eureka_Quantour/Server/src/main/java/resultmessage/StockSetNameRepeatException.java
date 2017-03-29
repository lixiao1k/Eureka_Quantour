package resultmessage;


public class StockSetNameRepeatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8432310804810955448L;
	public StockSetNameRepeatException(){
		super();
	}
	
	public String toString(){
		return "该股票池名字已存在";
	}
}
