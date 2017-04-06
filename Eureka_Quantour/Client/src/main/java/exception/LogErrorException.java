package exception;
/**
 * 
 * @Description: 用来表示登录的用户名或密码有误
 * @author: lyx
 * @time: 2017年3月27日
 */
public class LogErrorException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8952680480169631081L;

	/**
	 * 
	 */
	public LogErrorException(){
		super();
	}
	
	public String toString(){
		return "用户名或密码有误";
	}
}