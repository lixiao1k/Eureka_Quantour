package presentation.statisticsUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import en_um.ChartKind;
import exception.BeginInvalidException;
import exception.DateInvalidException;
import exception.EndInvalidException;
import exception.NullMarketException;
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
import presentation.chart.piechart.YieldFanChart;
import presentation.chart.scatterchart.YieldPointChart;
import rmi.RemoteHelper;
import vo.MarketInfoVO;

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
	AnchorPane meanPane;
	
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
	
	
	private DataContorller dataController;
	
	@FXML
	protected void setMarket(ActionEvent e){
		nameLabel.setText(marketComboBox.getValue());
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		try {
			MarketInfoVO marketInfoVO = stockLogicInterface.getMarketInfo((LocalDate)dataController.get("SystemTime"),
					marketComboBox.getValue());
			initialAllPane(marketInfoVO);

		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			Notifications.create().title("网络连接异常").text(e1.toString()).showWarning();
			e1.printStackTrace();
		} catch (DateInvalidException e1) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期异常").text(e1.toString()).showWarning();
			e1.printStackTrace();
		} catch (BeginInvalidException e1) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期异常").text(e1.toString()).showWarning();
			e1.printStackTrace();
		} catch (EndInvalidException e1) {
			// TODO Auto-generated catch block
			Notifications.create().title("日期异常").text(e1.toString()).showWarning();
			e1.printStackTrace();
		}catch (NullMarketException e1) {
			// TODO: handle exception
			Notifications.create().title("数据异常").text(e1.toString()).showWarning();
			e1.printStackTrace();
		}
	}
	
	private void initialAllPane(MarketInfoVO marketInfoVO){
		zhangtingvalue.setText(Integer.toString(marketInfoVO.getZhangting()));
		dietingValue.setText(Integer.toString(marketInfoVO.getDieting()));
		tingpaiValue.setText(Integer.toString(marketInfoVO.getTingpai()));
		volumeValue.setText(Long.toString(marketInfoVO.getVolume()));
		chartService chartservice = new YieldFanChart(marketInfoVO.getShanxingtu());
		Pane piePane = chartservice.getchart(334,276, true);
		RAFPieChartPane.getChildren().clear();
		RAFPieChartPane.getChildren().add(piePane);
		chartService chartService2 = new YieldPointChart(marketInfoVO.getDiantu(),ChartKind.POINTFULL);
		Pane dianPane = chartService2.getchart(334, 276, true);
		RAFContributionPane.getChildren().clear();
		RAFContributionPane.getChildren().add(dianPane);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		nameLabel.setStyle(
				"-fx-font-size:45;"
				+ "-fx-text-fill: rgb(255, 0, 0, 1);"
				+ "-fx-font-weight:bold");
		zhangtingLabel.setStyle("-fx-font-size:20;"
				+ "-fx-text-fill: #EE5C42;"
				+ "-fx-font-weight:bold");
		dietingLabel.setStyle("-fx-font-size:20;"
				+ "-fx-text-fill: #C0FF3E;"
				+ "-fx-font-weight:bold");
		tingpaiLabel.setStyle("-fx-font-size:20;"
				+ "-fx-text-fill: rgb(255, 255, 255, 1);"
				+ "-fx-font-weight:bold");
		volumeLabel.setStyle("-fx-font-size:20;"
				+ "-fx-text-fill: rgb(255, 255, 255, 1);"
				+ "-fx-font-weight:bold");
		zhangtingvalue.setStyle("-fx-font-size:20;"
				+ "-fx-text-fill: #EE5C42;"
				+ "-fx-font-weight:bold");
		dietingValue.setStyle("-fx-font-size:20;"
				+ "-fx-text-fill: #C0FF3E;"
				+ "-fx-font-weight:bold");
		ObservableList<String> marketList = FXCollections.observableArrayList();
		marketList.addAll("SHA","SHB","SZA","SZB","CYB","ZXB","HS300");
		marketComboBox.getItems().addAll(marketList);
		marketComboBox.setPromptText("选择股市");
	}

}
