package presentation.singleStockUI;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import exception.BeginInvalidException;
import exception.DateInvalidException;
import exception.DateOverException;
import exception.EndInvalidException;
import exception.NullDateException;
import exception.NullStockIDException;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.service.StockLogicInterface;
import logic.service.Stub;
import presentation.chart.chartService;
import presentation.chart.klineChart.KLineChart;
import presentation.chart.lineChart.EMAChart;
import rmi.RemoteHelper;
import vo.EMAInfoVO;
import vo.SingleStockInfoVO;

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
	
	private DataContorller dataController;
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
		dataController.upDate("SingleStockNow", name);
		initialAllPane();
	}
	private void initialAllPane(){
		DataContorller dataContorller = DataContorller.getInstance();
		String name = (String)dataContorller.get("SingleStockNow");
		System.out.println("Single"+name);
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		SingleStockInfoVO vo;

		try {
			String code = stockLogicInterface.nameToCode(name);
			vo = stockLogicInterface.getStockBasicInfo(code, (LocalDate)dataController.get("SystemTime"));
			setBasicInfoPane(vo);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
		    Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
			e.printStackTrace();
		} catch (NullStockIDException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("无股票").text(e.toString()).showError();
			e.printStackTrace();
		} catch (NullDateException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("无日期").text(e.toString()).showError();
			e.printStackTrace();
		}
		
	}
	
	public void setBasicInfoPane(SingleStockInfoVO vo){
		setStockInfoPane(vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu(), vo.getHigh(),
			    vo.getLow(), vo.getOpen(), vo.getVolume());
		setKlinePane(vo.getCode());
		setEMAChartPane(vo.getCode());
	}
	/*
	 * @description初始化股票基本信息界面
	 */
	private void setStockInfoPane(String code,String name,double close,double RAF,double high
			,double low,double open,long volume){
		codeLabel.setText(code);
		codeLabel.setStyle("-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold; -fx-font-size: 18;");
		nameLabel.setText(name);
		nameLabel.setStyle("-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold; -fx-font-size: 18;");
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
				label.setStyle("-fx-text-fill: rgb(255, 0, 0, 1);-fx-font-weight:bold");
			}else if(num<0){
				label.setStyle("-fx-text-fill: rgb(0, 255, 0, 1);-fx-font-weight:bold");
			}else{
				label.setStyle("-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold");
			}
		}else{
			if(num>0){
				label.setStyle("-fx-text-fill: rgb(255, 0, 0, 1);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}else if(num<0){
				label.setStyle("-fx-text-fill: rgb(0, 255, 0, 1);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}else{
				label.setStyle("-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}
		}

	}
	private void setKlinePane(String code){
		kChartAnchorPane.getChildren().clear();
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		LocalDate systime = (LocalDate)dataController.get("SystemTime");
		LocalDate beginTime = systime.minusDays(200);
		List<SingleStockInfoVO> vo;
		chartService chartservice;
		try {
			vo = stockLogicInterface.getSingleStockInfoByTime(code,
				beginTime,systime);
			chartservice = new KLineChart(vo);
			for(SingleStockInfoVO s :vo){
				System.out.println(s.getDate().toString());
			}
			kChartAnchorPane.getChildren().add(chartservice.getchart(758, 320));
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
			e.printStackTrace();
		} catch (DateInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
			e.printStackTrace();
		} catch (BeginInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
			e.printStackTrace();
		} catch (EndInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
			e.printStackTrace();
		} catch (NullStockIDException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("搜索异常").text(e.toString()).showError();
			e.printStackTrace();
		}
	}
	
	private void setEMAChartPane(String code){
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		LocalDate systime =(LocalDate)dataController.get("SystemTime");
		LocalDate begintime = systime.minusDays(200);
		List<EMAInfoVO> vo;
		chartService chartservice;
		try {
			emaChartAnchorPane.getChildren().clear();
			vo=stockLogicInterface.getEMAInfo(code, begintime, systime);
			chartservice = new EMAChart(vo);
			emaChartAnchorPane.getChildren().add(chartservice.getchart(758, 307));			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
			e.printStackTrace();
		} catch (DateInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
			e.printStackTrace();
		} catch (BeginInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
			e.printStackTrace();
		} catch (EndInvalidException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
			e.printStackTrace();
		} catch (NullStockIDException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("搜索异常").text(e.toString()).showError();
			e.printStackTrace();
		} catch (DateOverException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期错误").text(e.toString()).showError();
			e.printStackTrace();
		}	
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		Image searchImage = new Image(getClass().getResourceAsStream("search.png"));
		searchButton.setGraphic(new ImageView(searchImage));
		if(dataController.get("SingleStockNow")!=null){
			initialAllPane();
		}

	}

}
