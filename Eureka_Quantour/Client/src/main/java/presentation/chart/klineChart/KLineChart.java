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
import vo.SingleStockInfoVO;

public class KLineChart implements chartService {

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chart1 = new StackPane();
	private StackPane chart2 = new StackPane();
	private Label info = new Label();
	private Label infoD = new Label();
    private CandleStickChart candlestickchart;
    private VolumeChart volumeChart;
    private BarChart<String, Number> volumeBarChart;
    private int datanum = 0;

    public KLineChart(List<SingleStockInfoVO> stocklist){
        this.candlestickchart = new CandleStickChart("k线图", stocklist);
        this.volumeChart = new VolumeChart(stocklist);
        candlestickchart.setHorizontalGridLinesVisible(false);
        candlestickchart.setVerticalGridLinesVisible(false);
        
        datanum = stocklist.size();
//        if (stocklist.size()>63)
//        	candlestickchart.setCandlewidth(630/stocklist.size());
        
        candlestickchart.getStylesheets().add(getClass().getResource("/styles/CandleStickChartStyles.css").toExternalForm());
    }

    @Override
    public Pane getchart(int width, int height) {
    	if( width<=0 )
    		width = 317;
    	if( height<=0 )
    		height = 200;
    	
    	if( datanum>30 )
        	candlestickchart.setCandlewidth( width/datanum );
    	
    	
    	volumeBarChart = (BarChart<String, Number>)volumeChart.getchart(width, height/8*3);
    	candlestickchart.setMaxSize(width, height/8*6);
    	candlestickchart.setMinSize(width, height/8*6);
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPane(
    			candlestickchart, candlestickchart.dataMap, candlestickchart.dates, "date", 5);
    	infoD = catchMouseMove.catchMouseReturnInfoForStackPane(
    			volumeBarChart, candlestickchart.dataMap, candlestickchart.dates, "date", 5);
    	
    	chart1.getChildren().add(candlestickchart);
    	StackPane.setAlignment(candlestickchart, Pos.BOTTOM_CENTER);
    	chart1.getChildren().add(info);
    	chart1.getChildren().add(infoD);
    	
    	chart2.getChildren().add(volumeBarChart);
    	StackPane.setAlignment(volumeBarChart, Pos.TOP_CENTER);

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
