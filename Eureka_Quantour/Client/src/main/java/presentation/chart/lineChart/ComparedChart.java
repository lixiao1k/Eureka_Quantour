package presentation.chart.lineChart;

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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import en_um.ChartKind;

/**
 * @Description: 多只股票对比图的总方法
 * @author: hzp
 * @time: 2017年3月31日
 */
public class ComparedChart implements chartService{

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private ListToArrayService listToArray = new ListToArray();
	private CommonSetService commonSet = new CommonSet();
	
	private DecimalFormat df = new DecimalFormat("#0.00");
	private NumberFormat nf = NumberFormat.getPercentInstance();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chartpane = new StackPane();
	private StackPane datepane = new StackPane();
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
    protected ComparedChart(LocalDate[] date, List<Double[]> doubleList, List<String> dataName, ChartKind kind) {
        
    	xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setStartMargin(5);
        xAxis.setOpacity(1);

        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
//        yAxis.setTickLabelsVisible(false);
//        yAxis.setPrefWidth(1);
//        yAxis.setOpacity(0);
        
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
            
            boolean ifNoMul100 = false;
            for( int j=0; j<datas.length && !ifNoMul100; j++ )
            	if( datas[j]>1 )
            		ifNoMul100 = true;
        	
        	for(int j=0; j<date.length; j++){
	        	if( j<datas.length && datas[j]!=Integer.MAX_VALUE ){
	        		serie.getData().add( new XYChart.Data<>(dates[j], datas[j]) );
	        		String dataFormat = df.format( datas[j] );
	        		if( kind==ChartKind.YIELDCOMPARED){
	        			if( !ifNoMul100 )
	        				dataFormat = df.format(datas[j]*100.0) + "%";
	        			else
	        				dataFormat = df.format(datas[j]) + "%";
	        		}
	        		else if( datas[j]<1 )
	        			dataFormat = df.format(datas[j]);

	        		if( dataStrings[j]!=null )
	        			dataStrings[j] += "/"+name+" : "+dataFormat;
	        		else
	        			dataStrings[j] = name+" : "+dataFormat;
	        	}
	        	else
	        		dataStrings[j] = "";

        	}
        	serie.setName(name);
        	series.add(serie);
        }
        for(int i=0; i<date.length; i++){
        	if( dataStrings[i]!=null )
        		dataMap.put(dates[i], dataStrings[i]);
        }
        lineChart.getData().addAll(series);
        lineChart.setLegendSide(Side.TOP);
    }
    
    @Override
    public Pane getchart(int width, int height, boolean withdate) {
    	if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	double dateheight = 10, dategap = 10;
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
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPaneSN(lineChart, dataMap, dates, "date", 10);
    	
    	chartpane.getChildren().add(lineChart);
    	chartpane.getChildren().add(info);
    	StackPane.setAlignment(lineChart, Pos.CENTER);
    	pane.getChildren().add(chartpane);
    	AnchorPane.setTopAnchor(chartpane, 3.0);
    	
    	if( withdate ){
    		pane.getChildren().add(datepane);
    		AnchorPane.setTopAnchor(datepane, height+3.0);
    		AnchorPane.setLeftAnchor(datepane, dategap);
    	}	
    	
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
