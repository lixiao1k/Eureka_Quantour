package presentation.chart.function;

import javafx.scene.control.Label;

public interface CommonSetService {

	Label beignDataForAnchorPane(String content, int height);
	
	Label endDataForAnchorPane(String content, int width, int height);
	
	Label beignDataForStackPane(String content, int height);
	
	Label endDataForStackPane(String content, int width, int height);
	
}
