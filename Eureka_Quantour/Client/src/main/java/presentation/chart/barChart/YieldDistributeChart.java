package presentation.chart.barChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
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
import vo.YieldDistributionHistogramDataVO;

public class YieldDistributeChart implements chartService{
	
	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private CommonSetService commonSet = new CommonSet();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chartpane = new StackPane();
	private Label info = new Label();
	private Label begin = new Label();
	private Label end = new Label();
	
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
//        yAxis.setTickLabelsVisible(false);
        yAxis.setPrefWidth(1);
        yAxis.setOpacity(0.7);
	    
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setVerticalGridLinesVisible(false);
//        barChart.setHorizontalGridLinesVisible(false);
        barChart.setBarGap(0);
        barChart.setPadding(new Insets(10,10,10,10));
	    
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
        	seriep.getData().add( new XYChart.Data<>(yield[i], Math.abs(pandm.get(0))) );
        	seriem.getData().add( new XYChart.Data<>(yield[i], -Math.abs(pandm.get(1))) );
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
	public Pane getchart(int width, int height) {
		// TODO Auto-generated method stub
		if( width>0 ){
    		barChart.setMaxWidth(width);
    		barChart.setMinWidth(width);
    	}
    	if( height>0 ){
    		barChart.setMaxHeight(height);
    		barChart.setMinHeight(height);
    	}
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPane(barChart, dataMap, yield, "收益率", 10);
    	begin = commonSet.beignDataForAnchorPane( yield[0], (int)Math.max(height, barChart.getWidth()) );
    	end = commonSet.endDataForAnchorPane( yield[yield.length-1], 
    			(int)Math.max(width, barChart.getWidth()), 
    			(int)Math.max(height, barChart.getWidth()) );
    	begin.setLayoutX(begin.getLayoutX()+10);
    	end.setLayoutX(end.getLayoutX()+10);
    	
    	chartpane.getChildren().add(barChart);
    	chartpane.getChildren().add(info);
    	
    	pane.getChildren().add(chartpane);
    	pane.getChildren().add(begin);
    	pane.getChildren().add(end);
    	
    	info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/SingleLineChart.css").toExternalForm() );
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
