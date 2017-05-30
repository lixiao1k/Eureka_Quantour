package presentation.chart.lineChart;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import en_um.ChartKind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rmi.RemoteHelper;
import vo.EMAInfoVO;
import vo.YieldChartDataVO;

public class test1 extends Application{
	private static RemoteHelper remote;
    @Override
	public void start(Stage primaryStage){
		// TODO Auto-generated method stub
//		new test().testYieldComparedChart();
    	if(remote==null){
    		System.out.println("null remote");
    		System.exit(0);
    	}
    	else{
    		try{
    		remote.getStockLogic().getTimeSharingData("000001", LocalDate.of(2017, 4, 19));
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    	List<LocalDate> list1=new ArrayList<LocalDate>();
    	List<Double> list3=null;
    	try{
    		list3=remote.getStockLogic().getTimeSharingData("000001", LocalDate.of(2017, 4, 19));
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	
    	if(list3==null){
    		System.out.println("null");
    		System.exit(0);
    	}
    	else{
    		System.out.println(list3.size());
    	}
    	LocalDate ld=LocalDate.now();
    	List<Double> list2=new ArrayList<Double>();
    	for(int i=0;i<1000;i++){
    		ld=ld.plusDays(1);
    		list1.add(ld);
    		list2.add(list3.get(i));
    	}
    	long i1=System.currentTimeMillis();
    	SingleLineChart sc=new SingleLineChart(list1,list2,"hello",ChartKind.YIELDDISTRIBUTE);
//    	new test().testEMAChart();
		try{
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Birthday Statistics");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
//	        Scene scene = new Scene(yieldComparedChart.getchart(800, 300, true));
	        
//	        Scene scene = new Scene(sc.getchart(334, 200, true));
	        Scene scene = new Scene(sc.getchart(800, 400, true));
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
		try {
			remote = RemoteHelper.getInstance();
			remote.setRemote(Naming.lookup("rmi://localhost:8888/DateRemote"));
//			rmic.setRemote(Naming.lookup("rmi://114.212.43.109:8888/DateRemote"));
			System.out.println("连接服务器成功！");
		} catch (MalformedURLException e) {
			e.toString();
		} catch (RemoteException e) {
			e.toString();
		} catch (NotBoundException e) {
			e.toString();
		}
    	long i1=System.currentTimeMillis();
		launch(args);
		long i2=System.currentTimeMillis();
		System.out.println(i2-i1);
    }
}