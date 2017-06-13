package logic.supportimpl;

import logic.supportservice.StatisticInterface;

public class StatisticImpl implements StatisticInterface{

	@Override
	public boolean predictROE(double average, double variance, int num, double alpha, double preValue) {
		// TODO Auto-generated method stub
		double zalpha = 0.0;
		if( alpha>0.09 && alpha<0.11 )
			zalpha = 1.65;
		else if( alpha>0.049 && alpha<0.051 )
			zalpha = 1.96;
		else if( alpha>0.009 && alpha<0.011 )
			zalpha = 2.58;
		double lower = average - Math.sqrt(variance)*zalpha/Math.sqrt(num);
		double upper = average + Math.sqrt(variance)*zalpha/Math.sqrt(num);

		if( preValue>=lower && preValue<=upper ){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public double preMinPriceByRODOf90ZhiXin(double average, double variance, int num, double todayPrice ) {
		// TODO Auto-generated method stub
		// alpha = 0.1
		double zalpha = 1.65;
		double lower = average - Math.sqrt(variance)*zalpha/Math.sqrt(num);
		
		lower = todayPrice * ( 1 + lower );
		
		return lower;
	}



	@Override
	public double preMinPriceByRODOf99ZhiXin(double average, double variance, int num, double todayPrice ) {
		// TODO Auto-generated method stub
		// alpha = 0.0
		double zalpha = 2.58;
		double lower = average - Math.sqrt(variance)*zalpha/Math.sqrt(num);
		
		lower = todayPrice * ( 1 + lower );
		
		return lower;
	}

	@Override
	public double getWeight(int k, int i) {
		// TODO Auto-generated method stub
		if( i>k )
			return 0;
		double w0 = 0.004;
		double d = 2.0 * (1.0-k*w0) / ( k*(k-1.0) );

		return w0 + i*d;
	}
	
}