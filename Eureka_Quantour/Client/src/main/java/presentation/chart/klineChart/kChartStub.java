//package presentation.chart.klineChart;
///*
//Copyright 2014 Zoi Capital, LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
// */
//
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.ResourceBundle;
//import javafx.fxml.Initializable;
//
//
//public class kChartStub implements Initializable {
//
//
//    public CandleStickChart getKChart() {
//        CandleStickChart candleStickChart = new CandleStickChart("S&P 500 Index", buildBars());
//        candleStickChart.getStylesheets().add("/styles/CandleStickChartStyles.css");
////        Scene scene = new Scene(candleStickChart);
////        scene.getStylesheets().add("/styles/CandleStickChartStyles.css");
//
////        stage.setTitle("JavaFX and Maven");
////        stage.setScene(scene);
////        stage.show();
////
////        candleStickChart.setYAxisFormatter(new DecimalAxisFormatter("#000.00"));
//        return candleStickChart;
//    }
////
//
//    public List<BarData> buildBars() {
//        double previousClose = 1850;
//
//
//        final List<BarData> bars = new ArrayList<>();
//        GregorianCalendar now = new GregorianCalendar();
//        for (int i = 0; i < 26; i++) {
//            double open = getNewValue(previousClose);
//            double close = getNewValue(open);
//            double high = Math.max(open + getRandom(),close);
//            double low = Math.min(open - getRandom(),close);
//            previousClose = close;
//
////            BarData bar = new BarData((GregorianCalendar) now.clone(), open, high, low, close, 1);
////            now.add(Calendar.MONTH,1);
////            bars.add(bar);
//        }
//        return bars;
//    }
//
//
//    protected double getNewValue( double previousValue ) {
//        int sign;
//
//        if( Math.random() < 0.5 ) {
//            sign = -1;
//        } else {
//            sign = 1;
//        }
//        return getRandom() * sign + previousValue;
//    }
//
//    protected double getRandom() {
//        double newValue = 0;
//        newValue = Math.random() * 10;
//        return newValue;
//    }
//
//
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		// TODO Auto-generated method stub
//
//	}
//
////    /**
////     * The main() method is ignored in correctly deployed JavaFX application.
////     * main() serves only as fallback in case the application can not be
////     * launched through deployment artifacts, e.g., in IDEs with limited FX
////     * support. NetBeans ignores main().
////     *
////     * @param args the command line arguments
////     */
////    public static void main(String[] args) {
////        launch(args);
////    }
//
//}
