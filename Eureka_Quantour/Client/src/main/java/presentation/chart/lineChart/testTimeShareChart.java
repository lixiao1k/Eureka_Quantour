package presentation.chart.lineChart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class testTimeShareChart extends Application{
	
	private static TimeShareChart chart;
	
	private void testChart(){
		double[] prices = new double[14400];
		double d = 7.8; 
		for( int i=0; i<prices.length; i++ ){
			double dt = 0;
			if( Math.random()>0.5 )
				dt = d*(1+Math.random()/15);
			else
				dt = d*(1-Math.random()/15);
			prices[i] = dt;
		}
		chart = new TimeShareChart(prices, 7.8, 60);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		new testTimeShareChart().testChart();
		
		try{
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Birthday Statistics");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
//	        Scene scene = new Scene(chart.getchart(334, 200, true));
	        Scene scene = new Scene(chart.getchart(800, 400, true));
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
