package data.serviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import data.datahelperimpl.StockDataHelperImp;
import data.datahelperservice.IStockDataHelper;
import po.SingleStockInfoPO;

/**
 * 股票模块方法的实现
 * @author 刘宇翔
 *
 */
public class StockDataController {
	private IStockDataHelper stockdatahelper;
	private static StockDataController stockdata;
	private HashMap<String,HashMap<String,String>> stockinfo_StringType;
	private StockDataController(){
		stockdatahelper=StockDataHelperImp.getInstance();
		stockinfo_StringType=stockdatahelper.getAllStock();
	}
	public static StockDataController getInstance(){
		if(stockdata==null) stockdata=new StockDataController();
		return stockdata;
	}
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode, Calendar begin, Calendar end) {
		ArrayList<String> in=stockdatahelper.find(stockinfo_StringType, stockcode, begin, end);
		List<SingleStockInfoPO> result=new ArrayList<SingleStockInfoPO>();
		for(String i:in){
			result.add(new SingleStockInfoPO(i));
		}
		return result;
	}
	public List<SingleStockInfoPO> getMarketByDate(Calendar date) {
		HashMap<String,String> datamap=stockinfo_StringType.get(date);
		Iterator<Entry<String, String>> it=datamap.entrySet().iterator();
		ArrayList<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		while(it.hasNext()){
			String info=it.next().getValue();
			list.add(new SingleStockInfoPO(info));
		}
		return list;
	}
	
}
