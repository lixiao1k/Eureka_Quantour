package presentation.marketUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MarketUIController implements Initializable {
	@FXML
	HBox buttonHBox;
	
	@FXML
	ScrollPane upTenScrollPane;
	
	@FXML
	ScrollPane downTenScrollPane;
	
	@FXML
	ScrollPane stocksScrollPane;
	
	@FXML
	AnchorPane stockBasicInfoAnchorPane;
	
	@FXML
	AnchorPane kChartAnchorPane;
	
	@FXML
	AnchorPane emaChartAnchorPane;
	
	@FXML
	protected void goSetHSButtons(ActionEvent e){
		
	}
	
	@FXML
	protected void goSetSSButtons(ActionEvent e){
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		upTenScrollPane.setStyle("-fx-background-color:transparent;");
		downTenScrollPane.setStyle("-fx-background-color:transparent;");
		
	}

}
