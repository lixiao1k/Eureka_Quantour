package presentation.chart.lineChart;

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
        
        Double[] datas ;
        String name = "";
        String[] dataStrings = new String[date.length];
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for(int i=0; i<doubleList.size(); i++){
        	serie = new XYChart.Series<>();
        	datas = doubleList.get(i);
        	name = "";
        	if( i<dataName.size() ){
        		name = dataName.get(i);
        		serie.setName(name);
        	}
        	for(int j=0; j<date.length; j++){
	        	if( j<datas.length && datas[j]!=0 && datas[j]!=Integer.MIN_VALUE ){
	        		serie.getData().add( new XYChart.Data<>(dates[j], datas[j]) );
	        		
	        		String dataFormat = NumberFormat.getPercentInstance().format(datas[j]);
	        		if( dataStrings[j]!=null )
	        			dataStrings[j] += "/"+name+","+dataFormat;
	        		else
	        			dataStrings[j] = name+","+dataFormat;
	        	}
        		else{
        			if( dataStrings[j]!=null )
        				dataStrings[j] += "/"+name+","+"0";
        			else
        				dataStrings[j] = name+","+"0";
        		}

        	}
        	lineChart.getData().add(serie);
        }
        for(int i=0; i<date.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(dates[i], dataStrings[i]);
        }
        
        lineChart.getStylesheets().add(getClass().getResource("/styles/ComparedChart.css").toExternalForm());
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
    	info = createCursorGraphCoordsMonitorLabel(lineChart);
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(lineChart);
    	info.setLayoutX(5);
    	AnchorPane.setTopAnchor(lineChart, 30.0);
        return pane;
    }
    
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		lineChart.setTitle(name);
	}
    
	/**
	 * @Description: catch mouse movement and get relevant data
	 * @author: hzp
	 * @time: 2017年4月1日
	 * @return: Label
	 */
    private Label createCursorGraphCoordsMonitorLabel(LineChart<String, Number> lineChart){
	    Axis<String> xAxis = lineChart.getXAxis();
	    Axis<Number> yAxis = lineChart.getYAxis();
	
	    Label cursorCoords = new Label();
	
	    final Node chartBackground = lineChart.lookup(".chart-plot-background");
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
	    		double temp = mouseEvent.getX()*(dates.length-1)/xAxis.getWidth();
	    		int index = 0;
	    		if( (temp%1)<0.2 )
	    			index = (int)(temp/1);
	    		else if( (temp%1)>0.8 )
	    			index = (int)(temp/1)+1;
	    		else
	    			index = -1;

	    		if( index>-1){
		    		String dataInfo = dataMap.get(dates[index]);
		    		String infos[] = dataInfo.split("/");
		    		String info = "date : "+dates[index]+"\n";
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
