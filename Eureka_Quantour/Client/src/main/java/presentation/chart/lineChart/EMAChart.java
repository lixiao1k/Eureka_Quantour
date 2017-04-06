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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huihantao on 2017/3/9.
 */

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年4月6日
 */
public class EMAChart implements chartService {
	
	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	
	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
	
    protected NumberAxis yAxis;
    protected CategoryAxis xAxis;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");

    private LineChart<String, Number> lineChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] dates;

    public EMAChart(Calendar[] date, List<Double[]> doubleList, List<String> dataName) {
        xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        
        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        
        dates = new String[date.length];
        for(int i=0; i<date.length; i++)
        	dates[i] = sdf.format(date[i].getTime());
        
        String[] dataStrings = new String[date.length];
        List<XYChart.Series<String, Number>> series = new ArrayList<>();
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
            serie.setName(1 + i + "");
            series.add(serie);
        }
        lineChart.getData().addAll(series);
        for(XYChart.Series<String, Number> s:series){
            System.out.println("series-"+s.getName());
            s.getNode().getStyleClass().add("series-"+s.getName());
        }

        

        lineChart.getStylesheets().add(getClass().getResource("/styles/EMAChart.css").toExternalForm());
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
        lineChart.setTitle(name);
    }

}
