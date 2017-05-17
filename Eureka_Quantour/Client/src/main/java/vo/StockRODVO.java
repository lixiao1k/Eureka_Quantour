package vo;

import java.io.Serializable;

public class StockRODVO implements Serializable{

	private static final long serialVersionUID = 8243916259062852191L;
	
	
	public int[][] wROD = new int[5][23];
	public int[][] RODw = new int[23][5];
	/*
	* [][0] presents the number of days which xingqi owns
	* [][1] presents the number of days which xingqi doesn't has data
	*/
	public int[][] nodata = new int[5][2];

	/*
	* yesterday and today's ROD guanxi
	* yesterday,the day before yesterday and today's ROD guanxi
	*/
	public int[][] firstFloor = new int[23][23];
	public int[][][] secondFloor = new int[23][23][23];

	public int[][] YesAvgROD = new int[23][23];

	public int[] zhixin = new int[2];
	
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
			
		for( int i=0; i<firstFloor.length; i++ )
			for( int j=0; j<firstFloor[0].length; j++ )
				firstFloor[i][j] = 0;

		for( int i=0; i<secondFloor.length; i++ )
			for( int j=0; j<secondFloor[0].length; j++ )
				for( int k=0; k<secondFloor[0][0].length; k++ )
					secondFloor[i][j][k] = 0;

		for( int i=0; i<YesAvgROD.length; i++ )
			for( int j=0; j<YesAvgROD[0].length; j++ )
				YesAvgROD[i][j] = 0;

		for( int i=0; i<zhixin.length; i++ )
			zhixin[i] = 0;

		for( int i=0; i<Pos.length; i++ )
			Pos[i] = 0;
		
		for( int i=0; i<Neg.length; i++ )
			Neg[i] = 0;
		
	}	
}
