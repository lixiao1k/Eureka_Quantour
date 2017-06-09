package data.service;

import java.time.LocalDate;
import java.util.List;

import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.TimeShraingLackException;
import po.SingleStockInfoPO;
import po.TimeSharingPO;

public interface IStockDataInterface {
	
	/**
	 * 获取一只股票的最早日期
	 * @param stockCode 股票编号
	 * @return 最早日期
	 * @throws NullStockIDException 该股票不存在时抛出异常
	 */
	public LocalDate getMinDay(String stockCode) throws NullStockIDException;
	/**
	 * 获取一只股票的最晚日期
	 * @param stockCode 股票编号
	 * @return 最晚日期
	 * @throws NullStockIDException 该股票不存在时抛出异常
	 */
	public LocalDate getMaxDay(String stockCode) throws NullStockIDException;
	/**
	 * 获得一个指数最后一个交易日日期
	 * @param name 股票名称
	 * @return 日期
	 */
	public LocalDate getExponentMaxDay(String name);
	/**
	 * 获得一个指数最早一个交易日日期
	 * @param name 股票名称
	 * @return 日期
	 */
	public LocalDate getExponentMinDay(String name);
	/**
	 * 
	 * @param stockCode
	 */
	public void addBrowseTimes(String stockCode)throws  NullStockIDException;
	/**
	 * 获取股票分时线
	 * @param code
	 * @param date
	 * @return 从9点到11点半，再从13点到15点的以1秒为单位的double数组
	 * @throws TimeShraingLackException
	 * @throws NullStockIDException
	 */
	public TimeSharingPO getTimeSharingData(String code,LocalDate date)throws TimeShraingLackException,NullStockIDException;
	/**
	 * 获得指数的一段时间的信息（里面只有名字，编号，开盘，收盘，涨跌幅，最高，最低，交易量）
	 * @param name
	 * @param start
	 * @param end
	 * @return
	 */
	public List<SingleStockInfoPO> getPeriodExponent(String name,LocalDate start,LocalDate end);
	/**
	 * 编号转名字
	 * @param code 股票编号
	 * @return 股票名字
	 */
	public String codeToname(String code);
	/**
	 * 编号转名字
	 * @param code 股票编号
	 * @return 股票名字
	 */
	public String nameTocode(String name);
	/**
	 * 模糊搜索
	 * @param code 输入的内容
	 * @return 输出的list，格式如000001	平安银行
	 */
	public List<String> fuzzySearch(String code);
	
	/**
	 * 获取股票某一天的数据
	 * @param stockcode 股票编号
	 * @param date 日期
	 * @return 股票信息
	 * @throws NullStockIDException 不存在该支股票时抛出该异常
	 * @throws NullDateException 该日期不存在时抛出该异常
	 */
	public SingleStockInfoPO getSingleStockInfo(String stockcode,LocalDate date) 
			throws  NullStockIDException, NullDateException;
	/**
	 * 获取某一天起之后last个交易日之后的天数
	 * @param date 起始日期
	 * @param last 推移的天数
	 * @return 交易日日期
	 * @throws DateOverException
	 */
	public LocalDate addDays(LocalDate date,int last) throws DateOverException;
}
