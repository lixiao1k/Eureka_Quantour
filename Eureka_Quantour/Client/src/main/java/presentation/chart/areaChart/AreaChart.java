package presentation.chart.areaChart;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import presentation.chart.chartService;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年4月2日
 */
public class AreaChart 
//implements chartService
{


	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
    private NumberAxis yAxis;
    private CategoryAxis xAxis;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");

    private LineChart<String, Number> areaChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private int[] cycleSave;

//    public AreaChart(int[] cycles, Double[] doubleList, String dataName) {
//        xAxis = new CategoryAxis();
//        xAxis.setGapStartAndEnd(false);
//        
//        yAxis = new NumberAxis();
//    	yAxis.autoRangingProperty().set(true);
//        yAxis.setAnimated(true);
//        yAxis.forceZeroInRangeProperty().setValue(false);
//        yAxis.setLowerBound(0);
//        yAxis.setUpperBound(1);
//        
//        areaChart = new LineChart<>(xAxis, yAxis);
//        areaChart.setHorizontalGridLinesVisible(false);
//        areaChart.setVerticalGridLinesVisible(false);
//        areaChart.setCreateSymbols(false);
//        
//        cycleSave = new int[cycles.length];
//        for(int i=0; i<cycles.length; i++)
//        	cycleSave[i] = cycles[i];
//        
//        Double[] datas = doubleList;
//        String[] dataStrings = new String[cycles.length];
//        
//        XYChart.Series<String, Number> serie = new XYChart.Series<>();
//        serie = new XYChart.Series<>();
//        serie.setName(dataName);
//        for(int j=0; j<date.length; j++){
//	        if( j<datas.length && datas[j]!=0 && datas[j]!=Integer.MIN_VALUE ){
//	        	serie.getData().add( new XYChart.Data<>(cycleSave[j], datas[j]) );
//	        		
//	        	String dataFormat = NumberFormat.getPercentInstance().format(datas[j]);
//	        	if( dataStrings[j]!=null )
//	        		dataStrings[j] += "/"+dataName+","+dataFormat;
//	        	else
//	        		dataStrings[j] = dataName+","+dataFormat;
//	        }
//	        else{
//	        	if( dataStrings[j]!=null )
//	        		dataStrings[j] += "/"+dataName+","+"0";
//	        	else
//	        		dataStrings[j] = dataName+","+"0";
//        	}
//        }
//        areaChart.getData().add(serie);
//        
//        for(int i=0; i<date.length; i++){
//        	if( dataStrings[i].length()!=0 )
//        		dataMap.put(dates[i], dataStrings[i]);
//        }
//        
//        info.getStyleClass().add("/styles/SingleLineChart.css");
//    }
//    
//    @Override
//    public Pane getchart(int width, int height) {
//    	if( width>0 ){
//    		areaChart.setMaxWidth(width);
//    		areaChart.setMinWidth(width);
//    	}
//    	if( height>0 ){
//    		areaChart.setMaxHeight(height);
//    		areaChart.setMaxHeight(height);
//    	}
//    	info = createCursorGraphCoordsMonitorLabel(areaChart);
//    	
//    	pane.getChildren().add(info);
//    	pane.getChildren().add(areaChart);
//    	info.setLayoutX(5);
//    	AnchorPane.setTopAnchor(areaChart, 30.0);
//        return pane;
//    }
//    
//	@Override
//	public void setName(String name) {
//		// TODO Auto-generated method stub
//		areaChart.setTitle(name);
//	}
//    
//    private Label createCursorGraphCoordsMonitorLabel(LineChart<String, Number> lineChart){
//	    Axis<String> xAxis = lineChart.getXAxis();
//	    Axis<Number> yAxis = lineChart.getYAxis();
//	
//	    Label cursorCoords = new Label();
//	
//	    final Node chartBackground = lineChart.lookup(".chart-plot-background");
//	    for( Node n: chartBackground.getParent().getChildrenUnmodifiable() ) {
//	    	if ( n!=chartBackground && n!=xAxis && n!=yAxis) {
//	    		n.setMouseTransparent(true);
//	    	}
//	    }
//
//	    chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
//	    	@Override 
//	    	public void handle(MouseEvent mouseEvent) {
//	    		cursorCoords.setVisible(true);
//	    	}
//	    });
//	
//	    chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
//	    	@Override 
//	    	public void handle(MouseEvent mouseEvent) {		
//	    		double temp = mouseEvent.getX()*(dates.length-1)/xAxis.getWidth();
//	    		int index = 0;
//	    		if( (temp%1)<0.2 )
//	    			index = (int)(temp/1);
//	    		else if( (temp%1)>0.8 )
//	    			index = (int)(temp/1)+1;
//	    		else
//	    			index = -1;
//
//	    		if( index>-1){
//		    		String dataInfo = dataMap.get(dates[index]);
//		    		String infos[] = dataInfo.split("/");
//		    		String info = "date : "+dates[index]+"\n";
//		    		for(int i=0; i<infos.length; i++){
//		    			String tempInfos[] = infos[i].split(",");
//		    			info += tempInfos[0]+" : "+tempInfos[1]+"\n";
//		    		}
//		    		cursorCoords.setText(info);
//	    		}
//	    		else
//	    			cursorCoords.setText("");
//	    	}
//	    });
//
//	    chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
//	    	@Override 
//	    	public void handle(MouseEvent mouseEvent) {
//	    		cursorCoords.setVisible(false);
//	    	}
//	    });
//	    return cursorCoords;
//	}
}

