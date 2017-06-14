package logic.serviceimpl;

import java.time.LocalDate;

import vo.PredictVO;

public class testForecastRODImpl {

	private static ForecastRODImpl fimpl = new ForecastRODImpl();
	
	public static void main( String args[] ){
		long time1 = System.currentTimeMillis();
		
		PredictVO predictVO = new PredictVO();
		try{
			predictVO = fimpl.predict( "300038", LocalDate.of(2014, 4, 1) );
			long time2 = System.currentTimeMillis();
			System.out.println( time2 - time1 );
		}catch( Exception e ){}
		System.out.println( predictVO.getPredictPrice() );
		System.out.println( predictVO.getPredictROD() );
		System.out.println( predictVO.getMinPrice90ZhiXin() );
		System.out.println( predictVO.getMinPrice99ZhiXin() );
		System.out.println( predictVO.getMaxPrice90ZhiXin() );
		System.out.println( predictVO.getMaxPrice99ZhiXin() );
	}
}
