package presentation.saveAsPNG;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by shelton on 2017/5/31.
 */
public class SaveAsPNG {
    public void print(Pane pane){
        if(pane==null){
            Notifications.create().title("异常").text("没有图片可以保存").showWarning();
        }else{
            Scene scene1 = new Scene(pane);
            save(scene1);
        }
    }
    private void save(Scene scene){
        WritableImage image = scene.snapshot(null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存为");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PNG files (*.png)","*.png");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showSaveDialog(null);
        if(file!=null){
            if(!file.getPath().endsWith(".png")){
                file = new File(file.getPath()+".png");
            }
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
