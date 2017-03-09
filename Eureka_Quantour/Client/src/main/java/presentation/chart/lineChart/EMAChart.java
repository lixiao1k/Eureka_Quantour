package presentation.chart.lineChart;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import presentation.chart.chartService;
import vo.EMAInfoVO;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by huihantao on 2017/3/9.
 */
public class EMAChart implements chartService {
    protected NumberAxis yAxis;
    protected CategoryAxis xAxis;
    SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");

    private LineChart<String, Number> lineChart ;

    public EMAChart(List<EMAInfoVO> list){
        xAxis=new CategoryAxis();
        yAxis=new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.forceZeroInRangeProperty().setValue(Boolean.FALSE);

        lineChart=new LineChart<>(xAxis,yAxis);

        XYChart.Series<String,Number> series =new XYChart.Series<>();

        for (EMAInfoVO info:list){
            String label =sdf.format(info.getDate().getTime());
            series.getData().add(new XYChart.Data<>(label,info.getEMA()));
        }
        series.setName("");
        lineChart.getStylesheets().add(getClass().getResource("/styles/CandleStickChartStyles.css").toExternalForm());
        lineChart.getData().add(series);
        lineChart.setTitle("均线图");

    }

    @Override
    public XYChart<String, Number> getchart() {
        return lineChart;
    }
}
