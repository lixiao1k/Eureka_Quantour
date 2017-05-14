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
	public void fetchAllStockSet() throws InternetdisconnectException;
	public void fetchIndustryList() throws InternetdisconnectException;
	public void fetchConceptList() throws InternetdisconnectException;
	public void fetchAreaList() throws InternetdisconnectException;
	public void fetchInit() throws InternetdisconnectException;
	/**
	 * 获取手工添加的股票池
	 * @return 名字与编号组成的String的列表（格式:"编号名字"）
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	public void getArtificialSet();
	/**
	 * 获取所有股票的信息
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	public void fetchAllStockInfo() throws InternetdisconnectException;
}
