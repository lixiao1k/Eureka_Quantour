package presentation.loginUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginController implements Initializable{
	@FXML
    TextField nameTextField;
	
	@FXML
	TextField passwordTextField;
	
	@FXML
	Label feedbackLabel;
	
	@FXML
	protected void login(ActionEvent e){
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

}
