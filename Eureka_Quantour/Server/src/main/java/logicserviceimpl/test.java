package logicserviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	private StockLogicImpl SLIm = new StockLogicImpl();

	public static void main(String args[]){
//		new test().testGetEMAInfo();
//		new test().testCalendarAdvance();
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
	
	private void testCalendarAdvance(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance(), tempCal = Calendar.getInstance();
		Date date = new Date(), tempDate = new Date();
		cal.set(2017, 2, 10); tempCal.set(2017, 2, 9);
		date = SLIm.calendarAdvance(cal).getTime(); tempDate = tempCal.getTime();
		System.out.println(format.format(date)+" "+ format.format(tempDate));
		
		cal.set(2017, 2, 1); tempCal.set(2017, 1, 28);
		date = SLIm.calendarAdvance(cal).getTime(); tempDate = tempCal.getTime();
		System.out.println(format.format(date)+" "+ format.format(tempDate));
		
		cal.set(2017, 0, 1); tempCal.set(2016, 11, 30);
		date = SLIm.calendarAdvance(cal).getTime(); tempDate = tempCal.getTime();
		System.out.println(format.format(date)+" "+ format.format(tempDate));
	}
	
	private void testGetEMAInfo(){
		List<EMAInfoVO> list1 = SLI.getEMAInfo("1", Calendar.getInstance(), Calendar.getInstance()).get(0);
		List<Double> list2 = new ArrayList<Double>();
		for(int i=0;i<list1.size();i++)
			list2.add(list1.get(i).getEMA());
		List<Double> list3 = new ArrayList<Double>();
		list3.add(11.16); list3.add(11.03); list3.add(11.25);
		list3.add(11.23); list3.add(11.19); 
		System.out.println(ifListEqual(list2, list3));
	}
}
