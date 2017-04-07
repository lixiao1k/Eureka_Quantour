package data.serviceimpl;

import java.util.Calendar;

import data.datahelperimpl.StockDataHelperImpl_2;
import data.datahelperservice.IStockDataHelper_2;
import data.parse.Parse;
import data.parse.Translate;
import exception.NoneStockIDException;
import exception.StockHaltingException;
import po.SingleStockInfoPO;
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
	public StockDataController_2 getInstance(){
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
			throws StockHaltingException, NoneStockIDException {
		int cal=date.get(Calendar.YEAR)*10000+date.get(Calendar.MONTH)*100+100+date.get(Calendar.DAY_OF_MONTH);
		int code;
		String result;
		try{
			code=Integer.parseInt(stockcode);
			translate.containsCode(parse.supCode(stockcode));
		}catch(NumberFormatException e1){
			try{
				code=Integer.parseInt(translate.trans_nameTocode(stockcode));
			}catch(NumberFormatException e2){
				throw new NoneStockIDException(stockcode);
			}
		}
		result=datahelper.getSingleInfo(cal, code);
		return null;
	}
}
