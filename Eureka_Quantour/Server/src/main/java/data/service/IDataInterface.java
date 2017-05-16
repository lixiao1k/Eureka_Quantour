package data.service;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import exception.DateOverException;
import exception.LogErrorException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.StockHaltingException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;
import exception.StrategyRepeatException;
import exception.TimeShraingLackException;
import exception.UserNameRepeatException;
import po.CommentPO;
import po.SingleStockInfoPO;
import po.StockSetInfoPO;
import po.StrategyInfoPO;
import po.StrategyShowPO;
import vo.StrategyShowVO;
import vo.StrategyVO;

/**
 * 数据层提供的接口
 * @author 刘宇翔
 *
 */
public interface IDataInterface 
{
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
	public List<Double> getTimeSharingData(String code,LocalDate date)throws TimeShraingLackException,NullStockIDException;
	/**
	 * 应用策略，获得该策略的各个参数
	 * @param createrName
	 * @param strategyName
	 * @return
	 */
	public StrategyInfoPO applyStrategy(String createrName,String strategyName);
	/**
	 * 获得指数的一段时间的信息（里面只有名字，编号，开盘，收盘，涨跌幅，最高，最低，交易量）
	 * @param name
	 * @param start
	 * @param end
	 * @return
	 */
	public List<SingleStockInfoPO> getPeriodExponent(String name,LocalDate start,LocalDate end);
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
	 * 获取行业板块中的各种分类
	 * @return 行业板块中的各种分类
	 */
	public List<String> getIndustryList();
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
	 * 获取某个用户自定义股票池的股票编号
	 * @param stockSetName 股票池名字
	 * @param userName,用户名字
	 * @return 一个股票编号的列表
	 */
	public List<String>  getStockSetInfo(String stockSetName,String userName);
	/**
	 * 获取某个软件自带的股票池的股票编号
	 * @param stockSetName 股票池名字
	 * @return 一个股票编号的列表
	 */
	public List<String>  getStockSetInfo(String stockSetName);
	
	
	
	public List<SingleStockInfoPO> getMarketByDate(Calendar date) ;
	
	/**
	 * 获取概念板块中的各种分类
	 * @return 行业板块中的各种分类
	 */
	public List<String> getConceptList();
	/**
	 * 获取地区板块中的各种分类
	 * @return 行业板块中的各种分类
	 */
	public List<String> getAreaList();
	/**
	 * 存储策略
	 * @param po 一个策略的po
	 * @throws StrategyRepeatException 
	 */
	public void saveStrategy(StrategyInfoPO po,String strategyName,String userName) throws StrategyRepeatException;

	/**
	 * 删除策略
	 * @param createName 创建者名字
	 * @param strategyName 策略名字
	 */
	public void deleteStrategy ( String createName, String strategyName);

	/**
	 * 评价策略
	 * @param Username 创建者名字
	 * @param strategyName 策略名字
	 * @param commenterName 评价者名字
	 * @param time 评价时间
	 * @param comment 评价内容
	 */
	public void comment(String Username, String strategyName, String commenterName, LocalDateTime time, String comment);

	/**
	 * 获取策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public StrategyShowPO getStrategy ( String createrName, String StrategyName );
	
	/**
	 * 获取策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void addStrategyShow ( String createrName, String StrategyName ,StrategyShowPO vo);
	/**
	 * 清空策略详细图标
	 */
	public void clearStrategyShow ();


	/**
	 * 获取某个用户的所有策略
	 * @param createrName 用户名
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( String createrName);

	/**
	 * 获取所有公开的策略
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( );

	/**
	 * 修改策略是否公开
	 * @param creatroName 创建者名字
	 * @param straetgyName 策略名字
	 * @param property 是否公开
	 */
	public void setPublic(String creatroName, String straetgyName,boolean property);
	/**
	 * 获取评价
	 * @param createrName
	 * @param strategyName
	 * @return
	 */
	public List<CommentPO> getStrategyComments(String createrName,String strategyName);
	/**
	 * 模糊搜索
	 * @param code 输入的内容
	 * @return 输出的list，格式如000001	平安银行
	 */
	public List<String> fuzzySearch(String code);
	
//新增接口————————————————————————————————————————————————————————————————————————————————
	//针对单支股票
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
//	/**
//	 * 获取一支股票从起始时间到终止时间的所有信息
//	 * @param stockcode String,股票编号
//	 * @param begin Calendar,起始时间
//	 * @param end Calendar,终止时间
//	 * @return 一个股票信息的对象的列表
//	 * @throws NullStockIDException 该股票不存在时抛出异常
//	 */
//	public List<SingleStockInfoPO> getSingleStockInfo_byEnd(String stockcode,Calendar begin,Calendar end) throws NullStockIDException;
//	/**
//	 * 获取一支股票从起点时间（交易日）往后推x个交易日的全部数据（x>=0）
//	 * @param stockcode String,股票编号
//	 * @param begin Calendar,起始时间
//	 * @param last int,长度
//	 * @return 一个股票信息的对象的列表
//	 * @throws NullStockIDException 该股票不存在时抛出异常
//	 */
//	public List<SingleStockInfoPO> getSingleStockInfo_byLast(String stockcode,Calendar begin,int last) throws NullStockIDException;
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
	 * 获取某一天起之后last个交易日之后的天数
	 * @param date 起始日期
	 * @param last 推移的天数
	 * @return 交易日日期
	 * @throws DateOverException
	 */
	public LocalDate addDays(LocalDate date,int last) throws DateOverException;
//	
//	//针对市场或股票池
//	/**
//	 * 获取某个软件自带的股票池的股票的某天信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @return StockSetInfoPO 股票池信息的po
//	 * @throws NullDateException 该天不是交易日时抛出异常
//	 */
//	public StockSetInfoPO getStockInfoinSet(String set,Calendar date) throws NullDateException;
//	/**
//	 * 获取某个用户自定义的股票池的股票的某天信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param userName 用户名
//	 * @return StockSetInfoPO 股票池信息的po
//	 * @throws NullDateException 该天不是交易日时抛出异常
//	 */
//	public StockSetInfoPO getStockInfoinSet(String set,Calendar date,String userName) throws NullDateException;
//	/**
//	 * 获取某个股票池的股票的某天与往后x天的信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param last 往后推的交易日数（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的po的列表
//	 * @throws NullDateException 起始日期非交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByLast(String set,Calendar date,int last) throws NullDateException;
//	
//	/**
//	 * 获取某个股票池的股票的某天与往后x天的信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param userName 用户名
//	 * @param last 往后推的交易日数（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 * @throws NullDateException 起始日期非交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByLast(String set,Calendar date,String userName,int last) throws NullDateException;
//	
//	/**
//	 * 获取某个软件自带的股票池的股票的起始于终止日期之间的所有信息
//	 * @param set 股票池名称
//	 * @param startDate 起始日期
//	 * @param endDate 终止日期
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByEnd(String set,Calendar startDate,Calendar endDate);
//	
//	/**
//	 * 获取某个软件自带的股票池的股票的起始于终止日期之间的所有信息
//	 * @param set 股票池名称
//	 * @param startDate 起始日期
//	 * @param endDate 终止日期
//	 * @param userName 用户名
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByEnd(String set,Calendar startDate,Calendar endDate,String userName);
//	/**
//	 * 判断是否是交易日
//	 * @param day 需要判断的日期
//	 * @return	是交易日则返回true，否则返回false
//	 */
//	public boolean isMarketDay(Calendar day);
//	
//	/**
//	 * 获取某个股票池的股票的某天与往前x天的信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param userName 用户名
//	 * @param last 往前推的交易日数（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 * @throws NullDateException 起始日期非交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_forwardByLast(String set,Calendar date,String userName,int last) throws NullDateException;
//	/**
//	 * 获取某个股票池的股票的某天与往前x天的信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param last 往前推的交易日数（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 * @throws NullDateException 起始日期非交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_forwardByLast(String set,Calendar date,int last) throws NullDateException;
}
