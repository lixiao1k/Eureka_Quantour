package presentation.statisticsUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class StatisticsUIController implements Initializable {
	@FXML
	Label nameLabel;
	
	@FXML
	ComboBox<String> marketComboBox;
	
	@FXML
	AnchorPane RAFPieChartPane;
	
	@FXML
	AnchorPane RAFContributionPane;
	
	@FXML
	AnchorPane meanPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
