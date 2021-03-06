package presentation.chart.scatterchart;

import java.util.ArrayList;
import java.util.List;

import en_um.ChartKind;
import javafx.scene.layout.Pane;
import presentation.chart.chartService;

/**
 * 
 * @Description: 涨跌图的分布点图，可以根据需要绘制单点图、半点图、全点图
 * @author: hzp 
 * @date: Apr 19, 2017
 */
public class YieldPointChart implements chartService{
	
	private PointChart pointChart;
	
	public YieldPointChart(List<Integer> nums, ChartKind kind){
		int[] yield = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; 
		
		List<int[]> num = new ArrayList<>();
		int[] numt = new int[nums.size()];
		for( int i=0; i<nums.size(); i++ ){
			numt[i] = nums.get(i);
		}
		num.add(numt);
		
		List<String> dataName = new ArrayList<>();
		dataName.add("Num");

		pointChart = new PointChart( yield, num, dataName, kind, true);
	}

	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		return pointChart.getchart(width, height, withdate);
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		pointChart.setName(name);
	}

}
