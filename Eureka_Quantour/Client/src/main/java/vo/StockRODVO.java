package vo;

import java.io.Serializable;

public class StockRODVO implements Serializable{

	private static final long serialVersionUID = 8243916259062852191L;
	
	
	public int[][] wROD = new int[5][23];
	public int[][] RODw = new int[23][5];
	public int[][] nodata = new int[5][2];
	
	public int[] ROETimes = new int[11];
	
	public int[] Pos = new int[2];
	public int[] Neg = new int[2];
	
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
		
		for( int i=0; i<ROETimes.length; i++)
			ROETimes[i] = 0;
			
		for( int i=0; i<Pos.length; i++ )
			Pos[i] = 0;
		
		for( int i=0; i<Neg.length; i++ )
			Neg[i] = 0;
		
	}	
}