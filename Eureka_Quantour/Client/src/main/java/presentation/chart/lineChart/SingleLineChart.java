package presentation.chart.lineChart;

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
import presentation.chart.function.CommonSet;
import presentation.chart.function.CommonSetService;
import presentation.chart.function.ListToArray;
import presentation.chart.function.ListToArrayService;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @author: hzp
 * @time: 2017年4月2日
 */
public class SingleLineChart implements chartService{

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private ListToArrayService listToArray = new ListToArray();
	private CommonSetService commonSet = new CommonSet();

	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
	private Label begin = new Label();
	private Label end = new Label();
	
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private LineChart<String, Number> lineChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] dates;

    public SingleLineChart(Calendar[] date, Double[] doubleList, String dataName) {
        xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickLabelsVisible(false);
//        xAxis.setPrefHeight(0);
//        xAxis.setOpacity(0);
        
        yAxis = new NumberAxis();
    	yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setUpperBound(1.25);
        yAxis.setOpacity(0.5);
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        
        dates = listToArray.formatCalendar(date);
        
        Double[] datas = doubleList;
        String[] dataStrings = new String[date.length];
        
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(dataName);
        for(int j=0; j<date.length; j++){
	        if( j<datas.length && datas[j]!=0 && datas[j]!=Integer.MIN_VALUE ){
	        	serie.getData().add( new XYChart.Data<>(dates[j], datas[j]) );
	        	
	        	String dataFormat = NumberFormat.getPercentInstance().format(datas[j]);
	        	if( dataStrings[j]!=null )
	        		dataStrings[j] += "/"+dataName+" : "+dataFormat;
	        	else
	        		dataStrings[j] = dataName+" : "+dataFormat;
	        }
	        else{
	        	if( dataStrings[j]!=null )
	        		dataStrings[j] += "/"+dataName+" : "+"0";
	        	else
	        		dataStrings[j] = dataName+" : "+"0";
        	}
        }
        lineChart.getData().add(serie);
        
        for(int i=0; i<date.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(dates[i], dataStrings[i]);
        }
        
    }
    
    @Override
    public Pane getchart(int width, int height) {
    	if( width>0 ){
    		lineChart.setMaxWidth(width);
    		lineChart.setMinWidth(width);
    	}
    	if( height>0 ){
    		lineChart.setMaxHeight(height-20);
    		lineChart.setMinHeight(height-20);
    	}
    	
    	info = catchMouseMove.catchMouseReturnInfoForAnchorPane(lineChart, dataMap, dates, "date", 0);
    	begin = commonSet.beignDataForAnchorPane( dates[0], (int)Math.max(height, lineChart.getWidth()) );
    	end = commonSet.endDataForAnchorPane( dates[dates.length-1], 
    			(int)Math.max(width, lineChart.getWidth()), 
    			(int)Math.max(height, lineChart.getWidth()) );
    	
    	pane.getChildren().add(lineChart);
    	pane.getChildren().add(info);
    	pane.getChildren().add(begin);
    	pane.getChildren().add(end);
    	AnchorPane.setTopAnchor(lineChart, 20.0);
    	
    	info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/SingleLineChart.css").toExternalForm() );
    	return pane;
    }
    
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		lineChart.setTitle(name);
	}
}
