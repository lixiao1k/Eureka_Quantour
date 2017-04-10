package presentation.chart.klineChart;

import java.util.List;

import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    	int h = height-20;
    	volumeBarChart = (BarChart<String, Number>)volumeChart.getchart(width, h/8*4);
    	if( width>0 ){
    		candlestickchart.setMaxWidth(width);
    		candlestickchart.setMinWidth(width);
    	}
    	if( height>0 ){
    		candlestickchart.setMaxHeight( h/8*5 );
    		candlestickchart.setMinHeight( h/8*5 );
    	}
    	info = catchMouseMove.catchMouseReturnInfo(
    			candlestickchart, candlestickchart.dataMap, candlestickchart.dates, "date", 10);
    	infoD = catchMouseMove.catchMouseReturnInfo(
    			volumeBarChart, candlestickchart.dataMap, candlestickchart.dates, "date", 10);
    	begin = commonSet.beignData( candlestickchart.dates[0], (int)Math.max(height, candlestickchart.getWidth()) );
    	end = commonSet.endData(candlestickchart.dates[candlestickchart.dates.length-1], 
    			(int)Math.max(width, candlestickchart.getWidth()), 
    			(int)Math.max(height, candlestickchart.getWidth()) );
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(infoD);
    	pane.getChildren().add(candlestickchart);
    	pane.getChildren().add(volumeBarChart);
    	pane.getChildren().add(begin);
    	pane.getChildren().add(end);
    	AnchorPane.setTopAnchor(candlestickchart, 20.0);
    	AnchorPane.setTopAnchor(volumeBarChart, 20.0+h/8*4);
    	
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
