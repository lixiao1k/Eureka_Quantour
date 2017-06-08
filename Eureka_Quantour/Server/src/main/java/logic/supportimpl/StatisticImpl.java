package logic.supportimpl;

import logic.supportservice.StatisticInterface;

public class StatisticImpl implements StatisticInterface{

	@Override
	public boolean predictROE(double preValue, double square, int num, double alpha, double realValue) {
		// TODO Auto-generated method stub
		double zalpha = 0.0;
		if( alpha==0.1 )
			zalpha = 1.65;
		else if( alpha==0.05 )
			zalpha = 1.96;
		else if( alpha==0.01 )
			zalpha = 2.58;
		double lower = realValue - Math.sqrt(square)*zalpha/Math.sqrt(num);
		double upper = realValue + Math.sqrt(square)*zalpha/Math.sqrt(num);

		if( preValue>=lower && preValue<=upper ){
			return true;
		}
		else{
			return false;
		}
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
