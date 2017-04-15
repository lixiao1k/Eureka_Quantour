package presentation.chart.function;

import javafx.scene.control.Label;

public interface CommonSetService {

	/**
	 * @Description: according to AnchorPane's feature and chart's size, set label's layoutX and layoutY
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label beignDataForAnchorPane(String content, int height);
	
	/**
	 * @Description: according to AnchorPane's feature and chart's size, set label's layoutX and layoutY
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label endDataForAnchorPane(String content, int width, int height);
	
	/**
	 * @Description: according to StackPane's feature, set label's layoutX as Pos.BOTTOM_LEFT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label beignDataForStackPane(String content, int height);
	
	/**
	 * @Description: according to StackPane's feature, set label's layoutX as Pos.BOTTOM_LEFT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label endDataForStackPane(String content, int width, int height);
	
}
