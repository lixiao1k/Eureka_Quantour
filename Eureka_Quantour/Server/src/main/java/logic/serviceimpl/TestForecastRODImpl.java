package logic.serviceimpl;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class TestForecastRODImpl {
	
	private ForecastRODImpl fROD = new ForecastRODImpl();
	
	private DecimalFormat df = new DecimalFormat("0.00");
	
	private void test1(){
		int vLen = 100;
		double price = 10.8;
		
		double[] closes = new double[vLen];
		LocalDate[] dates = new LocalDate[vLen];
		
		closes[0] = price;
		dates[0] = LocalDate.of(2014, 3, 1);
		int k = 3, l = 2;
		for( int i=1; i<vLen; i++ ){
			if( Math.random()>0.5 )
				closes[i] = closes[i-1] + Math.random()/2;
			else
				closes[i] = closes[i-1] - Math.random()/2;
			dates[i] = LocalDate.of(2014, k, l);
			l++;
			if( l==31 ){
				k++;
				l = 1;
			}
		}
		
		int m = 5;
		k = 25;
		double result = fROD.KNNPredictPrice(closes, dates, m, k);
		
		double sum = 0;
		for( int i=0; i<vLen; i++ )
			sum += closes[i];
		sum /= vLen;
		System.out.println( "average: " + df.format( sum ) );
		
		System.out.println( result );
	}
	
	public static void main(String args[]){
		new TestForecastRODImpl().test1();
	}
	
}
