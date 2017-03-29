package presentation.strategyUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class StrategyUIController implements Initializable{
	@FXML
	ComboBox<String> stockSetComboBox;
	
	@FXML
	DatePicker beginTimeDatePicker;
	
	@FXML
	CheckBox momentumCheckBox;
	
	@FXML
	CheckBox meanCheckBox;
	
	@FXML
	TextField holdPeriodTextField;
	
	@FXML
	CheckBox closeCheckBox;
	
	@FXML
	CheckBox openCheckBox;
	
	@FXML
	TextField numOfStockTextField;
	
	@FXML
	Button makeStrategyButton;
	
	@FXML
	AnchorPane chart1AnchorPane;
	
	@FXML
	AnchorPane chart2AnchorPane;
	
	@FXML
	AnchorPane chart3AnchorPane;
	
	@FXML
	protected void makeStrategy(ActionEvent e){
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
