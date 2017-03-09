package logicserviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vo.SingleStockInfoVO;
/**
 * 
 * @Description: for logic as stub to get data
 * @author: hzp
 * @time: 2017年3月7日
 */
public class StockLogicImplStub {
	
	protected List<SingleStockInfoVO> getSingleStockInfo(String stockCode, Calendar begin, Calendar end, int method){
		SingleStockInfoVOs.init();
		List<SingleStockInfoVO> lssi = new ArrayList<SingleStockInfoVO>();
		if(stockCode.equals("1")){
			lssi.add(SingleStockInfoVOs.ssi1);
			lssi.add(SingleStockInfoVOs.ssi2);
			lssi.add(SingleStockInfoVOs.ssi3);
			lssi.add(SingleStockInfoVOs.ssi4);
			lssi.add(SingleStockInfoVOs.ssi5);
		}
		else if(stockCode.equals("151")){
			lssi.add(SingleStockInfoVOs.ssi6);
			lssi.add(SingleStockInfoVOs.ssi7);
			lssi.add(SingleStockInfoVOs.ssi8);
			lssi.add(SingleStockInfoVOs.ssi9);
			lssi.add(SingleStockInfoVOs.ssi10);
		}
		return lssi;
	}
	
	protected List<SingleStockInfoVO> getSingleStockInfo(String stockCode, Calendar begin, Calendar end){
		SingleStockInfoVOs.init();
		List<SingleStockInfoVO> lssi = new ArrayList<SingleStockInfoVO>();
		if(stockCode.equals("1")){
			lssi.add(SingleStockInfoVOs.ssi1);
			lssi.add(SingleStockInfoVOs.ssi2);
			lssi.add(SingleStockInfoVOs.ssi3);
			lssi.add(SingleStockInfoVOs.ssi4);
			lssi.add(SingleStockInfoVOs.ssi5);
		}
		else if(stockCode.equals("151")){
			lssi.add(SingleStockInfoVOs.ssi6);
			lssi.add(SingleStockInfoVOs.ssi7);
			lssi.add(SingleStockInfoVOs.ssi8);
			lssi.add(SingleStockInfoVOs.ssi9);
			lssi.add(SingleStockInfoVOs.ssi10);
		}
		return lssi;
	}
	
	protected List<SingleStockInfoVO> getMarketInfo( Calendar date){
		SingleStockInfoVOs.init();
		List<SingleStockInfoVO> lssi = new ArrayList<SingleStockInfoVO>();
		return lssi;
	}
}
