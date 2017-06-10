package presentation.statisticsUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.org.apache.regexp.internal.RE;
import exception.*;
import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import en_um.ChartKind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import logic.service.StockLogicInterface;
import presentation.chart.chartService;
import presentation.chart.klineChart.KLineChart;
import presentation.chart.piechart.YieldFanChart;
import presentation.chart.scatterchart.YieldPointChart;
import rmi.RemoteHelper;
import sun.jvm.hotspot.oops.Klass;
import vo.EMAInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;

public class StatisticsUIController implements Initializable {
	@FXML
	Label nameLabel;
	
	@FXML
	ComboBox<String> marketComboBox;
	
	@FXML
	AnchorPane RAFPieChartPane;
	
	@FXML
	AnchorPane RAFContributionPane;
	
	@FXML
	Label zhangtingLabel;
	
	@FXML
	Label dietingLabel;
	
	@FXML
	Label tingpaiLabel;
	
	@FXML
	Label volumeLabel;
	
	@FXML
	Label zhangtingvalue;
	
	@FXML
	Label dietingValue;
	
	@FXML
	Label tingpaiValue;
	
	@FXML
	Label volumeValue;
	
	@FXML
	Label meanValue;
	
	@FXML
	Label varianValue;
	
	@FXML
	Label RAFfengbuLabel;
	
	@FXML
	Label meanLabel;
	
	@FXML
	Label varianLabel;

	@FXML
	AnchorPane emaAnchorPane;

	@FXML
	AnchorPane KLineAnchorPane;
	
	
	private DataContorller dataController;
	/*
	 * 监听入口
	 */
	@FXML
	protected void setMarket(ActionEvent e){
		nameLabel.setText(marketComboBox.getValue());
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		try {
			MarketInfoVO marketInfoVO = stockLogicInterface.getMarketInfo((LocalDate)dataController.get("SystemTime"),
					marketComboBox.getValue());
			initialAllPane(marketInfoVO);
			if(marketComboBox.getValue().equals("HS300")){
				Notifications.create().title("异常").text("暂时无沪深300大盘指数").showWarning();
			}else{
				initKLinePane(marketComboBox.getValue());
			}


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
		}catch (NullMarketException e1) {
			// TODO: handle exception
			Notifications.create().title("数据异常").text(e1.toString()).showWarning();
		}
	}
	
	private void initialAllPane(MarketInfoVO marketInfoVO){
	    NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(4);
		zhangtingvalue.setText(Integer.toString(marketInfoVO.getZhangting()));
		dietingValue.setText(Integer.toString(marketInfoVO.getDieting()));
		tingpaiValue.setText(Integer.toString(marketInfoVO.getTingpai()));
		volumeValue.setText(Long.toString(marketInfoVO.getVolume()));
		meanValue.setText(nf.format(marketInfoVO.getJunzhi()));
		varianValue.setText(nf.format(marketInfoVO.getFangcha()));
		chartService chartservice = new YieldFanChart(marketInfoVO.getShanxingtu());
		Pane piePane = chartservice.getchart(334,276, true);
		RAFPieChartPane.getChildren().clear();
		RAFPieChartPane.getChildren().add(piePane);
		chartService chartService2 = new YieldPointChart(marketInfoVO.getDiantu(),ChartKind.POINTFULL);
		Pane dianPane = chartService2.getchart(334, 276, true);
		RAFContributionPane.getChildren().clear();
		RAFContributionPane.getChildren().add(dianPane);
	}
	private void initKLinePane(String code){
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		LocalDate end = (LocalDate)dataController.get("SystemTime");
		LocalDate begin = end.minusDays(200);
		List<SingleStockInfoVO> list = null;
		try {
			list = stockLogicInterface.getExponentInfoByTime(code,begin,end);
			System.out.println(list);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (DateInvalidException e) {
			Notifications.create().title("异常").text(e.toString()).showWarning();
		} catch (BeginInvalidException e) {
			Notifications.create().title("异常").text(e.toString()).showWarning();
		} catch (EndInvalidException e) {
			Notifications.create().title("异常").text(e.toString()).showWarning();
		} catch (NullStockIDException e) {
			Notifications.create().title("异常").text(e.toString()).showWarning();
		}
		chartService service = new KLineChart(list);
		Pane pane = service.getchart(463,300,true);
		KLineAnchorPane.getChildren().clear();
		KLineAnchorPane.getChildren().add(pane);
	}
	private  void intiEMAPane(String code){
		LocalDate end = (LocalDate)dataController.get("SystemTime");
		LocalDate begin = end.minusDays(200);
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
//		EMAInfoVO vo = stockLogicInterface.getExponentEMAInfo(code,begin,end);


	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		nameLabel.setStyle(
				"-fx-font-size:45;"
				+ "-fx-text-fill: rgb(255, 0, 0, 1);"
				+ "-fx-font-weight:bold");
		zhangtingLabel.setStyle("-fx-font-size:18;"
				+ "-fx-text-fill: #EE5C42;"
				+ "-fx-font-weight:bold");
		dietingLabel.setStyle("-fx-font-size:18;"
				+ "-fx-text-fill: #C0FF3E;"
				+ "-fx-font-weight:bold");
		tingpaiLabel.setStyle("-fx-font-size:18;"
				+ "-fx-text-fill: rgb(255, 255, 255, 1);"
				+ "-fx-font-weight:bold");
		volumeLabel.setStyle("-fx-font-size:18;"
				+ "-fx-text-fill: rgb(255, 255, 255, 1);"
				+ "-fx-font-weight:bold");
		varianLabel.setStyle("-fx-font-size:18;"
				+ "-fx-text-fill: rgb(255, 255, 255, 1);"
				+ "-fx-font-weight:bold");
		RAFfengbuLabel.setStyle("-fx-font-size:20;"
				+ "-fx-text-fill: rgb(255, 255, 255, 1);"
				+ "-fx-font-weight:bold");
		meanLabel.setStyle("-fx-font-size:18;"
				+ "-fx-text-fill: rgb(255, 255, 255, 1);"
				+ "-fx-font-weight:bold");
		zhangtingvalue.setStyle("-fx-font-size:18;"
				+ "-fx-text-fill: #EE5C42;"
				+ "-fx-font-weight:bold");
		dietingValue.setStyle("-fx-font-size:18;"
				+ "-fx-text-fill: #C0FF3E;"
				+ "-fx-font-weight:bold");

		ObservableList<String> marketList = FXCollections.observableArrayList();
		marketList.addAll("SHA","SHB","SZA","SZB","CYB","ZXB","HS300");
		marketComboBox.getItems().addAll(marketList);
		marketComboBox.setPromptText("选择股市");
	}

}
