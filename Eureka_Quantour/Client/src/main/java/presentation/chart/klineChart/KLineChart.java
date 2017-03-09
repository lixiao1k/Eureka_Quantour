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



public class KLineChart implements chartService {


    private List<BarData> barlist;

    public KLineChart(List<SingleStockInfoVO> stocklist){
        this.barlist=convertdata(stocklist);
<<<<<<< HEAD
        System.out.println(barlist.size());
=======
>>>>>>> ba3514983e2dd69f921c53e425e144e0ca8acc14
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
        return new CandleStickChart("K-Line Chart", barlist);
    }


}
