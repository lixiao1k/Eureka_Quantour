package presentation.singleStockUI;

import dataController.DataContorller;
import en_um.ChartKind;
import exception.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.service.ForecastRODInterface;
import logic.service.StockLogicInterface;
import org.controlsfx.control.Notifications;
import presentation.chart.chartService;
import presentation.chart.klineChart.KLineChart;
import presentation.chart.lineChart.EMAChart;
import presentation.chart.lineChart.SingleLineChart;
import presentation.chart.lineChart.TimeShareChart;
import presentation.chart.scatterchart.YieldPointChart;
import presentation.saveAsPNG.SaveAsPNG;
import rmi.RemoteHelper;
import vo.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class SingleStockUIController implements Initializable{
	@FXML
	AnchorPane kChartAnchorPane;
	
	@FXML
	AnchorPane emaChartAnchorPane;
	
	@FXML
	AnchorPane basicInfoAnchorPane;
	
	@FXML
	Button searchButton;
	
	@FXML
	Button addButton;
	
	@FXML
	Label codeLabel;
	
	@FXML
	Label nameLabel;
	
	@FXML
	Label closeLabel;
	
	@FXML
	Label RAFLabel;
	
	@FXML
	Label highLabel;
	
	@FXML
	Label openLabel;
	
	@FXML
	Label lowLabel;
	
	@FXML
	Label volumeLabel;
	
	@FXML
	TextField searchTextField;
	
	@FXML
	AnchorPane RAFdistributionPane;//涨跌幅分布图
	
	@FXML
	AnchorPane logPane;//对数收益率图
	
	@FXML
	Label shouyilvLabel;

	@FXML
	Pane fuzzy;
	
	private DataContorller dataController;

	private VBox fuzzySearch=new VBox();
//  查看年月k线图
	@FXML
	protected void goBrowseDay(ActionEvent e){
		String name = (String)dataController.get("SingleStockNow");
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		try {
			String code = stockLogicInterface.nameToCode(name);
			setKlinePane(code,200);
			setEMAChartPane(code,200);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

	}
 //查看日k线图
	@FXML
	protected void goBrowseMinute(ActionEvent e) throws IOException {
		String name = (String)dataController.get("SingleStockNow");
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		ForecastRODInterface forecastRODInterface = remoteHelper.getForecastROD();
		String code = "";
		PredictVO predictVO =null;
		TimeSharingVO timeSharingVO = null;
		CompanyInfoVO companyInfoVO = null;
		emaChartAnchorPane.getChildren().clear();
		try {
			code = stockLogicInterface.nameToCode(name);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if(code!=""){
			try {
				predictVO = forecastRODInterface.predict(code,(LocalDate) dataController.get("SystemTime"));
				try {
                	timeSharingVO = stockLogicInterface.getTimeSharingData(code,(LocalDate) dataController.get("SystemTime"));
				} catch (TimeShraingLackException e1) {
					e1.printStackTrace();
				} catch (NullStockIDException e1) {
					Notifications.create().title("异常").text(e1.toString()).showWarning();
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			try {
				companyInfoVO = stockLogicInterface.getLatestCommpanyInfo((LocalDate)dataController.get("SystemTime"),code);
			} catch (NullStockIDException e1) {
				Notifications.create().title("异常").text(e1.toString()).showWarning();
			} catch (NullDateException e1) {
				Notifications.create().title("异常").text(e1.toString()).showWarning();
			}
		}
		if(predictVO!=null&&companyInfoVO!=null){
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("presentation/singleStockUI/Predict.fxml"));
			Pane pane = loader.load();
			PredictController controller = loader.getController();
			controller.set(predictVO,companyInfoVO);
			emaChartAnchorPane.getChildren().add(pane);
		}
		if(timeSharingVO!=null){
			chartService service = new TimeShareChart(timeSharingVO.getMinute_data(),timeSharingVO.getLast_close(),3);
			Pane pane = service.getchart(758,320,true);
			kChartAnchorPane.getChildren().clear();
			kChartAnchorPane.getChildren().add(pane);
		}



	}

	/*
	 * @description添加至股池，弹出相应界面
	 */
	@FXML
	protected void add2StockSet(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SingleStockUIPopup.fxml"));
		Parent popUp = (AnchorPane)loader.load();
		Scene scene = new Scene(popUp);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}
	/*
	 * @description搜索按钮的监听
	 */
	@FXML
	protected void search(ActionEvent e){
		String name = searchTextField.getText();
		if(name.length()==0){
			Notifications.create().title("异常").text("没有输入股票").showWarning();
		}
		dataController.upDate("SingleStockNow", name);
		initialAllPane();
	}
	private void initialAllPane(){
		DataContorller dataContorller = DataContorller.getInstance();
		String name = (String)dataContorller.get("SingleStockNow");
//		System.out.println("Single"+name);
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		SingleStockInfoVO vo;
		ComparedInfoVO vo1 = null;
		LocalDate end = (LocalDate)dataContorller.get("SystemTime");
		LocalDate begin = end.minusDays(200);

		try {
			vo1 = stockLogicInterface.getComparedInfo((String)dataContorller.get("SingleStockNow"),
					begin, end);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
		    Notifications.create().title("网络连接异常").text(e1.toString()).showWarning();
		} catch (DateInvalidException e1) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期异常").text(e1.toString()).showWarning();
		} catch (BeginInvalidException e1) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期异常").text(e1.toString()).showWarning();
		} catch (EndInvalidException e1) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期异常").text(e1.toString()).showWarning();
		} catch (NullStockIDException e) {

		}
		try {
			String code = stockLogicInterface.nameToCode(name);
			vo = stockLogicInterface.getStockBasicInfo(code, (LocalDate)dataController.get("SystemTime"));
			setBasicInfoPane(vo,vo1);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
		    Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
		} catch (NullStockIDException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("无股票").text(e.toString()).showError();
		} catch (NullDateException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("无日期").text(e.toString()).showError();
		}


	}


	public void setBasicInfoPane(SingleStockInfoVO vo,ComparedInfoVO vo1){
		setStockInfoPane(vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu(), vo.getHigh(),
			    vo.getLow(), vo.getOpen(), vo.getVolume());
		setKlinePane(vo.getCode(),200);
		setEMAChartPane(vo.getCode(),200);
		setDotPane(vo1.getDiantu());
		setLogPane(vo1);
	}

	/**
	 * 设置对数收益率图
	 * @param vo
	 */
	private void setLogPane(ComparedInfoVO vo){
		chartService chartservice = new SingleLineChart(vo.getDate(),vo.getLogYieldA(), "对数收益率", ChartKind.YIELDDISTRIBUTE);
		Pane pane = chartservice.getchart(271, 212, true);
		logPane.getChildren().clear();
		logPane.getChildren().add(pane);
		chartservice = new SingleLineChart(vo.getDate(),vo.getLogYieldA(), "对数收益率", ChartKind.YIELDDISTRIBUTE);
		Pane pane1 = chartservice.getchart(271, 212, true);
		dataController.upDate("LogPane",pane1);
	}
	/*
	 * 画收益率分布图
	 */
	private void setDotPane(List<Integer> list){
		chartService chartservice = new YieldPointChart(list, ChartKind.POINTONE);
		Pane pane = chartservice.getchart(271, 203, true);
		RAFdistributionPane.getChildren().clear();
		RAFdistributionPane.getChildren().add(pane);
		chartservice = new YieldPointChart(list, ChartKind.POINTONE);
		Pane pane1 = chartservice.getchart(271,203,true);
		dataController.upDate("DotPane",pane1);
	}
	/*
	 * @description初始化股票基本信息界面
	 */
	private void setStockInfoPane(String code,String name,double close,double RAF,double high
			,double low,double open,long volume){
		codeLabel.setText(code);
		codeLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);-fx-font-weight:bold; -fx-font-size: 18;");
		nameLabel.setText(name);
		nameLabel.setStyle("-fx-text-fill: rgb(255, 255, 255);-fx-font-weight:bold; -fx-font-size: 18;");
		closeLabel.setText(Double.toString(close));
		addLabelColor(closeLabel, close,28);
		if(RAF>0){
			RAFLabel.setText("+"+Double.toString(RAF)+"%");
		}else if(RAF<0){
			RAFLabel.setText(Double.toString(RAF)+"%");
		}else{
			RAFLabel.setText(Double.toString(RAF)+"%");
		}
	    addLabelColor(RAFLabel, RAF,0);
	    highLabel.setText(Double.toString(high));
	    addLabelColor(highLabel, high,0);
	    lowLabel.setText(Double.toString(low));
	    addLabelColor(lowLabel, -1,0);
	    openLabel.setText(Double.toString(open));
	    addLabelColor(openLabel, open,0);
	    volumeLabel.setText(Long.toString(volume));		
	}
	/*
	 * @description设置字体颜色，为setStockInfoPane方法所调用
	 */
	public void addLabelColor(Label label,double num,int size){
		if(size==0){
			if(num>0){
				label.setStyle("-fx-text-fill: rgb(255, 0, 0);-fx-font-weight:bold");
			}else if(num<0){
				label.setStyle("-fx-text-fill: rgb(0, 255, 0);-fx-font-weight:bold");
			}else{
				label.setStyle("-fx-text-fill: rgb(255, 255, 255);-fx-font-weight:bold");
			}
		}else{
			if(num>0){
				label.setStyle("-fx-text-fill: rgb(255, 0, 0);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}else if(num<0){
				label.setStyle("-fx-text-fill: rgb(0, 255, 0);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}else{
				label.setStyle("-fx-text-fill: rgb(255, 255, 255);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}
		}

	}
	private void setKlinePane(String code,int range){
		kChartAnchorPane.getChildren().clear();
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		LocalDate systime = (LocalDate)dataController.get("SystemTime");
		LocalDate beginTime = systime.minusDays(range);
		List<SingleStockInfoVO> vo;
		List<SingleStockInfoVO> vo1;
		chartService chartservice;
		try {
			vo = stockLogicInterface.getSingleStockInfoByTime(code,
				beginTime,systime);
			chartservice = new KLineChart(vo);
			kChartAnchorPane.getChildren().add(chartservice.getchart(758, 320,true));
			vo1 = stockLogicInterface.getSingleStockInfoByTime(code,
					beginTime,systime);
			chartservice = new KLineChart(vo1);
			Pane pane = chartservice.getchart(758,320,true);
			dataController.upDate("KLineChart",pane);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
		} catch (DateInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
		} catch (BeginInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
		} catch (EndInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
		} catch (NullStockIDException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("搜索异常").text(e.toString()).showError();
		}
	}
	//均线图
	private void setEMAChartPane(String code,int range){
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		LocalDate systime =(LocalDate)dataController.get("SystemTime");
		LocalDate begintime = systime.minusDays(range);
		List<EMAInfoVO> vo;
		List<EMAInfoVO> vo1;
		chartService chartservice;
		try {
			emaChartAnchorPane.getChildren().clear();
			vo=stockLogicInterface.getEMAInfo(code, begintime, systime);
			chartservice = new EMAChart(vo);
			emaChartAnchorPane.getChildren().add(chartservice.getchart(758, 295,true));
			vo1=stockLogicInterface.getEMAInfo(code, begintime, systime);
			chartservice = new EMAChart(vo1);
			Pane pane = chartservice.getchart(758,295,true);
			dataController.upDate("EMAChart",pane);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
		} catch (DateInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
		} catch (BeginInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
		} catch (EndInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
		} catch (NullStockIDException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("搜索异常").text(e.toString()).showError();
		} catch (DateOverException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
		}	
	}
//打印点图
	@FXML
	protected void printRAF(ActionEvent e){
		Pane pane = (Pane) dataController.get("DotPane");
		print(pane);
	}
//打印对数收益率图
	@FXML
	protected void printLog(ActionEvent e){
		Pane pane = (Pane) dataController.get("LogPane");
		print(pane);
	}
	private void print(Pane pane){
		SaveAsPNG saveAsPNG = new SaveAsPNG();
		saveAsPNG.print(pane);
	}
//查看100天k线图
	@FXML
	protected void go100(ActionEvent e){
		System.out.println("here");
		setKlinePane(getcode(),100);
		setEMAChartPane(getcode(),100);
	}

	@FXML
	protected void go125(ActionEvent e){
		setKlinePane(getcode(),125);
		setEMAChartPane(getcode(),125);
	}

	@FXML
	protected void go150(ActionEvent e){
		setKlinePane(getcode(),150);
		setEMAChartPane(getcode(),150);
	}

	@FXML
	protected void go175(ActionEvent e){
		setKlinePane(getcode(),175);
		setEMAChartPane(getcode(),175);
	}

	@FXML
	protected void go200(ActionEvent e){
		setKlinePane(getcode(),200);
		setEMAChartPane(getcode(),200);
	}
	//获取股票代码,将名字转化为代码
	private String getcode(){
		String name = (String)dataController.get("SingleStockNow");
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		String code = "";
		try {
			code = stockLogicInterface.nameToCode(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		Image searchImage = new Image(getClass().getResourceAsStream("search.png"));
		searchButton.setGraphic(new ImageView(searchImage));
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();

		fuzzySearch.setStyle("-fx-background-color: lightgrey;-fx-opacity: 0.4;");

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			try {
				fuzzySearch.getChildren().clear();
				List<String> list=stockLogicInterface.fuzzySearch(newValue);
				for (String name:list){
					Button bt=new Button(name);
					fuzzySearch.getChildren().add(bt);
					bt.setPrefWidth(140);
					bt.setStyle("-fx-background-color: transparent;");
					bt.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							searchTextField.setText(bt.getText().split("\t")[0]);
							fuzzySearch.getChildren().clear();
							search(new ActionEvent());
						}
					});
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		});
		//模糊搜索
		fuzzySearch.setVisible(true);
		searchTextField.focusedProperty().addListener(( observable,  oldValue,  newValue) -> {
			if (newValue){
				try {
					fuzzySearch.getChildren().clear();
					List<String> list=stockLogicInterface.fuzzySearch(searchTextField.getText());
					for (String name:list){
						Button bt=new Button(name);
						bt.setPrefWidth(140);
						bt.setStyle("-fx-background-color: transparent;");
						fuzzySearch.getChildren().add(bt);
						bt.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								searchTextField.setText(bt.getText().split("\t")[0]);
								fuzzySearch.getChildren().clear();
								search(new ActionEvent());
							}
						});
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}








			}else{
				fuzzySearch.getChildren().clear();
			}
		});
		fuzzy.getChildren().add(fuzzySearch);
		if(dataController.get("SingleStockNow")!=null){
			initialAllPane();
		}

	}

}
