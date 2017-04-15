package presentation.loginUI;

import java.awt.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dataController.DataContorller;
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
import logic.service.ClientLogicInterface;
import rmi.RemoteHelper;

public class LoginController implements Initializable{
	@FXML
	TextField usernameTextField;
	
	@FXML
	TextField passwordTextField;
	
	@FXML
	HBox callBackHBox;
	
	@FXML
	ImageView logoImageView;
	
	private DataContorller dataController;
	
	@FXML
	protected void logUp(ActionEvent e){
		String username = usernameTextField.getText();
		char[] password = passwordTextField.getText().toCharArray();
		RemoteHelper remote = RemoteHelper.getInstance();
		ClientLogicInterface clientLogicInterface = remote.getClientLogic();
		
	}
	
	@FXML
	protected void logIn(ActionEvent e) throws IOException{
		if(true){
			String username = usernameTextField.getText();
	        dataController.upDate("UserName", username);
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("presentation/mainScreen/MainScreen.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
			Stage stagenow = (Stage) usernameTextField.getScene().getWindow();
			stagenow.close();
		}else{
			System.out.println("");
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		Image image = new Image(getClass().getResourceAsStream("Title.png"));
		logoImageView.setImage(image);
	}

}
