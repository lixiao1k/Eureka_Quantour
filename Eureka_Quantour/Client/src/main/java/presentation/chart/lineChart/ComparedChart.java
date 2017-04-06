package presentation.chart.lineChart;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import presentation.chart.chartService;
import presentation.chart.show.CatchMouseMove;
import presentation.chart.show.CatchMouseMoveService;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huihantao on 2017/3/13.
 */

/**
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月31日
 */
public class ComparedChart implements chartService{

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();

	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
	
    private NumberAxis yAxis;
    private CategoryAxis xAxis;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");

    private LineChart<String, Number> lineChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] dates;

    public ComparedChart(Calendar[] date, List<Double[]> doubleList, List<String> dataName) {
        xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);

        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        
        dates = new String[date.length];
        for(int i=0; i<date.length; i++)
        	dates[i] = sdf.format(date[i].getTime());
        
        String[] dataStrings = new String[date.length];
        for(int i=0; i<doubleList.size(); i++){
        	XYChart.Series<String, Number> serie = new XYChart.Series<>();
        	Double[] datas = doubleList.get(i);
            String name = "";
        	if( i<dataName.size() ){
        		name = dataName.get(i);
        		serie.setName(name);
        	}
        	for(int j=0; j<date.length; j++){
	        	if( j<datas.length && datas[j]!=0 && datas[j]!=Integer.MIN_VALUE ){
	        		serie.getData().add( new XYChart.Data<>(dates[j], datas[j]) );
	        		
	        		String dataFormat = NumberFormat.getPercentInstance().format(datas[j]);
	        		if( dataStrings[j]!=null )
	        			dataStrings[j] += "/"+name+" : "+dataFormat;
	        		else
	        			dataStrings[j] = name+" : "+dataFormat;
	        	}
        		else{
        			if( dataStrings[j]!=null )
        				dataStrings[j] += "/"+name+" : "+"0";
        			else
        				dataStrings[j] = name+" : "+"0";
        		}

        	}
        	lineChart.getData().add(serie);
        }
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
    		lineChart.setMaxHeight(height);
    		lineChart.setMaxHeight(height);
    	}
    	info = catchMouseMove.createCursorGraphCoordsMonitorLabel(lineChart, dataMap, dates);
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(lineChart);
    	info.setLayoutX(5);
    	AnchorPane.setTopAnchor(lineChart, 30.0);
    	
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
