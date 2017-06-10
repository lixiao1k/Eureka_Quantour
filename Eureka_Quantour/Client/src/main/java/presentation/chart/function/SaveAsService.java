package presentation.chart.function;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public interface SaveAsService {
	
	/**
	 * @Description: save scene as image( .png )
	 * @author: hzp
	 */
	public void saveAsPng( Scene scene, String path );
	
	/**
	 * @Description: save pane as image( .png )
	 * @author: hzp
	 */
	public void saveAsPng( Pane pane, String path );
}
