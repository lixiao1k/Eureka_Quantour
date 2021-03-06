package exception;

/**
 * 不存在该日期时抛出该异常
 * @author 刘宇翔
 *
 */
public class NullDateException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4124778620578346442L;
	/**
	 * 
	 */
	private int cal;
	private String str;
	/**
	 * 
	 */
	
	public NullDateException(int _cal){
		super();
		this.cal=_cal;
		str="";
	}
	public NullDateException(int _cal,String str){
		super();
		this.cal=_cal;
		this.str=str;
	}
	public String toString(){
		return "搜索不到日期为"+cal+"的信息."+str;
	}
}
