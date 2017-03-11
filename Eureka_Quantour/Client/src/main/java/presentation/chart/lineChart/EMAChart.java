package presentation.chart.lineChart;

import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
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

    private LineChart<String, Number> lineChart ;

    public EMAChart(List<List<EMAInfoVO>> lists){
        xAxis=new CategoryAxis();
        yAxis=new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setPrefWidth(28);
//        xAxis.setStartMargin(20);

        yAxis.forceZeroInRangeProperty().setValue(Boolean.FALSE);

        lineChart=new LineChart<>(xAxis,yAxis);

        List<XYChart.Series<String,Number>> series =new ArrayList<>();
        series.add(new XYChart.Series<>());
        series.add(new XYChart.Series<>());
//        series.add(new XYChart.Series<>());
//        series.add(new XYChart.Series<>());
//        series.add(new XYChart.Series<>());



        for (List<EMAInfoVO> list:lists) {
            int index=lists.indexOf(list);
            XYChart.Series<String,Number> serie=series.get(index);

            for (EMAInfoVO info : list) {
                String label = sdf.format(info.getDate().getTime());
                serie.getData().add(new XYChart.Data<>(label, info.getEMA()));
            }
            serie.setName(1+index+"");


        }
        lineChart.getData().addAll(series);

        lineChart.setTitle("均线图");

        for (XYChart.Series<String, Number> s : lineChart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "\n" +
                                "Number Of Events : " + d.getYValue()));

            }
        }

        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);


    }

    @Override
    public XYChart<String, Number> getchart() {
        return lineChart;
    }

    @Override
    public StackPane overlay(XYChart<String, Number> chart) {
        StackPane stackpane = new StackPane();

        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.getXAxis().setVisible(false);

        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.getYAxis().setSide(Side.RIGHT);
//        chart.getYAxis().setOpacity(0);

        ((NumberAxis)chart.getYAxis()).setUpperBound(yAxis.getUpperBound());
        lineChart.getStylesheets().addAll(getClass().getResource("/overlay-chart.css").toExternalForm());

        stackpane.getChildren().addAll(chart,lineChart);

        return stackpane;
    }
}
