package presentation.chart.lineChart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rmi.RemoteHelper;
import vo.EMAInfoVO;
import vo.YieldChartDataVO;

public class test1 extends Application{
    
    @Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
//		new test().testYieldComparedChart();
    	List<LocalDate> list1=new ArrayList<LocalDate>();
    	List<Double> list2=new ArrayList<Double>();
    	LocalDate ld=LocalDate.now();
    	for(int i=0;i<=16200;i++){
    		ld=ld.plusDays(1);
    		list1.add(ld);
    		list2.add(5.5);
    	}
    	long i1=System.currentTimeMillis();
    	SingleLineChart sc=new SingleLineChart(list1,list2,"hello");
//    	new test().testEMAChart();
		try{
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Birthday Statistics");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
//	        Scene scene = new Scene(yieldComparedChart.getchart(800, 300, true));
	        
	        Scene scene = new Scene(sc.getchart(334, 200, true));
	        long i2=System.currentTimeMillis();
	        System.out.println(i2-i1);
//	        Scene scene = new Scene(emaChart.getchart(500, 300, true));
	        dialogStage.setScene(scene);

	        dialogStage.show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
    	long i1=System.currentTimeMillis();
		launch(args);
		long i2=System.currentTimeMillis();
		System.out.println(i2-i1);
    }
}