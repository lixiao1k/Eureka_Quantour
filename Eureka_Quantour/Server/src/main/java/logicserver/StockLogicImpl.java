package logicserver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vo.ComparedInfoVO;
import vo.EMAInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;
/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月8日
 */
public class StockLogicImpl implements StockLogicInterface{

	private StockLogicImplStub slis = new StockLogicImplStub();   
	
	@Override
	public List<SingleStockInfoVO> getSingleStockInfoByTime(String stockCode, Calendar begin, Calendar end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EMAInfoVO> getEMAInfo(String stockCode, Calendar begin, Calendar end, int method) {
		// TODO Auto-generated method stub
		// invoke stub to get data
		List<SingleStockInfoVO> lsti1 = slis.getSingleStockInfo(stockCode, begin, end, method);
		List<EMAInfoVO> lemai = new ArrayList<EMAInfoVO>();
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		if(lsti1.size()<method){
			for(int i=0;i<lsti1.size();i++){
				ssi = lsti1.get(i);
				lemai.add( new EMAInfoVO(ssi.getDate(), ssi.getClose()) );
			}
		}
		else{
			double tempDouble = 0.0;
			for(int i=0;i<method-1;i++){
				ssi = lsti1.get(i);
				lemai.add( new EMAInfoVO(ssi.getDate(), ssi.getClose()) );
				tempDouble += lemai.get(i).getEMA();
			}
			for(int i=method-1;i<lsti1.size();i++){
				ssi = lsti1.get(i);
				tempDouble += ssi.getClose();
				lemai.add( new EMAInfoVO(ssi.getDate(), formatDouble(tempDouble/method)) );
				tempDouble -= lemai.get(i-method+1).getEMA();
			}
		}
		return lemai;
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

	private double formatDouble(double d){
		DecimalFormat df = new DecimalFormat("#0.00");
		return Double.parseDouble(df.format(d));
	}
}
