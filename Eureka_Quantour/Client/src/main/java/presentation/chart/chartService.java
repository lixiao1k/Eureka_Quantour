package presentation.chart;

import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

/**
 * Created by huihantao on 2017/3/8.
 */
public interface chartService {

    XYChart<String, Number> getchart();

    StackPane overlay(XYChart<String, Number>  chart) ;

}
