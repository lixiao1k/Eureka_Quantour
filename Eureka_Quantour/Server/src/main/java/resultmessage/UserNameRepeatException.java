package resultmessage;
/**
 * 
 * @Description: 用来表示注册的用户名已经存在
 * @author: lyx
 * @time: 2017年3月27日
 */
public class UserNameRepeatException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2824885247184386905L;
	/**
	 * 
	 */
	public UserNameRepeatException(){
		super();
	}
	
	public String toString(){
		return "该用户名已存在";
	}
}
