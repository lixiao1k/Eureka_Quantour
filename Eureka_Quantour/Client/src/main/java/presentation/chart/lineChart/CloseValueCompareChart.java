package presentation.chart.lineChart;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.chart.chartService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;

/**
 * A chart in which lines connect a series of data points. Useful for viewing
 * data trends over time.
 *
 * @see javafx.scene.chart.LineChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.Axis
 * @see javafx.scene.chart.NumberAxis
 * @related charts/area/AreaChart
 * @related charts/scatter/ScatterChart
 */
public class CloseValueCompareChart implements chartService {
	    private String [] xValue;
	    private Number [] yValue1;
	    private Number [] yValue2;
	    protected NumberAxis yAxis;
	    protected CategoryAxis xAxis;
	    protected LineChart<String, Number> chart;
	    
	    public CloseValueCompareChart(String [] args1,Number [] args2,Number[] args3) {
			// TODO Auto-generated constructor stub
	    	xValue = args1;
	    	yValue1 = args2;
	    	yValue2 = args3;
	    	ObservableList<String> x = FXCollections.observableArrayList();
	    	x.addAll(xValue);
	        xAxis = new CategoryAxis(x);
	        yAxis = new NumberAxis();
	        yAxis.autoRangingProperty().set(true);
	        yAxis.setForceZeroInRange(true);
	        ObservableList<XYChart.Data<String, Number>> os1 = FXCollections.observableArrayList();
	        int length = xValue.length;
	        for(int i=0;i<length;i++){
	        	os1.add(new XYChart.Data<String, Number>(xValue[i], yValue1[i]));
	        }
	        
	        ObservableList<XYChart.Data<String, Number>> os2 = FXCollections.observableArrayList();
	        for(int i=0;i<length;i++){
	        	os1.add(new XYChart.Data<String, Number>(xValue[i], yValue2[i]));
	        }
	        ObservableList<XYChart.Series<String, Number>> lineChartData = FXCollections.observableArrayList(
	            new LineChart.Series<String,Number>("Series1",os1),
	            new LineChart.Series<String,Number>("Series2",os2)
	        );
	        
//	        ObservableList<XYChart.Series<Double,Double>> lineChartData = FXCollections.observableArrayList(
//	            new LineChart.Series<Double,Double>("Series 1", FXCollections.observableArrayList(
//	                new XYChart.Data<Double,Double>(0.0, 1.0),
//	                new XYChart.Data<Double,Double>(1.2, 1.4),
//	                new XYChart.Data<Double,Double>(2.2, 1.9),
//	                new XYChart.Data<Double,Double>(2.7, 2.3),
//	                new XYChart.Data<Double,Double>(2.9, 0.5)
//	            )),
//	            new LineChart.Series<Double,Double>("Series 2", FXCollections.observableArrayList(
//	                new XYChart.Data<Double,Double>(0.0, 1.6),
//	                new XYChart.Data<Double,Double>(0.8, 0.4),
//	                new XYChart.Data<Double,Double>(1.4, 2.9),
//	                new XYChart.Data<Double,Double>(2.1, 1.3),
//	                new XYChart.Data<Double,Double>(2.6, 0.9)
//	            ))
//	        );
	        
	        chart = new LineChart(xAxis, yAxis, lineChartData);

	    	
		}
	    
	    
		@Override
		public XYChart<String, Number> getchart() {
			// TODO Auto-generated method stub
			return chart;
		}

		
}
