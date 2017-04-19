package presentation.chart.function;

import java.util.Map;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 * @Description: catch mouse movement and calculate corresponding data according to mouse's location
 * @author: hzp
 * @time: 2017年4月6日
 */
public interface CatchMouseMoveService {
	/**
	 * @Description: according to AnchorPane's feature and mouse's location, 
	 * 				 set label's layoutX and layoutY
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label catchMouseReturnInfoForAnchorPane( 
			XYChart<String, Number> chart, Map<String, String> dataMap, String[] dates, String name,int index );

	/**
	 * @Description: according to StackPane's feature and mouse's location, 
	 * 				 set label's layoutX as StackPane.LEFT or StackPane.RIGHT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label catchMouseReturnInfoForStackPane( 
			XYChart<String, Number> chart, Map<String, String> dataMap, String[] dates, String name,int index );
	
	Label catchMouseReturnInfoForStackPaneNN( 
			XYChart<Number, Number> chart, Map<String, String> dataMap, String[] dates, String name,int index );
}
