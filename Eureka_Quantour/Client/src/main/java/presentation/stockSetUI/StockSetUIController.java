package presentation.stockSetUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class StockSetUIController implements Initializable {
	@FXML
	AnchorPane stockSetMainAnchorPane;
	
	@FXML
	ScrollPane stockSetScrollPane;
	
	@FXML
	ScrollPane stocksScrollPane;
	
	@FXML
	FlowPane stockSetFlowPane;
	
	@FXML
	FlowPane stocksFlowPane;
	
	@FXML
	AnchorPane stockBasicInfoAnchorPane;
	
	@FXML
	AnchorPane kChartAnchorPane;
	
	@FXML
	AnchorPane emaChartAnchorPane;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
//		javafx8之后这条语句单独使用没有效果，需要在css上加上
//		.scroll-pane > .viewport {
//		   -fx-background-color: transparent;
//	    }
		stockSetScrollPane.setStyle("-fx-background-color:transparent;");
		stocksScrollPane.setStyle("-fx-background-color:transparent;");

	}

}
