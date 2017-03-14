package presentation.borderUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import dataController.DataContorller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import presentation.chart.chartService;
import presentation.chart.barChart.ExtremeValueComparedChart;
import presentation.chart.lineChart.ComparedCloseChart;
import presentation.chart.lineChart.EMAChart;
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
	protected void goBrowesClose(ActionEvent e){
		compareChartPane3.getChildren().clear();
        Calendar[] date = vo.getDate();
        double[] closeA = vo.getCloseA();
        double[] closeB = vo.getCloseB();
        String stockA = vo.getNameA();
        String stockB = vo.getNameB();
		chartService service = new ComparedCloseChart(date, closeA, closeB, stockA, stockB);
		XYChart<String, Number> closeComparedchart = service.getchart();
		compareChartPane3.getChildren().add(closeComparedchart);
	}
	
	@FXML
	protected void goBrowseLogYield(ActionEvent e){
		compareChartPane3.getChildren().clear();
        Calendar[] date = vo.getDate();
        double[] logYieldA = vo.getLogYieldA();
        double[] logYieldB = vo.getLogYieldB();
        String stockA = vo.getNameA();
        String stockB = vo.getNameB();
		chartService service = new ComparedCloseChart(date, logYieldA, logYieldB, stockA, stockB);
		XYChart<String, Number> closeComparedchart = service.getchart();
		compareChartPane3.getChildren().add(closeComparedchart);
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ExtremeValueComparedChart chart = new ExtremeValueComparedChart();
		BarChart<String, Number> barChart = chart.createChart(vo.getNameA(), vo.getNameB(), vo.getHighA(), vo.getHighB(), vo.getLowA(), vo.getLowB());
		compareChartPane1.getChildren().add(barChart);
		
	}

}
