package presentation.chart.barChart;

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

        yAxis.autoRangingProperty().set(true);
        yAxis.forceZeroInRangeProperty().setValue(Boolean.FALSE);



        volumechart=new BarChart<>(xAxis,yAxis);

        volumechart.setHorizontalGridLinesVisible(false);
        volumechart.setVerticalGridLinesVisible(false);

        XYChart.Series<String,Number> series =new XYChart.Series<>();

        for (SingleStockInfoVO info:list){ 
            String label =sdf.format(info.getDate().getTime());
            series.getData().add(new XYChart.Data<>(label,info.getVolume()));
        }

        volumechart.getData().add(series);
        volumechart.setTitle("成交量图");
        volumechart.setLegendVisible(false);
        volumechart.setMaxSize(1000, 50);
        volumechart.setBarGap(0);
        volumechart.getYAxis().setTickLabelsVisible(false);
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
