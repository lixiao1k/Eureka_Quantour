package presentation.marketUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import exception.NullMarketException;
import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import exception.StockNameRepeatException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.service.StockLogicInterface;
import rmi.RemoteHelper;
import vo.SingleStockInfoVO;

public class MarketUIPopupController implements Initializable{
	@FXML
	Label nameLabel;
	
	@FXML
	ComboBox<String> comboBox;
	
	private DataContorller dataController;
	private ObservableList<String> stockSets = FXCollections.observableArrayList();
	
	@FXML
	protected void cancel(ActionEvent e){
		Stage stage = (Stage) nameLabel.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	protected void ensure(ActionEvent e){
		String stockset = comboBox.getValue();
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		String name =(String)dataController.get("Market_StockNow");
		String code ="";
		String setflag = "null";
		String temp = (String)dataController.get("SetFlag");
		if(temp!=null){
			setflag=temp;
		}
		List<SingleStockInfoVO> stocklist = null;
		List<String> stockcodelist = new ArrayList<String>();
		if(setflag.equals(name)){//如果相等则为将板块加入股池
			try {
				stocklist = stockLogicInterface.getStockSetSortedInfo(name, (LocalDate)dataController.get("SystemTime"),null);
				if(stocklist.size()!=0){
					for(SingleStockInfoVO vo:stocklist){//将板块中的所有股票都加入相应股池
						stockcodelist.add(vo.getCode());
					}
					stockLogicInterface.addStockList_to_StockSet((String)dataController.get("UserName"),stockset,stockcodelist);
//					Thread notification = new Notification("成功","成功将添加至"+stockset);
//					notification.start();

					Notifications.create().title("成功").text("成功将添加至"+stockset).showInformation();
//					Stage stage = (Stage) nameLabel.getScene().getWindow();
//					stage.close();
				}

			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (NullMarketException e1) {
				Notifications.create().title("异常").text(e.toString()).showWarning();
			}
		}else{
			try {
				code = stockLogicInterface.nameToCode(name);
				stockLogicInterface.addStockToStockSet(code
						, stockset, (String)dataController.get("UserName"));
//				Thread notification = new Notification("成功","成功将"+(String)dataController.
//						get("Market_StockNow")+"添加至"+stockset);

				Notifications.create().title("成功").text("成功将"+(String)dataController.
						get("Market_StockNow")+"添加至"+stockset).showInformation();
//				Stage stage = (Stage) nameLabel.getScene().getWindow();
//				stage.close();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				Notifications.create().title("网络连接异常").text(e1.toString()).showWarning();
				e1.printStackTrace();
			} catch (StockNameRepeatException e1) {
				// TODO Auto-generated catch block
				Notifications.create().title("添加错误").text(e1.toString()).showError();
			}
		}

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		String stockName = (String) dataController.get("Market_StockNow");
		nameLabel.setText(stockName);
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		try {
			List<String> vo = stockLogicInterface.getStockSet((String)dataController.get("UserName"));
            int length = vo.size();
            for(int i=0;i<length;i++){
            	stockSets.add(vo.get(i));
            }
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
			e.printStackTrace();
		}
		comboBox.getItems().clear();
		comboBox.getItems().addAll(stockSets);

	}

}
