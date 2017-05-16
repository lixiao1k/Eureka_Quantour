package presentation.marketUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import exception.NullMarketException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import logic.service.StockLogicInterface;
import rmi.RemoteHelper;
import vo.SingleStockInfoVO;

public class MarketAreaController implements Initializable{
	@FXML
	FlowPane buttonsPane;
	
	private MarketUIController controller;
	
	public void setController(MarketUIController controller){
		this.controller = controller;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		DataContorller dataContorller = DataContorller.getInstance();
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		List<String> list = null;

		try {
			list = stockLogicInterface.getAreaList();
			int length = list.size();
			list = list.subList(1, length);
//			System.out.println(list.size());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if(list!=null){
//			for(String str:list){
//				Button button = new Button(str);
//				button.setPrefSize(180, 30);
//				button.setOnAction(new EventHandler<ActionEvent>() {
//					@Override
//					public void handle(ActionEvent event) {
//						// TODO Auto-generated method stub
//						controller.setIndustryLabel(str);
//						List<SingleStockInfoVO> stocklist = null;
//						RemoteHelper remote = RemoteHelper.getInstance();
//						StockLogicInterface stockLogicInterface = remote.getStockLogic();
//						try {
//							stocklist = stockLogicInterface.getStockSetSortedInfo(str,(LocalDate)dataContorller.get("SystemTime"),null);
//							controller.initialAllStocksPane(stocklist);
//							if(stocklist!=null){
//								controller.setStockDetailInfo(stocklist.get(0));
//							}
//
//						} catch (RemoteException e) {
//							// TODO Auto-generated catch block
//							Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
//							e.printStackTrace();
//						} catch (NullMarketException e) {
//							// TODO Auto-generated catch block
//							Notifications.create().title("输入异常").text(e.toString()).showWarning();
//							e.printStackTrace();
//						}
//						
//						Stage stage = (Stage)button.getScene().getWindow();
//						stage.close();
//						
//					}
//				});
//				buttonsPane.getChildren().add(button);
//			}
//		}
	}

}
