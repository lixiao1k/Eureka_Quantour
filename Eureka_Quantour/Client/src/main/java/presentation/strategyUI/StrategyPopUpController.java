package presentation.strategyUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class StrategyPopUpController implements Initializable{

	@FXML
	TextField name;
	
	@FXML
	RadioButton publicRB;
	
	@FXML
	RadioButton privateRB;
	
	StrategyUIController controller;
	
	ToggleGroup isPublic;
	
	@FXML
	protected void save(ActionEvent e){
		
	}
	
	@FXML
	protected void cancel(ActionEvent e){
		
	}
	public void setController(StrategyUIController controller){
		this.controller=controller;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		privateRB.setSelected(true);
	}

}
