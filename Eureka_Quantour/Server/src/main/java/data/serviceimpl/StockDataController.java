package data.serviceimpl;

import java.text.SimpleDateFormat;
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
	private SimpleDateFormat sdf;
	private StockDataController(){
		stockdatahelper=StockDataHelperImp.getInstance();
		stockinfo_StringType=stockdatahelper.getAllStock();
		sdf=new SimpleDateFormat("MM/dd/yy");
	}
	public static StockDataController getInstance(){
		if(stockdata==null) stockdata=new StockDataController();
		return stockdata;
	}
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode, Calendar begin, Calendar end) {
		Calendar i=begin;
		List<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		Boolean hascalendar=false;
		Boolean hascode=false;
		while(i.compareTo(end)<=0){
			hascalendar=stockinfo_StringType.containsKey(tostring(sdf.format(i.getTime())));
			if(hascalendar){
				hascode=stockinfo_StringType.get(tostring(sdf.format(i.getTime()))).containsKey(stockcode);
				if(hascode){
					list.add(new SingleStockInfoPO
							(stockinfo_StringType.get(
									tostring(sdf.format(i.getTime()))).get(stockcode)));
				}
			}
			i.set(Calendar.DATE, i.get(Calendar.DATE)+1);
		}
		return list;
	}
	private String tostring(String str){
		String[] out=str.split("/");
		if(out[0].length()==2&&out[0].charAt(0)=='0'){
			out[0]=out[0].substring(1);
		}
		if(out[1].length()==2&&out[1].charAt(0)=='0'){
			out[1]=out[1].substring(1);
		}
		return out[0]+"/"+out[1]+"/"+out[2];
	}
	public List<SingleStockInfoPO> getMarketByDate(Calendar date) {
		String string_date=tostring(sdf.format(date.getTime()));
		if(!stockinfo_StringType.containsKey(string_date)){
			return null;
		}
		HashMap<String,String> datamap=stockinfo_StringType.get(string_date);
		Iterator<Entry<String, String>> it=datamap.entrySet().iterator();
		ArrayList<SingleStockInfoPO> list=new ArrayList<SingleStockInfoPO>();
		while(it.hasNext()){
			String info=it.next().getValue();
			list.add(new SingleStockInfoPO(info));
		}
		return list;
	}
	
}
