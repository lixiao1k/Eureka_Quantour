package presentation.chart.function;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public interface SaveAsService {
	
	public void saveAsPng( Scene scene, String path );
	
	public void saveAsPng( Pane pane, String path );
}
