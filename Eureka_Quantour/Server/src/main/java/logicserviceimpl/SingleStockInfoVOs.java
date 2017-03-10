package logicserviceimpl;

import java.util.Calendar;

import vo.SingleStockInfoVO;
/**
 * 
 * @Description: initialize objects for logic to test
 * @author: hzp
 * @time: 2017年3月7日
 */
public class SingleStockInfoVOs {
	
	protected static SingleStockInfoVO ssi1 ;
	protected static SingleStockInfoVO ssi2 ;
	protected static SingleStockInfoVO ssi3 ;
	protected static SingleStockInfoVO ssi4 ;
	protected static SingleStockInfoVO ssi5 ;
	protected static SingleStockInfoVO ssi6 ;
	protected static SingleStockInfoVO ssi7 ;
	protected static SingleStockInfoVO ssi8 ;
	protected static SingleStockInfoVO ssi9 ;
	protected static SingleStockInfoVO ssi10 ;
	private static Calendar time = Calendar.getInstance();
	
	public static void init(){
		time = Calendar.getInstance();
		time.set(2014, 3, 29);
		ssi1 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 11.02, 11.16, 11.25, 10.92, 41362100, 11.16, "SZ");
		ssi6 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151", 7.29, 7.38, 7.4, 7.29, 927600, 7.38, "SZ");

		time.set(2014, 3, 28);
		ssi2 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 11.25, 11.03, 11.28, 10.96, 52604500, 11.03, "SZ");
		ssi7 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151",7.53, 7.29, 7.53, 7.23, 1581700, 7.29, "SZ");

		time.set(2014, 3, 25);
		ssi3 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 11.23, 11.25, 11.52, 11.11, 71433500, 11.25, "SZ");
		ssi8 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151", 7.55, 7.47, 7.62, 7.45, 1153400, 7.47, "SZ");
		
		time.set(2014, 3, 24);
		ssi4 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 11.42, 11.23, 11.45, 11.12, 63400400, 11.23, "SZ");
		ssi9 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151", 7.74, 7.54, 7.74, 7.52, 1402300, 7.54, "SZ");
		
		time.set(2014, 3, 23);
		ssi5 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 11.08, 11.3, 11.45, 11.08, 119102800, 11.3, "SZ");
		ssi10 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151", 7.71, 7.74, 7.77, 7.63, 1074200, 7.74, "SZ");
	}
}
