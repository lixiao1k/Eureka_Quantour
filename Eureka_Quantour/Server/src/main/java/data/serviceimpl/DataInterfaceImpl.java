package data.serviceimpl;

import java.util.Calendar;
import java.util.List;

import data.service.IDataInterface;
import exception.LogErrorException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;
import exception.UserNameRepeatException;
import po.SingleStockInfoPO;
/**
 * 数据层接口的实现
 * @author 刘宇翔
 *
 */
public class DataInterfaceImpl implements IDataInterface
{
	private UserDataController user;
	private StockDataController stock;
	private StockSetDataController stockset;
	public DataInterfaceImpl(){
		stock=StockDataController.getInstance();
		user=UserDataController.getInstance();
		stockset=StockSetDataController.getInstance();
	}
	@Override
	public void signUpCheck(String username, String password) throws UserNameRepeatException {
		user.signUpCheck(username, password);
	}

	@Override
	public void signInCheck(String username, String password) throws LogErrorException {
		user.signInCheck(username, password);
	}

	@Override
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode, Calendar begin, Calendar end) {
		return stock.getSingleStockInfo(stockcode, begin, end);
	}

	@Override
	public List<SingleStockInfoPO> getMarketByDate(Calendar date) {
		return stock.getMarketByDate(date);
	}
	@Override
	public void logout(String username) {
		user.logout(username);
	}
	@Override
	public List<String> getStockSet(String username) {
		return stockset.getStockSet(username);
	}
	
	
////uncompleted
//	@Override
//	public List<SingleStockInfoPO> getStockSetSortedInfo(String stockSetName, String userName, Calendar date) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	/**
	 * 添加一个新的股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 * @throws StockSetNameRepeatException 股票池名字重复时抛出该异常
	 */
	public void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException{
		stockset.addStockSet(stockSetName, username);
	}
	/**
	 * 删除一个股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	public void deleteStockSet(String stockSetName, String username){
		stockset.deleteStockSet(stockSetName, username);
	}
	/**
	 * 添加一个新的股票到股票池中
	 * @param stockName 股票的名字
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 * @throws StockNameRepeatException 股票池中已存在该股票时抛出异常
	 */
	public void addStockToStockSet(String stockName, String stockSetName, String username) throws
	StockNameRepeatException{
		stockset.addStockToStockSet(stockName, stockSetName, username);
	}
	/**
	 * 从股票池中删除一个股票
	 * @param stockName 股票的名字
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username){
		stockset.deleteStockFromStockSet(stockName, stockSetName, username);
	}
	@Override
	public SingleStockInfoPO getSingleStockInfo(String stockcode, Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> getStockSetInfo(String stockSetName, String userName) {
		// TODO Auto-generated method stub
		return null;
	}
}
