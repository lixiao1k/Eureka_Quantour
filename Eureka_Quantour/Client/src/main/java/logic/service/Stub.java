package logic.service;

import java.util.ArrayList;
import java.util.List;

import vo.SingleStockInfoVO;

public class Stub {
	private List<String> list = new ArrayList<String>();
	public List<String> getStockSet(String username){
		list.add("股池1");
		list.add("股池2");
		return list;
	}
	private List<SingleStockInfoVO> list1 = new ArrayList<SingleStockInfoVO>();
	public List<SingleStockInfoVO> getStockSetSortedInfo(){
		SingleStockInfoVO vo1 = new SingleStockInfoVO();
		vo1.setCode("600500");
		vo1.setName("保变电气");
		vo1.setClose(8.560);
		vo1.setOpen(8.560);
		vo1.setFudu(0.780);
		vo1.setHigh(8.560);
		vo1.setLow(8.560);
		vo1.setVolume(45100);
		list1.add(vo1);
		SingleStockInfoVO vo2 = new SingleStockInfoVO();
		vo2.setCode("603330");
		vo2.setName("上海天洋");
		vo2.setClose(87.880);
		vo2.setOpen(94.000);
		vo2.setHigh(96.000);
		vo2.setLow(87.880);
		vo2.setFudu(-10.00);
		vo2.setVolume(69000);
		list1.add(vo2);
		return list1;
		
	}
}
