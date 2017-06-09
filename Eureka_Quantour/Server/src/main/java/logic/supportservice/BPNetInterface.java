package logic.supportservice;

public interface BPNetInterface {
	
	double[] computeOut( double[] in );
	
	void updateWeight( double[] tar );
	
	void train( double[] in, double[] tar );
}
