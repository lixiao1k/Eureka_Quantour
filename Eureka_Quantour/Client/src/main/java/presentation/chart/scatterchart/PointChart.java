package presentation.chart.scatterchart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @Description: TODO
 * @author: hzp 
 * @date: Apr 19, 2017
 */
public class PointChart {
	
	private AnchorPane pane = new AnchorPane();
	
	private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private ScatterChart<String, Number> scatterChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] yield;
    
    public PointChart(List<Double[]> yield, List<int[]> num, List<String> dataName){
    	List<String> yieldRange = new ArrayList<>();
    	double k = -0.1, gap = 0.01;
    	for( int i=0; i<20; i++, k+=gap )
    		yieldRange.add( NumberFormat.getPercentInstance().format(k) );
    	ObservableList<String> yields = FXCollections.observableList(yieldRange);
    	
    	xAxis = new CategoryAxis(yields);
    	xAxis.setTickLabelsVisible(false);
    	xAxis.autoRangingProperty().set(true);
    	xAxis.setAnimated(true);
        xAxis.setOpacity(0.7);
        
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
        
        String[] dataStrings = new String[20];
        
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for( int i=0; i<yield.size(); i++ ){
        	Double[] yieldt = yield.get(i);
        	int[] numt = num.get(i);
        	String name = dataName.get(i);
        	
        	serie.setName(name);
        	for( int j=0; j<yieldt.length; j++ ){
        		if( j<numt.length && numt[j]!=Integer.MAX_VALUE ){
        			for( int h=1; h<=numt[j]; h++ )
        			serie.getData().add( new XYChart.Data<>(
        					NumberFormat.getPercentInstance().format(yieldt[j]), h) );
        			
        			
        		}
        			
        	}
        }
    }
}
