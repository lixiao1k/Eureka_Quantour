package exception;
/**
 * 日期溢出时抛出该错误
 * @author 刘宇翔
 *
 */
public class DateOverException extends Exception{
		private static final long serialVersionUID = 4124778620578346442L;
		/**
		 * 
		 */
		private int cal;
		private int last;
		/**
		 *
		 */
		public DateOverException(int _cal,int _last){
			super();
			this.cal=_cal;
			this.last=_last;
		}
		public String toString(){
			return "搜索不到日期为"+cal+"+"+last+"的信息";
		}
}
