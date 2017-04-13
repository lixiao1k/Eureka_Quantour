package presentation.chart.klineChart;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import presentation.chart.chartService;
import presentation.chart.barChart.VolumeChart;
import presentation.chart.function.CatchMouseMove;
import presentation.chart.function.CatchMouseMoveService;
import presentation.chart.function.CommonSet;
import presentation.chart.function.CommonSetService;
import vo.SingleStockInfoVO;

public class KLineChart implements chartService {

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private CommonSetService commonSet = new CommonSet();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chart1 = new StackPane();
	private StackPane chart2 = new StackPane();
	private Label info = new Label();
	private Label infoD = new Label();
	private Label begin = new Label();
	private Label end = new Label();
    private CandleStickChart candlestickchart;
    private VolumeChart volumeChart;
    private BarChart<String, Number> volumeBarChart;

    public KLineChart(List<SingleStockInfoVO> stocklist){
        this.candlestickchart = new CandleStickChart("k线图", stocklist);
        this.volumeChart = new VolumeChart(stocklist);
        candlestickchart.setHorizontalGridLinesVisible(false);
        candlestickchart.setVerticalGridLinesVisible(false);
        
        if (stocklist.size()>63) 
        	candlestickchart.setCandlewidth(630/stocklist.size());
        
        candlestickchart.getStylesheets().add(getClass().getResource("/styles/CandleStickChartStyles.css").toExternalForm());
    }

    @Override
    public Pane getchart(int width, int height) {
    	volumeBarChart = (BarChart<String, Number>)volumeChart.getchart(width, height/8*3);
    	if( width>0 ){
    		candlestickchart.setMaxWidth(width);
    		candlestickchart.setMinWidth(width);
    		pane.setMaxWidth(width);
    	}
    	if( height>0 ){
    		candlestickchart.setMaxHeight( height/8*6 );
    		candlestickchart.setMinHeight( height/8*6 );
    		pane.setMaxHeight(height);
    	}
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPane(
    			candlestickchart, candlestickchart.dataMap, candlestickchart.dates, "date", 10);
    	infoD = catchMouseMove.catchMouseReturnInfoForStackPane(
    			volumeBarChart, candlestickchart.dataMap, candlestickchart.dates, "date", 10);
    	begin = commonSet.beignDataForStackPane( candlestickchart.dates[0], (int)Math.max(height, candlestickchart.getWidth()) );
    	end = commonSet.endDataForStackPane(candlestickchart.dates[candlestickchart.dates.length-1], 
    			(int)Math.max(width, candlestickchart.getWidth()), 
    			(int)Math.max(height, candlestickchart.getWidth()) );
    	
    	chart1.getChildren().add(candlestickchart);
    	StackPane.setAlignment(candlestickchart, Pos.BOTTOM_CENTER);
    	chart1.getChildren().add(info);
    	chart1.getChildren().add(infoD);
    	
    	chart2.getChildren().add(volumeBarChart);
    	StackPane.setAlignment(volumeBarChart, Pos.TOP_CENTER);
    	chart2.getChildren().add(begin);
    	chart2.getChildren().add(end);
    	
    	pane.getChildren().add(chart1);
    	pane.getChildren().add(chart2);
    	AnchorPane.setTopAnchor(chart2, height/8*5.0);
    	
    	info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	infoD.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/SingleLineChart.css").toExternalForm() );
    	return pane;
    }

    @Override
    public void setName(String name) {
        candlestickchart.setTitle(name);
    }

}
