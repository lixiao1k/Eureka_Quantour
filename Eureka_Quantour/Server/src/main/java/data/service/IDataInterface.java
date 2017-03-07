package data.service;

import java.util.Calendar;
import java.util.List;

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
	 * @return 一个boolean值，注册成功返回true，否则返回false
	 */
	public boolean signUpCheck(String username,String password);
	
	/**
	 * 判断用户是否登录成功，如果成功，登录用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @return 一个boolean值，登录成功返回true，否则返回false
	 */
	public boolean signInCheck(String username,String password);
	
	/**
	 * 获取一支股票从起始时间到终止时间的所有信息
	 * @param stockcode String,股票编号
	 * @param begin Calendar,起始时间
	 * @param end Calendar,终止时间
	 * @return 一个股票信息的对象的列表
	 */
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode,Calendar begin,Calendar end);
	
	/**
	 * 获取市场某一天的所有股票的信息
	 * @param date Calendar,需要查找的时间
	 * @return 一个股票信息的对象的列表
	 */
	public List<SingleStockInfoPO> getMarketByDate(Calendar date);
}
