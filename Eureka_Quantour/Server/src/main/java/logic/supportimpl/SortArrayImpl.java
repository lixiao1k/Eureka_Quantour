package logic.supportimpl;

import logic.supportservice.SortArrayInterface;

public class SortArrayImpl implements SortArrayInterface{

	@Override
	public int[] getSortIndexMinToMax(double[] nums) {
		// TODO Auto-generated method stub
		int len = nums.length;

		double[][] temp = new double[len][2];
		for( int i=0; i<len; i++ ){
			temp[i][0] = nums[i];
			temp[i][1] = i;
		}

		for( int k=0; k<len-1; k++ ){
			for( int i=0; i<len-1-k; i++ ){
				if( temp[i][0]>temp[i+1][0] ){
					double tempD = temp[i][0];
					double tempIndex = temp[i][1];

					temp[i][0] = temp[i+1][0];
					temp[i][1] = temp[i+1][1];

					temp[i+1][0] = tempD;
					temp[i+1][1] = tempIndex;
				}
			}
		}

		int[] index = new int[len];
		for( int i=0; i<len; i++ )
			index[(int)temp[i][1]] = i;

		return index;
	}

	@Override
	public int[] getSortIndexMaxToMin(double[] nums) {
		// TODO Auto-generated method stub
		int[] indexMinToMax = getSortIndexMinToMax( nums );
		return arrayConverse( indexMinToMax );
	}

	@Override
	public int[] arrayConverse(int[] index) {
		// TODO Auto-generated method stub
		int len = index.length;

		int[] result = new int[len];
		for( int i=0; i<len; i++ )
			result[i] = index[len-1-i];

		return result;
	}
	
}