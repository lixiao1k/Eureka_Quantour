package presentation.singleStockUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

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

public class SingleStockUIPopupController implements Initializable{
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
		String name =(String)dataController.get("SingleStockNow");
		if(name!=null){
			try {
				String code = stockLogicInterface.nameToCode(name);
				stockLogicInterface.addStockToStockSet(code
						, stockset, (String)dataController.get("UserName"));
				Notifications.create().title("成功").text("成功将"+(String)dataController.
						get("SingleStockNow")+"添加至"+stockset).showInformation();
				Stage stage = (Stage) nameLabel.getScene().getWindow();
				stage.close();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				Notifications.create().title("网络连接异常").text(e1.toString()).showWarning();
				e1.printStackTrace();
			} catch (StockNameRepeatException e1) {
				// TODO Auto-generated catch block
				Notifications.create().title("添加错误").text(e1.toString()).showError();
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		String stockName = (String) dataController.get("SingleStockNow");
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
