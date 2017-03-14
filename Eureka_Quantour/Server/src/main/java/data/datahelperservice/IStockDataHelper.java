package data.datahelperservice;

import java.util.HashMap;

/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public interface IStockDataHelper {
	/**
	 * 获得所有股票的信息
	 * @return	HashMap<String,HashMap<String,String>>股票信息
	 */
	public HashMap<String,HashMap<String,String>> getAllStock();
}
