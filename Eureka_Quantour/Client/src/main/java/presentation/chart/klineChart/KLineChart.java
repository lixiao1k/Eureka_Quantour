package presentation.chart.klineChart;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import presentation.chart.chartService;
import presentation.chart.show.CatchMouseMove;
import presentation.chart.show.CatchMouseMoveService;
import vo.SingleStockInfoVO;

public class KLineChart implements chartService {

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	
	private AnchorPane pane = new AnchorPane();
	private Label info = new Label();
    private CandleStickChart candlestickchart;

    public KLineChart(List<SingleStockInfoVO> stocklist){
        this.candlestickchart = new CandleStickChart("k线图", stocklist);
        candlestickchart.setHorizontalGridLinesVisible(false);
        candlestickchart.setVerticalGridLinesVisible(false);
        
        if (stocklist.size()>63) 
        	candlestickchart.setCandlewidth(630/stocklist.size());
        
        candlestickchart.getStylesheets().add(getClass().getResource("/styles/CandleStickChartStyles.css").toExternalForm());
    }

    @Override
    public Pane getchart(int width, int height) {
    	if( width>0 ){
    		candlestickchart.setMaxWidth(width);
    		candlestickchart.setMinWidth(width);
    	}
    	if( height>0 ){
    		candlestickchart.setMaxHeight(height);
    		candlestickchart.setMaxHeight(height);
    	}
    	info = catchMouseMove.createCursorGraphCoordsMonitorLabel(candlestickchart, candlestickchart.dataMap, candlestickchart.dates);
    	
    	pane.getChildren().add(info);
    	pane.getChildren().add(candlestickchart);
    	info.setLayoutX(5);
    	AnchorPane.setTopAnchor(candlestickchart, 30.0);
    	
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/SingleLineChart.css").toExternalForm() );
    	return pane;
    }

    @Override
    public void setName(String name) {
        candlestickchart.setTitle(name);
    }

}
