package data.serviceimpl;

import java.util.Calendar;
import java.util.List;

import data.datahelperimpl.StockDataHelperImpl_2;
import data.datahelperservice.IStockDataHelper_2;
import data.parse.Parse;
import data.parse.Translate;
import exception.NullSetInfoException;
import exception.NullStockIDException;
import exception.StockHaltingException;
import po.SingleStockInfoPO;
import po.StockSetInfoPO;
/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public class StockDataController_2 {
	private static StockDataController_2 stockdatacontroller;
	private IStockDataHelper_2 datahelper;
	private Translate translate;
	private Parse parse;
	private StockDataController_2(){
		datahelper=new StockDataHelperImpl_2();
		translate=Translate.getInstance();
		parse=Parse.getInstance();
	}
	public static StockDataController_2 getInstance(){
		if(stockdatacontroller==null) stockdatacontroller=new StockDataController_2();
		return stockdatacontroller;
	}
	/**
	 * 获取股票某一天的数据
	 * @param stockcode 股票编号
	 * @param date 日期
	 * @return 股票信息
	 * @throws StockHaltingException 股票停牌时抛出该异常(即找不到该股票当天的信息)
	 * @throws NoneStockIDException 搜索不到该股票时抛出该异常()
	 */
	public SingleStockInfoPO getSingleStockInfo(String stockcode, Calendar date) 
			throws StockHaltingException, NullStockIDException {
		int cal=date.get(Calendar.YEAR)*10000+date.get(Calendar.MONTH)*100+100+date.get(Calendar.DAY_OF_MONTH);
		int code;
		String result;
		String strCode;
		String name;
		try{
			code=Integer.parseInt(stockcode);
			strCode=parse.supCode(stockcode);
			name=translate.trans_codeToname(strCode);
			if(name==null){
				throw new NullStockIDException(stockcode);
			}
		}catch(NumberFormatException e1){
			try{
				strCode=translate.trans_nameTocode(stockcode);
				code=Integer.parseInt(strCode);
				name=stockcode;
			}catch(NumberFormatException e2){
				throw new NullStockIDException(stockcode);
			}
		}
		result=datahelper.getSingleInfo(cal, code);
		return new SingleStockInfoPO(result,name,strCode,date);
	}
	
	/**
	 * 获取某个软件自带的股票池的股票的某天信息
	 * @param set 股票池名称
	 * @param date 日期
	 * @return StockSetInfoPO 股票池信息的po
	 */
	public StockSetInfoPO getStockInfoinSet(List<String> set,Calendar date,String setname){
		int cal=date.get(Calendar.YEAR)*10000+date.get(Calendar.MONTH)*100+100+date.get(Calendar.DAY_OF_MONTH);
		datahelper.remain(cal);
		int count=0;
		int size=set.size();
		String info;
		int code;
		StockSetInfoPO setinfo=new StockSetInfoPO(date);
		for(String strCode:set){
			code=parse.strToint(strCode);
			try {
				info=datahelper.getStockInfoinSet_throughRemain(code);
				setinfo.addStockInfo(info, strCode, translate.trans_codeToname(strCode));
			} catch (StockHaltingException e) {
				count++;
				setinfo.addHalt(strCode,translate.trans_codeToname(strCode));
			}
		}
		return setinfo;
	}
}
