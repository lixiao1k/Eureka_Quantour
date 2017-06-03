package logic.supportservice;

public interface SortArrayInterface{

	/**
	 * @Description:
	 * @author: 	 hzp
	 * @date:        2017-06-01
	 * @param        nums       sort nums and return every num's order
	 */
	int[] getSortIndexMinToMax( double[] nums );

	int[] getSortIndexMaxToMin( double[] nums );

	/**
	 * @Description: 将传入数组内容 反序
	 * @author: hzp
	 * @date: 2017年6月3日
	 */
	int[] arrayConverse( int[] index );
}