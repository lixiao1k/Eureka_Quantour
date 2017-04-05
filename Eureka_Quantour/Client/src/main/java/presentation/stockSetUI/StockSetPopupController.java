package presentation.stockSetUI;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class StockSetPopupController implements Initializable{
	private StockSetUIController Upcontroller;

	@FXML
	TextField stockSetName;
	
	@FXML
	Button ensureButton;
	
	@FXML
	protected void cancel(ActionEvent e){
		Stage root =(Stage) stockSetName.getScene().getWindow();
		root.close();
	}
	
	@FXML
	protected void ensure(ActionEvent e){
		String name = stockSetName.getText();
		if(name.length()!=0){
			Upcontroller.creatSet(name);
			Stage root =(Stage) stockSetName.getScene().getWindow();
			root.close();
		}else{
			Notifications.create().text("Please input stock-set name").show();
		}
	}
	

	public void setUpController(StockSetUIController controller){
		this.Upcontroller = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		
	}

}
