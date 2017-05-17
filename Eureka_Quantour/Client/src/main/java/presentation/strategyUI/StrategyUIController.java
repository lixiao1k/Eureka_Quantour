package presentation.strategyUI;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.service.StockLogicInterface;
import presentation.chart.areaChart.DanTengChart;
import presentation.chart.chartService;
import presentation.chart.barChart.YieldDistributeChart;
import presentation.chart.lineChart.YieldComparedChart;
import presentation.marketUI.MarketConceptController;
import rmi.RemoteHelper;
import vo.SaleVO;
import vo.StrategyConditionVO;
import vo.YieldChartDataVO;
import vo.YieldDistributionHistogramDataVO;

public class StrategyUIController implements Initializable{
	
	@FXML
	ComboBox<String> stockSetComboBox;
	
	@FXML
	DatePicker beginTimeDatePicker;
	
	@FXML
	RadioButton momentumRadioButton;
	
	@FXML
	RadioButton meanRadioButton;
	
	@FXML
	TextField holdPeriodTextField;
	
	@FXML
	RadioButton closeRadioButton;
	
	@FXML
	RadioButton openRadioButton;
	
	@FXML
	TextField numOfStockTextField;
	
	@FXML
	Button makeStrategyButton;
	
	@FXML
	Button saveButton;

	@FXML
	AnchorPane chart1AnchorPane;
	
	@FXML
	AnchorPane chart2AnchorPane;
	
	@FXML
	AnchorPane chart3AnchorPane;
	
	@FXML
	ToggleGroup strategy;
	
	@FXML
	ToggleGroup price;
	
	@FXML
	Label timeLabel;
	
	@FXML
	AnchorPane anchorPane4;
	
	@FXML
	AnchorPane anchorPane5;
	
	@FXML
	Label changableLabel;
	
	@FXML
	TextField changableTextField;
	
	private DataContorller dataController;
	
	private ObservableList<String> stocksetlist = FXCollections.observableArrayList();
	
	/*
	 * 预测策略的监听
	 */
//	@FXML
//	protected void goBrowsePredict(ActionEvent e){
//		String marketName = "";
//		String chiyouqi = "";
//		String xinchengqi = "";
//		if(stockSetComboBox.getValue()!=null){
//			marketName = stockSetComboBox.getValue();
//		}else{
//			Notifications.create().title("异常").text("请选择市场").showWarning();
//		}
//		chiyouqi = holdPeriodTextField.getText();
//		xinchengqi = createPeriodTextField.getText();
//		RemoteHelper remote = RemoteHelper.getInstance();
//		StockLogicInterface stockLogicInterface = remote.getStockLogic();
//		List<List<Double>> datelist1=new ArrayList<>();
//		List<List<Double>> datelist2=new ArrayList<>();
//		try {
//			if(momentumRadioButton.isSelected()) {
//
//			}else{
//
//			}
////			主pane上把pane加上
//		} catch (RemoteException e1) {
//			e1.printStackTrace();
//		}
//
//	}
//	
	@FXML
	protected void makeStrategy(ActionEvent e){
		anchorPane4.getChildren().clear();
		anchorPane5.getChildren().clear();
		StrategyConditionVO strategyConditionVO;
		SaleVO saleVO;
		String stockSet = "";
		String price = "收盘价";
		int createdays = 0;//形成期 
		int holddays = 0;
		int nums = 0;//股票数
		int meandays = 0;
		boolean flag = true;//判断是否能够继续调用策略
		if(momentumRadioButton.isSelected()){
			if(stockSetComboBox.getValue()!=null){
				stockSet = stockSetComboBox.getValue();
			}else{
				flag = false;
				Notifications.create().title("输入异常").text("请正确选择股票池").showWarning();
			}
			
			if(holdPeriodTextField.getText().length()!=0){
				int num = 0;
				try{
				    num = Integer.parseInt(holdPeriodTextField.getText());
				    if(num<=0){
				    	Notifications.create().title("输入异常").text("持有期请输入正数").showWarning();
				    	flag = false;
				    }
				}catch(NumberFormatException e1){
					flag = false;
					Notifications.create().title("输入异常").text("持有期请输入整数").showWarning();
				}
				holddays = num;
			}else{
				flag = false;
				Notifications.create().title("输入异常").text("请输入持有期").showWarning();
			}
			
			if(changableTextField.getText().length()!=0){
				int num =0;
			    num = Integer.parseInt(changableTextField.getText());
				try{
				    num = Integer.parseInt(changableTextField.getText());
				    if(num<=0){
				    	Notifications.create().title("输入异常").text("形成期请输入正数").showWarning();
				    	flag = false;
				    }
				}catch(NumberFormatException e1){
					flag = false;
					Notifications.create().title("输入异常").text("形成期请输入整数").showWarning();
				}
				createdays = num;
			}else{
				flag = false;
				Notifications.create().title("输入异常").text("请输入形成期").showWarning();
			}
			
			if(closeRadioButton.isSelected()){
				price = "收盘价";
			}else{
				price = "开盘价";
			}
			
			if(numOfStockTextField.getText().length()!=0){
				int num =0;
				try{
				    num = Integer.parseInt(numOfStockTextField.getText());
				    if(num<=0){
				    	Notifications.create().title("输入异常").text("股票数请输入正数").showWarning();
				    	flag = false;
				    }
				}catch(NumberFormatException e1){
					flag = false;
					Notifications.create().title("输入异常").text("股票数请输入整数").showWarning();
				}
				nums = num;
			}else{
				flag = false;
				Notifications.create().title("输入异常").text("请输入股票数").showWarning();
			}
			
			if(flag){

				List<Integer> list = new ArrayList<>();
				list.add(createdays);

				strategyConditionVO = new StrategyConditionVO("动量策略",list,nums);
				saleVO = new SaleVO(holddays,price);
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					stockLogicInterface.setStrategy(strategyConditionVO, saleVO,(LocalDate)dataController.get("BeginTime"),
							(LocalDate)dataController.get("SystemTime"), stockSet,(String)dataController.get("UserName"));

					YieldDistributionHistogramDataVO yieldDistributionHistogramDataVO = stockLogicInterface.getYieldDistributionHistogramData();
					YieldChartDataVO yieldChartDataVO = stockLogicInterface.getYieldChartData();

					chartService chartservice = new YieldComparedChart(yieldChartDataVO);
					Pane pane = chartservice.getchart(900,250,true);
					chartservice = new YieldDistributeChart(yieldDistributionHistogramDataVO);
					Pane pane1 = chartservice.getchart(900, 250, true);
					chart2AnchorPane.getChildren().clear();
					chart2AnchorPane.getChildren().add(pane);
					chart1AnchorPane.getChildren().clear();
					chart1AnchorPane.getChildren().add(pane1);
	 			} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e1.toString()).showWarning();
					e1.printStackTrace();
				}	
			}
		}else{
			if(stockSetComboBox.getValue()!=null){
				stockSet = stockSetComboBox.getValue();
			}else{
				flag = false;
				Notifications.create().title("输入异常").text("请正确选择股票池").showWarning();
			}
			if(holdPeriodTextField.getText().length()!=0){
				int num = 0;
				try{
				    num = Integer.parseInt(holdPeriodTextField.getText());
				    if(num<=0){
				    	Notifications.create().title("输入异常").text("持有期请输入正数").showWarning();
				    	flag = false;
				    }
				}catch(NumberFormatException e1){
					flag = false;
					Notifications.create().title("输入异常").text("持有期请输入整数").showWarning();
				}
				holddays = num;
			}else{
				flag = false;
				Notifications.create().title("输入异常").text("请输入持有期").showWarning();
			}
			if(closeRadioButton.isSelected()){
				price = "收盘价";
			}else{
				price = "开盘价";
			}
			
			if(numOfStockTextField.getText().length()!=0){
				int num =0;
				try{
				    num = Integer.parseInt(numOfStockTextField.getText());
				    if(num<=0){
				    	Notifications.create().title("输入异常").text("股票数请输入正数").showWarning();
				    	flag = false;
				    }
				}catch(NumberFormatException e1){
					flag = false;
					Notifications.create().title("输入异常").text("股票数请输入整数").showWarning();
				}
				nums = num;
			}else{
				flag = false;
				Notifications.create().title("输入异常").text("请输入股票数").showWarning();
			}
			
			if(changableTextField.getText().length()!=0){
				int num =0;
				try{
				    num = Integer.parseInt(changableTextField.getText());
				    if(num<=0){
				    	Notifications.create().title("输入异常").text("几日均值请输入正数").showWarning();
				    	flag = false;
				    }
				}catch(NumberFormatException e1){
					flag = false;
					Notifications.create().title("输入异常").text("几日均值请输入整数").showWarning();
				}
				meandays = num;
			}else{
				flag = false;
				Notifications.create().title("输入异常").text("请输入几日均值数").showWarning();
			}
			if(flag){

				List<Integer> meandaylist =new ArrayList<>();

				meandaylist.add(meandays);
				StrategyConditionVO strategyConditionVO2 = new StrategyConditionVO("均值策略",meandaylist,nums);
				SaleVO saleVO2 = new SaleVO(holddays,price);
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					stockLogicInterface.setStrategy(strategyConditionVO2, saleVO2,(LocalDate)dataController.get("BeginTime"),
							(LocalDate)dataController.get("SystemTime"), stockSet, (String)dataController.get("UserName"));

					YieldDistributionHistogramDataVO yieldDistributionHistogramDataVO1 = stockLogicInterface.getYieldDistributionHistogramData();
					YieldChartDataVO yieldChartDataVO1 = stockLogicInterface.getYieldChartData();

					chartService chartservice = new YieldComparedChart(yieldChartDataVO1);
					Pane pane = chartservice.getchart(900,250,true);
					chartservice = new YieldDistributeChart(yieldDistributionHistogramDataVO1);
					Pane pane1 = chartservice.getchart(900, 250, true);
					chart2AnchorPane.getChildren().clear();
					chart2AnchorPane.getChildren().add(pane);
					chart1AnchorPane.getChildren().clear();
					chart1AnchorPane.getChildren().add(pane1);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e1.toString()).show();
					e1.printStackTrace();
				}
			}

			
		}

	}

	@FXML
	protected void saveStrategy(ActionEvent e){	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("StrategyPopUp.fxml"));
		Parent popup = null;
		try {
			popup = (AnchorPane)loader.load();
			StrategyPopUpController controller = loader.getController();
			controller.setController(this);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Scene scene = new Scene(popup);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}
	
	public StrategyConditionVO getStrategyConditionVO(){
		List<Integer> list = new ArrayList<>();
		int num=0,stocknums;
		try{
			num=Integer.parseInt(changableTextField.getText());
			list.add(num);
		}catch(NumberFormatException e){
			Notifications.create().title("错误").text("请输入正整数").showWarning();
		}
		if(num<=0){
			Notifications.create().title("错误").text("请输入正整数").showWarning();
		}


		String name = strategy.getSelectedToggle().getUserData().toString();
		stocknums=Integer.parseInt(numOfStockTextField.getText());
		StrategyConditionVO strategyConditionVO = new StrategyConditionVO(name,list,stocknums);
		return strategyConditionVO;
	}
	
	public SaleVO getSaleVO(){
		List<Integer> list = new ArrayList<>();
		int holddays;
		holddays = Integer.parseInt(holdPeriodTextField.getText());
		String priceStr = price.getSelectedToggle().getUserData().toString();
		SaleVO saleVO = new SaleVO(holddays,priceStr);
		return saleVO;
	}
	
	
	@FXML
	protected void saveTime(ActionEvent e){
		LocalDate begin = beginTimeDatePicker.getValue();
		if(begin==null){
			Notifications.create().title("输入异常").text("请输入开始时间").showWarning();
		}
		else if(begin.isBefore((LocalDate)dataController.get("SystemTime"))){
			timeLabel.setText(begin.toString());
			dataController.upDate("BeginTime", begin);
		}else{
			Notifications.create().title("时间异常").text("开始时间需在系统时间之前").showWarning();
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		changableLabel.setText("形成期");
		momentumRadioButton.setUserData("动量");
		meanRadioButton.setUserData("均值");
		closeRadioButton.setUserData("收盘价");
		openRadioButton.setUserData("开盘价");
		strategy.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(strategy.getSelectedToggle()!=null){
				  if(strategy.getSelectedToggle().getUserData().equals("均值")){
					  changableLabel.setText("几日均值");
				  }else if(strategy.getSelectedToggle().getUserData().equals("动量")){
					  changableLabel.setText("持有期");
				  }
				}
			}
		});
		dataController = DataContorller.getInstance();
		Image saveImage = new Image(getClass().getResourceAsStream("save.png"));
		saveButton.setGraphic(new ImageView(saveImage));
		beginTimeDatePicker.setShowWeekNumbers(false);
	    Locale.setDefault(Locale.ENGLISH);
	    LocalDate systemTime = (LocalDate) dataController.get("SystemTime");
	    LocalDate beginTime = systemTime.minusDays(200);
	    timeLabel.setText(beginTime.toString());
	    stockSetComboBox.getItems().clear();
		stocksetlist.addAll("SHA","SHB","SZA","SZB","CYB","ZXB","HS300");
	    dataController.upDate("BeginTime", beginTime);
	    RemoteHelper remote = RemoteHelper.getInstance();
	    StockLogicInterface stockLogicInterface = remote.getStockLogic();
	    try {
			List<String> stockset = stockLogicInterface.getStockSet((String)dataController.get("UserName"));
			if(stockset!=null){
				stocksetlist.addAll(stockset);	
			}
		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    stockSetComboBox.getItems().addAll(stocksetlist);
	    
	}

}
