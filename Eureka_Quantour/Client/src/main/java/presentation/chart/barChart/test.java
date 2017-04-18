package presentation.chart.barChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vo.YieldDistributionHistogramDataVO;

public class test extends Application{
	
	private static YieldDistributeChart yieldDistuributeChart;
	
	private Map<Double,List<Integer>> zuhe = new HashMap<Double,List<Integer>>();
    private List<Integer> pandm = new ArrayList<>();
    
	private void testYieldDistributeChart(){
		YieldDistributionHistogramDataVO ydhd;
		
		for( int i=0; i<10; i++){
			double date = Math.random();
			pandm.add( (int)(date*30) );
			pandm.add( (int)(date*30) );
			zuhe.put( date, pandm);
		}
		ydhd = new YieldDistributionHistogramDataVO( zuhe );
		
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
		        
			Scene scene = new Scene(yieldDistuributeChart.getchart(334, 200, true));
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
