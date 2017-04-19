package presentation.chart.barChart;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import en_um.ChartKind;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import presentation.chart.chartService;
import presentation.chart.function.CatchMouseMove;
import presentation.chart.function.CatchMouseMoveService;
import presentation.chart.function.CommonSet;
import presentation.chart.function.CommonSetService;
import vo.YieldDistributionHistogramDataVO;

public class YieldDistributeChart implements chartService{
	
	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private CommonSetService commonSet = new CommonSet();
	
	private DecimalFormat df = new DecimalFormat("0.00");
	private NumberFormat nf = NumberFormat.getPercentInstance();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chartpane = new StackPane();
	private StackPane datepane = new StackPane();
	private Label info = new Label();
	
	private NumberAxis yAxis;
//    private CategoryAxis xAxis;
	private NumberAxis xAxis;

    private BarChart<Number, Number> barChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] yield;
    private Map<Double,List<Integer>> zuhe;
    
    private int min = 0;
    private int max = 0;
	
	public YieldDistributeChart(YieldDistributionHistogramDataVO ydhd){
		zuhe = ydhd.getZuhe();
		Set<Double> keySet = zuhe.keySet();
		
		List<Double> yieldL = new ArrayList<>( keySet );
        yieldL = sortDouble(yieldL);
		min = (int)((double)yieldL.get(0));
		max = (int)((double)yieldL.get(yieldL.size()-1));
		if( min>-100 )
			min -= 5;
		if( max<100 )
			max += 5;
			
		List<String> value = new ArrayList<>();
		double k = min, gap = 5;
		for( ; k<=max; k+=gap )
			value.add( nf.format(k/100) );
		ObservableList<String> values = FXCollections.observableList(value);
		
//		xAxis = new CategoryAxis(values);
		xAxis = new NumberAxis();
        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelsVisible(false);
//        xAxis.setStartMargin(0);
        xAxis.setOpacity(0.7);
        xAxis.setLowerBound(min);
        xAxis.setUpperBound(max);
	        
        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(false);
//        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false); 
        yAxis.setLowerBound(0);
        yAxis.setOpacity(0.7);
	    
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setVerticalGridLinesVisible(false);
        barChart.setBarGap(0);
//        barChart.setPadding(new Insets(10,10,10,10));
        barChart.setCategoryGap(0);
        barChart.setLegendVisible(false);
        barChart.setOpacity(0.9);
        
        yield = new String[yieldL.size()];
        for(int i =0; i<yieldL.size(); i++){
        	yield[i] = nf.format( yieldL.get(i)/100 );
        }
	     
        String[] dataStrings = new String[yield.length];

        List<Integer> pandm = new ArrayList<>();
	     
        XYChart.Series<Number, Number> seriem = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriep = new XYChart.Series<>();
        
        String namep = "正收益",  namem= "负收益";
        max = 0;
        for( int i=0; i<yield.length; i++){
        	pandm = zuhe.get(yieldL.get(i));
        	if( Math.abs(pandm.get(0))>max )
        		max = Math.abs(pandm.get(0));
        	if( Math.abs(pandm.get(1))>max )
        		max = Math.abs(pandm.get(1));
        	XYChart.Data<Number,Number> sp= new XYChart.Data<>(yieldL.get(i)/100 , Math.abs(pandm.get(0)));
            XYChart.Data<Number,Number> sm= new XYChart.Data<>(yieldL.get(i)/100 , Math.abs(pandm.get(1)));
            sp.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                    if (newValue != null) {
                        newValue.setStyle("-fx-bar-fill: #FF5151;");
                    }
                }
            });
            sm.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                    if (newValue != null) {
                        newValue.setStyle("-fx-bar-fill: #53FF53;");
                    }
                }
            });
        	seriep.getData().add( sp );
        	seriem.getData().add( sm );
        	dataStrings[i] = namep+" : "+pandm.get(0)+"/"+namem+" : "+pandm.get(1);
        }
        yAxis.setUpperBound(max+2);
        barChart.getData().add(seriep);
        barChart.getData().add(seriem);
        
        for(int i=0; i<yield.length; i++)
        	if( dataStrings[i].length()!=0 ){
        		dataMap.put(yield[i], dataStrings[i]);
        	}
	}

	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		double chartgap = 8;
		if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	height -= chartgap;
    	double dateheight = 10, dategap = 10;
    	if( withdate ){
    		height -= dateheight;
    		datepane.getChildren().addAll( 
    				commonSet.dateForStackPane(
    						nf.format(min/100), nf.format((min+max)/200), nf.format(max/100)).getChildren() );
    		datepane.setPrefSize(width-dategap, dateheight);
    		datepane.getStylesheets().add(
        			getClass().getResource("/styles/DateLabel.css").toExternalForm() );
    	}
    	else{
    		yAxis.setPrefWidth(1);
    	}
    	barChart.setMaxSize(width, height);
    	barChart.setMinSize(width, height);
   
    	info = catchMouseMove.catchMouseReturnInfoForStackPaneNN(barChart, dataMap, yield, "收益率", 0, ChartKind.YIELDDISTRIBUTE);
    	
    	chartpane.getChildren().add(barChart);
    	chartpane.getChildren().add(info);
    	
    	pane.getChildren().add(chartpane);
    	AnchorPane.setTopAnchor(chartpane, chartgap);
    	if( withdate ){
    		pane.getChildren().add(datepane);
    		AnchorPane.setTopAnchor(datepane, height+chartgap);
    		AnchorPane.setLeftAnchor(datepane, dategap);
    	}
    	
    	info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/YieldDistributeChart.css").toExternalForm() );
    	return pane;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}
	
	private List<Double> sortDouble(List<Double> list){
		List<Double> listD = new ArrayList<>();
		Double temp = 0.0;
		int length = list.size();
		for( int i=0; i<length; i++){
			temp = list.get(0);
			for( int j=1; j<list.size(); j++){
				if( temp>list.get(j) )
					temp = list.get(j);
			}
			list.remove(temp);
			listD.add(temp);
		}
		return listD;
	}
}
