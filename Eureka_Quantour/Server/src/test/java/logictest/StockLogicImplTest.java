package logictest;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
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
	
	public void testGetEMAInfo() throws RemoteException{
		List<EMAInfoVO> listA = SLI.getEMAInfo("1", Calendar.getInstance(), Calendar.getInstance()).get(0);
		List<EMAInfoVO> listB = SLI.getEMAInfo("1", Calendar.getInstance(), Calendar.getInstance()).get(1);
		List<EMAInfoVO> listC = SLI.getEMAInfo("1", Calendar.getInstance(), Calendar.getInstance()).get(2);
		
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
		Assert.assertTrue( ifListEqual(listSave, listRight) );
		
		listSave = new ArrayList<Double>();
		listRight = new ArrayList<Double>();
		for(int i=0;i<listB.size();i++)
			listSave.add(listB.get(i).getEMA());
		listRight.add(11.16); listRight.add(11.03); listRight.add(11.25);
		listRight.add(11.23); listRight.add(11.3); listRight.add(11.06); 
		listRight.add(10.69); listRight.add(10.8); listRight.add(10.9); 
		listRight.add(11.04); 
		Assert.assertTrue( ifListEqual(listSave, listRight) );
		
		listSave = new ArrayList<Double>();
		listRight = new ArrayList<Double>();
		for(int i=0;i<listC.size();i++)
			listSave.add(listC.get(i).getEMA());
		listRight.add(11.16); listRight.add(11.03); listRight.add(11.25);
		listRight.add(11.23); listRight.add(11.3); listRight.add(11.06); 
		listRight.add(10.69); listRight.add(10.8); listRight.add(10.9); 
		listRight.add(10.99); 
		Assert.assertTrue( ifListEqual(listSave, listRight) );
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
		Assert.assertEquals( format.format(date), format.format(tempDate));
		
		cal.set(2017, 2, 1); tempCal.set(2017, 1, 28);
		date = SLIm.calendarAdvance(cal).getTime(); tempDate = tempCal.getTime();
		Assert.assertEquals( format.format(date), format.format(tempDate));
		
		cal.set(2017, 0, 1); tempCal.set(2016, 11, 30);
		date = SLIm.calendarAdvance(cal).getTime(); tempDate = tempCal.getTime();
		Assert.assertEquals( format.format(date), format.format(tempDate));
	}
	
	public void testIfDoubleEqual(){
		Assert.assertEquals( SLIm.ifDoubleEqual(2.30, 2.3), true);
		Assert.assertEquals( SLIm.ifDoubleEqual(2.34, 2.3), false);
		Assert.assertEquals( SLIm.ifDoubleEqual(2.34, 2.34), true);
	}
	/**
	 * 
	 * @Description: to compare if two lists like List<Double> are same
	 * @author: hzp
	 * @time: 2017年3月8日
	 * @return: boolean
	 */
	private boolean ifListEqual(List<Double> ListA, List<Double> ListB){
		if(ListA.size()!=ListB.size()){
			System.out.println("two list's length aren't equal");
			return false;
		}
		else{
			for(int i=0;i<ListA.size();i++){
				if( !String.valueOf(ListA.get(i)).equals( String.valueOf(ListB.get(i)) ) )
					return false;
			}
		}
		return true;
	}
}
