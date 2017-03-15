package presentation.chart.lineChart;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import presentation.chart.chartService;
import vo.EMAInfoVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huihantao on 2017/3/9.
 */
public class EMAChart implements chartService {
    protected NumberAxis yAxis;
    protected CategoryAxis xAxis;
    SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");

    private LineChart<String, Number> lineChart;

    public EMAChart(List<List<EMAInfoVO>> lists) {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);

        yAxis.setPrefWidth(35);

        yAxis.forceZeroInRangeProperty().setValue(false);

        lineChart = new LineChart<>(xAxis, yAxis);

        List<XYChart.Series<String, Number>> series = new ArrayList<>();



        for (List<EMAInfoVO> list : lists) {
            int index = lists.indexOf(list);
            XYChart.Series<String, Number> serie = new XYChart.Series<>();

            for (EMAInfoVO info : list) {
                String label = sdf.format(info.getDate().getTime());
                serie.getData().add(new XYChart.Data<>(label, info.getEMA()));
            }
            serie.setName(1 + index + "");
            series.add(serie);


        }
        lineChart.getData().addAll(series);

        lineChart.setTitle("均线图");
        lineChart.setCreateSymbols(false);

        for (XYChart.Series<String, Number> s : lineChart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "   " +d.getYValue()));

            }
        }
        lineChart.getStylesheets().add(getClass().getResource("/styles/EMAChart.css").toExternalForm());


    }

    @Override
    public XYChart<String, Number> getchart() {
        return lineChart;
    }

}
