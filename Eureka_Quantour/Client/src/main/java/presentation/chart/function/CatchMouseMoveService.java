package presentation.chart.function;

import java.util.Map;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

/**
 * 
 * @Description: catch mouse movement and get relevant data
 * @author: hzp
 * @time: 2017年4月6日
 */
public interface CatchMouseMoveService {
	Label catchMouseReturnInfo( XYChart<String, Number> chart, Map<String, String> dataMap, String[] dates, String name,int index );
}
