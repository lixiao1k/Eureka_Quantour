package logic.supportservice;

public interface BPNetSupportInterface {

	double[] EMV( double[] highPrice, double[] lowPrice, double[] volume, 
			double[][] QMaxNumDayData );
	
	double[] EMA5( double[] closePrice, double[][] QMaxNumDayData );
	
	double[] EMA60( double[] closePrice, double[][] QMaxNumDayData );
	
	double[] MA5( double[] closePrice, double[][] QMaxNumDayData );
	
	double[] MA60( double[] closePrice, double[][] QMaxNumDayData );
	
	double[] MTM( double[] closePrice, double[][] QMaxNumDayData );
	
	double[] MACD( double[] volume, double[][] QMaxNumDayData );
	
	double[] CR5( double[] closePrice, double[] highPrice, double[] lowPrice, 
			double[] openPrice, double[][] QMaxNumDayData );
	
	double[][] bpTrain( double[] overPrice, double[] highPrice, double[] lowPrice,
			double[] openPrice, double[] vol, double[][] QMaxNumDayData );
}
