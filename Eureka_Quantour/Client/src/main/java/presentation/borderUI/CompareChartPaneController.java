package presentation.borderUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import presentation.chart.barChart.ComparedChart;

public class CompareChartPaneController implements Initializable{
	@FXML
	AnchorPane compareChartPane1;
	
	@FXML
	AnchorPane compareChartPane2;
	
	@FXML
	AnchorPane compareChartPane3;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ComparedChart chart = new ComparedChart();
		BarChart<String, Number> barChart = chart.createChart("A", "B", 50, 60, 50, 40);
		compareChartPane1.getChildren().add(barChart);
		
	}

}
