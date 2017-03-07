package logicserver;

import java.util.Calendar;
import java.util.Iterator;

import vo.ComparedInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;

public class StockLogicImpl implements StockLogicInterface{

	@Override
	public Iterator<SingleStockInfoVO> getSingleStockInfoByTime(String stockCode, Calendar begin, Calendar end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Double> getEMAInfo(String stockCode, Calendar begin, Calendar end, int method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComparedInfoVO getComparedInfo(String stockCodeA, String stockCodeB, Calendar begin, Calendar end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketInfoVO getMarketInfo(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

}
