package data.serviceimpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import data.datahelperimpl.StockDataHelperImpl_2;
import data.datahelperimpl_ByDataBase.ExponentDataHelperImpl_DBO;
import data.datahelperservice.IExponentDataHelper;
import data.datahelperservice.IStockDataHelper_2;
import data.parse.Parse;
import data.parse.Translate;
import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.StockHaltingException;
import exception.TimeShraingLackException;
import po.SingleStockInfoPO;
/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public class StockDataController_2 {
	private static StockDataController_2 stockdatacontroller;
	private IStockDataHelper_2 datahelper;
	private IExponentDataHelper exphelper;
	private Translate translate;
	private Parse parse;
	private StockDataController_2(){
		datahelper=new StockDataHelperImpl_2();
		exphelper=ExponentDataHelperImpl_DBO.getInstance();
		translate=Translate.getInstance();
		parse=Parse.getInstance();
	}
	public static StockDataController_2 getInstance(){
		if(stockdatacontroller==null) stockdatacontroller=new StockDataController_2();
		return stockdatacontroller;
	}
	public void reload(){
		datahelper=null;
		datahelper=new StockDataHelperImpl_2();
	}
//	/**
//	 * 判断是否是交易日
//	 * @param day 需要判断的日期
//	 * @return	是交易日则返回true，否则返回false
//	 */
//	public boolean isMarketDay(Calendar day){
//		int cal=day.get(Calendar.YEAR)*10000+day.get(Calendar.MONTH)*100+100+day.get(Calendar.DAY_OF_MONTH);
//		return datahelper.isMarketDay(cal);
//	}
//	/**
//	 * 获取某一天起之后last个交易日之后的天数
//	 * @param date 起始日期
//	 * @param last 推移的天数
//	 * @return 交易日日期
//	 * @throws DateOverException
//	 */
//	public LocalDate addDays(LocalDate date,int last) throws DateOverException{
//		int cal=parse.getIntDate(date);
//		DateLeaf start;
//		try {
//			start=datahelper.remain_forAllinfo(cal);
//		} catch (NullDateException e) {
//			LocalDate temp=LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
//			temp=temp.plusDays(1);
//			if(temp.isAfter(LocalDate.now())){
//				throw new DateOverException(cal,last);
//			}
//			return addDays(date,last);
//		}
//		while(last!=0){
//			if(last>0){
//				try {
//					start=datahelper.nextDay_forAllinfo(start);
//				} catch (NullDateException e) {
//					throw new DateOverException(cal,last);
//				}
//				last--;
//			}
//			else{
//				try {
//					start=datahelper.previousDay_forAllinfo(start);
//				} catch (NullDateException e) {
//					throw new DateOverException(cal,last);
//				}
//				last++;
//			}
//		}
//		return parse.getlocalDate(start.getCal());
//	}
	public List<SingleStockInfoPO> getPeriodExponent(String name,LocalDate start,LocalDate end){
		return exphelper.getPeriodExponent(name, start, end);
	}
	public List<Double> getTimeSharingData(String code,LocalDate date)throws TimeShraingLackException,NullStockIDException{
		int c=transStockCode(code);
		int day=Parse.getInstance().getIntDate(date);
		return datahelper.getTimeSharingData(code, day);
	}
	public void addBrowseTimes(String stockCode) throws NullStockIDException{
		int i=transStockCode(stockCode);
		datahelper.addBrowseTimes(Parse.getInstance().supCode(i));
	}
	public List<String> fuzzySearch(String code){
		return datahelper.fuzzySearch(code);
	}
	/**
	 * 获取某只股票最早的一天
	 * @param code 股票编号
	 * @return 日期
	 * @throws NullStockIDException 没有该股票时抛出异常
	 */
	public LocalDate getMinDay(String stockCode) throws NullStockIDException{
		transStockCode(stockCode);
		try {
			return datahelper.getMinDay(parse.supCode(stockCode));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取某只股票最晚的一天
	 * @param code 股票编号
	 * @return 日期
	 * @throws NullStockIDException 没有该股票时抛出该异常
	 */
	public LocalDate getMaxDay(String stockCode) throws NullStockIDException{
		transStockCode(stockCode);
		try {
			return datahelper.getMaxDay(parse.supCode(stockCode));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取股票某一天的数据
	 * @param stockcode 股票编号
	 * @param date 日期
	 * @return 股票信息
	 * @throws StockHaltingException 股票停牌时抛出该异常(即找不到该股票当天的信息)
	 * @throws NullDateException 该日期不存在时抛出该异常
	 * @throws NoneStockIDException 搜索不到该股票时抛出该异常()
	 */
	public SingleStockInfoPO getSingleStockInfo(String stockcode, LocalDate date) 
			throws NullStockIDException, NullDateException {
		int cal=parse.getIntDate(date);
		String result;
		String strCode;
		String name;
		try{
			Integer.parseInt(stockcode);
			strCode=parse.supCode(stockcode);
			name=translate.trans_codeToname(strCode);
			if(name==null){
				throw new NullStockIDException(stockcode);
			}
		}catch(NumberFormatException e1){
			try{
				strCode=translate.trans_nameTocode(stockcode);
				Integer.parseInt(strCode);
				name=stockcode;
			}catch(NumberFormatException e2){
				throw new NullStockIDException(stockcode);
			}
		}
		try {
			result=datahelper.getSingleInfo(cal, strCode);
		} catch (StockHaltingException e) {
			throw new NullDateException(cal);
		}
		return new SingleStockInfoPO(result,name,strCode,date);
	}
	/**
	 * 获取某一天起之后last个交易日之后的天数
	 * @param date 起始日期
	 * @param last 推移的天数
	 * @return 交易日日期
	 * @throws DateOverException
	 */
	public LocalDate addDays(LocalDate date,int last,boolean zheng) throws DateOverException{
		int cal=parse.getIntDate(date);
		try {
			return datahelper.addDays(cal, last);
		} catch (NullDateException e) {
			if(zheng){
				LocalDate temp=LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				temp=temp.plusDays(1);
				if(temp.isAfter(LocalDate.now())){
					throw new DateOverException(cal,last);
				}
				return addDays(temp,last,zheng);
			}
			else{
				LocalDate temp=LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				temp=temp.minusDays(1);
				if(temp.isBefore(LocalDate.of(2005, 2, 1))){
					throw new DateOverException(cal,last);
				}
				return addDays(temp,last,zheng);
			}
		}
	}
	private int transStockCode(String stockCode) throws NullStockIDException{
		int code;
		String strCode;
		try{
			code=Integer.parseInt(stockCode);
			if(!translate.containsCode(parse.supCode(stockCode))){
				throw new NullStockIDException(stockCode);
			}
			else{
				return code;
			}
		}catch(NumberFormatException e1){
			try{
				strCode=translate.trans_nameTocode(stockCode);
				code=Integer.parseInt(strCode);
				return code;
			}catch(NumberFormatException e2){
				throw new NullStockIDException(stockCode);
			}
		}
	}
	public LocalDate getExponentMaxDay(String name)
	{
		return exphelper.getExponentMaxDay(name);
	}
	public LocalDate getExponentMinDay(String name)
	{
		return exphelper.getExponentMinDay(name);
	}
//	
//	/**
//	 * 获取某个股票池的股票的某天信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @return StockSetInfoPO 股票池信息的po
//	 * @throws NullDateException 不存在该日期时抛出该异常
//	 */
//	public StockSetInfoPO getStockInfoinSet(List<String> set,Calendar date) throws NullDateException{
//		int cal=date.get(Calendar.YEAR)*10000+date.get(Calendar.MONTH)*100+100+date.get(Calendar.DAY_OF_MONTH);
//		DateLeaf leaf=datahelper.remain_forAllinfo(cal);
//		int count=0;
//		int size=set.size();
//		String info;
//		int code;
//		LocalDate local=parse.getlocalDate(cal);
//		StockSetInfoPO setinfo=new StockSetInfoPO(local);
//		long t1=0;
//		long t2=0;
//		long tota=0;
//		for(String strCode:set){
//			code=parse.strToint(strCode);
//			try {
//				t1=System.currentTimeMillis();
//				info=datahelper.getStockInfoinSet_throughRemain(code,leaf);
//				t2=System.currentTimeMillis();
//				setinfo.addStockInfo(info, strCode, translate.trans_codeToname(strCode));
//			} catch (StockHaltingException e) {
//				count++;
//				setinfo.addHalt(strCode,translate.trans_codeToname(strCode));
//			}
//			tota=tota+t2-t1;
//		}
//		if(count==size){
//			setinfo.allhalt();
//		}
//		System.out.println(tota);
//		return setinfo;
//	}
//
//	/**
//	 * 获取某个股票池的股票的某天即往后x天的信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param last 往后推的时间（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的po的列表
//	 * @throws NullDateException 起始日期不是交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByLast(List<String> set,Calendar date,int last) throws NullDateException{
//		int cal=date.get(Calendar.YEAR)*10000+date.get(Calendar.MONTH)*100+100+date.get(Calendar.DAY_OF_MONTH);
//		DateLeaf leaf=datahelper.remain_forAllinfo(cal);
//		List<Integer> setlist=new ArrayList<Integer>();
//		List<String> namelist=new ArrayList<String>();
//		List<StockSetInfoPO> result=new ArrayList<StockSetInfoPO>();
//		int code;
//		String info;
//		String name;
//		LocalDate local=parse.getlocalDate(parse.getIntDate(date));
//		String strCode;
//		StockSetInfoPO setinfo=new StockSetInfoPO(local);
//		int count=0;
//		for(int i=0;i<set.size();i++){
//			strCode=set.get(i);
//			code=parse.strToint(strCode);
//			name=translate.trans_codeToname(strCode);
//			setlist.add(code);
//			namelist.add(name);
//			try {
//				info=datahelper.getStockInfoinSet_throughRemain(code,leaf);
//				setinfo.addStockInfo(info, strCode, name);
//			} catch (StockHaltingException e) {
//				count++;
//				setinfo.addHalt(strCode,name);
//			}
//		}
//		if(count==set.size()){
//			setinfo.allhalt();
//		}
//		result.add(setinfo);
//		last--;
//		while(last>=0){
//			count=0;
//			int tempcal;
//			try{
//				leaf=datahelper.nextDay_forAllinfo(leaf);
//				tempcal=leaf.getCal();
//			}catch(NullDateException e){
//				break;
//			}
//			Calendar calendar=Calendar.getInstance();
//			try {
//				calendar.setTime(sdf.parse(String.valueOf(tempcal)));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			LocalDate localtemp=parse.getlocalDate(tempcal);
//			StockSetInfoPO tempset=new StockSetInfoPO(localtemp);
//			for(int i=0;i<set.size();i++){
//				try {
//					tempset.addStockInfo(datahelper.getStockInfoinSet_throughRemain(setlist.get(i),leaf), set.get(i), namelist.get(i));
//				} catch (StockHaltingException e) {
//					count++;
//					tempset.addHalt(set.get(i),namelist.get(i));
//				}
//			}
//			if(count==set.size()){
//				tempset.allhalt();
//			}
//			result.add(tempset);
//			last--;
//		}
//		return result;
//	}
//	
//	/**
//	 * 获取某个软件自带的股票池的股票的起始于终止日期之间的所有信息
//	 * @param set 股票池名称
//	 * @param startDate 起始日期
//	 * @param endDate 终止日期
//	 * @return List<StockSetInfoPO> 股票池信息的po的列表
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_StopByEnd(List<String> set,Calendar startDate,Calendar endDate){
//		int cal=parse.getIntDate(startDate);
//		int end=parse.getIntDate(endDate);
//		DateLeaf leaf;
//		try {
//			leaf=datahelper.remain_forAllinfo(cal);
//		} catch (NullDateException e) {
//			startDate.set(Calendar.DATE, startDate.get(Calendar.DATE)+1);
//			cal=parse.getIntDate(startDate);
//			if(cal>end){
//				return null;
//			}
//			else{
//				return getStockInfoinSet_StopByEnd(set,startDate,endDate);
//			}
//		}
//		List<Integer> setlist=new ArrayList<Integer>();
//		List<String> namelist=new ArrayList<String>();
//		List<StockSetInfoPO> result=new ArrayList<StockSetInfoPO>();
//		int code;
//		String info;
//		String name;
//		String strCode;
//		LocalDate startlocal=parse.getlocalDate(parse.getIntDate(startDate));
//		StockSetInfoPO setinfo=new StockSetInfoPO(startlocal);
//		int count=0;
//		for(int i=0;i<set.size();i++){
//			strCode=set.get(i);
//			code=parse.strToint(strCode);
//			name=translate.trans_codeToname(strCode);
//			setlist.add(code);
//			namelist.add(name);
//			try {
//				info=datahelper.getStockInfoinSet_throughRemain(code,leaf);
//				setinfo.addStockInfo(info, strCode, name);
//			} catch (StockHaltingException e) {
//				count++;
//				setinfo.addHalt(strCode,name);
//			}
//		}
//		if(count==set.size()){
//			setinfo.allhalt();
//		}
//		result.add(setinfo);
//		while(true){
//			count=0;
//			int nextday;
//			try{
//				leaf=datahelper.nextDay_forAllinfo(leaf);
//				nextday=leaf.getCal();
//			}catch(NullDateException e){
//				break;
//			}
//			if(nextday>end){
//				break;
//			}
//			Calendar calendar=Calendar.getInstance();
//			try {
//				calendar.setTime(sdf.parse(String.valueOf(nextday)));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			LocalDate tempcal=parse.getlocalDate(nextday);
//			StockSetInfoPO tempset=new StockSetInfoPO(tempcal);
//			for(int i=0;i<set.size();i++){
//				try {
//					tempset.addStockInfo(datahelper.getStockInfoinSet_throughRemain(setlist.get(i),leaf), set.get(i), namelist.get(i));
//				} catch (StockHaltingException e) {
//					count++;
//					tempset.addHalt(set.get(i),namelist.get(i));
//				}
//			}
//			if(count==set.size()){
//				tempset.allhalt();
//			}
//			result.add(tempset);
//		}
//		return result;
//	}
//	
//	/**
//	 * 获取某个股票池的某天与往前x天的信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @param userName 用户名
//	 * @param last 往前推的交易日数（至少为0天）
//	 * @return List<StockSetInfoPO> 股票池信息的列表
//	 * @throws NullDateException 起始日期非交易日
//	 */
//	public List<StockSetInfoPO> getStockInfoinSet_forwardByLast(List<String> set,Calendar date,int last) throws NullDateException{
//		int cal=date.get(Calendar.YEAR)*10000+date.get(Calendar.MONTH)*100+100+date.get(Calendar.DAY_OF_MONTH);
//		DateLeaf leaf=datahelper.remain_forAllinfo(cal);
//		List<Integer> setlist=new ArrayList<Integer>();
//		List<String> namelist=new ArrayList<String>();
//		List<StockSetInfoPO> result=new ArrayList<StockSetInfoPO>();
//		int code;
//		String info;
//		String name;
//		String strCode;
//		LocalDate localdate=parse.getlocalDate(parse.getIntDate(date));
//		StockSetInfoPO setinfo=new StockSetInfoPO(localdate);
//		int count=0;
//		for(int i=0;i<set.size();i++){
//			strCode=set.get(i);
//			code=parse.strToint(strCode);
//			name=translate.trans_codeToname(strCode);
//			setlist.add(code);
//			namelist.add(name);
//			try {
//				info=datahelper.getStockInfoinSet_throughRemain(code,leaf);
//				setinfo.addStockInfo(info, strCode, name);
//			} catch (StockHaltingException e) {
//				count++;
//				setinfo.addHalt(strCode,name);
//			}
//		}
//		if(count==set.size()){
//			setinfo.allhalt();
//		}
//		result.add(setinfo);
//		last--;
//		while(last>=0){
//			int tempcal;
//			try{
//				leaf=datahelper.previousDay_forAllinfo(leaf);
//				tempcal=leaf.getCal();
//			}catch(NullDateException e){
//				break;
//			}
//			Calendar calendar=Calendar.getInstance();
//			try {
//				calendar.setTime(sdf.parse(String.valueOf(tempcal)));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			LocalDate temp=parse.getlocalDate(cal);
//			StockSetInfoPO tempset=new StockSetInfoPO(temp);
//			for(int i=0;i<set.size();i++){
//				try {
//					tempset.addStockInfo(datahelper.getStockInfoinSet_throughRemain(setlist.get(i),leaf), set.get(i), namelist.get(i));
//				} catch (StockHaltingException e) {
//					count++;
//					tempset.addHalt(set.get(i),namelist.get(i));
//				}
//			}
//			if(count==set.size()){
//				tempset.allhalt();
//			}
//			result.add(tempset);
//			last--;
//		}
//		return result;
//	}
//	/**
//	 * 获取一支股票从起点时间（交易日）往后推x个交易日的全部数据（x>=0）
//	 * @param stockcode String,股票编号
//	 * @param begin Calendar,起始时间
//	 * @param last int,长度
//	 * @return 一个股票信息的对象的列表
//	 * @throws NullStockIDException 该股票不存在时抛出该异常
//	 */
//	public List<SingleStockInfoPO> getSingleStockInfo_byLast(String stockcode,Calendar begin,int last) throws NullStockIDException{
//		int cal=parse.getIntDate(begin);
//		int code=transStockCode(stockcode);
//		StockLeaf leaf=null;
//		try {
//			leaf=datahelper.remain_forSingleinfo(code, cal);
//		} catch (NullDateException e) {
//			System.out.println(e.toString());
//			e.printStackTrace();
//			return null;
//		}
//		String name=translate.trans_codeToname(parse.supCode(String.valueOf(code)));
//		String strCode=parse.supCode(String.valueOf(code));
//		List<SingleStockInfoPO> result=new ArrayList<SingleStockInfoPO>();
//		while(last>=0){
//			Calendar temp=Calendar.getInstance();
//			try {
//				temp.setTime(sdf.parse(String.valueOf(cal)));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			LocalDate tempcal=parse.getlocalDate(cal);
//			String info=datahelper.getSingleInfo_throughRemain(leaf);
//			SingleStockInfoPO po=new SingleStockInfoPO(info,name,strCode,tempcal);
//			result.add(po);
//			try {
//				leaf=datahelper.nextDay_forSingleinfo(leaf);
//				cal=((DateLeaf)leaf.getParent()).getCal();
//			} catch (NullDateException e) {
//				break;
//			}
//			last--;
//		}
//		return result;
//	}
//	/**
//	 * 获取单支股票一段时间的数据
//	 * @param stockcode
//	 * @param begin
//	 * @param end
//	 * @return
//	 * @throws NullStockIDException 不存在改股票时抛出异常
//	 */
//	public List<SingleStockInfoPO> getSingleStockInfo_ByEnd(String stockcode, Calendar begin, Calendar end) throws NullStockIDException {
//		int cal=parse.getIntDate(begin);
//		int endcal=parse.getIntDate(end);
//		int code=transStockCode(stockcode);
//		StockLeaf leaf;
//		try {
//			leaf=datahelper.remain_forSingleinfo(code, cal);
//		} catch (NullDateException e) {
//			begin.set(Calendar.DATE, begin.get(Calendar.DATE)+1);
//			cal=parse.getIntDate(begin);
//			if(cal<=endcal){
//				return getSingleStockInfo_ByEnd(stockcode,begin,end);
//			}
//			else{
//				return null;
//			}
//		}
//		String name=translate.trans_codeToname(parse.supCode(String.valueOf(code)));
//		String strCode=parse.supCode(String.valueOf(code));
//		List<SingleStockInfoPO> result=new ArrayList<SingleStockInfoPO>();
//		while(cal<=endcal){
//			Calendar temp=Calendar.getInstance();
//			try {
//				temp.setTime(sdf.parse(String.valueOf(cal)));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			String info=datahelper.getSingleInfo_throughRemain(leaf);
//			LocalDate tempcal=parse.getlocalDate(cal);
//			SingleStockInfoPO po=new SingleStockInfoPO(info,name,strCode,tempcal);
//			result.add(po);
//			try {
//				leaf=datahelper.nextDay_forSingleinfo(leaf);
//				cal=((DateLeaf)leaf.getParent()).getCal();
//			} catch (NullDateException e) {
//				break;
//			}
//		}
//		return result;
//	}
}
