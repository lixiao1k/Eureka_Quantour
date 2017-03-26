package presentation.loginUI;

import java.awt.Label;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class LoginController implements Initializable{
	@FXML
	TextField usernameTextField;
	
	@FXML
	TextField passwordTextField;
	
	@FXML
	HBox callBackLabel;
	
	@FXML
	ImageView logoImageView;
	
	@FXML
	protected void logUp(ActionEvent e){
		
	}
	
	@FXML
	protected void logIn(ActionEvent e){
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Image image = new Image(getClass().getResourceAsStream("Title.png"));
		logoImageView.setImage(image);
	}

}
