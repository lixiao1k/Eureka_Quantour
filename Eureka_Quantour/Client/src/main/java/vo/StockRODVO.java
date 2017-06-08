package vo;

import java.io.Serializable;

public class StockRODVO implements Serializable{

	private static final long serialVersionUID = 8243916259062852191L;

	public int[] zhixin = new int[2];
	
	public int[] Pos = new int[2];
	public int[] Neg = new int[2];
	
	public StockRODVO(){
		
		for( int i=0; i<zhixin.length; i++ )
			zhixin[i] = 0;

		for( int i=0; i<Pos.length; i++ )
			Pos[i] = 0;
		
		for( int i=0; i<Neg.length; i++ )
			Neg[i] = 0;
		
	}	
}
