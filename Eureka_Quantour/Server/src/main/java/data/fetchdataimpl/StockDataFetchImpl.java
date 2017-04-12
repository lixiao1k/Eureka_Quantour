package data.fetchdataimpl;

import data.fetchdataservice.IStockDataFetch;
import exception.InternetdisconnectException;

public class StockDataFetchImpl implements IStockDataFetch{
	private StockSetFetchByWeb setByweb;
	private StockInfoFetchByWeb infoByweb;
	private StockInfoCalculate infocalculate;
	private static StockDataFetchImpl impl;
	private StockDataFetchImpl(){
		setByweb=new StockSetFetchByWeb();
		infoByweb=new StockInfoFetchByWeb();
		infocalculate=new StockInfoCalculate();
	}
	public static StockDataFetchImpl getInstance(){
		if(impl==null) impl=new StockDataFetchImpl();
		return impl;
	}

	@Override
	public void getArtificialSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchAllStockSet() throws InternetdisconnectException {
		setByweb.getAllStockName();
		setByweb.getHS300List();
		setByweb.getZXBList();
	}
	@Override
	public void fetchAllStockInfo() throws InternetdisconnectException {
		System.out.println("开始爬取股票信息");
		infoByweb.fetchAllStockInfo();
		System.out.println("爬取股票信息完成");
		System.out.println("开始爬取股票复权信息");
		infoByweb.fetchAllsubscription();
		System.out.println("爬取股票复权信息完成");
		System.out.println("开始计算均线");
		infocalculate.processAverage();
		System.out.println("计算均线完成");
		System.out.println("开始汇总股票信息");
		infoByweb.indexationAllDate(true);
		System.out.println("汇总股票信息完成");
	}

}