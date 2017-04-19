package presentation.chart.piechart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class test extends Application{
	
	private static FanChart fanChart;
    
	private String[] nameOfContent;
	private int[] num;
			
	private void testPointChart(){
		nameOfContent = new String[5];
		num = new int[5];
		
		nameOfContent[0] = "s1";
		nameOfContent[1] = "s2";
		nameOfContent[2] = "s3";
		nameOfContent[3] = "s4";
		nameOfContent[4] = "s5";
		
		num[0] = 45;
		num[1] = 24;
		num[2] = 25;
		num[3] = 35;
		num[4] = 12;
		
		fanChart = new FanChart( nameOfContent, num );
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 // TODO Auto-generated method stub
		 new test().testPointChart();
		 
		try{
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
		        
//			Scene scene = new Scene(fanChart.getchart(334, 200, true));
			Scene scene = new Scene(fanChart.getchart(334, 200, false));
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
