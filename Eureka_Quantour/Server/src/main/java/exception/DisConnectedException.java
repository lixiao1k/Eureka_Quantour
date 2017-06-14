package exception;

public class DisConnectedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1528466446882648577L;
	/**
	 * 
	 */
	String str;
	/**
	 * 
	 */
	public DisConnectedException(){
		super();
	}
	public DisConnectedException(String name){
		super();
		str=name;
	}
	public String toString(){
		if(str==null)
			return "网络连接中断";
		else
			return str;
	}
}
