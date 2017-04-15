package data.serviceimpl;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import data.service.IDataInterface;
import exception.DateOverException;
import exception.LogErrorException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.StockHaltingException;
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
	private StockDataController_2 stock2;
	private StockSetDataController stockset;
	public DataInterfaceImpl(){
		stock2=StockDataController_2.getInstance();
		user=UserDataController.getInstance();
		stockset=StockSetDataController.getInstance();
	}
//------------------------------------用户------------------------------------
	/**
	 * 判断用户是否注册成功，如果成功，登记用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @throws UserNameRepeatException 如果用户名已存在则抛出该异常
	 */
	@Override
	public void signUpCheck(String username, String password) throws UserNameRepeatException {
		user.signUpCheck(username, password);
	}
	/**
	 * 登出账号。
	 * @param username String,用户的登录名
	 * @return 一个boolean值，登录成功返回true，否则返回false
	 */
	@Override
	public void logout(String username) {
		user.logout(username);
	}
	/**
	 * 判断用户是否登录成功，如果成功，登录用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @throws LogErrorException 如果用户名或密码错误则抛出该异常。
	 */
	@Override
	public void signInCheck(String username, String password) throws LogErrorException {
		user.signInCheck(username, password);
	}
//-------------------------------------股票池--------------------------------------
	/**
	 * 获取某个用户的自定义股票池
	 * @param username 用户名
	 * @return 所有股票池的自定义名字
	 */
	@Override
	public List<String> getStockSet(String username) {
		return stockset.getStockSet(username);
	}
	/**
	 * 添加一个新的股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 * @throws StockSetNameRepeatException 股票池名字重复时抛出该异常
	 */
	@Override
	public void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException{
		stockset.addStockSet(stockSetName, username);
	}
	/**
	 * 删除一个股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	@Override
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
	@Override
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
	@Override
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username){
		stockset.deleteStockFromStockSet(stockName, stockSetName, username);
	}
	/**
	 * 获取某个用户自定义股票池的股票编号
	 * @param stockSetName 股票池名字
	 * @param userName,用户名字
	 * @return 一个股票编号的列表
	 */
	@Override
	public List<String> getStockSetInfo(String stockSetName, String userName) {
		return stockset.getStockSetInfo(stockSetName, userName);
	}
	/**
	 * 获取某个软件自带的股票池的股票编号
	 * @param stockSetName 股票池名字
	 * @return 一个股票编号的列表
	 */
	public List<String>  getStockSetInfo(String stockSetName){
		return stockset.getStockSetInfo(stockSetName);
	}
//----------------------------------查询单个股票-------------------------------------
	/**
	 * 获取股票某一天的数据
	 * @param stockcode 股票编号
	 * @param date 日期
	 * @return 股票信息
	 * @throws NullStockIDException 不存在该支股票时抛出该异常
	 * @throws StockHaltingException 该日期股票停牌时抛出该异常
	 * @throws NullDateException 该日期不存在时抛出该异常
	 */
	public SingleStockInfoPO getSingleStockInfo(String stockcode, LocalDate date) 
			throws  NullStockIDException, NullDateException {
		return stock2.getSingleStockInfo(stockcode, date);
	}
//	/**
//	 * 判断是否是交易日
//	 * @param day 需要判断的日期
//	 * @return	是交易日则返回true，否则返回false
//	 */
//	public boolean isMarketDay(Calendar day){
//		return stock2.isMarketDay((Calendar) day.clone());
//	}
	/**
	 * 获取一只股票的最早日期
	 * @param stockCode 股票编号
	 * @return 最早日期
	 * @throws NullStockIDException 该股票不存在时抛出异常
	 */
	public LocalDate getMinDay(String stockCode) throws NullStockIDException{
		return stock2.getMinDay(stockCode);
		
	}
	/**
	 * 获取一只股票的最晚日期
	 * @param stockCode 股票编号
	 * @return 最晚日期
	 * @throws NullStockIDException 该股票不存在时抛出异常
	 */
	public LocalDate getMaxDay(String stockCode) throws NullStockIDException{
		return stock2.getMaxDay(stockCode);
	}
	
//	
//	/**
//	 * 获取一支股票从起点时间（交易日）往后推x个交易日的全部数据（x>=0）
//	 * @param stockcode String,股票编号
//	 * @param begin Calendar,起始时间
//	 * @param last int,长度
//	 * @return 一个股票信息的对象的列表
//	 * @throws NullStockIDException 该股票不存在时抛出异常
//	 */
//	public List<SingleStockInfoPO> getSingleStockInfo_byLast(String stockcode,Calendar begin,int last) throws NullStockIDException{
//		return stock2.getSingleStockInfo_byLast(stockcode, (Calendar) begin.clone(), last);
//	}
//	/**
//	 * 获取一支股票从起始时间到终止时间的所有信息
//	 * @param stockcode String,股票编号
//	 * @param begin Calendar,起始时间
//	 * @param end Calendar,终止时间
//	 * @return 一个股票信息的对象的列表
//	 * @throws NullStockIDException 该股票不存在时抛出异常
//	 */
//	public List<SingleStockInfoPO> getSingleStockInfo_byEnd(String stockcode, Calendar begin, Calendar end) throws NullStockIDException {
//		return stock2.getSingleStockInfo_ByEnd(stockcode,(Calendar) begin.clone(), (Calendar)end.clone());
//	}
	/**
	 * 获取某一天起之后last个交易日之后的天数
	 * @param date 起始日期
	 * @param last 推移的天数
	 * @return 交易日日期
	 * @throws DateOverException
	 */
	public LocalDate addDays(LocalDate date,int last) throws DateOverException{
		if(last>0){
			LocalDate temp=LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
			temp=temp.plusDays(1);
			last--;
			return stock2.addDays(temp, last,true);
		}
		else{
			LocalDate temp=LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
			temp=temp.minusDays(1);
			last--;
			return stock2.addDays(temp, last,false);
		}
	}
	
	
//----------------------------------------查询股票池----------------------------------
//	/**
//	 * 获取某个软件自带的股票池的股票的某天信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @return StockSetInfoPO 股票池信息的po
//	 * @throws NullDateException 该天不是交易日时抛出异常
//	 */
//	public StockSetInfoPO getStockInfoinSet(String set,Calendar date) throws NullDateException{
//		List<String> stocklist=stockset.getStockSetInfo(set);
//		return stock2.getStockInfoinSet(stocklist, (Calendar) date.clone());
//	}
//	/**
//	 * 获取某个用户自定义的股票池的股票的某天信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param userName 用户名
//	 * @return StockSetInfoPO 股票池信息的po
//	 * @throws NullDateException 该天不是交易日时抛出异常
//	 */
//	public StockSetInfoPO getStockInfoinSet(String set,Calendar date,String userName) throws NullDateException{
//		List<String> stocklist=stockset.getStockSetInfo(set,userName);
//		return stock2.getStockInfoinSet(stocklist, (Calendar) date.clone());
//	}
//	
//	/**
//	 * 获取某个软件自带的股票池的股票的某天信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param last 持续时间（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的po的列表
//	 * @throws NullDateException 起始日期非交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByLast(String set,Calendar date,int last) throws NullDateException{
//		List<String> stocklist=stockset.getStockSetInfo(set);
//		return stock2.getStockInfoinSet_StopByLast(stocklist, (Calendar) date.clone(), last);
//	}
//	
//	
//	/**
//	 * 获取某个股票池的股票的某天即往后x天的信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param userName 用户名
//	 * @param last 持续时间（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的po的列表
//	 * @throws NullDateException 起始日期非交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByLast(String set,Calendar date,String userName,int last) throws NullDateException{
//		List<String> stocklist=stockset.getStockSetInfo(set,userName);
//		return stock2.getStockInfoinSet_StopByLast(stocklist, (Calendar) date.clone(), last);
//	}
//	/**
//	 * 获取某个软件自带的股票池的股票的起始于终止日期之间的所有信息
//	 * @param set 股票池名称
//	 * @param startDate 起始日期
//	 * @param endDate 终止日期
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByEnd(String set,Calendar startDate,Calendar endDate){
//		List<String> stocklist=stockset.getStockSetInfo(set);
//		return stock2.getStockInfoinSet_StopByEnd(stocklist, (Calendar) startDate.clone(),(Calendar) endDate.clone());
//	}
//	
//	/**
//	 * 获取某个软件自带的股票池的股票的起始于终止日期之间的所有信息
//	 * @param set 股票池名称
//	 * @param startDate 起始日期
//	 * @param endDate 终止日期
//	 * @param userName 用户名
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByEnd(String set,Calendar startDate,Calendar endDate,String userName){
//		List<String> stocklist=stockset.getStockSetInfo(set,userName);
//		return stock2.getStockInfoinSet_StopByEnd(stocklist, (Calendar) startDate.clone(),(Calendar) endDate.clone());
//	}
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
//	public List<StockSetInfoPO> getStockInfoinSet_forwardByLast(String set,Calendar date,String userName,int last) throws NullDateException{
//		List<String> stocklist=stockset.getStockSetInfo(set,userName);
//		return stock2.getStockInfoinSet_forwardByLast(stocklist, (Calendar) date.clone(), last);
//	}
//	
//	/**
//	 * 获取某个股票池的股票的某天与往前x天的信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param last 往前推的交易日数（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 * @throws NullDateException 起始日期非交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_forwardByLast(String set,Calendar date,int last) throws NullDateException{
//		List<String> stocklist=stockset.getStockSetInfo(set);
//		return stock2.getStockInfoinSet_forwardByLast(stocklist, (Calendar) date.clone(), last);
//	}
	
	
//old


	@Override
	public List<SingleStockInfoPO> getMarketByDate(Calendar date) {
		return stock.getMarketByDate((Calendar) date.clone());
	}
	
	
////uncompleted
	
	
}
