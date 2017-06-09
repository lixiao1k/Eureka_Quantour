package logic.supportimpl;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.NullDateException;
import exception.NullStockIDException;
import vo.SingleStockInfoVO;

public class testBPNetImpl {
	
	private static IDataInterface idata = new DataInterfaceImpl();
	
	private static DecimalFormat df = new DecimalFormat("#0.0000"); 
	
	private static LocalDate zuizao = LocalDate.of(2005,2,1);
	private static String stockcode = "000938";
	
	
	public static void main( String args[] ){
		
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		
		// 创建序列
	    List<Double> openPrice = new ArrayList<Double>();
	    List<Double> highPrice = new ArrayList<Double>();
	    List<Double> closePrice = new ArrayList<Double>();
	    List<Double> lowPrice = new ArrayList<Double>();
	    List<Double> vol = new ArrayList<Double>();
	    
		// get before 100 days' data
		int vLen = 100;
		LocalDate dateT = LocalDate.of(2008, 4, 1);
		int index = vLen-1;
		while( index>-1 && dateT.compareTo(zuizao)>0 ){
			try{
				dateT = new testBPNetImpl().getValidBeforeDate( dateT );
				ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, dateT) );
				openPrice.add( ssi.getOpen() );
				highPrice.add( ssi.getHigh() );
				closePrice.add( ssi.getClose() );
				lowPrice.add( ssi.getLow() );
				vol.add( (double)ssi.getVolume() );
				index--;
			}catch ( NullStockIDException e ){
				e.printStackTrace();
			}catch ( NullDateException e){}
		}
		
	    BPNetSupportImpl m = new BPNetSupportImpl();
	    double[][] dataset = m.bpTrain( closePrice, highPrice, lowPrice, openPrice, vol );
	    double[][] target = new double[dataset.length][];
	    for( int i=0; i<dataset.length; i++ ){
	        target[i] = new double[1];
	        target[i][0] = closePrice.get( closePrice.size() - dataset.length + i );
	    }
	    
	    
	    
	    //创建序列
        List<Double> openPrice2 = new ArrayList<Double>();
        List<Double> highPrice2 = new ArrayList<Double>();
        List<Double> closePrice2 = new ArrayList<Double>();
        List<Double> lowPrice2 = new ArrayList<Double>();
        List<Double> vol2 = new ArrayList<Double>();
        
        // get before 100 days' data
        dateT = LocalDate.of(2008, 4, 1);
        index = vLen-1;
        while( index>-1 && dateT.compareTo(zuizao)>0 ){
        	try{
        		dateT = new testBPNetImpl().getValidLatterDate( dateT );
        		ssi = new SingleStockInfoVO( idata.getSingleStockInfo(stockcode, dateT) );
        		openPrice2.add( ssi.getOpen() );
        		highPrice2.add( ssi.getHigh() );
        		closePrice2.add( ssi.getClose() );
        		lowPrice2.add( ssi.getLow() );
        		vol2.add( (double)ssi.getVolume() );
        		index--;
        	}catch ( NullStockIDException e ){
        		e.printStackTrace();
        	}catch ( NullDateException e){}
        }
        
        BPNetSupportImpl m2 = new BPNetSupportImpl();
        double[][] dataset2 = m2.bpTrain( closePrice2, highPrice2, lowPrice2, openPrice2, vol2 );
        double[][] target2 = new double[dataset2.length][];
        for(int i = 0;i<dataset2.length;i++){
            target2[i] = new double[1];
            target2[i][0] = closePrice2.get( closePrice2.size() - dataset2.length + i );
        }
        
        BPNetImpl bp = new BPNetImpl( new int[]{6,13,13,1}, 0.15, 0.8 );
        //迭代训练5000次
        for( int n=0; n<50000; n++ )
            for( int i=0; i<dataset.length; i++ )
                bp.train( dataset[i], target[i] );


        //测试数据集
        double[] result = new double[dataset2.length];
        List<ArrayList<Double>> resultList = new ArrayList<ArrayList<Double>>();
        for( int i=0; i<dataset2.length; i++ ){
            double[] a = bp.computeOut( dataset2[i] );
            ArrayList<Double> list = new ArrayList<Double>();
            result[i] = 100*(-Math.log(1/a[0]-1));
            list.add(result[i]);
            list.add(target2[i][0]);
            resultList.add(list);
            System.out.println( Arrays.toString(dataset2[i])
            		+ ":" + df.format(result[i])
            		+ " real:" + df.format(target2[i][0]) );
        }
		
	}
	
	
	 private int getDayOfWeek( LocalDate date){
 		String dow = date.getDayOfWeek().toString();
 		if( dow.equals("MONDAY"))
 			return 0;
 		else if( dow.equals("TUESDAY") )
 			return 1;
 		else if( dow.equals("WEDNESDAY") )
 			return 2;
 		else if( dow.equals("THURSDAY") )
 			return 3;
 		else if( dow.equals("FRIDAY"))
 			return 4;
 		else
 			return -1;
 	}
     
	 
     private LocalDate getValidLatterDate( LocalDate date ){
 		date = date.plusDays(1);
 		while( getDayOfWeek(date)==-1 )
 			date = date.plusDays(1);
 		return date;
 	}

     
 	private LocalDate getValidBeforeDate( LocalDate date ){
 		date = date.minusDays(1);
 		while( getDayOfWeek(date)==-1 )
 			date = date.minusDays(1);
 		return date;
 	}
}
