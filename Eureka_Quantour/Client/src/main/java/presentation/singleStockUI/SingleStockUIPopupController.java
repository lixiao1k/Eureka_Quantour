package presentation.singleStockUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SingleStockUIPopupController implements Initializable{
	@FXML
	Label nameLabel;
	
	@FXML
	ComboBox<String> comboBox;
	
	private ObservableList<String> list;
	private SingleStockUIController controller;
	
	@FXML
	protected void cancel(ActionEvent e){
		Stage stage = (Stage) nameLabel.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	protected void ensure(ActionEvent e){
		
	}
	
	public void setList(ObservableList<String> list){
		
	}
	public void setController(SingleStockUIController controller){
		this.controller = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
