package presentation.chart.areaChart;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import presentation.chart.chartService;

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
        serie = new XYChart.Series<>();
        serie.setName(dataName);
        for(int j=0; j<cycles.length; j++){
	        if( j<datas.length && datas[j]!=0 && datas[j]!=Integer.MIN_VALUE ){
	        	serie.getData().add( new XYChart.Data<>(cycleSave[j], datas[j]) );
	        		
	        	String dataFormat = NumberFormat.getPercentInstance().format(datas[j]);
	        	if( dataStrings[j]!=null )
	        		dataStrings[j] += "/"+dataName+","+dataFormat;
	        	else
	        		dataStrings[j] = dataName+","+dataFormat;
	        }
	        else{
	        	if( dataStrings[j]!=null )
	        		dataStrings[j] += "/"+dataName+","+"0";
	        	else
	        		dataStrings[j] = dataName+","+"0";
        	}
        }
        areaChart.getData().add(serie);
        
        for(int i=0; i<cycles.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(cycleSave[i], dataStrings[i]);
        }
        
        info.getStyleClass().add("/styles/SingleLineChart.css");
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
    	info = createCursorGraphCoordsMonitorLabel(areaChart);
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(areaChart);
    	info.setLayoutX(5);
    	AnchorPane.setTopAnchor(areaChart, 30.0);
        return pane;
    }
    
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		areaChart.setTitle(name);
	}
    
    private Label createCursorGraphCoordsMonitorLabel(AreaChart<String, Number> areaChart){
	    Axis<String> xAxis = areaChart.getXAxis();
	    Axis<Number> yAxis = areaChart.getYAxis();
	
	    Label cursorCoords = new Label();
	
	    final Node chartBackground = areaChart.lookup(".chart-plot-background");
	    for( Node n: chartBackground.getParent().getChildrenUnmodifiable() ) {
	    	if ( n!=chartBackground && n!=xAxis && n!=yAxis) {
	    		n.setMouseTransparent(true);
	    	}
	    }

	    chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    	@Override 
	    	public void handle(MouseEvent mouseEvent) {
	    		cursorCoords.setVisible(true);
	    	}
	    });
	
	    chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
	    	@Override 
	    	public void handle(MouseEvent mouseEvent) {		
	    		double temp = mouseEvent.getX()*(cycleSave.length-1)/xAxis.getWidth();
	    		int index = 0;
	    		if( (temp%1)<0.2 )
	    			index = (int)(temp/1);
	    		else if( (temp%1)>0.8 )
	    			index = (int)(temp/1)+1;
	    		else
	    			index = -1;

	    		if( index>-1){
		    		String dataInfo = dataMap.get(cycleSave[index]);
		    		String infos[] = dataInfo.split("/");
		    		String info = "date : "+cycleSave[index]+"\n";
		    		for(int i=0; i<infos.length; i++){
		    			String tempInfos[] = infos[i].split(",");
		    			info += tempInfos[0]+" : "+tempInfos[1]+"\n";
		    		}
		    		cursorCoords.setText(info);
	    		}
	    		else
	    			cursorCoords.setText("");
	    	}
	    });

	    chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
	    	@Override 
	    	public void handle(MouseEvent mouseEvent) {
	    		cursorCoords.setVisible(false);
	    	}
	    });
	    return cursorCoords;
	}
}

