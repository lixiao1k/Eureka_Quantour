package exception;

public class NullSetException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9131972113760735474L;
	private String set;
	private String userName;
	public NullSetException(String _set,String _userName){
		super();
		set=_set;
		userName=_userName;
	}
	
	public String toString(){
		return "不存在改股票池\n"+"错误发生在——用户名："+userName+",股票池："+set;
	}
}
