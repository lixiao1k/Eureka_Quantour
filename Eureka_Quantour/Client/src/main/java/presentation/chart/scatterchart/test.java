package presentation.chart.scatterchart;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import en_um.ChartKind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presentation.chart.function.SaveAs;
import presentation.chart.function.SaveAsService;
import rmi.RemoteHelper;
import vo.StockRODVO;



public class test extends Application{
	
	private static PointChart pointChart;
	
	private RemoteHelper remote = RemoteHelper.getInstance();
	
	private SaveAsService sas = new SaveAs();
    
	private List<String> dataName = new ArrayList<>();
	
	public void testYieldPointChart3(){
		int[] numI1 = {5, 1, 3, 2, 0, 5, 7, 7, 4, 6, 4, 9, 12, 5, 8, 5, 4, 1, 2, 1, 4};
		int[] numI2 = {0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0};
		int[] numI3 = {12, 1, 1, 2, 1, 6, 6, 8, 7, 4, 0, 7, 12, 5, 8, 5, 2, 2, 3, 1, 1};
		int[] numI4 = {1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1};
		int[] numI5 = {14, 4, 1, 1, 3, 4, 6, 7, 8, 7, 1, 8, 4, 6, 4, 2, 4, 0, 1, 1, 0};
		List<int[]> numI = new ArrayList<>();
//		numI.add(numI1);
//		numI.add(numI2);
//		numI.add(numI3);
//		numI.add(numI4);
		numI.add(numI5);
		
		dataName.add("Mon");
		dataName.add("Tue");
		dataName.add("Wen");
		dataName.add("Th");
		dataName.add("Fri");
	
		int[] yield = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		pointChart = new PointChart( yield, numI, dataName, ChartKind.POINTONE, true);		
	}
	
	/**
	 * @Description: 获取股票收益率分布
	 * @author: hzp
	 * @date: May 13, 2017
	 */
	public void getROD(){
		StockRODVO srod = new StockRODVO();
		LocalDate begindate = LocalDate.of(2014, 3, 6);
		LocalDate enddate = LocalDate.of(2016, 3, 6);
		
		String stockcode = "600149";
		try{
			srod = remote.getForecastROD().getStockROD(stockcode, begindate, enddate);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		
		for( int i=0; i<5; i++){
			List<int[]> num = new ArrayList<>();
			num.add( srod.wROD[i] );
			List<String> name = new ArrayList<>();
			String namet = "星期"+i;
			name.add(namet);
			System.out.print("{");
			for( int j=0; j<21; j++){
				System.out.print( srod.wROD[i][j] +", ");
			}
			System.out.println("}");
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		new test().testYieldPointChart3();

		Pane pane = new Pane();
		Scene scene;
		Stage dialogStage = new Stage();
		try{
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			  
			pane = pointChart.getchart(334, 200, true);
//			pane = pointChart.getchart(334, 200, false);
			
			scene = new Scene( pane );
			sas.saveAsPng(scene, "星期五");
			
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
