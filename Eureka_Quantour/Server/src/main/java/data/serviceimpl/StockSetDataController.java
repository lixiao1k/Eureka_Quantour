package data.serviceimpl;

import java.util.List;

import data.datahelperimpl.StockSetDataHelperImpl;
import data.datahelperservice.IStockSetDataHelper;
import exception.NullSetException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;

public class StockSetDataController {
	private static StockSetDataController stocksetdata;
	private IStockSetDataHelper stocksetdatahelper;
	private StockSetDataController(){
		stocksetdatahelper=StockSetDataHelperImpl.getInstance();
	}
	public static StockSetDataController getInstance(){
		if(stocksetdata==null) stocksetdata=new StockSetDataController();
		return stocksetdata;
	}
	/**
	 * 获取某个用户的自定义股票池
	 * @param username 用户名
	 * @return 所有股票池的自定义名字
	 */
	public List<String> getStockSet(String username){
		return stocksetdatahelper.getStockSet(username);
	}
	/**
	 * 添加一个新的股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 * @throws StockSetNameRepeatException 股票池名字重复时抛出该异常
	 */
	public void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException{
		stocksetdatahelper.addStockSet(stockSetName, username);
	}
	/**
	 * 删除一个股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	public void deleteStockSet(String stockSetName, String username){
		stocksetdatahelper.deleteStockSet(stockSetName, username);
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
		stocksetdatahelper.addStockToStockSet(stockName, stockSetName, username);
	}
	/**
	 * 从股票池中删除一个股票
	 * @param stockName 股票的名字
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username){
		System.out.println(stockName);
		stocksetdatahelper.deleteStockFromStockSet(stockName, stockSetName, username);
	}
	/**
	 * 获取某个用户自定义股票池的股票编号
	 * @param stockSetName 股票池名字
	 * @param userName,用户名字
	 * @return 一个股票编号的列表
	 */
	public List<String>  getStockSetInfo(String stockSetName,String userName){
		try {
			return stocksetdatahelper.getStockSetInfo(stockSetName, userName);
		} catch (NullSetException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取软件中某个股票池的股票编号
	 * @param stockSetName 股票池名字
	 * @return 一个股票编号的列表
	 */
	public List<String>  getStockSetInfo(String stockSetName){
		try {
			return stocksetdatahelper.getStockSetInfo(stockSetName);
		} catch (NullSetException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取行业板块中的各种分类
	 * @return 行业板块中的各种分类
	 */
	public List<String> getIndustryList(){
		return stocksetdatahelper.getIndustryList();
	}
}
