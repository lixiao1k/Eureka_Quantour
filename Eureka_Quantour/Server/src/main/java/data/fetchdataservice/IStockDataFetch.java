package data.fetchdataservice;

import exception.InternetdisconnectException;
/**
 * 为软件扩充股票池和股票
 * @author 刘宇翔
 *
 */
public interface IStockDataFetch {
	/**
	 * 处理从网上获取的股票名字与股票池
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	public void fetchAllStockName() throws InternetdisconnectException;
	/**
	 * 获取手工添加的股票池
	 * @return 名字与编号组成的String的列表（格式:"编号名字"）
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	public void getArtificialSet();
}
