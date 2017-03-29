package data.parse;
/**
 * 将股票名字转换为标准类型
 * @author 刘宇翔
 *
 */
public class ParseStockName {
	public static String chkHalf(String str){
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
}
