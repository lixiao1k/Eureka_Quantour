package presentation.chart.scatterchart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import en_um.ChartKind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import presentation.chart.chartService;
import presentation.chart.function.CatchMouseMove;
import presentation.chart.function.CatchMouseMoveService;
import presentation.chart.function.CommonSet;
import presentation.chart.function.CommonSetService;

/**
 * 
 * @Description: TODO
 * @author: hzp 
 * @date: Apr 19, 2017
 */
public class PointChart implements chartService{
	
	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private CommonSetService commonSet = new CommonSet();
	
	private NumberFormat nf = NumberFormat.getPercentInstance();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chartpane = new StackPane();
	private StackPane datepane = new StackPane();
	private Label info = new Label();
	
	private NumberAxis yAxis;
//    private CategoryAxis xAxis;
    private NumberAxis xAxis;

//    private ScatterChart<String, Number> scatterChart;
    private ScatterChart<Number, Number> scatterChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] yieldRange;
    
    protected PointChart(List<int[]> yield, List<int[]> num, List<String> dataName, ChartKind kind){
    	yieldRange = new String[21];
    	double k = -0.1, gap = 0.01;
    	for( int i=0; i<=20; i++, k+=gap ){
    		if( i==10 )
    			yieldRange[i] = "0%";
    		else
    			yieldRange[i] = nf.format(k);
    	}
//    	ObservableList<String> yields = FXCollections.observableList(yieldRange);
    	
//    	xAxis = new CategoryAxis(yields);
    	xAxis = new NumberAxis();
    	xAxis.setTickLabelsVisible(false);
    	xAxis.autoRangingProperty().set(true);
    	xAxis.setAnimated(true);
        xAxis.setOpacity(0.7);
        xAxis.setLowerBound(-0.11);
        xAxis.setUpperBound(0.11);
        xAxis.setAutoRanging(false);
        
        yAxis = new NumberAxis();
    	yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(true);
        
        scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setVerticalGridLinesVisible(false);
        scatterChart.setLegendVisible(false);
        
        List<XYChart.Series<Number, Number>> series = new ArrayList<>();
        String[] dataStrings = new String[21];
        for( int i=0; i<yield.size(); i++ ){
        	XYChart.Series<Number, Number> serie = new XYChart.Series<>();
        	int[] yieldt = yield.get(i);
        	int[] numt = num.get(i);
        	String name = dataName.get(i);
        	
        	serie.setName(name);
        	for( int j=0; j<yieldt.length; j++ ){
        		if( j<numt.length && numt[j]!=Integer.MAX_VALUE ){
        			if( kind==ChartKind.POINTFULL )
        				for( int h=1; h<=numt[j]; h++ )
        					serie.getData().add( new XYChart.Data<>( yieldt[j]/100.0, h) );
        			else if( kind==ChartKind.POINTONE )
        				serie.getData().add( new XYChart.Data<>( yieldt[j]/100.0, numt[j]) );
        			
        			if( dataStrings[j]!=null )
	        			dataStrings[j] += "/"+name+" : "+numt[j];
	        		else
	        			dataStrings[j] = name+" : "+numt[j];
        		}	
        	}
        	series.add(serie);
        }
        for(int i=0; i<yieldRange.length; i++){
        	if( dataStrings[i]!=null )
        		dataMap.put(yieldRange[i], dataStrings[i]);
        }
        scatterChart.getData().addAll(series);
    }

	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	double chartsmall = 6, dateheight = 10, dategap = 20;
    	
    	if( withdate ){
    		scatterChart.getYAxis().setOpacity(0.7);
    		height -= dateheight;
    		height -= chartsmall;
    		datepane.getChildren().addAll( 
    				commonSet.dateForStackPane("-10%", "0%", "10%").getChildren() );
    		datepane.setPrefSize(width-dategap, dateheight);
    		datepane.getStylesheets().add(
        			getClass().getResource("/styles/DateLabel.css").toExternalForm() );
    	}
    	else{
    		scatterChart.setHorizontalGridLinesVisible(false);
    		scatterChart.getYAxis().setTickLabelsVisible(false);
    		scatterChart.getYAxis().setPrefWidth(1);
    		scatterChart.getYAxis().setOpacity(0);
    	}
    	scatterChart.setMaxSize(width, height);
    	scatterChart.setMinSize(width, height);
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPaneNN(scatterChart, dataMap, yieldRange, "Yield", 10, ChartKind.POINTFULL);
    	
    	chartpane.getChildren().add(scatterChart);
    	chartpane.getChildren().add(info);
    	StackPane.setAlignment(scatterChart, Pos.CENTER);
    	
    	pane.getChildren().add(chartpane);
    	AnchorPane.setTopAnchor(chartpane, chartsmall);
    	if( withdate ){
    		pane.getChildren().add(datepane);
    		AnchorPane.setTopAnchor(datepane, height+chartsmall);
    		AnchorPane.setLeftAnchor(datepane, dategap);
    	}	
    	
    	info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/PointChart.css").toExternalForm() );
    	return pane;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		scatterChart.setTitle(name);
	}
}
