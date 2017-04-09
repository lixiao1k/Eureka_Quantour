package presentation.chart.areaChart;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import presentation.chart.chartService;
import presentation.chart.function.CatchMouseMove;
import presentation.chart.function.CatchMouseMoveService;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年4月2日
 */
public class AreaLineChart implements chartService {

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();

	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private AreaChart<String, Number> areaChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] cycleSave;

    public AreaLineChart(int[] cycles, double[] dataList, String dataName) {
        xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        
        yAxis = new NumberAxis();
    	yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setLowerBound(-1);
        yAxis.setUpperBound(1);
        
        areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setHorizontalGridLinesVisible(false);
        areaChart.setVerticalGridLinesVisible(false);
        areaChart.setCreateSymbols(false);
        
        cycleSave = new String[cycles.length];
        for(int i=0; i<cycles.length; i++)
        	cycleSave[i] = String.valueOf( cycles[i] );
        
        double[] datas = dataList;
        String[] dataStrings = new String[cycles.length];
        
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(dataName);
        for(int j=0; j<cycles.length; j++){
	        if( j<datas.length && datas[j]!=0 && datas[j]!=Integer.MIN_VALUE ){
	        	serie.getData().add( new XYChart.Data<>(cycleSave[j], datas[j]) );
	        		
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
        areaChart.getData().add(serie);
        
        for(int i=0; i<cycles.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(cycleSave[i], dataStrings[i]);
        }
    }
    
    @Override
    public Pane getchart(int width, int height) {
    	if( width>0 ){
    		areaChart.setMaxWidth(width);
    		areaChart.setMinWidth(width);
    	}
    	if( height>0 ){
    		areaChart.setMaxHeight(height);
    		areaChart.setMaxHeight(height);
    	}
    	info = catchMouseMove.createCursorGraphCoordsMonitorLabel(areaChart, dataMap, cycleSave);
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(areaChart);
    	info.setLayoutX(5);
    	AnchorPane.setTopAnchor(areaChart, 30.0);
    	
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/SingleLineChart.css").toExternalForm() );
        return pane;
    }
    
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		areaChart.setTitle(name);
	}

}

