package logicserviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import logicservice.StockLogicInterface;
import vo.EMAInfoVO;
/**
 * 
 * @Description: 用于辅助测试，并无真实用处
 * @author: hzp
 * @time: 2017年3月8日
 */
public class test {
	
	private static StockLogicInterface SLI = new StockLogicImpl();

	public static void main(String args[]){
		List<EMAInfoVO> list1 = SLI.getEMAInfo("1", Calendar.getInstance(), Calendar.getInstance()).get(0);
		List<Double> list2 = new ArrayList<Double>();
		for(int i=0;i<list1.size();i++)
			list2.add(list1.get(i).getEMA());
		List<Double> list3 = new ArrayList<Double>();
		list3.add(11.16); list3.add(11.03); list3.add(11.25);
		list3.add(11.23); list3.add(11.19); 
		System.out.println(ifListEqual(list2, list3));
	}
	
	private static boolean ifListEqual(List<Double> list1, List<Double> list2){
		if(list1.size()!=list2.size())
			return false;
		else{
			for(int i=0;i<list1.size();i++){
				if(!String.valueOf(list1.get(i)).equals(String.valueOf(list2.get(i))))
					return false;
			}
		}
		return true;
	}
}
