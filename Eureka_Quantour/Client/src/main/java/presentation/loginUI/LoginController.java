package presentation.loginUI;

import java.awt.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	@FXML
	TextField usernameTextField;
	
	@FXML
	TextField passwordTextField;
	
	@FXML
	HBox callBackHBox;
	
	@FXML
	ImageView logoImageView;
	
	@FXML
	protected void logUp(ActionEvent e){
		
	}
	
	@FXML
	protected void logIn(ActionEvent e) throws IOException{
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("presentation/mainScreen/MainScreen.fxml"));
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Image image = new Image(getClass().getResourceAsStream("Title.png"));
		logoImageView.setImage(image);
	}

}
