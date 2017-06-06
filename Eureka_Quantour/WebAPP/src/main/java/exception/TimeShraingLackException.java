package exception;

public class TimeShraingLackException extends Exception {
	private static final long serialVersionUID = 8432310804810955448L;
	public TimeShraingLackException(){
		super();
	}
	
	public String toString(){
		return "缺少该天分时信息";
	}
}