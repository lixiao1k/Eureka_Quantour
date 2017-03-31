package data.parse;
/**
 * 将股票名字转换为标准类型
 * @author 刘宇翔
 *
 */
public class ParseStockName {
	private static ParseStockName p;
	private ParseStockName(){
	}
	public static ParseStockName getInstance(){
		if(p==null) p=new ParseStockName();
		return p;
	}
	/**
	 * 将全角转换为半角并去掉中间的空格
	 * @param str 未处理的股票名字
	 * @return 处理后的股票名字
	 */
	public String chkHalf(String str){
		str=str.replace(" ", "");
    	String sum="";
        for(int i=0;i<str.length();i++)
            {   
               char strCode=str.charAt(i);   
               if((strCode>65248)||(strCode==12288)){  
            	   strCode=(char) (strCode-65248);
                } 
               sum=sum+strCode;
        }
        return sum;
	}
	/**
	 * 将股票编号补为6位
	 * @param code 未处理的股票编号
	 * @return 处理后的股票编号
	 */
	public String supCode(String code){
		return String.format("%06d", Integer.parseInt(code));
	}
}
