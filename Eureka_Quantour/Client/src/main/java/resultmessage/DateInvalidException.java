package resultmessage;
/**
 * 
 * @Description: 用来表示该日期段内没有股票数据
 * @author: hzp
 * @time: 2017年3月15日
 */
public class DateInvalidException extends Exception{

	private static final long serialVersionUID = 3302178450068302608L;

	public DateInvalidException(){
		super();
	}
	
	public String toString(){
		return "这段日期没有股票数据";
	}
}
