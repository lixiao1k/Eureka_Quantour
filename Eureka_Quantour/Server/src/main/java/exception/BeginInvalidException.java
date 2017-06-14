package exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @Description: 用来表示起始日期大于最大有效日期
 * @author: hzp
 * @time: 2017年3月15日
 */
public class BeginInvalidException extends Exception{

	private static final long serialVersionUID = -9005361763463590019L;
	LocalDate max;
	public BeginInvalidException(){
		super();
	}
	public BeginInvalidException(LocalDate max){
		super();
		this.max=max;
	}
	public String toString(){
		if(max==null)
			return "开始日期过大";
		else
			return "开始日期大于"+max.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
