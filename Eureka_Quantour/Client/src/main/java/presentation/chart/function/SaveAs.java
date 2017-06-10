package presentation.chart.function;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

public class SaveAs implements SaveAsService{
	
	/**
	 * @Description: save scene as image( .png )
	 * @author: hzp
	 */
	public void saveAsPng( Scene scene, String path ){
		 WritableImage image = scene.snapshot( null );
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	/**
	 * @Description: save pane as image( .png )
	 * @author: hzp
	 */
	public void saveAsPng( Pane pane, String path ){
		WritableImage image = pane.snapshot(new SnapshotParameters(), null);

	    // TODO: probably use a file chooser here
	    File file = new File(path+".png");

	    try {
	        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	    } catch (IOException e) {
	        // TODO: handle exception here
	    }
		
	}
}
