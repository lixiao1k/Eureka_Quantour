package logic.serviceimpl;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import logic.service.StockLogicInterface;
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
		try{
			new test().testGetEMAInfo();
//			new test().testCalendarAdvance();
//			System.out.println( Math.log(10.69/11.16) );
		}catch(Exception e){
			e.printStackTrace();
		}
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
	
	private void testGetEMAInfo() throws RemoteException{
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		begin.set(2014, 3, 16);
		end.set(2014, 3, 29);
		List<EMAInfoVO> listA = new ArrayList<EMAInfoVO>();
		List<EMAInfoVO> listB = new ArrayList<EMAInfoVO>();
		List<EMAInfoVO> listC = new ArrayList<EMAInfoVO>();
		try{
		listA = SLI.getEMAInfo("1", begin, end).get(0);
		listB = SLI.getEMAInfo("1", begin, end).get(1);
		listC = SLI.getEMAInfo("1", begin, end).get(2);
		}catch(Exception e){
			e.printStackTrace();
		}
		List<Double> listSave = new ArrayList<Double>();
		List<Double> listRight = new ArrayList<Double>();
		
		listSave = new ArrayList<Double>();
		listRight = new ArrayList<Double>();
		for(int i=0;i<listA.size();i++)
			listSave.add(listA.get(i).getEMA());	
		listRight.add(11.16); listRight.add(11.03); listRight.add(11.25);
		listRight.add(11.23); listRight.add(11.19); listRight.add(11.17); 
		listRight.add(11.11); listRight.add(11.02); listRight.add(10.95); 
		listRight.add(10.91); 
		for( int i=0; i<Math.min( listSave.size(), listRight.size() ) ; i++){
			System.out.println(listSave.get(i)+" "+listRight.get(i));
		}
		
		listSave = new ArrayList<Double>();
		listRight = new ArrayList<Double>();
		for(int i=0;i<listB.size();i++)
			listSave.add(listB.get(i).getEMA());
		listRight.add(11.16); listRight.add(11.03); listRight.add(11.25);
		listRight.add(11.23); listRight.add(11.3); listRight.add(11.06); 
		listRight.add(10.69); listRight.add(10.8); listRight.add(10.9); 
		listRight.add(11.04); 
		
		
		listSave = new ArrayList<Double>();
		listRight = new ArrayList<Double>();
		for(int i=0;i<listC.size();i++)
			listSave.add(listC.get(i).getEMA());
		listRight.add(11.16); listRight.add(11.03); listRight.add(11.25);
		listRight.add(11.23); listRight.add(11.3); listRight.add(11.06); 
		listRight.add(10.69); listRight.add(10.8); listRight.add(10.9); 
		listRight.add(10.99); 
		
	}
}
