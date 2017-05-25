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
		
		String stockcode = "000938";
		
		double alpha = 0.01;
		int numOfDay = 1;
		double maxs = 0;
		int maxsDay = 0;
		double maxz = 0;
		int maxsZhi = 0;
		int m = 50;
		int k = 5;
		for( int i=0; i<300; i++ ){
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
            for( int i=0; i<5; i++ ){
            	for( int j=1; j<22; j++){
            		if( j<20 ){
    					if( srod.wROD[i][j]<10 )
    						p.print( " "+srod.wROD[i][j]+" " );
    					else
    						p.print( srod.wROD[i][j]+" " );
    				}
    				else{
    					if( srod.wROD[i][j]<10 )
    						p.print( " "+srod.wROD[i][j]+" " );
    					else
    						p.print( srod.wROD[i][j]+" " );
    				}
            	}
            	p.println();
            }
            
            p.println();
            for( int i=0; i<5; i++){
            	if( srod.wROD[i][0]>=10 )
            		p.print( "星期 "+(i+1)+" 有 "+srod.wROD[i][0]+" 天跌 >10%;" );
            	else
            		p.print( "星期 "+(i+1)+" 有 "+" "+srod.wROD[i][0]+" 天跌 >10%;" );
            	p.print( " " );
            	if( srod.wROD[i][22]>=10 )
            		p.print( "有 "+srod.wROD[i][22]+" 天涨 >10%;" );    
            	else
            		p.print( "有 "+" "+srod.wROD[i][22]+" 天涨 >10%;" );  
            	
            	p.print(" ");
            	
            	if( srod.nodata[i][0]>=100 )
            		p.print( "有 "+srod.nodata[i][0]+" 天" );
            	else if( srod.nodata[i][0]>=10 )
            		p.print( "有 "+" "+srod.nodata[i][0]+" 天" );
            	else
            		p.print( "有 "+"  "+srod.nodata[i][0]+" 天" );
            	p.print( "  " );
            	if( srod.nodata[i][1]>=10 )
            		p.println( srod.nodata[i][1]+" 天没数据" );
            	else
            		p.println( " "+srod.nodata[i][1]+" 天没数据" );
            }
            
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

//          p.println();
//          for( int i=0; i<23; i++ ){
//              for( int j=0; j<23;j++ ){
//                  p.print(srod.firstFloor[i][j]+"  ");
//              }
//              p.println();
//          }
            p.close();
        }catch(FileNotFoundException e){
             e.printStackTrace();
        }
		
	}

}
