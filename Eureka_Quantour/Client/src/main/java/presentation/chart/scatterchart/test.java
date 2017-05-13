package presentation.chart.scatterchart;

import java.io.FileOutputStream;
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
	private static YieldPointChart yieldPointChart;
	
	private RemoteHelper remote = RemoteHelper.getInstance();
	
	private SaveAsService sas = new SaveAs();
    
	private List<int[]> yield = new ArrayList<>();
	private List<int[]> num = new ArrayList<>();
	private List<String> dataName = new ArrayList<>();
	private List<Integer> nums = new ArrayList<>();
	
	private AnchorPane paneT = new AnchorPane();
	
	private void testPointChart(){
		int[] yieldD = new int[21];
		int[] numI = new int[21];
		dataName.add("test");
		
		int j = -10;
		for( int i=0; i<=20; i++, j++)
			yieldD[i] = j;
		numI[0] = 5;
		numI[1] = 6;
		numI[2] = 4;
		numI[3] = 8;
		numI[4] = 8;
		numI[5] = 4;
		numI[6] = 12;
		numI[7] = 13;
		numI[8] = 20;
		numI[9] = 35;

		numI[10] = 40;
		numI[11] = 36;
		numI[12] = 22;
		numI[13] = 15;
		numI[14] = 14;
		numI[15] = 8;
		numI[16] = 15;
		numI[17] = 8;
		numI[18] = 6;
		numI[19] = 3;
		numI[20] = 2;
	
		yield.add(yieldD);
		num.add(numI);
		
		pointChart = new PointChart( yield, num, dataName, ChartKind.POINTFULL);
	}
	
	public void testYieldPointChart1(){
		nums.add(5);
		nums.add(6);
		nums.add(4);
		nums.add(8);
		nums.add(8);
		nums.add(4);
		nums.add(12);
		nums.add(13);
		nums.add(20);
		nums.add(35);
		
		nums.add(40);
		nums.add(36);
		nums.add(22);
		nums.add(15);
		nums.add(14);
		nums.add(8);
		nums.add(8);
		nums.add(6);
		nums.add(3);
		nums.add(2);
		yieldPointChart = new YieldPointChart(nums, ChartKind.POINTONE);
	}
	
	public void testYieldPointChart2(){
		nums.add(5);
		nums.add(6);
		nums.add(4);
		nums.add(8);
		nums.add(8);
		nums.add(4);
		nums.add(12);
		nums.add(13);
		nums.add(20);
		nums.add(35);
		
		nums.add(40);
		nums.add(36);
		nums.add(22);
		nums.add(15);
		nums.add(14);
		nums.add(8);
		nums.add(8);
		nums.add(6);
		nums.add(3);
		nums.add(2);
		
		yieldPointChart = new YieldPointChart(nums, ChartKind.POINTFULL);
	}
	
	public void testYieldPointChart3(List<int[]> numI){
		List<int[]> numT = new ArrayList<>();
		int[] yieldD = new int[21];
		dataName.add("test");
		
		int j = -10;
		for( int i=0; i<=20; i++, j++)
			yieldD[i] = j;
	
		yield.add(yieldD);
		numT.add(numI.get(0));
		pointChart = new PointChart( yield, numT, dataName, ChartKind.POINTFULL);
		
	}
	
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
		int[] yield = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		List<int[]> yields = new ArrayList<>();
		yields.add(yield);
		Scene scene;
//		PointChart pointChart2;
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
		int[] numI1 = {5, 1, 3, 2, 0, 5, 7, 7, 4, 6, 4, 9, 12, 5, 8, 5, 4, 1, 2, 1, 4};
		int[] numI2 = {0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0};
		int[] numI3 = {12, 1, 1, 2, 1, 6, 6, 8, 7, 4, 0, 7, 12, 5, 8, 5, 2, 2, 3, 1, 1};
		int[] numI4 = {1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1};
		int[] numI5 = {14, 4, 1, 1, 3, 4, 6, 7, 8, 7, 1, 8, 4, 6, 4, 2, 4, 0, 1, 1, 0};
		List<int[]> numI = new ArrayList<>();
		numI.add(numI1);
		numI.add(numI2);
		numI.add(numI3);
		numI.add(numI4);
		numI.add(numI5);
//		new test().testPointChart();
//		new test().testYieldPointChart1();
//		new test().testYieldPointChart2();
		new test().testYieldPointChart3(numI);;
		try{
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene ;  
			Pane pane = new Pane();
			pane = pointChart.getchart(334, 200, true);
//			pane = pointChart.getchart(334, 200, false);
//			pane = yieldPointChart.getchart(334, 200, false);
//			pane = yieldPointChart.getchart(334, 200, true);
			
			scene = new Scene( pane );
			sas.saveAsPng(scene, "星期二");
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
