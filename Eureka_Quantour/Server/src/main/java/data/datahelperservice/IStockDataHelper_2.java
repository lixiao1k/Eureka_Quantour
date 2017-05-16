package data.datahelperservice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import data.common.DateLeaf;
import data.common.StockLeaf;
import exception.DateOverException;
import exception.NullDateException;
import exception.StockHaltingException;
import exception.TimeShraingLackException;

/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public interface IStockDataHelper_2 {

	
//	/**
//	 * 获取某个软件自带的股票池的股票的某天信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @return StockSetInfoPO 股票池信息的po
//	 * @throws StockHaltingException 停牌时抛出该异常
//	 */
//	public String getStockInfoinSet_throughRemain(int code,DateLeaf leaf) throws StockHaltingException;
//	
//	/**
//	 * 将日期map停留在日期date上
//	 * @param date 日期
//	 * @throws NullDateException 不存在该日期时抛出该异常
//	 */
//	public DateLeaf remain_forAllinfo(int date) throws NullDateException;
//	/**
//	 * 将日期往后推一天（针对于汇总信息的表）
//	 * @return 
//	 * @throws NullDateException 如果已是最后一天抛出该异常
//	 */
//	public DateLeaf nextDay_forAllinfo(DateLeaf leaf) throws NullDateException;
//	/**
//	 * 将日期往后前一天（针对于汇总信息的表）
//	 * @throws NullDateException 如果已是第一天抛出该异常
//	 */
//	public DateLeaf previousDay_forAllinfo(DateLeaf leaf) throws NullDateException;
//	/**
//	 * 判断是否是交易日
//	 * @param day 需要判断的日期
//	 * @return	是交易日则返回true，否则返回false
//	 */
//	public boolean isMarketDay(int day);
//	
//	/**
//	 * 将股票code的日期map停留在日期date上
//	 * @param date 日期
//	 * @throws NullDateException 缺少该天信息
//	 */
//	public StockLeaf remain_forSingleinfo(int code,int date) throws NullDateException;
//	/**
//	 * 将日期往后推一天（针对于汇总信息的表）
//	 * @throws NullDateException 如果已是最后一天抛出该异常
//	 */
//	public StockLeaf nextDay_forSingleinfo(StockLeaf leaf) throws NullDateException;
//	/**
//	 * 获取单支股票在停留处的数据
//	 * @return 数据 string
//	 */
//	public	String getSingleInfo_throughRemain(StockLeaf leaf);
	
	/**
	 * 获取某只股票最早的一天
	 * @param code 股票编号
	 * @return 日期
	 * @throws IOException 
	 */
	public LocalDate getMinDay(String code) throws IOException;
	/**
	 * 获取某只股票最晚的一天
	 * @param code 股票编号
	 * @return 日期
	 * @throws IOException 
	 */
	public LocalDate getMaxDay(String code) throws IOException;
	
	/**
	 * 获取指定股票指定日期的信息
	 * @param cal 日期，形如XXXXXXXX,20170328
	 * @param code 股票编号，形如000001,1
	 * @return 股票信息，不存在时抛出异常
	 * @throws StockHaltingException 不存在股票数据时抛出该异常
	 * @throws NullDateException 不存在该日期时抛出该异常
	 */
	public String getSingleInfo(int cal,String code) throws StockHaltingException, NullDateException;
	/**
	 * 获取某一天起之后last个交易日之后的天数
	 * @param date 起始日期
	 * @param last 推移的天数
	 * @return 交易日日期
	 * @throws DateOverException
	 * @throws NullDateException 
	 */
	public LocalDate addDays(int date,int last) throws DateOverException, NullDateException;
	public List<Double> getTimeSharingData(String code,int date)throws TimeShraingLackException;
	public List<String> fuzzySearch(String code);
	public void addBrowseTimes(String stockCode);
}
