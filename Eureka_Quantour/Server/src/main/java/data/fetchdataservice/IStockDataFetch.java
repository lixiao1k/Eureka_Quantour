package data.fetchdataservice;

import java.util.List;

import exception.InternetdisconnectException;
/**
 * 从网上爬取股票
 * @author 刘宇翔
 *
 */
public interface IStockDataFetch {
	/**
	 * 从网上获取所有股票的名字与编号
	 * @return 名字与编号组成的String的列表（格式:"编号名字"）
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	public void getAllStockName() throws InternetdisconnectException;
}
