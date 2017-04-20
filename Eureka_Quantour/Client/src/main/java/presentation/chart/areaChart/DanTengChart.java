package presentation.chart.areaChart;

import java.util.List;

import javafx.scene.layout.Pane;
import presentation.chart.chartService;

public class DanTengChart implements chartService{
	
	private AreaLineChart areaLineChart;
	
	public DanTengChart( List<Double> shenglv ){
		int[] cycle = {0, 5, 10, 15, 20, 25, 30};
		
		Double[] dataList = new Double[shenglv.size()];
		for( int i=0; i<dataList.length; i++ )
			dataList[i] = shenglv.get(i);
		areaLineChart = new AreaLineChart( cycle, dataList, "");
	}

	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		return areaLineChart.getchart(width, height, withdate);
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		areaLineChart.setName(name);
	}

}
