package logic.supportimpl;

import java.text.DecimalFormat;

import logic.supportservice.StatisticInterface;

public class StatisticImpl implements StatisticInterface{
	
	private DecimalFormat df = new DecimalFormat("#0.0000");

	@Override
	public boolean predictROE(double average, double variance, int num, double alpha, double preValue) {
		// TODO Auto-generated method stub
		double zalpha = 0.0;
		if( alpha==0.1 )
			zalpha = 1.65;
		else if( alpha==0.05 )
			zalpha = 1.96;
		else if( alpha==0.01 )
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
	public double getWeight(int k, int i) {
		// TODO Auto-generated method stub
		if( i>k )
			return 0;
		double w0 = 0.004;
		double d = 2.0 * (1.0-k*w0) / ( k*(k-1.0) );

		return w0 + i*d;
	}

}
