package vo;

import java.io.Serializable;

public class StockRODVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8243916259062852191L;
	
	
	public int[][] wROD = new int[5][21];
	
	public int[][] RODw = new int[21][5];
	
	public StockRODVO(){
		for( int i=0; i<wROD.length; i++ )
			for( int j=0; j<wROD[0].length; j++ )
				wROD[i][j] = 0;
		
		for( int i=0; i<RODw.length; i++ )
			for( int j=0; j<RODw[0].length; j++ )
				RODw[i][j] = 0;
	}	
}
