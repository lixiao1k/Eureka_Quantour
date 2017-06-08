package presentation.chart.scatterchart;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.time.LocalDate;

import rmi.RemoteHelper;
import vo.StockRODVO;



public class test {
	private RemoteHelper remote = RemoteHelper.getInstance();
	private NumberFormat nf = NumberFormat.getPercentInstance();
	
	/**
	 * @Description: 获取股票收益率分布
	 * @author: hzp
	 * @date: May 13, 2017
	 */
	public void getROD(){
		StockRODVO srod = new StockRODVO();
		LocalDate begindate = LocalDate.of(2013, 3, 6);
		LocalDate enddate = LocalDate.of(2016, 3, 6);
		
//		String stockcode = "000938";
		String stockcode = "300038";
//		String stockcode = "600149";
		
		double alpha = 0.01;
		int numOfDay = 100;
		double maxs = 0;
		int maxsDay = 0;
		double maxz = 0;
		int maxsZhi = 0;
		int m = 50;
		int k = 5;
		
		for( int i=0; i<120; i++ ){
			try{
				srod = remote.getForecastROD().getStockROD( stockcode, begindate, enddate, numOfDay, alpha, m, k);
			}catch(RemoteException e){
				e.printStackTrace();
			}
			if( (srod.Pos[0]+srod.Neg[0]) / ((srod.Pos[0]+srod.Neg[0])+(srod.Pos[1]+srod.Neg[1])+0.0)>maxs ){
				maxs = (srod.Pos[0]+srod.Neg[0]) / ((srod.Pos[0]+srod.Neg[0])+(srod.Pos[1]+srod.Neg[1])+0.0);
				maxsDay = numOfDay;
			}
			if( srod.zhixin[0]/(srod.zhixin[0]+srod.zhixin[1]+0.0)>maxz ){
				maxz = srod.zhixin[0]/(srod.zhixin[0]+srod.zhixin[1]+0.0);
				maxsZhi = numOfDay;
			}
			numOfDay++;
		}

		try{
			srod = remote.getForecastROD().getStockROD( stockcode, begindate, enddate, maxsDay, alpha, m, k);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		
		try{
            FileOutputStream out=new FileOutputStream( stockcode+".txt" );
            PrintStream p=new PrintStream(out);
            
            p.println();
            p.println( stockcode );
            p.println();
            
            p.println();
            if( srod.Pos[0]>=100 )
            	p.print( "预测涨，成功 "+srod.Pos[0]);
            else
            	p.print( "预测涨，成功 "+" "+srod.Pos[0]);
            p.print("   ");
            if( srod.Pos[1]>=100 )
            	p.println( "失败 "+srod.Pos[1]);
            else
            	p.println( "失败 "+" "+srod.Pos[1]);

            if( srod.Neg[0]>=100 )
            	p.print( "预测跌，成功 "+srod.Neg[0]);
            else
            	p.print( "预测跌，成功 "+" "+srod.Neg[0]);
            p.print("   ");
            if( srod.Neg[1]>=100 )
            	p.println( "失败 "+srod.Neg[1]);
            else
            	p.println( "失败 "+" "+srod.Neg[1]);
            
            if( (srod.Pos[0]+srod.Neg[0])>=100 )
            	p.print( "总预测，成功 "+ (srod.Pos[0]+srod.Neg[0]) );
            else
            	p.print( "总预测，成功 "+" "+ (srod.Pos[0]+srod.Neg[0]) );
            p.print("   ");
            if( (srod.Pos[1]+srod.Neg[1])>=100 )
            	p.println( "失败 "+ (srod.Pos[1]+srod.Neg[1]) );
            else
            	p.println( "失败 "+" "+ (srod.Pos[1]+srod.Neg[1]) );
            
            p.println("预测成功率 : "+nf.format( 
            		(srod.Pos[0]+srod.Neg[0]) / ((srod.Pos[0]+srod.Neg[0])+(srod.Pos[1]+srod.Neg[1])+0.0) )  );
            p.println("最优预测成功率预估天数 : "+maxsDay);
            
            
            try{
    			srod = remote.getForecastROD().getStockROD( stockcode, begindate, enddate, maxsZhi, alpha, 50, 5);
    		}catch(RemoteException e){
    			e.printStackTrace();
    		}
            
            p.println();
            p.println("在置信区间 "+srod.zhixin[0]+"  "+"不在 "+srod.zhixin[1]);
            p.println("在置信区间 "+nf.format( srod.zhixin[0]/(srod.zhixin[0]+srod.zhixin[1]+0.0) ));
            p.println("最优预测置信区间预估天数 : "+maxsZhi);

            p.close();
        }catch(FileNotFoundException e){
             e.printStackTrace();
        }
		
	}

}
