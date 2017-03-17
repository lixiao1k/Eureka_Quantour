package logic.serviceimpl;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import logic.service.StockLogicInterface;
import vo.ComparedInfoVO;
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
//			new test().testGetEMAInfo();
//			new test().testCalendarAdvance();
//			System.out.println( Math.log(10.69/11.16) );
//			new test().testGetComparedInfo();
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
		Calendar cal = Calendar.getInstance();
		Calendar tempCal = Calendar.getInstance();
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
		
		cal.set(2014, 3, 16); tempCal.set(2014, 3, 15);
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
		List<EMAInfoVO> listD = new ArrayList<EMAInfoVO>();
		List<EMAInfoVO> listE = new ArrayList<EMAInfoVO>();
		try{
			listA = SLI.getEMAInfo("1", begin, end).get(0);
			listB = SLI.getEMAInfo("1", begin, end).get(1);
			listC = SLI.getEMAInfo("1", begin, end).get(2);
			listD = SLI.getEMAInfo("1", begin, end).get(3);
			listE = SLI.getEMAInfo("1", begin, end).get(4);
		}catch(Exception e){
			e.printStackTrace();
		}
		List<Double> listSave = new ArrayList<Double>();
		List<Double> listRight = new ArrayList<Double>();
		
		listSave = new ArrayList<Double>();
		listRight = new ArrayList<Double>();
		for(int i=0;i<listA.size();i++)
			listSave.add(listA.get(i).getEMA());	
		listRight.add(11.2); listRight.add(11.11); listRight.add(10.99);
		listRight.add(10.87); listRight.add(10.89); listRight.add(10.95); 
		listRight.add(11.02); listRight.add(11.11); listRight.add(11.17); 
		listRight.add(11.19); 
		for( int i=0; i<Math.min( listSave.size(), listRight.size() ) ; i++){
			System.out.print(listSave.get(i)+"<br/>");
			/* +" "+listRight.get(i) */
		}
		System.out.println();
		
		listSave = new ArrayList<Double>();
		listRight = new ArrayList<Double>();
		for(int i=0;i<listB.size();i++)
			listSave.add(listB.get(i).getEMA());
		listRight.add(11.08); listRight.add(11.09); listRight.add(11.10);
		listRight.add(11.09); listRight.add(11.06); listRight.add(11.07); 
		listRight.add(11.06); listRight.add(11.05); listRight.add(11.02); 
		listRight.add(11.04); 
		for( int i=0; i<Math.min( listSave.size(), listRight.size() ) ; i++){
			System.out.print(listSave.get(i)+"<br/>");
			/* +" "+listRight.get(i) */
		}
		
		System.out.println();
		listSave = new ArrayList<Double>();
		listRight = new ArrayList<Double>();
		for(int i=0;i<listC.size();i++)
			listSave.add(listC.get(i).getEMA());
		listRight.add(10.89); listRight.add(10.93); listRight.add(10.93);
		listRight.add(10.93); listRight.add(10.95); listRight.add(10.98); 
		listRight.add(11.00); listRight.add(11.03); listRight.add(11.04); 
		listRight.add(11.06); 
		for( int i=0; i<Math.min( listSave.size(), listRight.size() ) ; i++){
			System.out.print(listSave.get(i)+"<br/>");
			/* +" "+listRight.get(i) */
		}
		
//		System.out.println();
//		listSave = new ArrayList<Double>();
//		for(int i=0;i<listD.size();i++)
//			listSave.add(listD.get(i).getEMA());
//		for( int i=0; i<listSave.size() ; i++){
//			System.out.print(listSave.get(i)+"<br/>");
//		}
	}
	
	private void testGetComparedInfo()throws RemoteException{
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		begin.set(2014, 3, 16);
		end.set(2014, 3, 29);
		ComparedInfoVO ci = new ComparedInfoVO(0);
		try{
			ci = SLI.getComparedInfo("1", "151", begin, end);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println( ci.getLowA()+" "+10.67 );
		System.out.println( ci.getLowB()+" "+7.23 );
		System.out.println( ci.getHighA()+" "+11.52 );
		System.out.println( ci.getHighB()+" "+8.07 );
		System.out.println( ci.getRODA()+" "+ 0.01732 );
		System.out.println( ci.getRODB()+" "+ -0.07519 );
		double[] dLogYieldA = { 0.00182, -0.00819, -0.00917, -0.01019, 0.03461, 0.02170, -0.00619, 0.00178, -0.01956, 0.01179};
		double[] dLogYieldB = { 0.00625, -0.00125, 0.00125, -0.03678, -0.00648, 0.00648, -0.02618, -0.00933, -0.02439, 0.01227};
		for( int i=0;i<dLogYieldA.length;i++){
			System.out.print(ci.getLogYieldA()[i]+"<br/>");
		};
		System.out.println();
		for( int i=0;i<dLogYieldA.length;i++){
			System.out.print(ci.getLogYieldB()[i]+"<br/>");
		};
	}
}
