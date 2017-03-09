package data.datahelperservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public interface IStockDataHelper {
	public HashMap<String,HashMap<String,String>> getAllStock();
	public ArrayList<String> find(HashMap<String,HashMap<String,String>> map,String code,Calendar start,Calendar end);
}
