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
	
	protected static SingleStockInfoVO ssi11 ;
	protected static SingleStockInfoVO ssi12 ;
	protected static SingleStockInfoVO ssi13 ;
	protected static SingleStockInfoVO ssi14 ;
	protected static SingleStockInfoVO ssi15 ;
	protected static SingleStockInfoVO ssi16 ;
	protected static SingleStockInfoVO ssi17 ;
	protected static SingleStockInfoVO ssi18 ;
	protected static SingleStockInfoVO ssi19 ;
	protected static SingleStockInfoVO ssi20 ;
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
	
		time.set(2014, 3, 22);
		ssi11 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 10.71, 11.06, 11.18, 10.68, 52328100, 11.06, "SZ");
		ssi16 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151", 7.29, 7.38, 7.4, 7.29, 927600, 7.38, "SZ");

		time.set(2014, 3, 21);
		ssi12 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 10.75, 10.69, 10.9, 10.67, 29815600, 10.69, "SZ");
		ssi17 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151",7.53, 7.29, 7.53, 7.23, 1581700, 7.29, "SZ");

		time.set(2014, 3, 18);
		ssi13 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 10.85, 10.8, 10.88, 10.72, 32354400, 10.8, "SZ");
		ssi18 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151", 7.55, 7.47, 7.62, 7.45, 1153400, 7.47, "SZ");
		
		time.set(2014, 3, 17);
		ssi14 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 11.02, 10.9, 11.08, 10.83, 30488500, 10.9, "SZ");
		ssi19 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151", 7.74, 7.54, 7.74, 7.52, 1402300, 7.54, "SZ");
		
		time.set(2014, 3, 16);
		ssi15 = new SingleStockInfoVO().initObject("深发展A", (Calendar)time.clone(), "1", 10.93, 10.99, 11.13, 10.86, 37664600, 10.99, "SZ");
		ssi20 = new SingleStockInfoVO().initObject("中成股份", (Calendar)time.clone(), "151", 7.71, 7.74, 7.77, 7.63, 1074200, 7.74, "SZ");
	}
}
