package exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @Description: 用来表示结束日期小于最小有效日期
 * @author: hzp
 * @time: 2017年3月15日
 */
public class EndInvalidException extends Exception{
	
	private static final long serialVersionUID = 3302178450068302608L;

	LocalDate min;
	public EndInvalidException(){
		super();
	}
	public EndInvalidException(LocalDate max){
		super();
		this.min=max;
	}
	public String toString(){
		if(min==null)
			return "结束日期过小";
		else
			return "结束日期小于"+min.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
