package presentation.chart.lineChart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.chart.chartService;
import presentation.chart.klineChart.KLineChart;
import vo.EMAInfoVO;
import vo.SingleStockInfoVO;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by huihantao on 2017/3/9.
 */
public class Driver extends Application {
    protected static EMAInfoVO ssi1 ;
    protected static EMAInfoVO ssi2 ;
    protected static EMAInfoVO ssi3 ;
    protected static EMAInfoVO ssi4 ;
    protected static EMAInfoVO ssi5 ;

    private static Calendar time = Calendar.getInstance();

    public void start(Stage stage) throws Exception {

    time = new GregorianCalendar();
        time.set(2014, 3, 29);
    ssi1 = new EMAInfoVO((Calendar) time.clone(),20);



        time.set(2014, 03, 28);
    ssi2 = new EMAInfoVO((Calendar) time.clone(),11);


        time.set(2014, 03, 25);
    ssi3 = new EMAInfoVO((Calendar) time.clone(),18);

        time.set(2014, 03, 24);
    ssi4 = new EMAInfoVO((Calendar) time.clone(),35);

        time.set(2014, 03, 23);
    ssi5 = new EMAInfoVO((Calendar) time.clone(),6);
    List<EMAInfoVO> x=new LinkedList<>();
        x.add(ssi1);
        x.add(ssi2);
        x.add(ssi3);
        x.add(ssi4);
        x.add(ssi5);


    chartService s=new EMAChart(x);
    Scene scene = new Scene(s.getchart());


        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();

}




    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
