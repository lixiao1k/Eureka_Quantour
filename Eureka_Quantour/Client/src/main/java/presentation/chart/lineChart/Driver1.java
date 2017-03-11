package presentation.chart.lineChart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Driver1 extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		String[] str ={"2017/11/11","2017/11/12","2017/11/13"};
		Number[] num1 ={201,222,204};
		Number[] num2 ={50,60,70};
		CloseValueCompareChart chart = new CloseValueCompareChart(str, num1, num2);
		Scene scene = new Scene(chart.getchart());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
