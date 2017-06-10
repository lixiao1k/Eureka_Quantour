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

/**
 * 
 * @Description: 绘制K线图，并在CandleStickChart下面添加交易量的柱状图
 * @author: hzp
 */
public class KLineChart implements chartService {

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private CommonSetService commonSet = new CommonSet();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chart1 = new StackPane();
	private StackPane chart2 = new StackPane();
	private StackPane datepane = new StackPane();
	private Label info = new Label();
	private Label infoD = new Label();
    private CandleStickChart candlestickchart;
    private VolumeChart volumeChart;
    private BarChart<String, Number> volumeBarChart;
    private int datanum = 0;
    private String bdate = "";
    private String mdate = "";
    private String edate = "";

    public KLineChart(List<SingleStockInfoVO> stocklist){
        this.candlestickchart = new CandleStickChart("k线图", stocklist);
        this.volumeChart = new VolumeChart(stocklist);
        candlestickchart.setHorizontalGridLinesVisible(false);
        candlestickchart.setVerticalGridLinesVisible(false);
        
        datanum = stocklist.size();
        if( datanum>0 ){
        	bdate = stocklist.get(0).getDate().toString();
        	mdate = stocklist.get(datanum/2).getDate().toString();
        	edate = stocklist.get(datanum-1).getDate().toString();
        }
//        if (stocklist.size()>63)
//        	candlestickchart.setCandlewidth(630/stocklist.size());
        
        candlestickchart.getStylesheets().add(
        		getClass().getResource("/styles/CandleStickChartStyles.css").toExternalForm());
    }

    @Override
    public Pane getchart(int width, int height, boolean withdate) {
    	if( width<=0 )
    		width = 317;
    	if( height<=0 )
    		height = 200;
    	int dateheight = 10;
    	if( withdate ){
    		height -= dateheight;
    		datepane.getChildren().addAll( 
    				commonSet.dateForStackPane(bdate, mdate, edate).getChildren() );
    		datepane.setPrefSize(width, dateheight);
    		datepane.getStylesheets().add(
            		getClass().getResource("/styles/DateLabel.css").toExternalForm());
    	}
    	if( datanum>30 )
        	candlestickchart.setCandlewidth( width/datanum );

    	volumeBarChart = (BarChart<String, Number>)volumeChart.getchart(width, height/8*3);
    	candlestickchart.setMaxSize(width, height/8*5.5);
    	candlestickchart.setMinSize(width, height/8*5.5);
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPaneSN(
    			candlestickchart, candlestickchart.dataMap, candlestickchart.dates, "date", 5);
    	infoD = catchMouseMove.catchMouseReturnInfoForStackPaneSN(
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
    	if( withdate ){
    		pane.getChildren().add(datepane);
    		AnchorPane.setTopAnchor(datepane, height+0.0);
    	}
    	
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
