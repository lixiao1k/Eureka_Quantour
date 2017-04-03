package presentation.chart;

import javafx.scene.layout.Pane;

/**
 * Created by huihantao on 2017/3/8.
 */
public interface chartService {

    Pane getchart(int width, int height);

    void setName(String name);


}
