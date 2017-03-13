package presentation.chart.lineChart;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import presentation.chart.chartService;
import vo.ComparedInfoVO;
import vo.EMAInfoVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by huihantao on 2017/3/13.
 */
public class ComparedCloseChart implements chartService {


    protected NumberAxis yAxis;
    protected CategoryAxis xAxis;
    SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");

    private LineChart<String, Number> lineChart;

    public ComparedCloseChart(Calendar[] date,double[] data1, double[] data2,String s1,String s2) {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);





        yAxis.setPrefWidth(35);

        yAxis.forceZeroInRangeProperty().setValue(false);

        lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> serie1 = new XYChart.Series<>();
        serie1.setName(s1);
        XYChart.Series<String, Number> serie2 = new XYChart.Series<>();
        serie2.setName(s2);

        for (int i=0;i<date.length;i++){
            String label = sdf.format(date[i].getTime());
            if (data1[i]!=0)
            serie1.getData().add(new XYChart.Data<>(label, data1[i]));

            if(data2[i]!=0)
            serie2.getData().add(new XYChart.Data<>(label, data2[i]));
        }





        lineChart.getData().addAll(serie1,serie2);

        lineChart.setTitle("对比图");

        for (XYChart.Series<String, Number> s : lineChart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "   " +d.getYValue()));

            }
        }


    }

    @Override
    public XYChart<String, Number> getchart() {
        return lineChart;
    }
}
