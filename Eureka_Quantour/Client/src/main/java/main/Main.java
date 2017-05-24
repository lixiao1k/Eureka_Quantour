package main;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import dataController.DataContorller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rmi.RemoteHelper;

public class Main extends Application{
	private static RemoteHelper rmic;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("presentation/loginUI/Login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		DataContorller dataController = DataContorller.getInstance();

		try {
			rmic = RemoteHelper.getInstance();
//			rmic.setRemote(Naming.lookup("rmi://localhost:8888/DateRemote"));
			rmic.setRemote(Naming.lookup("rmi://172.28.165.219:8888/DateRemote"));

			System.out.println("连接服务器成功！");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.toString();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.toString();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.toString();
		}
		launch(args);
	}
}
