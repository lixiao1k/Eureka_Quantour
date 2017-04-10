package presentation.chart.klineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presentation.chart.barChart.VolumeChart;
import vo.SingleStockInfoVO;

/**
 * 
 * @Description: Test ComparedChart. when test is finished, I will delete it.
 * @author: hzp
 * @time: 2017年4月1日
 */
public class test extends Application{
	
    private static KLineChart KLineChart;
    private static VolumeChart volumeChart;
    private List<SingleStockInfoVO> stocklist = new ArrayList<>();
    
    private void testKLineChart(){
    	SingleStockInfoVO ssi = new SingleStockInfoVO();
    	Calendar cal = Calendar.getInstance();
    	int j = 2;
    	for(int i=0; i<20; i++, j++){
    		ssi = new SingleStockInfoVO();
    		cal.set(2014, 3, j);
    		ssi.setDate( (Calendar)cal.clone() );
    		double d = Math.random();
    		if( d>0.5 ){
    			ssi.setOpen(d*7);
    			ssi.setClose(d*9);
    			ssi.setHigh(d*10);
    			ssi.setLow(d*6);
    			ssi.setVolume((long)(d*8000));
    		}
    		else{
    			ssi.setOpen(d*9);
    			ssi.setClose(d*7);
    			ssi.setHigh(d*11);
    			ssi.setLow(d*5.5);
    			ssi.setVolume((long)(d*11000));
    		}
    		stocklist.add(ssi);
    	}
    	KLineChart = new KLineChart(stocklist);
    }
    
    private void testVolumeChart(){
    	SingleStockInfoVO ssi = new SingleStockInfoVO();
    	Calendar cal = Calendar.getInstance();
    	int j = 2;
    	for(int i=0; i<20; i++, j++){
    		ssi = new SingleStockInfoVO();
    		cal.set(2014, 3, j);
    		ssi.setDate( (Calendar)cal.clone() );
    		double d = Math.random();
    		if( d>0.5 ){
    			ssi.setOpen(d*7);
    			ssi.setClose(d*9);
    			ssi.setVolume((long)(d*8000));
    		}
    		else{
    			ssi.setOpen(d*9);
    			ssi.setClose(d*7);
    			ssi.setVolume((long)(d*11000));
    		}
    		stocklist.add(ssi);
    	}
    	volumeChart = new VolumeChart(stocklist);
    }
    
    @Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		new test().testKLineChart();
//		new test().testVolumeChart();
		try{
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Birthday Statistics");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
	        Scene scene = new Scene(KLineChart.getchart(334, 200));
//	        Scene scene = new Scene(KLineChart.getchart(500, 400));
	        dialogStage.setScene(scene);

	        dialogStage.show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
    	launch(args);
    }
}

