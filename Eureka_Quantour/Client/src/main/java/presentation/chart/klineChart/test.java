package presentation.chart.klineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vo.SingleStockInfoVO;

/**
 * 
 * @Description: Test ComparedChart. when test is finished, I will delete it.
 * @author: hzp
 * @time: 2017年4月1日
 */
public class test extends Application{
	
    private static KLineChart KLineChart;
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
    		}
    		else{
    			ssi.setOpen(d*9);
    			ssi.setClose(d*7);
    			ssi.setHigh(d*11);
    			ssi.setLow(d*5.5);
    		}
    		stocklist.add(ssi);
    	}
    	KLineChart = new KLineChart(stocklist);
    }
    
    @Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		new test().testKLineChart();
		try{
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Birthday Statistics");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
	        Scene scene = new Scene(KLineChart.getchart(600, 500));
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

