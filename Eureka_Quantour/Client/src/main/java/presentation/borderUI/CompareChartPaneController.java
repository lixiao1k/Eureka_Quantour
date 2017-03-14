package presentation.borderUI;

import java.net.URL;
import java.util.ResourceBundle;

import dataController.DataContorller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import presentation.chart.barChart.ExtremeValueComparedChart;
import vo.ComparedInfoVO;
import presentation.chart.barChart.ExtremeValueComparedChart;

public class CompareChartPaneController implements Initializable{
	private DataContorller dataController = DataContorller.getInstance();
	private ComparedInfoVO vo = (ComparedInfoVO) dataController.get("COMPAREDINFO");
	
	@FXML
	AnchorPane compareChartPane1;
	
	@FXML
	AnchorPane compareChartPane2;
	
	@FXML
	AnchorPane compareChartPane3;
	
	@FXML
	protected void goBrowseClose(ActionEvent e){
		
	}
	
	@FXML
	protected void goBrowseLogYield(ActionEvent e){
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ExtremeValueComparedChart chart = new ExtremeValueComparedChart();
		BarChart<String, Number> barChart = chart.createChart(vo.getNameA(), vo.getNameB(), vo.getHighA(), vo.getHighB(), vo.getLowA(), vo.getLowB());
		compareChartPane1.getChildren().add(barChart);
		
	}

}
