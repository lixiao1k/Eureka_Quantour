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
		SingleStockInfoVO vo3 = new SingleStockInfoVO();
		vo3.setCode("300011");
		vo3.setName("鼎汉技术");
		vo3.setClose(21.400);
		vo3.setOpen(19.470);
		vo3.setHigh(21.400);
		vo3.setLow(19.380);
		vo3.setFudu(10.03);
		vo3.setVolume(171600);
		list1.add(vo3);
		SingleStockInfoVO vo4 = new SingleStockInfoVO();
		vo4.setCode("002853");
		vo4.setName("皮阿诺");
		vo4.setClose(126.230);
		vo4.setOpen(126.230);
		vo4.setHigh(138.260);
		vo4.setLow(126.230);
		vo4.setFudu(-14.030);
		vo4.setVolume(69100);
		list1.add(vo4);
		return list1;
		
	}
	public List<SingleStockInfoVO> getSingleStockInfoList(){
		List<SingleStockInfoVO> list = new ArrayList<>();
		SingleStockInfoVO vo1 = new SingleStockInfoVO();
		vo1.setCode("600500");
		vo1.setName("保变电气");
		vo1.setClose(8.560);
		vo1.setOpen(8.560);
		vo1.setFudu(0.780);
		vo1.setHigh(8.560);
		vo1.setLow(8.560);
		vo1.setVolume(45100);
		list.add(vo1);
		SingleStockInfoVO vo2 = new SingleStockInfoVO();
		vo2.setCode("600500");
		vo2.setName("保变电气");
		vo2.setClose(8.60);
		vo2.setOpen(8.540);
		vo2.setFudu(0.740);
		vo2.setHigh(8.800);
		vo2.setLow(8.500);
		vo2.setVolume(43100);
		list.add(vo2);
		SingleStockInfoVO vo3 = new SingleStockInfoVO();
		vo3.setCode("600500");
		vo3.setName("保变电气");
		vo3.setClose(8.560);
		vo3.setOpen(8.560);
		vo3.setFudu(0.780);
		vo3.setHigh(8.560);
		vo3.setLow(8.560);
		vo3.setVolume(45100);
		list.add(vo3);
		SingleStockInfoVO vo4 = new SingleStockInfoVO();
		vo4.setCode("600500");
		vo4.setName("保变电气");
		vo4.setClose(8.560);
		vo4.setOpen(8.560);
		vo4.setFudu(0.780);
		vo4.setHigh(8.560);
		vo4.setLow(8.560);
		vo4.setVolume(45100);
		list.add(vo4);
		for (int i=0;i<10;i++){
			list.add(vo1);
		}
		return list;
	}
}
