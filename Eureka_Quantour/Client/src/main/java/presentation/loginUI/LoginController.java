package presentation.loginUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable{
	@FXML
    TextField nameTextField;
	
	@FXML
	TextField passwordTextField;
	
	@FXML
	Label feedbackLabel;
	
	@FXML
	protected void login(ActionEvent e) throws IOException{
		if(true){
			Stage stage = (Stage)nameTextField.getScene().getWindow();
			stage.close();
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("presentation/borderUI/BorderUI.fxml"));
			Scene scene = new Scene(root);
			Stage stageNew = new Stage();
			stageNew.initStyle(StageStyle.TRANSPARENT);
			stageNew.setScene(scene);
			stageNew.show();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

}
