package presentation.mainScreen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MainScreenController implements Initializable{
	
	@FXML
	DatePicker nowDatePicker;
	
	@FXML
	Button editButton;
	
	@FXML
	Button exitButton;
	
	@FXML
	Label nameLabel;
	
	@FXML
	Button stockSetButton;
	
	@FXML
	Button marketButton;
	
	@FXML
	Button singleStockButton;
	
	@FXML
	Button mineButton;
	
	@FXML
	AnchorPane mainAnchorPane;
	
	@FXML
	protected void browseStockSet(ActionEvent e) throws IOException{
		AnchorPane stockSetPane = (AnchorPane)FXMLLoader.load(getClass().getClassLoader().getResource("presentation/stockSetUI/StockSetUI.fxml"));
		mainAnchorPane.getChildren().add(stockSetPane);
	}
	
	@FXML
	protected void browseMarket(ActionEvent e){
		
	}
	
	@FXML
	protected void browseSingleStock(ActionEvent e){
		
	}
	
	@FXML
	protected void browseMine(ActionEvent e){
		
	}
	
	@FXML
	protected void edit(ActionEvent e){
		
	}
	
	@FXML
	protected void exit(ActionEvent e){
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Image editImage = new Image(getClass().getResourceAsStream("edit.png"));
		editButton.setGraphic(new ImageView(editImage));
		Image exitImage = new Image(getClass().getResourceAsStream("exit.png"));
		exitButton.setGraphic(new ImageView(exitImage));
		stockSetButton.setText("股池");
		marketButton.setText("市场");
		singleStockButton.setText("个股");
		mineButton.setText("我的");
		
	}

}
