package data.datahelperservice;

import exception.StockHaltingException;

/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public interface IStockDataHelper_2 {
	/**
	 * 获取指定股票指定日期的信息
	 * @param cal 日期，形如XXXXXXXX,20170328
	 * @param code 股票编号，形如000001,1
	 * @return 股票信息，不存在时抛出异常
	 * @throws StockHaltingException 不存在股票数据时抛出该异常
	 */
	public String getSingleInfo(int cal,int code) throws StockHaltingException;
}
