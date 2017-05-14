package vo;

import java.io.Serializable;

public class StockRODVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8243916259062852191L;
	
	
	public int[][] wROD = new int[5][23];
	
	public int[][] RODw = new int[23][5];
	
	public int[][] nodata = new int[5][2];
	
	public int rightTimes = 0;
	public int errorTimes = 0;
	
	public StockRODVO(){
		for( int i=0; i<wROD.length; i++ )
			for( int j=0; j<wROD[0].length; j++ )
				wROD[i][j] = 0;
		
		for( int i=0; i<RODw.length; i++ )
			for( int j=0; j<RODw[0].length; j++ )
				RODw[i][j] = 0;
		
		for( int i=0; i<nodata.length; i++ )
			for( int j=0; j<nodata[0].length; j++ )
				nodata[i][j] = 0;
	}	
}
