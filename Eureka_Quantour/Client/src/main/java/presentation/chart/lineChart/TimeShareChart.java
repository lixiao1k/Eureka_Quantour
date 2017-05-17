package presentation.chart.lineChart;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import presentation.chart.chartService;
import presentation.chart.function.CatchMouseMove;
import presentation.chart.function.CatchMouseMoveService;

public class TimeShareChart implements chartService{

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	
	private DecimalFormat df = new DecimalFormat("0.00");
	
	private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private LineChart<String, Number> lineChart;
    
    private Map<String, String> dataMap = new HashMap<String,String>();
    String[] point;
    
	public TimeShareChart( double[] prices, double yMidValue, int mergePoint){
		if( mergePoint<1 )
			mergePoint = 1;
		if( mergePoint>prices.length )
			mergePoint = prices.length/2;
		int arrayLen = prices.length / mergePoint + 1;
		point = new String[arrayLen];
		int hour = 9;
		int minute = 30;
		int second = 0;
		for( int i=0; i<arrayLen; i++ ){
			point[i] = hour+":"+minute+":"+second;
			second += mergePoint;
			if( second>60 ){
				second -= 60;
				minute++;
				if( minute>59 ){
					minute -= 59;
					hour++;
				}
				if( hour==11 && minute==30 ){
					hour = 13;
					minute = 0;
					second = 0;
				}
			}
		}
		
		xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setStartMargin(5);
        xAxis.setOpacity(1);

        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(false);
        yAxis.setAnimated(true);
//      yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setLowerBound( yMidValue*0.9 );
        yAxis.setUpperBound( yMidValue*1.1 );
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(true);
        lineChart.setOpacity(0.8);
		
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for( int i=0; i<point.length; i++ ){
	        if( prices[i*mergePoint]<(yMidValue*0.8) )
	        	prices[i*mergePoint] = yMidValue*0.9;
	        if( prices[i*mergePoint]>(yMidValue*1.2) )
	        	prices[i*mergePoint] = yMidValue*1.1;
	        serie.getData().add( new XYChart.Data<>(point[i], prices[i*mergePoint]) );
	        dataMap.put( point[i], "price:"+df.format(prices[i*mergePoint]) );
	    }
        lineChart.getData().add(serie);
	}
	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		int timeHeight = 20;
		height -= timeHeight;
		lineChart.setPrefSize(width, height);
		
		StackPane timePane = new StackPane();
		timePane.setPrefSize(width, timeHeight);
		Label beginTime = new Label("9:30:00");
		Label midTime = new Label("11:30:00\n"+"13:00:00");
		Label endTime = new Label("15:00:00");
		timePane.getChildren().add(beginTime);
		timePane.getChildren().add(midTime);
		timePane.getChildren().add(endTime);
		StackPane.setAlignment(beginTime, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(midTime, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(endTime, Pos.BOTTOM_RIGHT);
		timePane.getStylesheets().add(
    			getClass().getResource("/styles/DateLabel.css").toExternalForm() );
		
		StackPane chartPane = new StackPane();
		Label info = catchMouseMove.catchMouseReturnInfoForStackPaneSN(lineChart, dataMap, point, "time", 5);
		chartPane.getChildren().add(lineChart);
		chartPane.getChildren().add(info);
		StackPane.setAlignment(lineChart, Pos.CENTER);
		
		AnchorPane pane = new AnchorPane();
		pane.getChildren().add(chartPane);
		pane.getChildren().add(timePane);
		AnchorPane.setTopAnchor(timePane, height+0.0);
		
		info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/ComparedLineChart.css").toExternalForm() );
		
		return pane;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}
	
}
