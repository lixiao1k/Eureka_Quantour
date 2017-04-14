package presentation.chart.lineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vo.EMAInfoVO;
import vo.YieldChartDataVO;

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
    private static EMAChart emaChart;
    
    private void testComparedChart(){
    	Calendar cal = Calendar.getInstance();
    	List<String> dataName = new ArrayList<String>();
    	int j = 2;
    	for(int i=0; i<40; i++,j++){
    		cal.set(2014, 3, j);
    		datelist.add( (Calendar)cal.clone() );
    		jizhunlist.add( Math.random() );
    		celuelist.add( Math.random() ); 
    	}
    	Double[] d1 = new Double[jizhunlist.size()];
    	Double[] d2 = new Double[celuelist.size()];
    	for(int i=0; i<datelist.size(); i++){
    		d1[i] = jizhunlist.get(i);
    		d2[i] = celuelist.get(i);
    	}
    	
    	YieldChartDataVO ycd = new YieldChartDataVO(datelist,jizhunlist, celuelist);
		comparedChart = new ComparedChart().setData(ycd);
//		comparedChart.setName("ComparedChart");
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
		singleLineChart.setName("SingleLineChart");
    }
    
    private void testEMAChart(){
    	List<EMAInfoVO> emaList = new ArrayList<>();
    	
    	Calendar cal = Calendar.getInstance();	
    	int j = 2;
    	for(int i=0; i<20; i++,j++){
    		cal.set(2014, 3, j);
    		datelist.add( (Calendar)cal.clone() );
    		jizhunlist.add( Math.random() ); 
    	}
    	EMAInfoVO emaVO1 = new EMAInfoVO(datelist, jizhunlist, 20);
    	emaList.add(emaVO1);
    	jizhunlist = new ArrayList<>();
    	for( int k=0; k<20; k++){
    		jizhunlist.add( Math.random() );
    	}
    	EMAInfoVO emaVO2 = new EMAInfoVO(datelist, jizhunlist, 20);
    	emaList.add(emaVO2);
    	jizhunlist = new ArrayList<>();
    	for( int k=0; k<20; k++){
    		jizhunlist.add( Math.random() );
    	}
    	EMAInfoVO emaVO3 = new EMAInfoVO(datelist, jizhunlist, 20);
    	emaList.add(emaVO3);
    	jizhunlist = new ArrayList<>();
    	for( int k=0; k<20; k++){
    		jizhunlist.add( Math.random() );
    	}
    	EMAInfoVO emaVO4 = new EMAInfoVO(datelist, jizhunlist, 20);
    	emaList.add(emaVO4);
    	jizhunlist = new ArrayList<>();
    	for( int k=0; k<20; k++){
    		jizhunlist.add( Math.random() );
    	}
    	EMAInfoVO emaVO5 = new EMAInfoVO(datelist, jizhunlist, 20);
    	emaList.add(emaVO5);
    	
    	emaChart = new EMAChart(emaList);
//    	emaChart.setName("EMAChart");
    }
    
    @Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
//		new test().testComparedChart();
//		new test().testSingleLineChart();
    	new test().testEMAChart();
		try{
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Birthday Statistics");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
//	        Scene scene = new Scene(comparedChart.getchart(334, 200));
//	        Scene scene = new Scene(singleLineChart.getchart(334, 200));
	        Scene scene = new Scene(emaChart.getchart(334, 200));
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
