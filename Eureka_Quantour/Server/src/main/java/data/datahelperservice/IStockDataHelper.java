package data.datahelperservice;

import java.util.HashMap;

/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public interface IStockDataHelper {
	public HashMap<String,HashMap<String,String>> getAllStock();
}
