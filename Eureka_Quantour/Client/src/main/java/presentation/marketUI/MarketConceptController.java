package presentation.marketUI;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
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

public class MarketConceptController implements Initializable{

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
			list = stockLogicInterface.getConceptList();
			int length = list.size();
			list = list.subList(0, 160);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(list!=null){
			for(String str:list){
				Button button = new Button(str);
				button.setPrefSize(90, 30);
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						controller.setIndustryLabel(str);
						List<SingleStockInfoVO> stocklist = null;
						RemoteHelper remote = RemoteHelper.getInstance();
						StockLogicInterface stockLogicInterface = remote.getStockLogic();
						try {
							stocklist = stockLogicInterface.getStockSetSortedInfo(str,(LocalDate)dataContorller.get("SystemTime"),null);
							controller.initialAllStocksPane(stocklist);
							if(stocklist!=null){
								controller.setStockDetailInfo(stocklist.get(0));
							}

						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
						} catch (NullMarketException e) {
							// TODO Auto-generated catch block
							Notifications.create().title("输入异常").text(e.toString()).showWarning();
						}
						
						Stage stage = (Stage)button.getScene().getWindow();
						stage.close();
						
					}
				});
				ContextMenu contextMenu = new ContextMenu();
				MenuItem menuItem = new MenuItem("添至");
				menuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
//
						dataContorller.upDate("Market_StockNow",str);//用以判断添加至股池的是板块还是单只股票，如果Market_stockNow
						dataContorller.upDate("SetFlag",str);//       =setFlag,则说明加入的是板块，否则是单只股票
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("MarketUIPopup.fxml"));
						Parent popUp = null;
						try {
							popUp = (AnchorPane)loader.load();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Scene scene = new Scene(popUp);
						Stage stage = new Stage();
						stage.setScene(scene);
//						stage.initStyle(StageStyle.TRANSPARENT);
						stage.show();
					}
				});
				contextMenu.getItems().add(menuItem);
				button.setContextMenu(contextMenu);
				buttonsPane.getChildren().add(button);
			}
		}
		
	}

}
