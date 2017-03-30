package data.service;

import java.util.Calendar;
import java.util.List;

import exception.LogErrorException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;
import exception.UserNameRepeatException;
import po.SingleStockInfoPO;

/**
 * 数据层提供的接口
 * @author 刘宇翔
 *
 */
public interface IDataInterface 
{
	/**
	 * 判断用户是否注册成功，如果成功，登记用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @throws UserNameRepeatException 如果用户名已存在则抛出该异常
	 */
	public void signUpCheck(String username,String password) throws UserNameRepeatException;
	/**
	 * 登出账号。
	 * @param username String,用户的登录名
	 * @return 一个boolean值，登录成功返回true，否则返回false
	 */
	public void logout(String username);
	/**
	 * 判断用户是否登录成功，如果成功，登录用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @throws LogErrorException 如果用户名或密码错误则抛出该异常。
	 */
	public void signInCheck(String username,String password) throws LogErrorException;
	/**
	 * 获取某个用户的自定义股票池
	 * @param username 用户名
	 * @return 所有股票池的自定义名字
	 */
	public List<String> getStockSet(String username);
	/**
	 * 添加一个新的股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 * @throws StockSetNameRepeatException 股票池名字重复时抛出该异常
	 */
	public void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException;
	/**
	 * 删除一个股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	public void deleteStockSet(String stockSetName, String username);
	/**
	 * 添加一个新的股票到股票池中
	 * @param stockName 股票的名字
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 * @throws StockNameRepeatException 股票池中已存在该股票时抛出异常
	 */
	public void addStockToStockSet(String stockName, String stockSetName, String username) throws
	StockNameRepeatException;
	/**
	 * 从股票池中删除一个股票
	 * @param stockName 股票的名字
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username);

	/**
	 * 获取一支股票从起始时间到终止时间的所有信息
	 * @param stockcode String,股票编号
	 * @param begin Calendar,起始时间
	 * @param end Calendar,终止时间
	 * @return 一个股票信息的对象的列表
	 */
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode,Calendar begin,Calendar end);
	public List<SingleStockInfoPO> getMarketByDate(Calendar date) ;
	
	/**
	 * 获取股票某一天的数据
	 * @param stockcode 股票编号
	 * @param date 日期
	 * @return 股票信息
	 */
	public SingleStockInfoPO getSingleStockInfo(String stockcode,Calendar date);
	/**
	 * 获取某个股票池的股票编号
	 * @param date Calendar,需要查找的时间
	 * @return 一个股票编号的列表
	 */
	public List<String>  getStockSetSortedInfo(String stockSetName,String userName);
}
