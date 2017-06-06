package exception;

public class StrategyRepeatException extends Exception{
		/**
	 * 
	 */
	private static final long serialVersionUID = 8528988812285804533L;
		public StrategyRepeatException(){
			
		}
		public String toString(){
			return "已有该名字";
		}
}
