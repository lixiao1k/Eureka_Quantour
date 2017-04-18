package presentation.chart.areaChart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class test extends Application{

	private int[] cycles;
	private Double[] dataList;
	private static AreaLineChart areaLineChart;
	
	private void testAreaLineChart(){
		cycles = new int[20];
		dataList = new Double[20];
		
		int j=2;
		for(int i=0; i<cycles.length; i++, j+=2){
			cycles[i] = j;
			dataList[i] = Math.random();
			if( i==4 || i==6 || i==7 || i==8 || i==15 || i==16)
				dataList[i] *= -1;
		}
		
		areaLineChart = new AreaLineChart(cycles, dataList, "dafa");
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 // TODO Auto-generated method stub
		 new test().testAreaLineChart();
		 
		try{
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
		        
			Scene scene = new Scene(areaLineChart.getchart(334, 200, true));
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
