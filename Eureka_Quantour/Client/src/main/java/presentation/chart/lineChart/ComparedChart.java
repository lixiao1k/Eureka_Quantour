package presentation.chart.lineChart;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import presentation.chart.chartService;
import presentation.chart.function.CatchMouseMove;
import presentation.chart.function.CatchMouseMoveService;
import presentation.chart.function.ListToArray;
import presentation.chart.function.ListToArrayService;
import vo.ComparedInfoVO;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
	
	private NumberFormat nf = NumberFormat.getPercentInstance();
	
	private StackPane pane = new StackPane();
	private Label info = new Label();
	
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private LineChart<String, Number> lineChart;
    /**
     * dataMap: store every point's information and key is its abscissa which has been format
     */
    private Map<String, String> dataMap = new HashMap<String,String>();
    /**
     * dates: store xAxis's value which has been format
     */
    private String[] dates;

    public ComparedChart(){
    	nf.setMinimumFractionDigits(1);
    };
    protected ComparedChart(LocalDate[] date, List<Double[]> doubleList, List<String> dataName) {
        
    	xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setStartMargin(5);
        xAxis.setOpacity(0.5);

        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setTickLabelsVisible(false);
        yAxis.setPrefWidth(1);
        yAxis.setOpacity(0);
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(true);
        lineChart.setOpacity(0.8);
        
        dates = listToArray.formatLocalDate(date);
        
        List<XYChart.Series<String, Number>> series = new ArrayList<>();
        String[] dataStrings = new String[date.length];
        for(int i=0; i<doubleList.size(); i++){
        	XYChart.Series<String, Number> serie = new XYChart.Series<>();
        	Double[] datas = doubleList.get(i);
            String name = "";
        	if( i<dataName.size() )
        		name = dataName.get(i);
        	System.out.println(name);
        	
        	for(int j=0; j<date.length; j++){
	        	if( j<datas.length && datas[j]!=0 && datas[j]!=Integer.MAX_VALUE ){
	        		serie.getData().add( new XYChart.Data<>(dates[j], datas[j]) );
	        		String dataFormat = ""+datas[j];
	        		if( datas[j]<1 )
	        			dataFormat = nf.format(datas[j]);
	        		
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
//        	lineChart.getData().add(serie);
        }
        for(int i=0; i<date.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(dates[i], dataStrings[i]);
        }
        lineChart.getData().addAll(series);
        lineChart.setLegendSide(Side.BOTTOM);
    }
    /**
     * @Description: input a ComparedInfoVO and draw a ComparedChart to compare two stock's situation
     * @author: hzp
     * @time: 2017年4月3日
     * @return: ComparedChart
     */
    public ComparedChart setData(ComparedInfoVO ci){
    	return null;
    }
    
    @Override
    public Pane getchart(int width, int height) {
    	
    	if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	
    	lineChart.setMaxSize(width, height);
    	lineChart.setMinSize(width, height);
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPane(lineChart, dataMap, dates, "date", 10);
    	
    	pane.getChildren().add(lineChart);
    	pane.getChildren().add(info);

    	StackPane.setAlignment(lineChart, Pos.CENTER);
    	
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
