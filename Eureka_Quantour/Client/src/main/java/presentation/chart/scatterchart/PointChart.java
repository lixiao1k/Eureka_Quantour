package presentation.chart.scatterchart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public PointChart(List<Double[]> yield, List<int[]> num, List<String> dataName){
    	yieldRange = new String[20];
    	double k = -0.1, gap = 0.01;
    	for( int i=0; i<20; i++, k+=gap )
    		yieldRange[i] = nf.format(k);
//    	ObservableList<String> yields = FXCollections.observableList(yieldRange);
    	
//    	xAxis = new CategoryAxis(yields);
    	xAxis = new NumberAxis();
    	xAxis.setTickLabelsVisible(false);
    	xAxis.autoRangingProperty().set(true);
    	xAxis.setAnimated(true);
        xAxis.setOpacity(0.7);
        xAxis.setLowerBound(-0.1);
        xAxis.setUpperBound(0.1);
        xAxis.setAutoRanging(false);
        
        yAxis = new NumberAxis();
    	yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(true);
//        yAxis.setPrefWidth(1);
        yAxis.setOpacity(0.7);
        
        scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setHorizontalGridLinesVisible(false);
        scatterChart.setVerticalGridLinesVisible(false);
        scatterChart.setPadding(new Insets(10,10,10,10));
        scatterChart.setLegendVisible(false);
        
        List<XYChart.Series<Number, Number>> series = new ArrayList<>();
        String[] dataStrings = new String[20];
        for( int i=0; i<yield.size(); i++ ){
        	XYChart.Series<Number, Number> serie = new XYChart.Series<>();
        	Double[] yieldt = yield.get(i);
        	int[] numt = num.get(i);
        	String name = dataName.get(i);
        	
        	serie.setName(name);
        	for( int j=0; j<yieldt.length; j++ ){
        		if( j<numt.length && numt[j]!=Integer.MAX_VALUE ){
        			for( int h=1; h<=numt[j]; h++ )
        				serie.getData().add( new XYChart.Data<>( yieldt[j]/100, h) );
        			
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
    	double dateheight = 10, dategap = 10;
    	
    	if( withdate ){
    		scatterChart.getYAxis().setOpacity(0.9);
    		height -= dateheight;
    		String bdate = yieldRange[0];
    		String mdate = yieldRange[yieldRange.length/2];
    		String edate = yieldRange[yieldRange.length-1];
    		datepane.getChildren().addAll( 
    				commonSet.dateForStackPane(bdate, mdate, edate).getChildren() );
    		datepane.setPrefSize(width-10, dateheight);
    		datepane.getStylesheets().add(
        			getClass().getResource("/styles/DateLabel.css").toExternalForm() );
    	}
    	else{
    		scatterChart.getYAxis().setTickLabelsVisible(false);
    		scatterChart.getYAxis().setPrefWidth(1);
    		scatterChart.getYAxis().setOpacity(0);
    	}
    	scatterChart.setMaxSize(width, height);
    	scatterChart.setMinSize(width, height);
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPaneNN(scatterChart, dataMap, yieldRange, "yield", 0);
    	
    	chartpane.getChildren().add(scatterChart);
    	chartpane.getChildren().add(info);
    	StackPane.setAlignment(scatterChart, Pos.CENTER);
    	
    	pane.getChildren().add(chartpane);
    	AnchorPane.setTopAnchor(chartpane, 3.0);
    	if( withdate ){
    		pane.getChildren().add(datepane);
    		AnchorPane.setTopAnchor(datepane, height+3.0);
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
		
	}
}
