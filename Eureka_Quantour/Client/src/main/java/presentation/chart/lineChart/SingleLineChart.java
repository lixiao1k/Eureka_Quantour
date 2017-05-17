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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
	private StackPane chartpane = new StackPane();
	private StackPane datepane = new StackPane();
	private Label info = new Label();
	
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private LineChart<String, Number> lineChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] dates;

    public SingleLineChart(List<LocalDate> date, List<Double> doubleList, String dataName) {
        xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickLabelsVisible(false);
//        xAxis.setPrefHeight(0);
//        xAxis.setOpacity(0);
        
        yAxis = new NumberAxis();
    	yAxis.autoRangingProperty().set(false);
        yAxis.setLowerBound(8.85);
        yAxis.setUpperBound(9.20);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
 //       yAxis.setUpperBound(1.25);
        yAxis.setOpacity(0.5);
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        
        dates = listToArray.formatLocalDate(date);
        
        Double[] datas = listToArray.changeDouble( doubleList );
        String[] dataStrings = new String[date.size()];
        
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(dataName);

        for(int j=0; j<date.size(); j++){
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
        
        for(int i=0; i<date.size(); i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(dates[i], dataStrings[i]);
        }
        
    }
    
    @Override
    public Pane getchart(int width, int height, boolean withdate) {	
    	if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	double chartsmall = 5, dateheight = 10, dategap = 10;
    	if( withdate ){
    		lineChart.getYAxis().setOpacity(0.9);
    		height -= dateheight;
    		String bdate = dates[0];
    		String mdate = dates[dates.length/2];
    		String edate = dates[dates.length-1];
    		datepane.getChildren().addAll( 
    				commonSet.dateForStackPane(bdate, mdate, edate).getChildren() );
    		datepane.setPrefSize(width-10, dateheight);
    		datepane.getStylesheets().add(
        			getClass().getResource("/styles/DateLabel.css").toExternalForm() );
    	}
    	else{
    		lineChart.getYAxis().setTickLabelsVisible(false);
    		lineChart.getYAxis().setPrefWidth(1);
    		lineChart.getYAxis().setOpacity(0);
    	}
    	lineChart.setMaxSize(width, height);
    	lineChart.setMinSize(width, height);
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPaneSN(lineChart, dataMap, dates, "date", 0);
    	
    	chartpane.getChildren().add(lineChart);
    	chartpane.getChildren().add(info);
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
    			getClass().getResource("/styles/SingleLineChart.css").toExternalForm() );
    	return pane;
    }
    
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		lineChart.setTitle(name);
	}
}
