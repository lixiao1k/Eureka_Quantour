package presentation.chart.klineChart;

import java.util.*;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import presentation.chart.barChart.VolumeChart;
import presentation.chart.chartService;
import presentation.chart.lineChart.EMAChart;
import vo.EMAInfoVO;
import vo.SingleStockInfoVO;


public class Driver extends Application {

    protected static EMAInfoVO ss1 ;
    protected static EMAInfoVO ss2 ;
    protected static EMAInfoVO ss3 ;
    protected static EMAInfoVO ss4 ;
    protected static EMAInfoVO ss5 ;
    protected static EMAInfoVO ss6 ;
    protected static EMAInfoVO ss7 ;
    protected static EMAInfoVO ss8 ;
    protected static EMAInfoVO ss9 ;
    protected static EMAInfoVO ss10 ;

    protected static SingleStockInfoVO ssi1 ;
    protected static SingleStockInfoVO ssi2 ;
    protected static SingleStockInfoVO ssi3 ;
    protected static SingleStockInfoVO ssi4 ;
    protected static SingleStockInfoVO ssi5 ;
    protected static SingleStockInfoVO ssi6 ;
    protected static SingleStockInfoVO ssi7 ;
    protected static SingleStockInfoVO ssi8 ;
    protected static SingleStockInfoVO ssi9 ;
    protected static SingleStockInfoVO ssi10 ;
    private static Calendar time = Calendar.getInstance();

    @Override
    public void start(Stage stage) throws Exception {




        time = new GregorianCalendar();
        time.set(2014, 3, 29);
        ssi1 = new SingleStockInfoVO().initObject("深发展A", (Calendar) time.clone(), "1", 11.02, 11.16, 11.25, 10.92, 41362100, 11.16, "SZ");
        ssi6 = new SingleStockInfoVO().initObject("中成股份", (Calendar) time.clone(), "151", 7.29, 7.38, 7.4, 7.29, 927600, 7.38, "SZ");


        time.set(2014, 03, 28);
        ssi2 = new SingleStockInfoVO().initObject("深发展A", (Calendar) time.clone(), "1", 11.25, 11.03, 11.28, 10.96, 52604500, 11.03, "SZ");
        ssi7 = new SingleStockInfoVO().initObject("中成股份", (Calendar) time.clone(), "151",7.53, 7.29, 7.53, 7.23, 1581700, 7.29, "SZ");

        time.set(2014, 03, 25);
        ssi3 = new SingleStockInfoVO().initObject("深发展A", (Calendar) time.clone(), "1", 11.23, 11.25, 11.52, 11.11, 71433500, 11.25, "SZ");
        ssi8 = new SingleStockInfoVO().initObject("中成股份", (Calendar) time.clone(), "151", 7.55, 7.47, 7.62, 7.45, 1153400, 7.47, "SZ");

        time.set(2014, 03, 24);
        ssi4 = new SingleStockInfoVO().initObject("深发展A", (Calendar) time.clone(), "1", 11.42, 11.23, 11.45, 11.12, 63400400, 11.23, "SZ");
        ssi9 = new SingleStockInfoVO().initObject("中成股份", (Calendar) time.clone(), "151", 7.74, 7.54, 7.74, 7.52, 1402300, 7.54, "SZ");

        time.set(2014, 03, 23);
        ssi5 = new SingleStockInfoVO().initObject("深发展A", (Calendar) time.clone(), "1", 11.08, 11.3, 11.45, 11.08, 119102800, 11.3, "SZ");
        ssi10 = new SingleStockInfoVO().initObject("中成股份", (Calendar) time.clone(), "151", 7.71, 7.74, 7.77, 7.63, 1074200, 7.74, "SZ");
        List<SingleStockInfoVO> c=new LinkedList<>();
        c.add(ssi1);
        c.add(ssi2);
        c.add(ssi3);
        c.add(ssi4);
        c.add(ssi5);


        KLineChart s=new KLineChart(c);



        time = new GregorianCalendar();
        time.set(2014, 3, 29);
        ss1 = new EMAInfoVO((Calendar) time.clone(),20);
        ss6 = new EMAInfoVO((Calendar) time.clone(),11);



        time.set(2014, 03, 28);
        ss2 = new EMAInfoVO((Calendar) time.clone(),11);
        ss7 = new EMAInfoVO((Calendar) time.clone(),21);


        time.set(2014, 03, 25);
        ss3 = new EMAInfoVO((Calendar) time.clone(),18);
        ss8 = new EMAInfoVO((Calendar) time.clone(),23);

        time.set(2014, 03, 24);
        ss4 = new EMAInfoVO((Calendar) time.clone(),35);
        ss9 = new EMAInfoVO((Calendar) time.clone(),20);

        time.set(2014, 03, 23);
        ss5 = new EMAInfoVO((Calendar) time.clone(),38);
        ss10 = new EMAInfoVO((Calendar) time.clone(),10);
        List<EMAInfoVO> x=new LinkedList<>();
        List<EMAInfoVO> y=new LinkedList<>();
        x.add(ss1);
        x.add(ss2);
        x.add(ss3);
        x.add(ss4);
        x.add(ss5);
        y.add(ss6);
        y.add(ss7);
        y.add(ss8);
        y.add(ss9);
        y.add(ss10);

        List<List<EMAInfoVO> > z=new ArrayList<>();
        z.add(x);
        z.add(y);

        chartService q=new EMAChart(z);

        Scene scene = new Scene(s.overlay(q.getchart()));

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
