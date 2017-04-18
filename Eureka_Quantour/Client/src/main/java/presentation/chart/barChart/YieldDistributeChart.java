package presentation.chart.barChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chartpane = new StackPane();
	private StackPane datepane = new StackPane();
	private Label info = new Label();
	
	private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private BarChart<String, Number> barChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] yield;
    private Map<Double,List<Integer>> zuhe;
	
	public YieldDistributeChart(YieldDistributionHistogramDataVO ydhd){
		zuhe = ydhd.getZuhe();
		Set<Double> keySet = zuhe.keySet();
		
		xAxis = new CategoryAxis();
        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setStartMargin(10);
        xAxis.setOpacity(0.7);
        
	        
        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
	    
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setVerticalGridLinesVisible(false);
        barChart.setBarGap(0);
        barChart.setPadding(new Insets(10,10,10,10));
        barChart.setCategoryGap(0);
        barChart.setLegendVisible(false);
	    
        List<Double> yieldL = new ArrayList<>( keySet );
        yieldL = sortDouble(yieldL);
        
        yield = new String[yieldL.size()];
        for(int i =0; i<yieldL.size(); i++){
        	yield[i] = NumberFormat.getPercentInstance().format( yieldL.get(i) );
        }
	     
        String[] dataStrings = new String[yield.length];

        List<Integer> pandm = new ArrayList<>();
	     
        XYChart.Series<String, Number> seriem = new XYChart.Series<>();
        XYChart.Series<String, Number> seriep = new XYChart.Series<>();
        
        String namep = "正收益",  namem= "负收益";
        for( int i=0; i<yield.length; i++){
        	pandm = zuhe.get(yieldL.get(i));
        	XYChart.Data<String,Number> sp= new XYChart.Data<>(yield[i], Math.abs(pandm.get(0)));
            XYChart.Data<String,Number> sm= new XYChart.Data<>(yield[i], -Math.abs(pandm.get(0)));
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
		if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	double dateheight = 10, dategap = 25;
    	if( withdate ){
    		height -= dateheight;
    		String bdate = yield[0];
    		String mdate = yield[yield.length/2];
    		String edate = yield[yield.length-1];
    		datepane.getChildren().addAll( 
    				commonSet.dateForStackPane(bdate, mdate, edate).getChildren() );
    		datepane.setPrefSize(width-dategap, dateheight);
    	}
    	else{
    		yAxis.setPrefWidth(1);
            yAxis.setOpacity(0.7);
    	}
    	barChart.setMaxSize(width, height);
    	barChart.setMinSize(width, height);
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPane(barChart, dataMap, yield, "收益率", 10);
    	
    	chartpane.getChildren().add(barChart);
    	chartpane.getChildren().add(info);
    	
    	pane.getChildren().add(chartpane);
    	if( withdate ){
    		pane.getChildren().add(datepane);
    		AnchorPane.setTopAnchor(datepane, height+0.0);
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
