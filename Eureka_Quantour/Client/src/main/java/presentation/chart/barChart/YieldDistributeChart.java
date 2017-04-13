package presentation.chart.barChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.BarChart;
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
import vo.YieldDistributionHistogramDataVO;

public class YieldDistributeChart implements chartService{
	
	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private ListToArrayService listToArray = new ListToArray();
	private CommonSetService commonSet = new CommonSet();
	
	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
	private Label begin = new Label();
	private Label end = new Label();
	
	private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private BarChart<String, Number> barChart;
    private Map<String, String> dataMap = new HashMap<String,String>();
    private String[] yield;
	
	public YieldDistributeChart(YieldDistributionHistogramDataVO ydhd){
		xAxis = new CategoryAxis();
        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setStartMargin(5);
	        
        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setOpacity(0.5);
	    
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setVerticalGridLinesVisible(false);
//        barChart.setHorizontalGridLinesVisible(false);
	    
        yield = listToArray.formatDouble( sortDouble(ydhd.getYieldlist()) );
	     
        String[] dataStrings = new String[yield.length];

        List<Integer> plist = ydhd.getPluslist();
        List<Integer> mlist = ydhd.getMinuslist();
	     
        XYChart.Series<String, Number> seriem = new XYChart.Series<>();
        XYChart.Series<String, Number> seriep = new XYChart.Series<>();
        String namep = "正收益",  namem= "负收益";
        for( int i=0; i<yield.length; i++){
        	seriep.getData().add( new XYChart.Data<>(yield[i], Math.abs(plist.get(i))) );
        	seriem.getData().add( new XYChart.Data<>(yield[i], -Math.abs(mlist.get(i))) );
        	dataStrings[i] = namep+" : "+plist.get(i)+"/"+namem+" : "+mlist.get(i);
        }
        barChart.getData().add(seriep);
        barChart.getData().add(seriem);
        
        for(int i=0; i<yield.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(yield[i], dataStrings[i]);
        }
	}

	@Override
	public Pane getchart(int width, int height) {
		// TODO Auto-generated method stub
		if( width>0 ){
    		barChart.setMaxWidth(width);
    		barChart.setMinWidth(width);
    		barChart.setMaxWidth(width);
    		barChart.setMinWidth(width);
    	}
    	if( height>0 ){
    		barChart.setPrefHeight( height-20 );
    		barChart.setMaxHeight( height-20 );
    		barChart.setMinHeight( height-20 );
    	}
    	
    	info = catchMouseMove.catchMouseReturnInfoForAnchorPane(barChart, dataMap, yield, "收益率", 5);
    	begin = commonSet.beignDataForAnchorPane( yield[0], (int)Math.max(height, barChart.getWidth()) );
    	end = commonSet.endDataForAnchorPane( yield[yield.length-1], 
    			(int)Math.max(width, barChart.getWidth()), 
    			(int)Math.max(height, barChart.getWidth()) );
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(barChart);
    	pane.getChildren().add(begin);
    	pane.getChildren().add(end);
    	AnchorPane.setTopAnchor(barChart, 20.0);
    	
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
