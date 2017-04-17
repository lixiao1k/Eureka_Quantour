package presentation.chart.lineChart;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import presentation.chart.function.ListToArray;
import presentation.chart.function.ListToArrayService;
import vo.EMAInfoVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huihantao on 2017/3/9.
 */

/**
 * @Description: owing to EMAChart's special situation, draw a LineChart for it individually
 * @author: hzp
 * @time: 2017年4月6日
 */
public class EMAChart extends ComparedChart {
	/*
	 * 保利加通道
	 */
	private ListToArrayService listToArray = new ListToArray();

	private StackPane pane = new StackPane();	

    private ComparedChart comparedChart;

    public EMAChart(List<EMAInfoVO> EMAList) {
    	List<String> dataName = new ArrayList<>();
    	dataName.add("5日均线");
    	dataName.add("10日均线");
    	dataName.add("20日均线");
    	dataName.add("30日均线");
    	dataName.add("60日均线");
    	
    	LocalDate[] date = new LocalDate[0];
        date = listToArray.changeLocalDate( EMAList.get(0).getDate() );
    	
    	List<Double[]> doubleList = new ArrayList<>();
    	for( EMAInfoVO ema : EMAList)
    		doubleList.add( listToArray.changeDouble(ema.getEMA()) );
    	
    	comparedChart = new ComparedChart( date, doubleList, dataName );
    }

    @Override
    public Pane getchart(int width, int height) {
    	pane.getChildren().addAll( comparedChart.getchart(width, height).getChildren() );
    	
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/EMAChart.css").toExternalForm() );
        return pane;
    }

}
