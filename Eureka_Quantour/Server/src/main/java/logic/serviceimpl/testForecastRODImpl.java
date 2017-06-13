package logic.serviceimpl;

import java.time.LocalDate;

import vo.PredictVO;

public class testForecastRODImpl {

	private static ForecastRODImpl fimpl = new ForecastRODImpl();
	
	public static void main( String args[] ){
		PredictVO predictVO = new PredictVO();
		try{
			predictVO = fimpl.predict( "300038", LocalDate.of(2014, 4, 1) );
		}catch( Exception e ){}
		System.out.println( predictVO.getPredictPrice() );
		System.out.println( predictVO.getPredictROD() );
		System.out.println( predictVO.getFirstMinPrice() );
		System.out.println( predictVO.getSecondMinPrice() );
	}
}
