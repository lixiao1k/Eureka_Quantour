package presentation.chart.barChart;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vo.YieldDistributionHistogramDataVO;

public class test extends Application{
	
	private static YieldDistributeChart yieldDistuributeChart;
	
	private List<Double> yieldlist = new ArrayList<>();
    private List<Integer> minuslist = new ArrayList<>();
    private List<Integer> pluslist = new ArrayList<>();
    
	private void testYieldDistributeChart(){
		YieldDistributionHistogramDataVO ydhd;
		
		for( int i=0; i<10; i++){
			yieldlist.add( Math.random() );
			minuslist.add( (int)(Math.random()*60) );
			pluslist.add( (int)(Math.random()*60) );
		}
		ydhd = new YieldDistributionHistogramDataVO(yieldlist, minuslist, pluslist);
		
		yieldDistuributeChart = new YieldDistributeChart(ydhd);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 // TODO Auto-generated method stub
		 new test().testYieldDistributeChart();
		 
		try{
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
		        
			Scene scene = new Scene(yieldDistuributeChart.getchart(334, 200));
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
