package logic.supportimpl;

import java.util.ArrayList;
import java.util.List;

import logic.supportservice.StatisticInterface;

public class StatisticImpl implements StatisticInterface{
	
	private final double idealKaFang = 32.6706;
	
	public double getIdealKaFang(){
		return idealKaFang;
	}

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

	@Override
	public double calKaFang( int[] RODFenBu, double average, double biaozhuncha ){
		int length = RODFenBu.length;
		if( length!=22 )
			return 0;
		double[] idealProb = new double[length];
		double numOfROD = 0;
		for( int i=0; i<length; i++ ){
			numOfROD += RODFenBu[i];
			if( i==0 ){
				double x = (-0.1 - average) / biaozhuncha; 
				idealProb[i] = calNormalDistribution( x );
			}
			else if( i==RODFenBu.length-1 ){
				double x = (0.1 - average) / biaozhuncha; 
				idealProb[i] = 1 - calNormalDistribution( x );
				idealProb[i] += idealProb[i-1];
			}
			else{
				double Qx = RODIndexToDoubleLower( i );
				double Hx = Qx + 0.01;
				idealProb[i] = calNormalDistribution( Hx ) - calNormalDistribution( Qx );
				idealProb[i] += idealProb[i-1];
			}
		}
		
		if( numOfROD==0 )
			return 0;
		
		List<Integer> RODFenBu2 = new ArrayList<>();
		List<Double> GaiLv = new ArrayList<>();
		int pinshu = 0;
		double gailv = 0;
		for( int i=0; i<length; i++ ){
			pinshu += RODFenBu[i];
			gailv += idealProb[i];
			if( pinshu>=5 ){
				RODFenBu2.add( pinshu );
				GaiLv.add( gailv );
				pinshu = 0;
				gailv = 0;
			}
		}
		
		double x2 = 0;
		for( int i=0; i<RODFenBu2.size(); i++ ){
			pinshu = RODFenBu2.get(i);
			gailv = GaiLv.get(i);
			x2 += Math.pow(pinshu, 2) / (numOfROD * gailv);
		}
		x2 -= numOfROD;
		
		return x2;
	}

	private double calNormalDistribution( double x ){
		if( x>2.49 )
			return 1;
		else if( x<-2.49 )
			return 0;
		
		int row = (int)Math.abs(x / 0.1);
		int col = (int)( (Math.abs(x)-row*0.1) / 0.01 )+1;
			
		if( col>=NormalDistribution[0].length || col<0 )
			col = 0;
		
		double result = 0;
		if( x>0 )
			result = NormalDistribution[row][col];
		else
			result = 1 - NormalDistribution[row][col];
		return result;
	}
	
	private double RODIndexToDoubleLower( int index ){
		double result = -0.1;
		if( index<22 )
			result = -0.1+(index-1)*0.01;
		return result;
	}
	
	private double[][] NormalDistribution = {
/*      	 	  0.00    0.01    0.02    0.03    0.04    0.05    0.06    0.07    0.08    0.09     */

/*  0.0  */		{0.5000, 0.5040, 0.5080, 0.5120, 0.5160, 0.5199, 0.5239, 0.5279, 0.5319, 0.5359},

/*  0.1  */		{0.5398, 0.5438, 0.5478, 0.5517, 0.5557, 0.5596, 0.5636, 0.5675, 0.5714, 0.5753},

/*  0.2  */		{0.5793, 0.5832, 0.5871, 0.5910, 0.5948, 0.5987, 0.6026, 0.6064, 0.6103, 0.6141},

/*  0.3  */		{0.6179, 0.6217, 0.6255, 0.6293, 0.6331, 0.6368, 0.6404, 0.6443, 0.6480, 0.6517},

/*  0.4  */		{0.6554, 0.6591, 0.6628, 0.6664, 0.6700, 0.6736, 0.6772, 0.6808, 0.6844, 0.6879},

/*  0.5  */		{0.6915, 0.6950, 0.6985, 0.7019, 0.7054, 0.7088, 0.7123, 0.7157, 0.7190, 0.7224},

/*  0.6  */		{0.7257, 0.7291, 0.7324, 0.7357, 0.7389, 0.7422, 0.7454, 0.7486, 0.7517, 0.7549},

/*  0.7  */		{0.7580, 0.7611, 0.7642, 0.7673, 0.7703, 0.7734, 0.7764, 0.7794, 0.7823, 0.7852},

/*  0.8  */		{0.7881, 0.7910, 0.7939, 0.7967, 0.7995, 0.8023, 0.8051, 0.8078, 0.8106, 0.8133},

/*  0.9  */		{0.8159, 0.8186, 0.8212, 0.8238, 0.8264, 0.8289, 0.8355, 0.8340, 0.8365, 0.8389},

/*  1.0  */		{0.8413, 0.8438, 0.8461, 0.8485, 0.8508, 0.8531, 0.8554, 0.8577, 0.8599, 0.8621},

/*  1.1  */		{0.8643, 0.8665, 0.8686, 0.8708, 0.8729, 0.8749, 0.8770, 0.8790, 0.8810, 0.8830},

/*  1.2  */		{0.8849, 0.8869, 0.8888, 0.8907, 0.8925, 0.8944, 0.8962, 0.8980, 0.8997, 0.9015},

/*  1.3  */		{0.9032, 0.9049, 0.9066, 0.9082, 0.9099, 0.9115, 0.9131, 0.9147, 0.9162, 0.9177},

/*  1.4  */		{0.9192, 0.9207, 0.9222, 0.9236, 0.9251, 0.9265, 0.9279, 0.9292, 0.9306, 0.9319},

/*  1.5  */		{0.9332, 0.9345, 0.9357, 0.9370, 0.9380, 0.9394, 0.9406, 0.9418, 0.9430, 0.9441},

/*  1.6  */		{0.9452, 0.9463, 0.9474, 0.9484, 0.9495, 0.9505, 0.9515, 0.9525, 0.9535, 0.9535},

/*  1.7  */		{0.9554, 0.9564, 0.9573, 0.9582, 0.9591, 0.9599, 0.9608, 0.9616, 0.9625, 0.9633},

/*  1.8  */		{0.9641, 0.9648, 0.9656, 0.9664, 0.9672, 0.9678, 0.9686, 0.9693, 0.9700, 0.9706},

/*  1.9  */		{0.9713, 0.9719, 0.9726, 0.9732, 0.9738, 0.9744, 0.9750, 0.9756, 0.9762, 0.9767},

/*  2.0  */		{0.9772, 0.9778, 0.9783, 0.9788, 0.9793, 0.9798, 0.9803, 0.9808, 0.9812, 0.9817},

/*  2.1  */		{0.9821, 0.9826, 0.9830, 0.9834, 0.9838, 0.9842, 0.9846, 0.9850, 0.9854, 0.9857},

/*  2.2  */		{0.9861, 0.9864, 0.9868, 0.9871, 0.9874, 0.9878, 0.9881, 0.9884, 0.9887, 0.9890},

/*  2.3  */		{0.9893, 0.9896, 0.9898, 0.9901, 0.9904, 0.9906, 0.9909, 0.9911, 0.9913, 0.9916},

/*  2.4  */		{0.9918, 0.9920, 0.9922, 0.9925, 0.9927, 0.9929, 0.9931, 0.9932, 0.9934, 0.9936}  };
	
}
