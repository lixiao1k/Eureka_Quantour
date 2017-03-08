package presentation.borderUI;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BorderController implements Initializable {
	@FXML
	ImageView imageView;
	
	@FXML
	ImageView logoImageView;
	
	@FXML
	AnchorPane setInfoAnchorPane;
	
	private Image logo = new Image(getClass().getResourceAsStream("Title.png"));
	
	@FXML
	protected void browseKLine(ActionEvent e){
		ObservableList<Node> nodeList = setInfoAnchorPane.getChildren();
		for(Node node:nodeList){
			String name = (String)node.getProperties().get("Name");
			if(name!=null&&name.contains("HBox")){
				nodeList.remove(node);
				break;
			}
		}
		setInfoAnchorPane.getChildren().add(searchSingleStockHBox());
	}
	
	@FXML
	protected void browseAverageLine(ActionEvent e){
		
	}
	
	@FXML
	protected void goCompare(ActionEvent e){
		ObservableList<Node> nodeList = setInfoAnchorPane.getChildren();
		for(Node node:nodeList){
			String name = (String)node.getProperties().get("Name");
			if(name!=null&&name.contains("HBox")){
				nodeList.remove(node);
				break;
			}
		}
		setInfoAnchorPane.getChildren().add(compareHBox());
	}
	
	@FXML
	protected void browseMarket(ActionEvent e){
		
	}
	
	@FXML
	protected void exit(ActionEvent e){
		Stage stage = (Stage)imageView.getScene().getWindow();
		stage.close();
	}
	
	private HBox searchSingleStockHBox (){
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
		hb.getProperties().put("Name","searchSingleStockHBox");
		return hb;
	}
	
	private HBox compareHBox(){
		HBox hb = new HBox();
		hb.setPadding(new Insets(25, 5, 5, 25));
		hb.setSpacing(5);
		
		Label beginLabel = new Label("起始时间：");
		DatePicker beginDatePicker = new DatePicker();
		Label endLabel = new Label("结束时间：");
		DatePicker endDatePicker = new DatePicker();
		Label blank = new Label();
		blank.setPrefSize(40, 10);
		TextField stockNameA = new TextField();
		stockNameA.setPrefSize(100, 5);
		stockNameA.setPromptText("股票A");
		TextField stockNameB = new TextField();
		stockNameB.setPrefSize(100, 5);
		stockNameB.setPromptText("股票B");
		Button compareButton = new Button("比较");
		
		hb.getChildren().addAll(beginLabel,beginDatePicker,endLabel,endDatePicker,blank,stockNameA,stockNameB,compareButton);
		hb.getProperties().put("Name","compareHBox");
		return hb;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		logoImageView.setImage(logo);
		setInfoAnchorPane.getChildren().add(searchSingleStockHBox());
	}
}
