package exception;
/**
 * 不存在该股票时抛出该异常
 * @author 刘宇翔
 *
 */
public class SqlNotConnectedException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 132498277458039500L;

	public SqlNotConnectedException(){
		super();
	}
	
	public String toString(){
		return "服务器数据库未打开";
	}
}