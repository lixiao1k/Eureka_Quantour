package logic.supportimpl;

import logic.supportservice.CalculateValueInterface;

public class CalculateValueImpl implements CalculateValueInterface{

	@Override
	public double calAvg(double[] data) {
		// TODO Auto-generated method stub
		double sum = 0.0;
		for( int i=0; i<data.length; i++ )
			sum += data[i];
		return sum / data.length;
	}

	@Override
	public double calVariance(double[] data) {
		// TODO Auto-generated method stub
		double avg = calAvg( data );
		double avgSquare = Math.pow( avg, 2 );

		double[] data2 = new double[data.length];
		for( int i=0; i<data.length; i++ )
			data2[i] = Math.pow( data[i], 2 );
		double squareAvg = calAvg( data2 );

		return (squareAvg - avgSquare);
	}

	@Override
	public double calCosIncludeAngle(double[] vector1, double[] vector2) {
		// TODO Auto-generated method stub
		if( vector1.length!=vector2.length )
			return 0;
		double vectorMul = vectorMultiply( vector1, vector2 );
		double vectorModelMul = vectorModel( vector1 )*vectorModel( vector2 );

		return vectorMul / vectorModelMul;
	}

	@Override
	public double vectorMultiply(double[] vector1, double[] vector2) {
		// TODO Auto-generated method stub
		if( vector1.length!=vector1.length )
			return 0;

		double sum = 0;
		for( int i=0; i<vector1.length; i++ )
			sum += vector1[i]*vector2[i];
		return sum;
	}

	@Override
	public double vectorModel(double[] vector) {
		// TODO Auto-generated method stub
		double sum = 0;
		for( int i=0; i<vector.length; i++ )
			sum += Math.pow( vector[i], 2 );
		sum = Math.sqrt( sum );
		return sum;
	}
}