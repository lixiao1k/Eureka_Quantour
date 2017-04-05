package presentation.chart.lineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @Description: Test ComparedChart. when test is finished, I will delete it.
 * @author: hzp
 * @time: 2017年4月1日
 */
public class test extends Application{
	
	private List<Calendar> datelist = new ArrayList<Calendar>();
    private List<Double> jizhunlist = new ArrayList<Double>();;
    private List<Double> celuelist = new ArrayList<Double>();;
    private static ComparedChart comparedChart;
    private static SingleLineChart singleLineChart;
    
    private void testComparedChart(){
    	Calendar cal = Calendar.getInstance();
    	List<String> dataName = new ArrayList<String>();
    	int j = 2;
    	for(int i=0; i<20; i++,j++){
    		cal.set(2014, 3, j);
    		datelist.add( (Calendar)cal.clone() );
    		jizhunlist.add( Math.random() );
    		celuelist.add( Math.random() ); 
    	}
    	Calendar[] dates = new Calendar[datelist.size()];
    	Double[] d1 = new Double[jizhunlist.size()];
    	Double[] d2 = new Double[celuelist.size()];
    	for(int i=0; i<datelist.size(); i++){
    		dates[i] = datelist.get(i);
    		d1[i] = jizhunlist.get(i);
    		d2[i] = celuelist.get(i);
    	}
    	List<Double[]> doubleList = new ArrayList<Double[]>();
    	doubleList.add(d1);
    	doubleList.add(d2);
    	dataName.add("hello");
    	dataName.add("fuck");
		comparedChart = new ComparedChart(dates, doubleList, dataName);
    }
    
    private void testSingleLineChart(){
    	Calendar cal = Calendar.getInstance();
    	int j = 2;
    	for(int i=0; i<20; i++,j++){
    		cal.set(2014, 3, j);
    		datelist.add( (Calendar)cal.clone() );
    		jizhunlist.add( Math.random() ); 
    	}
    	Calendar[] dates = new Calendar[datelist.size()];
    	Double[] d1 = new Double[jizhunlist.size()];
    	for(int i=0; i<datelist.size(); i++){
    		dates[i] = datelist.get(i);
    		d1[i] = jizhunlist.get(i);
    	}
		singleLineChart = new SingleLineChart(dates, d1, "hello");
    }
    
    @Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		new test().testComparedChart();
//		new test().testSingleLineChart();
		try{
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Birthday Statistics");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
	        Scene scene = new Scene(comparedChart.getchart(334, 200));
//	        Scene scene = new Scene(singleLineChart.getchart(334, 200));
	        dialogStage.setScene(scene);

	        dialogStage.show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
    	launch(args);
    }
}
