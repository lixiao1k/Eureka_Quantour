package logic.supportservice;

import java.time.LocalDate;

public interface PredictInterface {

	/**
	 * @author H2P
	 * @date   2017-06-01
	 * @param  closes     datas of close price
	 * @param  m          length of vector
	 * @param  k          number of relevant character
	 */
	double KNNPredictPrice( double[] closes, LocalDate[] dates, int m, int k );
	
	double SBPredictPrice( double ZPrice, double QPrice );
	
	double KNNForStrategy( String stockcode, LocalDate date, int len, int m, int k );
	
}
