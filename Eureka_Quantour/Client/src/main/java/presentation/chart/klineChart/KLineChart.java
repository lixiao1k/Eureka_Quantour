package presentation.chart.klineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import presentation.chart.chartService;
import vo.SingleStockInfoVO;



public class KLineChart implements chartService {


    private List<BarData> barlist;
    private CandleStickChart candlestickchart;

    public KLineChart(List<SingleStockInfoVO> stocklist){
        this.barlist=convertdata(stocklist);
        this.candlestickchart=new CandleStickChart("K-Line Chart", barlist);

    }

    private static List<BarData> convertdata(List<SingleStockInfoVO> stocklist){
        ArrayList<BarData> bars=new ArrayList();
        for (SingleStockInfoVO stock : stocklist){
            bars.add(new BarData(stock));
        }

        return bars;
    }





    @Override
    public XYChart<String, Number> getchart() {
        return candlestickchart;
    }

    public StackPane overlay(XYChart<String, Number> chart) {
        StackPane stackpane = new StackPane();

        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);

        chart.setLegendVisible(false);
        chart.setAnimated(false);
//        chart.getYAxis().setOpacity(0);


        chart.getYAxis().setAutoRanging(false);
        List<Number> p=new ArrayList<>();
        p.add(1);
        p.add(5);
        p.add(2);
        p.add(3);
        p.add(4);
        chart.getYAxis().invalidateRange(p);

        chart.getYAxis().setAutoRanging(true);
        chart.getYAxis();
        System.out.println(
                ((NumberAxis)chart.getYAxis()).getUpperBound());



//        ((NumberAxis)chart.getYAxis()).setUpperBound(((NumberAxis) candlestickchart.getYAxis()).getUpperBound());
//        ((NumberAxis)chart.getYAxis()).setLowerBound(((NumberAxis) candlestickchart.getYAxis()).getLowerBound());

          System.out.println(
                ((NumberAxis)candlestickchart.getYAxis()).getUpperBound());
        chart.getStylesheets().addAll(getClass().getResource("/overlay-chart.css").toExternalForm());

        stackpane.getChildren().addAll(candlestickchart,chart);

        return stackpane;
    }


}
