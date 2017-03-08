package logicserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vo.ComparedInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;

public class StockLogicImpl implements StockLogicInterface{

	private StockLogicImplStub slis = new StockLogicImplStub();
	
	@Override
	public List<SingleStockInfoVO> getSingleStockInfoByTime(String stockCode, Calendar begin, Calendar end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Double> getEMAInfo(String stockCode, Calendar begin, Calendar end, int method) {
		// TODO Auto-generated method stub
		List<SingleStockInfoVO> lsti1 = slis.getSingleStockInfo(stockCode, begin, end, method);
		List<Double> ldb = new ArrayList<Double>();
		if(lsti1.size()<method){
			for(int i=0;i<lsti1.size();i++){
				ldb.add(lsti1.get(i).getClose());
			}
		}
		else{
			double tempDouble = 0.0;
			for(int i=0;i<method-1;i++){
				ldb.add(lsti1.get(i).getClose());
				tempDouble += ldb.get(i);
			}
			for(int i=method-1;i<lsti1.size();i++){
				tempDouble += lsti1.get(i).getClose();
				ldb.add(tempDouble/method);
				tempDouble -= ldb.get(i-method+1);
			}
		}
		return ldb;
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
