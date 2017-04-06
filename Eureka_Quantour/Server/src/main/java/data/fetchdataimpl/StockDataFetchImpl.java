package data.fetchdataimpl;

import data.fetchdataservice.IStockDataFetch;
import exception.InternetdisconnectException;

public class StockDataFetchImpl implements IStockDataFetch{
	private StockSetFetchByWeb setByweb;
	private StockInfoFetchByWeb infoByweb;
	private static StockDataFetchImpl impl;
	private StockDataFetchImpl(){
		setByweb=new StockSetFetchByWeb();
		infoByweb=new StockInfoFetchByWeb();
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
		infoByweb.fetchAllStockInfo();
		infoByweb.fetchAllsubscription();
	}

}
