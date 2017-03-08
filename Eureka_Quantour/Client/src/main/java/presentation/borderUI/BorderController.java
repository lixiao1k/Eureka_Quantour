package presentation.borderUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class BorderController implements Initializable {
	@FXML
	ImageView imageView;
	
	@FXML
	ImageView logoImageView;
	
	@FXML
	AnchorPane setInfoAnchorPane;
	
	private Image logo = new Image(getClass().getResourceAsStream("Title.png"));
	
	@FXML
	protected void goSearch(ActionEvent e){
		
	}
	
	@FXML
	protected void browseKLine(ActionEvent e){
		
	}
	
	@FXML
	protected void browseAverageLine(ActionEvent e){
		
	}
	
	@FXML
	protected void goCompare(ActionEvent e){
		
	}
	
	@FXML
	protected void browseMarket(ActionEvent e){
		
	}
	
	private HBox searchSingleStock (){
		HBox hb = new HBox();
		hb.setPadding(new Insets(25, 5, 5, 25));
		hb.setSpacing(5);
		
		Label beginLabel = new Label("起始时间：");
		DatePicker beginDatePicker = new DatePicker();
		Label endLabel = new Label("结束时间：");
		DatePicker endDatePicker = new DatePicker();
		Label blank = new Label();
	    blank.setPrefSize(40, 10);
		TextField stockName = new TextField();
		stockName.setPrefSize(100,5);
		stockName.setPromptText("股票名称");
		Button searchButton = new Button("搜索");
		
		hb.getChildren().addAll(beginLabel,beginDatePicker,endLabel,endDatePicker,blank,stockName,searchButton);
		return hb;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		logoImageView.setImage(logo);
		setInfoAnchorPane.getChildren().add(searchSingleStock());
	}
}
