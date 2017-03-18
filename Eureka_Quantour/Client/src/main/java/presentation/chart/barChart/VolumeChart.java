package presentation.chart.barChart;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import presentation.chart.chartService;
import vo.SingleStockInfoVO;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by huihantao on 2017/3/9.
 */
public class VolumeChart implements chartService {

    protected NumberAxis yAxis;
    protected CategoryAxis xAxis;
    SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");

    private BarChart<String, Number> volumechart ;

    public VolumeChart(List<SingleStockInfoVO> list){
        xAxis=new CategoryAxis();
        yAxis=new NumberAxis();
        yAxis.setPrefWidth(35);
        yAxis.autoRangingProperty().set(true);
        yAxis.forceZeroInRangeProperty().setValue(Boolean.FALSE);


        volumechart=new BarChart<>(xAxis,yAxis);
        volumechart.setHorizontalGridLinesVisible(false);
        volumechart.setVerticalGridLinesVisible(false);

        XYChart.Series<String,Number> series =new XYChart.Series<>();

        for (SingleStockInfoVO info:list){ 
            String label =sdf.format(info.getDate().getTime());
            XYChart.Data<String,Number> s= new XYChart.Data<>(label,info.getVolume());
            s.nodeProperty().addListener(new ChangeListener<Node>() {

                @Override
                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                    if (newValue != null) {
                        if (info.getClose()>info.getOpen() ) {
                            newValue.setStyle("-fx-bar-fill: red;");
                        } else  {
                            newValue.setStyle("-fx-bar-fill: green;");
                        }
                    }
                }
            });
            series.getData().add(s);
        }

        volumechart.getData().add(series);
        volumechart.setLegendVisible(false);
        volumechart.setMaxSize(1000, 50);
        volumechart.setBarGap(0);
        volumechart.getStylesheets().add(getClass().getClassLoader().getResource("styles/VolumeChart.css").toExternalForm());
        for (XYChart.Series<String, Number> s : volumechart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "   " +d.getYValue()));

            }
        }

    }

    @Override
    public XYChart<String, Number> getchart() {
        return volumechart;
    }

    @Override
    public void setName(String name) {
        volumechart.setTitle(name);
    }


}
