package presentation.chart.lineChart;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import presentation.chart.chartService;
import presentation.chart.function.ListToArray;
import presentation.chart.function.ListToArrayService;
import vo.EMAInfoVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import en_um.ChartKind;

/**
 * Created by huihantao on 2017/3/9.
 */

/**
 * @Description: owing to EMAChart's special situation, draw a LineChart for it individually
 * @author: hzp
 * @time: 2017年4月6日
 */
public class EMAChart implements chartService {
	/*
	 * 保利加通道
	 */
	private ListToArrayService listToArray = new ListToArray();

	private AnchorPane pane = new AnchorPane();	

    private ComparedChart comparedChart;

    public EMAChart(List<EMAInfoVO> EMAList) {
    	List<String> dataName = new ArrayList<>();
    	dataName.add("5日");
    	dataName.add("10日");
    	dataName.add("20日");
    	dataName.add("30日");
    	dataName.add("60日");
    	
    	LocalDate[] date = new LocalDate[0];
        date = listToArray.changeLocalDate( EMAList.get(0).getDate() );
    	
    	List<Double[]> doubleList = new ArrayList<>();
    	for( EMAInfoVO ema : EMAList)
    		doubleList.add( listToArray.changeDouble(ema.getEMA()) );
    	
    	comparedChart = new ComparedChart( date, doubleList, dataName, ChartKind.EMA);
    }

    @Override
    public Pane getchart(int width, int height, boolean withdate) {
    	pane = (AnchorPane)comparedChart.getchart(width, height, withdate);
    	
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/EMAChart.css").toExternalForm() );
        return pane;
    }

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		comparedChart.setName(name);
	}

}
