package data.parse;
/**
 * 将股票名字转换为标准类型
 * @author 刘宇翔
 *
 */
public class Parse {
	private static Parse p;
	private Parse(){
	}
	public static Parse getInstance(){
		if(p==null) p=new Parse();
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
		while(code.length()<6){
			code=0+code;
		}
		return code;
	}
	
	/**
	 * 将XXXX-XX-XX的日期形式转为XXXXXXXX
	 * @param str 原形式日期
	 * @return 转换后的日期
	 */
	public String encodeDate(String str){
		return str.substring(0, 4)+str.substring(5,7)+str.substring(8);
	}
	/**
	 * 将XXXX-XX-XX的日期形式转为数字
	 * @param str 原形式日期
	 * @return 转换后的日期
	 */
	public int getIntDate(String str){
		return Integer.parseInt(str.substring(0, 4)+str.substring(5,7)+str.substring(8));
	}
}
