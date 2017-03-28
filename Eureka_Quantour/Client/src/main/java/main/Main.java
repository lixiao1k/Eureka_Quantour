package main;

import java.rmi.Naming;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rmi.RemoteHelper;

public class Main extends Application{
	private static RemoteHelper rmic;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Parent root = FXMLLoader.load(getClass().getResource("/presentation/loginUI/Login.fxml"));
		Scene scene = new Scene(root);
//		primaryStage.initStyle(StageStyle.TRANSPARENT);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	}
	public static void main(String[] args) {
		
		try{
			rmic = RemoteHelper.getInstance();
			rmic.setRemote(Naming.lookup("rmi://localhost:8888/DateRemote"));
			System.out.println("连接服务器");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		launch(args);
	}

}
