package exception;
/**
 * 
 * @Description: 用来表示结束日期小于最小有效日期
 * @author: hzp
 * @time: 2017年3月15日
 */
public class EndInvalidException extends Exception{
	
	private static final long serialVersionUID = 3302178450068302608L;

	public EndInvalidException(){
		super();
	}
	
	public String toString(){
		return "结束日期过小";
	}
}
