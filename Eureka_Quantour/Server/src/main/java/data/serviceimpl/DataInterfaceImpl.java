package data.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import data.parse.Parse;
import data.parse.Translate;
import data.service.IDataInterface;
import exception.DateOverException;
import exception.LogErrorException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.SqlNotConnectedException;
import exception.StockHaltingException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;
import exception.StrategyRepeatException;
import exception.TimeShraingLackException;
import exception.UserNameRepeatException;
import po.CommentPO;
import po.CompanyInfoPO;
import po.SingleStockInfoPO;
import po.StrategyInfoPO;
import po.StrategyShowPO;
import po.TimeSharingPO;
import vo.StrategyShowVO;
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
	private StrategyDataController strategy;
	private CompanyDataController company;
	public DataInterfaceImpl(){
		stock2=StockDataController_2.getInstance();
		user=UserDataController.getInstance();
		stockset=StockSetDataController.getInstance();
		strategy=StrategyDataController.getInstance();
		company=CompanyDataController.getInstance();
	}
	
	public LocalDate getExponentMaxDay(String name)
	{
		return stock2.getExponentMaxDay(name);
	}
	public LocalDate getExponentMinDay(String name)
	{
		return stock2.getExponentMinDay(name);
	}
	
	public CompanyInfoPO getLatestCommpanyInfo(LocalDate time,String code) throws NullStockIDException{
		return company.getLatestCommpanyInfo(time, code);
	}
	public List<CompanyInfoPO> getCompanyDetail(String code) throws NullStockIDException{
		return company.getCompanyDetail(code);
	}
	
//------------------------------------用户------------------------------------
	
	
	
	/**
	 * 
	 * @param stockCode
	 * @throws NullStockIDException 
	 */
	public void addBrowseTimes(String stockCode) throws NullStockIDException{
		stock2.addBrowseTimes(stockCode);
	}
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
	 * @throws SqlNotConnectedException 
	 */
	@Override
	public void signInCheck(String username, String password) throws LogErrorException, SqlNotConnectedException {
		user.signInCheck(username, password);
	}
	/**
	 * 存储策略
	 * @param po 一个策略的po
	 * @throws StrategyRepeatException 
	 */
	public void saveStrategy(StrategyInfoPO po,String strategyName,String username) throws StrategyRepeatException{
		strategy.saveStrategy(po,strategyName,username);
	}
	/**
	 * 删除策略
	 * @param createName 创建者名字
	 * @param strategyName 策略名字
	 */
	public void deleteStrategy ( String createName, String strategyName){
		strategy.deleteStrategy(createName, strategyName);
	}
	
	

	/**
	 * 评价策略
	 * @param Username 创建者名字
	 * @param strategyName 策略名字
	 * @param commenterName 评价者名字
	 * @param time 评价时间
	 * @param comment 评价内容
	 */
	public void comment(String Username, String strategyName, String commenterName, LocalDateTime time, String comment){
		strategy.comment(Username, strategyName, commenterName, time, comment);
	}

	/**
	 * 获取策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public StrategyShowPO getStrategy ( String createrName, String StrategyName ){
		return strategy.getStrategy(createrName, StrategyName);
	}
	
	/**
	 * 添加策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void addStrategyShow ( String createrName, String StrategyName ,StrategyShowPO vo){
		strategy.addStrategyShow(createrName, StrategyName, vo);
	}
	public List<SingleStockInfoPO> getPeriodExponent(String name,LocalDate start,LocalDate end){
		return stock2.getPeriodExponent(name, start, end);
	}
	public StrategyInfoPO applyStrategy(String createrName,String strategyName){
		return strategy.applyStrategy(createrName, strategyName);
	}
	/**
	 * 清空策略详细图标
	 */
	public void clearStrategyShow (){
		strategy.clearStrategyShow();
	}
	public TimeSharingPO getTimeSharingData(String code,LocalDate date)throws TimeShraingLackException,NullStockIDException{
		return stock2.getTimeSharingData(code, date);
	}
	public List<CommentPO> getStrategyComments(String createrName,String strategyName){
		return strategy.getStrategyComments(createrName, strategyName);
	}
	public List<String> fuzzySearch(String code){
		return stock2.fuzzySearch(code);
	}

	/**
	 * 获取某个用户的所有策略
	 * @param createrName 用户名
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( String createrName){
		return strategy.getStrategyList(createrName);
	}

	/**
	 * 获取所有公开的策略
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( ){
		return strategy.getStrategyList();
	}

	/**
	 * 修改策略是否公开
	 * @param creatroName 创建者名字
	 * @param straetgyName 策略名字
	 * @param property 是否公开
	 */
	public void setPublic(String creatroName, String straetgyName,boolean property){
		strategy.setPublic(creatroName, straetgyName, property);
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
		if(userName==null){
			return stockset.getStockSetInfo(stockSetName);
		}
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
	/**
	 * 编号转名字
	 * @param code 股票编号
	 * @return 股票名字
	 */
	public String codeToname(String code){
		return Translate.getInstance().trans_codeToname(code);
	}
	/**
	 * 编号转名字
	 * @param code 股票编号
	 * @return 股票名字
	 */
	public String nameTocode(String name){
		try{
			Integer.parseInt(name);
			return Parse.getInstance().supCode(name);
		}catch(NumberFormatException e){
			return Translate.getInstance().trans_nameTocode(name);
		}
	}
	/**
	 * 获取行业板块中的各种分类
	 * @return 行业板块中的各种分类
	 */
	public List<String> getIndustryList(){
		return stockset.getIndustryList();
	}
	/**
	 * 获取概念板块中的各种分类
	 * @return 行业板块中的各种分类
	 */
	public List<String> getConceptList(){
		return stockset.getConceptList();
	}
	/**
	 * 获取地区板块中的各种分类
	 * @return 行业板块中的各种分类
	 */
	public List<String> getAreaList(){
		return stockset.getAreaList();
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
