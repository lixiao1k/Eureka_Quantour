package logic.service;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import logic.service.StockLogicInterface;
import logic.serviceimpl.StockLogicImpl;
import vo.ComparedInfoVO;
import vo.EMAInfoVO;

public class StockLogicImplTest extends TestCase {
	private StockLogicInterface SLI;
	private StockLogicImpl SLIm = new StockLogicImpl();

	protected void setUp() throws Exception {
		super.setUp();
		SLI = new StockLogicImpl();
	}
	
	public void testGetEMAInfo() throws RemoteException{
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
	/**
	 * 
	 * @Description: TODO
	 * @author: hzp
	 * @time: 2017年3月13日
	 */
	public void testGetComparedInfo()throws RemoteException{
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
		Assert.assertEquals( ci.getNameA(), "深发展A" );
		Assert.assertEquals( ci.getNameB(), "中成股份" );
		Assert.assertEquals( ci.getCodeA(), "1" );
		Assert.assertEquals( ci.getCodeB(), "151" );
		Assert.assertEquals( ci.getLowA(), 10.67 );
		Assert.assertEquals( ci.getLowB(), 7.23 );
		Assert.assertEquals( ci.getHighA(), 11.52 );
		Assert.assertEquals( ci.getHighB(), 8.07 );
		Assert.assertEquals( ci.getRODA(), -0.00272 );
		Assert.assertEquals( ci.getRODB(), 0.10151 );
		double[] dCloseA = { 11.16, 11.03, 11.25, 11.23, 11.3, 11.06, 10.69, 10.8, 10.9, 10.99};
		double[] dCloseB = { 7.38, 7.29, 7.47, 7.54, 7.74, 7.69, 7.74, 8.03, 8.02, 8.03};
		Assert.assertTrue( ifDoubleEqual(ci.getCloseA(), dCloseA) );
		Assert.assertTrue( ifDoubleEqual(ci.getCloseB(), dCloseB) );
		double[] dLogYieldA = { 0, -0.01171, 0.01975, -0.00178, 0.00621, -0.02147, -0.03402, 0.01023, 0.00922, 0.00822};
		double[] dLogYieldB = { 0, -0.01227, 0.02439, 0.00933, 0.02618, -0.00648, 0.00648, 0.03678, -0.00125, 0.00125};
		Assert.assertTrue( ifDoubleEqual(ci.getLogYieldA(), dLogYieldA) );
		Assert.assertTrue( ifDoubleEqual(ci.getLogYieldB(), dLogYieldB) );
	}
	
//	public void testGetMarketInfo()throws RemoteException{
//		
//	}
	
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
		Assert.assertEquals( ifDoubleEqual(2.30, 2.3), true);
		Assert.assertEquals( ifDoubleEqual(2.34, 2.3), false);
		Assert.assertEquals( ifDoubleEqual(2.34, 2.34), true);
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
//	/**
//	 * 
//	 * @Description: to compare if two ComparedInfoVOs are same
//	 * @author: hzp
//	 * @time: 2017年3月12日
//	 * @return: boolean
//	 */
//	private boolean ifComparedInfoVOEqual(ComparedInfoVO ciA, ComparedInfoVO ciB){
//		if( !ciA.getNameA().equals( ciB.getNameA() ) 
//				|| !ciA.getNameB().equals( ciB.getNameB() )
//				|| !ciA.getCodeA().equals( ciB.getCodeA() ) 
//				|| !ciA.getCodeB().equals( ciB.getCodeB() )
//				|| !ifDoubleEqual( ciA.getLowA(), ciB.getLowA() )
//				|| !ifDoubleEqual( ciA.getLowB(), ciB.getLowB() )
//				|| !ifDoubleEqual( ciA.getHighA(), ciB.getHighA() )
//				|| !ifDoubleEqual( ciA.getHighB(), ciB.getHighB() )
//				|| !ifDoubleEqual( ciA.getRODA(), ciB.getRODA() )
//				|| !ifDoubleEqual( ciA.getRODB(), ciB.getRODB() )
//				|| !ifDoubleEqual( ciA.getLogYieldVarianceA(), ciB.getLogYieldVarianceA() )
//				|| !ifDoubleEqual( ciA.getLogYieldVarianceB(), ciB.getLogYieldVarianceB() )
//				)
//			return false;
//		else{
//			double ACloseA[] = ciA.getCloseA();
//			double ACloseB[] = ciA.getCloseB();
//			double BCloseA[] = ciB.getCloseA();
//			double BCloseB[] = ciB.getCloseB();
//			for(int i=0; i<ACloseA.length; i++){
//				if( !ifDoubleEqual( ACloseA[i], BCloseA[i] ) 
//						|| !ifDoubleEqual( ACloseB[i], BCloseB[i] )
//						)
//					return false;
//			}
//			return true;
//		}
//	}
	
	private boolean ifDoubleEqual(double d1, double d2){
		String s1 = String.valueOf(formatDoubleTwo(d1));
		String s2 = String.valueOf(formatDoubleTwo(d2));
		return s1.equals(s2);
	}
	
	private double formatDoubleTwo(double d){
		DecimalFormat df = new DecimalFormat("#0.00");
		return Double.parseDouble(df.format(d));
	}
	
	private double formatDoubleFive(double d){
		DecimalFormat df = new DecimalFormat("#0.00000");
		return Double.parseDouble(df.format(d));
	}
	
	private boolean ifDoubleEqual(double[] d1, double[] d2){
		String s1 = "";
		String s2 = "";
		for( int i=0; i<d1.length; i++){
			s1 = String.valueOf( formatDoubleTwo(d1[i]) );
			s2 = String.valueOf( formatDoubleTwo(d2[i]) );
			if( !s1.equals(s2) )
				return false;
		}
		return true;
	}
}
