package presentation.stockSetUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import exception.StockSetNameRepeatException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import logic.service.StockLogicInterface;
import rmi.RemoteHelper;

public class StockSetPopupController implements Initializable{
	private StockSetUIController Upcontroller;

	@FXML
	TextField stockSetName;
	
	@FXML
	Button ensureButton;
	
	private DataContorller dataController;
	
	@FXML
	protected void cancel(ActionEvent e){
		Stage root =(Stage) stockSetName.getScene().getWindow();
		root.close();
	}
	
	@FXML
	protected void ensure(ActionEvent e){
		String name = stockSetName.getText();
		if(name.length()!=0){
			RemoteHelper remote = RemoteHelper.getInstance();
			StockLogicInterface stockLogicInterface = remote.getStockLogic();
			try {
				stockLogicInterface.addStockSet(name,(String)dataController.get("UserName"));
				Notifications.create().text("成功").text("成功创建股池"+name+"!").showInformation();
				Upcontroller.creatSet(name);
				Stage root =(Stage) stockSetName.getScene().getWindow();
				root.close();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				Notifications.create().title("网络连接异常").text(e1.toString()).showWarning();
				e1.printStackTrace();
			} catch (StockSetNameRepeatException e1) {
				// TODO Auto-generated catch block
				Notifications.create().title("命名重复").text(e1.toString()).showError();
				e1.printStackTrace();
			}

		}else{
			Notifications.create().title("输入错误").text("股池名不能为空！").showWarning();
		}
	}
	

	public void setUpController(StockSetUIController controller){
		this.Upcontroller = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		
	}

}
