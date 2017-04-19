package presentation.chart.piechart;

import java.util.List;

import javafx.scene.layout.Pane;
import presentation.chart.chartService;

public class YieldFanChart implements chartService{

	private FanChart fanChart;
	public YieldFanChart(List<Integer> nums) {
		// TODO Auto-generated constructor stub
		String[] nameOfContent = {"-10% ~ -5%", "-5% ~ 0", "0 ~ 5%", "5% ~ 10%"};
		int[] num = new int[4];
		for( int i=0; i<4; i++ ){
			num[i] = nums.get(i);
		}
		fanChart = new FanChart( nameOfContent, num);
	}
	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		return fanChart.getchart(width, height, withdate);
	}
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		fanChart.setName(name);
	}

}
