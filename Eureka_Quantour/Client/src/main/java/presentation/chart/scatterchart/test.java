package presentation.chart.scatterchart;

import java.util.ArrayList;
import java.util.List;

import en_um.ChartKind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class test extends Application{
	
	private static PointChart pointChart;
    
	private List<Double[]> yield = new ArrayList<>();
	private List<int[]> num = new ArrayList<>();
	private List<String> dataName = new ArrayList<>();
	private void testPointChart(){
		Double[] yieldD = new Double[21];
		int[] numI = new int[21];
		dataName.add("test");
		
		Double j = -10.0;
		for( int i=0; i<=20; i++, j++)
			yieldD[i] = j;
		numI[0] = 5;
		numI[1] = 6;
		numI[2] = 4;
		numI[3] = 8;
		numI[4] = 8;
		numI[5] = 4;
		numI[6] = 12;
		numI[7] = 13;
		numI[8] = 20;
		numI[9] = 35;

		numI[10] = 40;
		numI[11] = 36;
		numI[12] = 22;
		numI[13] = 15;
		numI[14] = 14;
		numI[15] = 8;
		numI[16] = 15;
		numI[17] = 8;
		numI[18] = 6;
		numI[19] = 3;
		numI[20] = 2;
	
		yield.add(yieldD);
		num.add(numI);
		
		pointChart = new PointChart( yield, num, dataName, ChartKind.POINTFULL);
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
		        
			Scene scene = new Scene(pointChart.getchart(334, 200, true));
//			Scene scene = new Scene(yieldDistuributeChart.getchart(334, 200, false));
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
