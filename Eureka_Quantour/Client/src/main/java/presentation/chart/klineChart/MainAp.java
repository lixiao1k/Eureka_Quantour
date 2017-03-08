package presentation.chart.klineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import presentation.chart.chartService;
import vo.SingleStockInfoVO;



public class MainAp
    extends Application
        implements chartService {


    private List<BarData> barlist;

//    public MainApp(List<SingleStockInfoVO> stocklist){
//        this.barlist=convertdata(stocklist);
//    }

    private List<BarData> convertdata(List<SingleStockInfoVO> stocklist){
        ArrayList<BarData> bars=new ArrayList();
        for (SingleStockInfoVO stock : stocklist){
            bars.add(new BarData(stock));
        }

        return bars;
    }


    public void start(Stage stage) throws Exception {
        CandleStickChart candleStickChart = new CandleStickChart("K-Line Chart", buildBars());
        Scene scene = new Scene(candleStickChart);
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }


    public List<BarData> buildBars() {
        double previousClose = 180;


        final List<BarData> bars = new ArrayList<>();
        Calendar now = new GregorianCalendar();
        for (int i = 0; i < 10; i++) {
            double open = getNewValue(previousClose);
            double close = getNewValue(open);
            double high = Math.max(open + getRandom(),close);
            double low = Math.min(open - getRandom(),close);
            previousClose = close;

            BarData bar = new BarData((Calendar) now.clone(), open, high, low, close, 1);
            now.add(Calendar.YEAR, 1);
            bars.add(bar);
        }
        return bars;
    }


    protected double getNewValue( double previousValue ) {
        int sign;

        if( Math.random() < 0.5 ) {
            sign = -1;
        } else {
            sign = 1;
        }
        return getRandom() * sign + previousValue;
    }

    protected double getRandom() {
        double newValue = 0;
        newValue = Math.random() * 10;
        return newValue;
    }

    public static void main(String[] args){
        launch(args);
    }



    @Override
    public XYChart<String, Number> getchart() {
        return new CandleStickChart("K-Line Chart", barlist);
    }


}
