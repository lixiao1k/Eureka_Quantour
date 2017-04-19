package presentation.statisticsUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import exception.BeginInvalidException;
import exception.DateInvalidException;
import exception.EndInvalidException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import logic.service.StockLogicInterface;
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
	HBox infoHBox;
	
	private DataContorller dataController;
	
	@FXML
	protected void setMarket(ActionEvent e){
		nameLabel.setText(marketComboBox.getValue());
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		try {
			MarketInfoVO marketInfoVO = stockLogicInterface.getMarketInfo((LocalDate)dataController.get("SystemTime"),
					marketComboBox.getValue());
			System.out.println(marketInfoVO);
			System.out.println(marketInfoVO.getDieting());
			System.out.println(marketInfoVO.getTingpai());
			System.out.println(marketInfoVO.getVolume());
			System.out.println(marketInfoVO.getZhangting());
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
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		nameLabel.setStyle(
				"-fx-font-size:45;"
				+ "-fx-text-fill: rgb(255, 0, 0, 0.9);"
				+ "-fx-font-weight:bold");
		ObservableList<String> marketList = FXCollections.observableArrayList();
		marketList.addAll("SHA","SHB","SZA","SZB","CYB","ZXB","HS300");
		marketComboBox.getItems().addAll(marketList);
		marketComboBox.setPromptText("选择股市");
	}

}
