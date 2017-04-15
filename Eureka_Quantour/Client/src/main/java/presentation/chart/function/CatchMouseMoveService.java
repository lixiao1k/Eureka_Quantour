package presentation.chart.function;

import java.util.Map;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * 
 * @Description: catch mouse movement and get relevant data
 * @author: hzp
 * @time: 2017年4月6日
 */
public interface CatchMouseMoveService {
	Label catchMouseReturnInfoForAnchorPane( XYChart<String, Number> chart, Map<String, String> dataMap, String[] dates, String name,int index );

	Label catchMouseReturnInfoForStackPane( XYChart<String, Number> chart, Map<String, String> dataMap, String[] dates, String name,int index );

//	Pane catchMouseReturnInfoForYieldComparedChart( XYChart<String, Number> chart, Map<String, String> dataMap, String[] dates, int index);
}
