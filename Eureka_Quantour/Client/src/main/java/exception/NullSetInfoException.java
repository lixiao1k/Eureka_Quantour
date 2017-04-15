package exception;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 不存在该股票时抛出该异常
 * @author 刘宇翔
 *
 */
public class NullSetInfoException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1401583672980612640L;
	private String set;
	private Calendar cal;
	private SimpleDateFormat sdf;
	/**
	 * 
	 */
	
	public NullSetInfoException(String _set,Calendar _cal){
		super();
		this.set=_set;
		this.cal=_cal;
		sdf=new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public String toString(){
		return "搜索不到股票池"+set+"于"+sdf.format(cal.getTime())+"的信息";
	}
}
