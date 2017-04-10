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
import presentation.chart.function.CommonSet;
import presentation.chart.function.CommonSetService;
import presentation.chart.function.ListToArray;
import presentation.chart.function.ListToArrayService;

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
	private ListToArrayService listToArray = new ListToArray();
	private CommonSetService commonSet = new CommonSet();
	
	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
	private Label begin = new Label();
	private Label end = new Label();
	
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private AreaChart<String, Number> areaChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] cycleSave;

    public AreaLineChart(int[] cycles, Double[] dataList, String dataName) {
        xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickLabelsVisible(false);
        
        yAxis = new NumberAxis();
    	yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        
        areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setHorizontalGridLinesVisible(false);
        areaChart.setVerticalGridLinesVisible(false);
        areaChart.setCreateSymbols(false);
        
        cycleSave = listToArray.formatInteger(cycles);
        
        Double[] datas = dataList;
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
    		areaChart.setMaxHeight(height-20);
    		areaChart.setMinHeight(height-20);
    	}
    	
    	info = catchMouseMove.catchMouseReturnInfo(areaChart, dataMap, cycleSave, "周期", 0);
    	begin = commonSet.beignData( cycleSave[0], (int)Math.max(height, areaChart.getWidth()) );
    	end = commonSet.endData(cycleSave[cycleSave.length-1], 
    			(int)Math.max(width, areaChart.getWidth()), 
    			(int)Math.max(height, areaChart.getWidth()) );
    	begin.setLayoutX(begin.getLayoutX()+7);
    	end.setLayoutX(end.getLayoutX()+35);
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(areaChart);
    	pane.getChildren().add(begin);
    	pane.getChildren().add(end);
    	AnchorPane.setTopAnchor(areaChart, 20.0);
    	
    	info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
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

