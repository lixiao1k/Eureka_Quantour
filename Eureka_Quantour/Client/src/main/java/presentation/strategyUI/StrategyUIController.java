package presentation.strategyUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import logic.service.StockLogicInterface;
import presentation.chart.chartService;
import presentation.chart.barChart.YieldDistributeChart;
import presentation.chart.lineChart.YieldComparedChart;
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
	TextField meandaysTextField;//几日均值
	
	@FXML
	TextField createPeriodTextField;//形成期
	
	private DataContorller dataController;
	
	private ObservableList<String> stocksetlist = FXCollections.observableArrayList();
	
	@FXML
	protected void makeStrategy(ActionEvent e){
		StrategyConditionVO strategyConditionVO;
		SaleVO saleVO;
		String stockSet = "";
		String price = "收盘价";
		int createdays = 0;//形成期 
		int holddays = 0;
		int nums = 0;//股票数
		boolean flag = true;//判断是否能够继续调用策略
		if(momentumRadioButton.isSelected()){
			meandaysTextField.setEditable(false);
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
			
			if(createPeriodTextField.getText().length()!=0){
				int num =0;
				try{
				    num = Integer.parseInt(createPeriodTextField.getText());
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
				List<Object> list = new ArrayList<Object>();
				list.add(createdays);
				strategyConditionVO = new StrategyConditionVO("动量策略",list,nums);
				saleVO = new SaleVO(holddays,price);
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					stockLogicInterface.setStrategy(strategyConditionVO, saleVO,(LocalDate)dataController.get("BeginTime"),
							(LocalDate)dataController.get("SystemTime"), stockSet,(String)dataController.get("UserName"));
					System.out.println(strategyConditionVO.getName());
					System.out.println(strategyConditionVO.getNums());
					System.out.println(strategyConditionVO.getExtra().get(0));
					System.out.println(saleVO.getTiaocangjiage());
					System.out.println(saleVO.getTiaocangqi());
					System.out.println((LocalDate)dataController.get("BeginTime"));
					System.out.println((LocalDate)dataController.get("SystemTime"));
					System.out.println((String)dataController.get("UserName"));
					YieldDistributionHistogramDataVO yieldDistributionHistogramDataVO = stockLogicInterface.getYieldDistributionHistogramData();
					YieldChartDataVO yieldChartDataVO = stockLogicInterface.getYieldChartData();
					System.out.println(yieldDistributionHistogramDataVO);
					System.out.println(yieldChartDataVO);
					chartService chartservice = new YieldComparedChart(yieldChartDataVO);
					Pane pane = chartservice.getchart(900,300,true);
					chart2AnchorPane.getChildren().clear();
					chart2AnchorPane.getChildren().add(pane);
	 			} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e1.toString()).showWarning();
					e1.printStackTrace();
				}	
			}
		}else{
			
		}

	}

	
	
	@FXML
	protected void saveTime(ActionEvent e){
		LocalDate begin = beginTimeDatePicker.getValue();
		if(begin==null){
			System.out.println("NoTime");
		}
		else if(begin.isBefore((LocalDate)dataController.get("SystemTime"))){
			timeLabel.setText(begin.toString());
			dataController.upDate("BeginTime", begin);
		}else{
			System.out.println("No");
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
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
			stocksetlist.addAll(stockset);			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    stockSetComboBox.getItems().addAll(stocksetlist);
	    
	}

}
