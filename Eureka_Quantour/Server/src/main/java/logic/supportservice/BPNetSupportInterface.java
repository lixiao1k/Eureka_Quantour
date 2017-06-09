package logic.supportservice;

import java.util.List;

public interface BPNetSupportInterface {

	List<Double> EMV( List<Double>highPrice, List<Double>lowPrice, List<Double>vol );
	
	List<Double> EMA5( List<Double>overPrice );
	
	List<Double> EMA60( List<Double>overPrice );
	
	List<Double> MA5( List<Double>overPrice );
	
	List<Double> MA60( List<Double>overPrice );
	
	List<Double> MTM( List<Double>overPrice );
	
	List<Double> MACD( List<Double>vol );
	
	List<Double> CR5( List<Double>overPrice, List<Double>highPrice, List<Double>lowPrice, 
			List<Double>openPrice );
	
	double[][] bpTrain( List<Double>overPrice, List<Double>highPrice, List<Double>lowPrice,
			List<Double>openPrice, List<Double>vol );
}
