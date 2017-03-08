package logicserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vo.SingleStockInfoVO;

public class StockLogicImplStub {
	
	protected List<SingleStockInfoVO> getSingleStockInfo(String stockCode, Calendar begin, Calendar end, int method){
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
}
