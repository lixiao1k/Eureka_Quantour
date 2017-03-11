package logictest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import logicservice.StockLogicInterface;
import logicserviceimpl.StockLogicImpl;
import vo.EMAInfoVO;

public class StockLogicImplTest extends TestCase {
	private StockLogicInterface SLI;
	private StockLogicImpl SLIm = new StockLogicImpl();

	protected void setUp() throws Exception {
		super.setUp();
		SLI = new StockLogicImpl();
	}
	
	public void testGetEMAInfo(){
		List<EMAInfoVO> list1 = SLI.getEMAInfo("1", Calendar.getInstance(), Calendar.getInstance()).get(0);
		List<Double> list2 = new ArrayList<Double>();
		for(int i=0;i<list1.size();i++)
			list2.add(list1.get(i).getEMA());
		List<Double> list3 = new ArrayList<Double>();
		list3.add(11.16); list3.add(11.03); list3.add(11.25);
		list3.add(11.23); list3.add(11.19); 
		this.assertTrue( ifListEqual(list2, list3) );
	}
	
	public void testGetComparedInfo(){
		
	}
	
	public void testGetMarketInfo(){
		
	}
	
	public void testCalendarAdvance(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance(), tempCal = Calendar.getInstance();
		Date date = new Date(), tempDate = new Date();
		
		cal.set(2017, 2, 10); tempCal.set(2017, 2, 9);
		date = SLIm.calendarAdvance(cal).getTime(); tempDate = tempCal.getTime();
		this.assertEquals( format.format(date), format.format(tempDate));
		
		cal.set(2017, 2, 1); tempCal.set(2017, 1, 28);
		date = SLIm.calendarAdvance(cal).getTime(); tempDate = tempCal.getTime();
		this.assertEquals( format.format(date), format.format(tempDate));
		
		cal.set(2017, 0, 1); tempCal.set(2016, 11, 30);
		date = SLIm.calendarAdvance(cal).getTime(); tempDate = tempCal.getTime();
		this.assertEquals( format.format(date), format.format(tempDate));
	}
	
	public void testIfDoubleEqual(){
		this.assertEquals( SLIm.ifDoubleEqual(2.30, 2.3), true);
		this.assertEquals( SLIm.ifDoubleEqual(2.34, 2.3), false);
		this.assertEquals( SLIm.ifDoubleEqual(2.34, 2.34), true);
	}
	/**
	 * 
	 * @Description: to compare if two lists like List<Double> are same
	 * @author: hzp
	 * @time: 2017年3月8日
	 * @return: boolean
	 */
	private boolean ifListEqual(List<Double> list1, List<Double> list2){
		if(list1.size()!=list2.size()){
			System.out.println("two list's length aren't equal");
			return false;
		}
		else{
			for(int i=0;i<list1.size();i++){
				if( !String.valueOf(list1.get(i)).equals( String.valueOf(list2.get(i)) ) )
					return false;
			}
		}
		return true;
	}
}
