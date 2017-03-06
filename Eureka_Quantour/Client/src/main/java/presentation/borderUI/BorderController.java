package presentation.borderUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class BorderController implements Initializable {
	@FXML
	DatePicker beginDatePicker;
	
	@FXML
	DatePicker endDatePicker;
	
	@FXML
	TextField searchTextField;
	
	@FXML
	Button searchButton;
	
	@FXML
	protected void goSearch(ActionEvent e){
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
