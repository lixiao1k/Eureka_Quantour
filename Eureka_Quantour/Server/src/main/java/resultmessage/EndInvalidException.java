package resultmessage;

public class EndInvalidException extends Exception{
	
	private static final long serialVersionUID = 3302178450068302608L;

	public EndInvalidException(){
		super();
	}
	
	public String toString(){
		return "结束日期过小";
	}
}
