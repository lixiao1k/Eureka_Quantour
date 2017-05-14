package presentation.chart.scatterchart;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import en_um.ChartKind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presentation.chart.function.SaveAs;
import presentation.chart.function.SaveAsService;
import rmi.RemoteHelper;
import vo.StockRODVO;



public class test 
//	extends Application
	{
	
	private static PointChart pointChart;
	
	private RemoteHelper remote = RemoteHelper.getInstance();
	private NumberFormat nf = NumberFormat.getPercentInstance();
	
	private SaveAsService sas = new SaveAs();
    
	private List<String> dataName = new ArrayList<>();
	
	public void testYieldPointChart3(){
		int[] numI1 = {5, 1, 3, 2, 0, 5, 7, 7, 4, 6, 4, 9, 12, 5, 8, 5, 4, 1, 2, 1, 4};
		int[] numI2 = {0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0};
		int[] numI3 = {12, 1, 1, 2, 1, 6, 6, 8, 7, 4, 0, 7, 12, 5, 8, 5, 2, 2, 3, 1, 1};
		int[] numI4 = {1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1};
		int[] numI5 = {14, 4, 1, 1, 3, 4, 6, 7, 8, 7, 1, 8, 4, 6, 4, 2, 4, 0, 1, 1, 0};
		List<int[]> numI = new ArrayList<>();
//		numI.add(numI1);
//		numI.add(numI2);
//		numI.add(numI3);
//		numI.add(numI4);
		numI.add(numI5);
		
		dataName.add("Mon");
		dataName.add("Tue");
		dataName.add("Wen");
		dataName.add("Th");
		dataName.add("Fri");
	
		int[] yield = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		
		pointChart = new PointChart( yield, numI, dataName, ChartKind.POINTONE, true);		
	}
	
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
		try{
			srod = remote.getForecastROD().getStockROD( stockcode, begindate, enddate, 0.01 );
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
    						p.print( " "+srod.wROD[i][j] +" ");
    					else
    						p.print( srod.wROD[i][j] +" ");
    				}
    				else{
    					if( srod.wROD[i][j]<10 )
    						p.print( " "+srod.wROD[i][j] );
    					else
    						p.print( srod.wROD[i][j] );
    				}
            	}
            	p.println();
            }
            
            p.println();
            for( int i=0; i<5; i++){
            	if( srod.wROD[i][0]>=10 )
            		p.print( "星期 "+(i+1)+" 有 "+" "+srod.wROD[i][0]+" 天跌 >10%" );
            	else
            		p.print( "星期 "+(i+1)+" 有 "+"  "+srod.wROD[i][0]+" 天跌 >10%" );
            	p.print( "   " );
            	if( srod.wROD[i][22]>=10 )
            		p.println( " 有 "+srod.wROD[i][22]+" 天涨 >10%" );    
            	else
            		p.println( " 有 "+" "+srod.wROD[i][22]+" 天涨 >10%" );    
            }
            
            p.println();  
            for( int i=0; i<5; i++){
            	if( srod.nodata[i][0]>=100 )
            		p.print( "星期 "+(i+1)+" 有 "+srod.nodata[i][0]+" 天" );
            	else if( srod.nodata[i][0]>=10 )
            		p.print( "星期 "+(i+1)+" 有 "+" "+srod.nodata[i][0]+" 天" );
            	else
            		p.print( "星期 "+(i+1)+" 有 "+"  "+srod.nodata[i][0]+" 天" );
            	p.print( "   " );
            	if( srod.nodata[i][1]>=10 )
            		p.println( " 有 "+srod.nodata[i][1]+" 天没数据" );
            	else
            		p.println( " 有 "+" "+srod.nodata[i][1]+" 天没数据" );
            }
            
            p.println();
            int num = 0;
            for( int i=0; i<srod.ROETimes.length-1; i++){
            	p.println( "误差 0.00"+i+"~0.00"+(i+1)+": " +srod.ROETimes[i] );
            	num += srod.ROETimes[i];
            }
            
            p.println();
            int it = srod.ROETimes[srod.ROETimes.length-1];
            double num2 = num + it;
            if( num>=100 )
            	p.println( "误差 <=0.01: " + num + "  " + nf.format( num/num2 ) );
            else
            	p.println( "误差 <=0.01: " + num + "   " + nf.format( num/num2 ) );
            if( it>=100 )
            	p.println( "误差  >0.01: " + it + "  " + nf.format( it/num2 ) );
            else
            	p.println( "误差  >0.01: " + it + "   " + nf.format( it/num2 ) );
            
            p.println();
            if( srod.Pos[0]>=100 )
            	p.print( "预测涨， 成功"+srod.Pos[0]);
            else
            	p.print( "预测涨， 成功"+" "+srod.Pos[0]);
            p.print("   ");
            if( srod.Pos[1]>=100 )
            	p.print( "失败"+srod.Pos[1]);
            else
            	p.print( "失败"+" "+srod.Pos[1]);
            
            p.println();
            if( srod.Neg[0]>=100 )
            	p.print( "预测跌， 成功"+srod.Neg[0]);
            else
            	p.print( "预测跌， 成功"+" "+srod.Neg[0]);
            p.print("   ");
            if( srod.Neg[1]>=100 )
            	p.print( "失败"+srod.Neg[1]);
            else
            	p.print( "失败"+" "+srod.Neg[1]);
            
            p.close();
        }catch(FileNotFoundException e){
             e.printStackTrace();
        }
		
	}
	
//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		// TODO Auto-generated method stub
//
//		new test().testYieldPointChart3();
//
//		Pane pane = new Pane();
//		Scene scene;
//		Stage dialogStage = new Stage();
//		try{
//			dialogStage.setTitle("Birthday Statistics");
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.initOwner(primaryStage);
//			  
//			pane = pointChart.getchart(334, 200, true);
////			pane = pointChart.getchart(334, 200, false);
//			
//			scene = new Scene( pane );
////			sas.saveAsPng(scene, "星期五");
//			
//			dialogStage.setScene(scene);
//			dialogStage.show();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
		
//	public static void main(String args[]){
//		launch(args);
//	}

}
