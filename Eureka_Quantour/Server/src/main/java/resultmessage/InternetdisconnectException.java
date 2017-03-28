package resultmessage;
/**
 * 
 * @Description: 用来表示网络连接断开或不稳定
 * @author: lyx
 * @time: 2017年3月27日
 */
public class InternetdisconnectException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8535913162866267522L;
	/**
	 * 
	 */
	public InternetdisconnectException(){
		super();
	}
	
	public String toString(){
		return "网络连接断开或不稳定";
	}
}
