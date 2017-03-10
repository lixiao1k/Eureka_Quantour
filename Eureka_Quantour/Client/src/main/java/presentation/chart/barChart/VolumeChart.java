package presentation.chart.barChart;

import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import presentation.chart.chartService;
import vo.EMAInfoVO;
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

        XYChart.Series<String,Number> series =new XYChart.Series<>();

        for (SingleStockInfoVO info:list){
            String label =sdf.format(info.getDate().getTime());
            series.getData().add(new XYChart.Data<>(label,info.getVolume()));
        }
        series.setName("");
        volumechart.getData().add(series);
        volumechart.setTitle("均线图");

        for (XYChart.Series<String, Number> s : volumechart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "\n" +
                                "Number Of Events : " + d.getYValue()));

            }
        }

    }

    @Override
    public XYChart<String, Number> getchart() {
        return volumechart;
    }

}
