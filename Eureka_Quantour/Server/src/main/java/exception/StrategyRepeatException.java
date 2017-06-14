package exception;

public class StrategyRepeatException extends Exception{
		/**
	 * 
	 */
	private static final long serialVersionUID = 8528988812285804533L;
	public String str;
		public StrategyRepeatException(){
			
		}
		public StrategyRepeatException(String str){
			this.str=str;
		}
		public String toString(){
			if(str==null)
				return "已有该名字";
			else
				return str;
		}
}
