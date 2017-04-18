package presentation.chart.function;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public interface CommonSetService {

	/**
	 * @Description: according to AnchorPane's feature and chart's size, set label's layoutX and layoutY
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label beignDateForAnchorPane(String content, int height);
	
	/**
	 * @Description: according to AnchorPane's feature and chart's size, set label's layoutX and layoutY
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label endDateForAnchorPane(String content, int width, int height);
	
	/**
	 * @Description: according to StackPane's feature, set label's layoutX as Pos.BOTTOM_LEFT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label beignDateForStackPane(String content, int height);
	
	/**
	 * @Description: according to StackPane's feature, set label's layoutX as Pos.BOTTOM_LEFT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label middleDateForStackPane(String content, int height);
	
	/**
	 * @Description: according to StackPane's feature, set label's layoutX as Pos.BOTTOM_LEFT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	Label endDateForStackPane(String content, int height);
	
	Pane dateForStackPane(String bdate, String mdate, String edate);
}
