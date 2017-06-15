package presentation.loginUI;

import java.awt.Label;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javax.management.Notification;
import javax.swing.JOptionPane;

import javafx.event.EventHandler;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import exception.LogErrorException;
import exception.SqlNotConnectedException;
import exception.UserNameRepeatException;
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
import main.Main;
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
	
	private UserPool pool;
	
	@FXML
	protected void logUp(ActionEvent e){
		if(usernameTextField.getText().length()==0||passwordTextField.getText().length()==0){
			Notifications.create().title("一场").text("用户名或密码不能为空").showWarning();
		}else{
			String username = usernameTextField.getText();
			char[] password = passwordTextField.getText().toCharArray();
			RemoteHelper remote = RemoteHelper.getInstance();
			ClientLogicInterface clientLogicInterface = remote.getClientLogic();
			try {
				clientLogicInterface.signUp(username, password);
				Notifications.create().title("注册提示").text("注册成功！").showInformation();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				Notifications.create().title("注册异常").text(e1.toString()).showError();
			} catch (UserNameRepeatException e1) {
				// TODO Auto-generated catch block
				Notifications.create().title("注册异常").text(e1.toString()).showError();
			}
		}
	}
	
	@FXML
	protected void logIn(ActionEvent e) throws IOException{
		String username = usernameTextField.getText();
		char[] password= passwordTextField.getText().toCharArray();
		RemoteHelper remote = RemoteHelper.getInstance();
		ClientLogicInterface clientLogicInterface = remote.getClientLogic();
		try {
			clientLogicInterface.signIn(username,password);
	        dataController.upDate("UserName", username);
	        Main.pool.setName(username);
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("presentation/mainScreen/MainScreen.fxml"));
			Notifications.create().title("登录提示").text("登录成功！").showInformation();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
//			stage.initStyle(StageStyle.TRANSPARENT);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					RemoteHelper remoteHelper = RemoteHelper.getInstance();
					ClientLogicInterface clientLogicInterface1 = remoteHelper.getClientLogic();
					try {
						clientLogicInterface.signOut((String)dataController.get("UserName"));
						Main.pool_thread.stop();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			});
			Stage stagenow = (Stage) usernameTextField.getScene().getWindow();
			stagenow.close();
		} catch (LogErrorException e1) {
//			 TODO Auto-generated catch block
			Notifications.create().title("登录异常").text(e1.toString()).showError();
		} catch (SqlNotConnectedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
