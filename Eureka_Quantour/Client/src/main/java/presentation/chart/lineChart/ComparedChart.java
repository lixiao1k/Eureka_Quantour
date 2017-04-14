package presentation.chart.lineChart;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
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
import vo.ComparedInfoVO;
import vo.YieldChartDataVO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月31日
 */
public class ComparedChart implements chartService{

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private ListToArrayService listToArray = new ListToArray();
	private CommonSetService commonSet = new CommonSet();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chartpane = new StackPane();
	private Label info = new Label();
	private Label begin = new Label();
	private Label end = new Label();
	
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private LineChart<String, Number> lineChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] dates;

    public ComparedChart(){};
    private ComparedChart(Calendar[] date, List<Double[]> doubleList, List<String> dataName) {
        xAxis = new CategoryAxis();
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setStartMargin(10);
        xAxis.setOpacity(0.7);

        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setTickLabelsVisible(false);
        yAxis.setPrefWidth(1);
        yAxis.setOpacity(0.7);
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(true);
        lineChart.setOpacity(0.9);
        lineChart.setPadding(new Insets(10,10,10,10));
        
        dates = listToArray.formatCalendar(date);
        
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
        lineChart.setLegendSide(Side.BOTTOM);
        for(int i=0; i<date.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(dates[i], dataStrings[i]);
        }
    }
    
    public ComparedChart setData(YieldChartDataVO ycd){
    	Calendar[] date = listToArray.changeCalendar(ycd.getDatelist());
    	List<Double[]> doubleList = new ArrayList<>();
    	List<String> dataName = new ArrayList<>();
    	doubleList.add( listToArray.changeDouble(ycd.getJizhunlist()) );
    	dataName.add("基础");
    	doubleList.add( listToArray.changeDouble(ycd.getCeluelist()) );
    	dataName.add("策略");
    	return new ComparedChart( date, doubleList, dataName );
    }
    
    public ComparedChart setData(ComparedInfoVO ci){
    	Calendar[] date = ci.getDate();
    	List<Double[]> doubleList = new ArrayList<>();
    	List<String> dataName = new ArrayList<>();
    	Double[] dA = listToArray.dToD(ci.getLogYieldA());
    	Double[] dB = listToArray.dToD(ci.getLogYieldB());
    	doubleList.add(dA);
    	doubleList.add(dB);
    	dataName.add(ci.getNameA());
    	dataName.add(ci.getNameB());
    	return new ComparedChart( date, doubleList, dataName );
    }
    
    @Override
    public Pane getchart(int width, int height) {
    	if( width>0 ){
    		lineChart.setMaxWidth(width);
    		lineChart.setMinWidth(width);
    	}
    	if( height>0 ){
    		lineChart.setMaxHeight(height);
    		lineChart.setMinHeight(height);
    	}
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPane(lineChart, dataMap, dates, "date", 10);
    	begin = commonSet.beignDataForAnchorPane( dates[0], (int)Math.max(height, lineChart.getWidth()) );
    	end = commonSet.endDataForAnchorPane(dates[dates.length-1], 
    			(int)Math.max(width, lineChart.getWidth()), 
    			(int)Math.max(height, lineChart.getWidth()) );
    	
    	chartpane.getChildren().add(lineChart);
    	chartpane.getChildren().add(info);
    	
    	pane.getChildren().add(chartpane);
    	pane.getChildren().add(begin);
    	pane.getChildren().add(end);
    	StackPane.setAlignment(chartpane, Pos.CENTER);
    	
    	info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/ComparedLineChart.css").toExternalForm() );
    	return pane;
    }
    
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		lineChart.setTitle(name);
	}
	
}
