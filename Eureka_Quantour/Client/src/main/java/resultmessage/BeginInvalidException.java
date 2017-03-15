package resultmessage;
/**
 * 
 * @Description: 用来表示起始日期大于最大有效日期
 * @author: hzp
 * @time: 2017年3月15日
 */
public class BeginInvalidException extends Exception{

	private static final long serialVersionUID = -9005361763463590019L;

	public BeginInvalidException(){
		super();
	}
	
	public String toString(){
		return "开始日期过大";
	}
}
