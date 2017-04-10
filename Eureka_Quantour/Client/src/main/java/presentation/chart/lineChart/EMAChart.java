package presentation.chart.lineChart;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
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
import vo.EMAInfoVO;

import java.text.NumberFormat;
import java.util.ArrayList;
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
	private ListToArrayService listToArray = new ListToArray();
	private CommonSetService commonSet = new CommonSet();
	
	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
	private Label begin = new Label();
	private Label end = new Label();
	
    protected NumberAxis yAxis;
    protected CategoryAxis xAxis;

    private LineChart<String, Number> lineChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] dates = new String[0];

    public EMAChart(List<EMAInfoVO> EMAList) {

    	xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickLabelsVisible(false);
        
        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        
        dates = listToArray.formatCalendar(EMAList.get(0).getDate());
    	
    	List<Double[]> doubleList = new ArrayList<>();
    	String[] dataName = {"1", "2", "3", "4", "5"};
    	for( EMAInfoVO ema : EMAList)
    		doubleList.add( listToArray.changeDouble(ema.getEMA()) );

        String[] dataStrings = new String[dates.length];
        List<XYChart.Series<String, Number>> series = new ArrayList<>();
        for(int i=0; i<doubleList.size(); i++){
        	Double[] datas = doubleList.get(i);
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            String name = "";
        	if( i<dataName.length )
        		name = dataName[i];
        	
        	for(int j=0; j<dates.length; j++){
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
            serie.setName(name);
            series.add(serie);
        }
        for(int i=0; i<dates.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(dates[i], dataStrings[i]);
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
    		lineChart.setMaxHeight(height-20);
    		lineChart.setMinHeight(height-20);
    	}
    	info = catchMouseMove.catchMouseReturnInfo(lineChart, dataMap, dates, "date", 0);
    	begin = commonSet.beignData( dates[0], (int)Math.max(height, lineChart.getWidth()) );
    	end = commonSet.endData(dates[dates.length-1], 
    			(int)Math.max(width, lineChart.getWidth()), 
    			(int)Math.max(height, lineChart.getWidth()) );
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(lineChart);
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
