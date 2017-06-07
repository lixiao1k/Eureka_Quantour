package presentation.chart.lineChart;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import presentation.chart.chartService;
import presentation.chart.function.CatchMouseMove;
import presentation.chart.function.CatchMouseMoveService;
import presentation.chart.function.ListToArray;
import presentation.chart.function.ListToArrayService;

public class TimeShareChart implements chartService{

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private ListToArrayService listToArray = new ListToArray();
	
	private DecimalFormat df = new DecimalFormat("0.00");
	
	private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private LineChart<String, Number> lineChart;
    
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] point;
    private double MidValue = 0;
    private double MaxROD = 0;
    
    /**
     * @Description:
     * @author: 	 hzp
     * @date:        2017-05-20
     * @param        prices     every second's data in 4h( 9:30-11:30,13:00-15:00 ), length = 4*3600
     * @param        yMidValue  yesterday's close
     * @param        mergePoint 多少秒为一点
     */
	public TimeShareChart( List<Double> price, double yMidValue, int mergePoint){
		MidValue = yMidValue;

		Double[] prices = listToArray.changeDouble( price );
				
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
			if( second>=60 ){
				second -= 60;
				minute++;
				if( minute>=60 ){
					minute -= 60;
					hour++;
				}
				if( hour==11 && minute==30 ){
					hour = 13;
					minute = 0;
					second = 0;
				}
			}
		}

		double maxPrice = 0;
		double minPrice = 1000000;
		for( int i=0; i<prices.length; i++ ){
			if( prices[i] > maxPrice )
				maxPrice = prices[i];
			if( prices[i] < minPrice )
				minPrice = prices[i];
		}
		double maxROD = ( maxPrice - yMidValue ) / yMidValue;
		double minROD = ( yMidValue - minPrice ) / yMidValue;
		if( Math.abs(maxROD)>0.05 || Math.abs(minROD)>0.05 )
			MaxROD = 0.1;
		else if( Math.abs(maxROD)>0.025 || Math.abs(minROD)>0.025  )
			MaxROD = 0.05;
		else
			MaxROD = 0.025;
		
		xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setStartMargin(5);
        xAxis.setOpacity(1);

        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(false);
        yAxis.setAnimated(true);
        yAxis.setMaxWidth(0);
//      yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setTickLabelsVisible(false);
        double yMinValue = yMidValue * (1 - MaxROD);
        double yMaxValue = yMidValue * (1 + MaxROD);
        yAxis.setLowerBound( yMinValue );
        yAxis.setUpperBound( yMaxValue );
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        lineChart.setOpacity(0.8);
		
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for( int i=0; i<point.length; i++ ){
        	if( i*mergePoint<(prices.length-1) ){
        		if( prices[i*mergePoint]<yMinValue )
        			prices[i*mergePoint] = yMinValue;
        		if( prices[i*mergePoint]>yMaxValue )
        			prices[i*mergePoint] = yMaxValue;
        		serie.getData().add( new XYChart.Data<>(point[i], prices[i*mergePoint]) );
        		dataMap.put( point[i], "price:"+df.format(prices[i*mergePoint]) );
        	}
        	else{
        		serie.getData().add( new XYChart.Data<>(point[i], prices[prices.length-1]) );
        		dataMap.put( point[i], "price:"+df.format(prices[prices.length-1]) );
        	}
	    }
        lineChart.getData().add(serie);
	}
	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		int topjuli = 10;
		int timeHeight = 20;
		int rightjuli = 5;
		int leftjuli = 42;
		if( MaxROD==0.025 )
			leftjuli = 48;
		height = height-timeHeight-topjuli;
		width = width-leftjuli-rightjuli;
		lineChart.setPrefSize(width, height);
		
/* *********************************************************************************************************** */	
		StackPane timePane = new StackPane();
		timePane.setPrefSize(width, timeHeight);
		Label beginTime = new Label("9:30:00");
		Label midTime = new Label("11:30:00 / 13:00:00");
		Label endTime = new Label("15:00:00");
		timePane.getChildren().add(beginTime);
		timePane.getChildren().add(midTime);
		timePane.getChildren().add(endTime);
		StackPane.setAlignment(beginTime, Pos.CENTER_LEFT);
		StackPane.setAlignment(midTime, Pos.CENTER);
		StackPane.setAlignment(endTime, Pos.CENTER_RIGHT);
		timePane.getStylesheets().add(
    			getClass().getResource("/styles/DateLabel.css").toExternalForm() );
/* *********************************************************************************************************** */

		
/* *********************************************************************************************************** */	
		// chart and path
		AnchorPane chartAPane = new AnchorPane();
		StackPane chartSPane = new StackPane();
		Label info = catchMouseMove.catchMouseReturnInfoForStackPaneSN(lineChart, dataMap, point, "time", 5);
		chartSPane.getChildren().add(lineChart);
		chartSPane.getChildren().add(info);
		StackPane.setAlignment(lineChart, Pos.CENTER);
		chartAPane.getChildren().add(chartSPane);
		
		int pathLeftjuli = 10;
		Path path1 = new Path();
		path1.getElements().add( new MoveTo(pathLeftjuli, 2) );
		path1.getElements().add( new LineTo(width, 2) );
		path1.setStroke( Color.GRAY );
		Path path2 = new Path();
		path2.getElements().add( new MoveTo(pathLeftjuli, height/4) );
		path2.getElements().add( new LineTo(width, height/4) );
		path2.setStroke( Color.GRAY );
		Path path3 = new Path();
		path3.getElements().add( new MoveTo(pathLeftjuli, height/2) );
		path3.getElements().add( new LineTo(width, height/2) );
		path3.setStroke( Color.WHITE );
		Path path4 = new Path();
		path4.getElements().add( new MoveTo(pathLeftjuli, height/4*3) );
		path4.getElements().add( new LineTo(width, height/4*3) );
		path4.setStroke( Color.GRAY );
		chartAPane.getChildren().add(path1);
		chartAPane.getChildren().add(path2);
		chartAPane.getChildren().add(path3);
		chartAPane.getChildren().add(path4);
/* *********************************************************************************************************** */
		
		
/* *********************************************************************************************************** */
		// y轴坐标轴
		AnchorPane yPane = new AnchorPane();
		yPane.setPrefSize(leftjuli, height);
		
		Label top1 = new Label( "   "+df.format(MidValue*(1+MaxROD)) +"\n"+ "(+"+100*MaxROD+"%)" );
		Label top2 = new Label( "   "+df.format(MidValue*(1+MaxROD/2)) +"\n"+ "(+"+100*MaxROD/2+"%)" );
//		top1.setTextFill(Color.valueOf("#f15b6c"));
//		top2.setTextFill(Color.valueOf("#f15b6c"));
		top1.setTextFill(Color.rgb( 241, 91, 108 ) );
		top2.setTextFill(Color.rgb( 241, 91, 108 ) );
		StackPane topP = new StackPane();
		topP.setPrefSize(leftjuli, height/4+15);
		topP.getChildren().add(top1);
		topP.getChildren().add(top2);
		StackPane.setAlignment(top1, Pos.TOP_CENTER);
		StackPane.setAlignment(top2, Pos.BOTTOM_CENTER);
		
		Label mid = new Label( df.format(MidValue) );
		mid.setTextFill(Color.WHITE);
		StackPane botM = new StackPane();
		botM.setPrefSize(leftjuli, height/2);
		botM.getChildren().add(mid);
		StackPane.setAlignment(mid, Pos.CENTER);
		
		Label bot1 = new Label( "   "+df.format(MidValue*(1-MaxROD/2)) +"\n"+ "(-"+100*MaxROD/2+"%)"  );
		Label bot2 = new Label( "   "+df.format(MidValue*(1-MaxROD)) +"\n"+ "(-"+100*MaxROD+"%)"  );
//		bot1.setTextFill(Color.valueOf("#45b97c"));
//		bot2.setTextFill(Color.valueOf("#45b97c"));
		bot1.setTextFill(Color.rgb( 69, 185, 124 ) );
		bot2.setTextFill(Color.rgb( 69, 185, 124 ) );
		StackPane botP = new StackPane();
		botP.setPrefSize(leftjuli, height/4+15);
		botP.getChildren().add(bot1);
		botP.getChildren().add(bot2);
		StackPane.setAlignment(bot1, Pos.TOP_CENTER);
		StackPane.setAlignment(bot2, Pos.BOTTOM_CENTER);
		
		yPane.getChildren().add(topP);
		yPane.getChildren().add(botM);
		yPane.getChildren().add(botP);	
		AnchorPane.setTopAnchor(botM, height/4+0.0);
		AnchorPane.setTopAnchor(botP, height/4*3-15.0);
/* *********************************************************************************************************** */		
	
		
/* *********************************************************************************************************** */		
		AnchorPane pane = new AnchorPane();
		pane.getChildren().add(yPane);
		pane.getChildren().add(chartAPane);
		pane.getChildren().add(timePane);
		AnchorPane.setTopAnchor(yPane, topjuli+0.0);
		AnchorPane.setTopAnchor(chartAPane, topjuli+0.0);
		AnchorPane.setLeftAnchor(chartAPane, leftjuli+0.0);
		AnchorPane.setRightAnchor(chartAPane, rightjuli+0.0);
		AnchorPane.setTopAnchor(timePane, height+topjuli+0.0);
		AnchorPane.setLeftAnchor(timePane, leftjuli+0.0);
		AnchorPane.setRightAnchor(timePane, rightjuli+0.0);
		
		info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/TimeShareChart.css").toExternalForm() );
/* *********************************************************************************************************** */		
		return pane;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}
	
}
