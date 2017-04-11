package presentation.marketUI;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import en_um.Positive;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import logic.service.Stub;
import presentation.mainScreen.MainScreenController;
import presentation.singleStockUI.SingleStockUIPopupController;
import vo.SingleStockInfoVO;

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
	FlowPane upTenFlowPane;
	
	@FXML
	FlowPane downTenFlowPane;
	
	@FXML
	FlowPane stocksFlowPane;
	
	@FXML
	AnchorPane menuAnchorPane;
	
	@FXML
	AnchorPane pagePane;
	
	private MainScreenController controller;
	
	@FXML
	protected void goSetHSButtons(ActionEvent e){
		buttonHBox.getChildren().clear();
		Button HSAButton = new Button("沪市A股");
		HSAButton.setPrefHeight(37);
		HSAButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		Button HSBButton = new Button("沪市B股");
		HSBButton.setPrefHeight(37);
		HSBButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		Separator separator = new Separator(Orientation.VERTICAL);
		buttonHBox.getChildren().addAll(HSAButton,separator,HSBButton);
	}
	
	@FXML
	protected void goSetSSButtons(ActionEvent e){
		buttonHBox.getChildren().clear();
		Button HSAButton = new Button("深市A股");
		HSAButton.setPrefHeight(37);
		HSAButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		Button HSBButton = new Button("深市B股");
		HSBButton.setPrefHeight(37);
		HSBButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		Separator separator = new Separator(Orientation.VERTICAL);
		buttonHBox.getChildren().addAll(HSAButton,separator,HSBButton);
	}
	
	private void initialTenScroll(List<SingleStockInfoVO> list){
		int length = list.size();
		for(int i =0;i<2;i++){
			SingleStockInfoVO vo = list.get(i);
			HBox hb = getHBox4TenScoll(vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu());
			upTenFlowPane.getChildren().add(hb);
		}
		for(int i=0;i<2;i++){
			SingleStockInfoVO vo = list.get(length-1-i);
			HBox hb = getHBox4TenScoll(vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu());
			downTenFlowPane.getChildren().add(hb);
		}
	}
	
	public HBox getHBox4TenScoll(String code,String name,double close,double RAF){
		HBox root = new HBox();
		root.setPrefSize(250, 25);
		Label codeLabel = getLabel(55, Pos.CENTER_RIGHT, code,Positive.ZERO);
		Label nameLabel = getLabel(65, Pos.CENTER_LEFT, name,Positive.ZERO);
		Label closeLabel = getLabel(65, Pos.CENTER_RIGHT, Double.toString(close),isPositive(RAF));
		Label RAFLabel = getLabel(65, Pos.CENTER_RIGHT, Double.toString(RAF)+"%",isPositive(RAF));
		root.getChildren().addAll(codeLabel,nameLabel,closeLabel,RAFLabel);
		root.setStyle("-fx-background-color: #38424b");
		//鼠标点击单条信息的效果
		root.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
			root.setStyle("-fx-background-color:rgba(0,0,255,0.1)");
		});
		ContextMenu menu = new ContextMenu();
		MenuItem copy = new MenuItem("添至");
		copy.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getClassLoader().getResource(
					"presentation/singleStockUI/SingleStockUIPopup.fxml"));
				Parent popUp = null;
				try {
					popUp = (AnchorPane)loader.load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Scene scene = new Scene(popUp);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.show();
			}
		});
		MenuItem look = new MenuItem("查看");
		look.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				try {
					controller.setSingleStockUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menu.getItems().addAll(copy,look);
		//menu的监听之后加
		
		//很冗余的代码，想不到解决的办法，因为HBox无法加入上下文菜单，用Button会是比HBox更好的选择，但是时间有限，有机会再改
		nameLabel.setContextMenu(menu);
		codeLabel.setContextMenu(menu);
		closeLabel.setContextMenu(menu);
		RAFLabel.setContextMenu(menu);	
		return root;
	}
	public Positive isPositive(Double num){
		if(num>0){
			return Positive.POSITIVE;
		}else if(num==0){
			return Positive.ZERO;
		}else{
			return Positive.NEGATIVE;
		}
	}
	public Label getLabel(double width, Pos alignment,String text,Positive positive){
		Label label = new Label(text);
		label.setPrefSize(width, 25);
		label.setMaxWidth(width);
		label.setAlignment(alignment);
		
		if(Positive.ZERO==positive){
			label.setStyle("-fx-border-width: 1;-fx-border-color: rgba(255,255,255,0.2);-fx-background-color:transparent;-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold");
		}else if(Positive.POSITIVE==positive){
			label.setStyle("-fx-border-width: 1;-fx-border-color: rgba(255,255,255,0.2);-fx-background-color:transparent;-fx-text-fill: rgb(255, 0, 0, 1);-fx-font-weight:bold");
		}else{
			label.setStyle("-fx-border-width: 1;-fx-border-color: rgba(255,255,255,0.2);-fx-background-color:transparent;-fx-text-fill: rgb(0, 255, 0, 1);-fx-font-weight:bold");
		}
		return label;
	}
	
	private void initialStocksFlowPane(List<SingleStockInfoVO> list){
		stocksFlowPane.getChildren().clear();
		int length = list.size();
		for(int i=0;i<length;i++){
			SingleStockInfoVO vo = list.get(i);
			HBox hb = getHBox(i+1, vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu()
					, vo.getOpen(), vo.getHigh(), vo.getLow(),(int)vo.getVolume());
			stocksFlowPane.getChildren().add(hb);
		}
	}
	public HBox getHBox(int num,String code,String name,double close,double RAF, double open,double high,double low,int volume){
		HBox root = new HBox();
		root.setPrefSize(645, 25);
		Label numLabel = getLabel(50, Pos.CENTER, Integer.toString(num),Positive.ZERO);
		Label codeLabel = getLabel(75, Pos.CENTER_RIGHT, code,Positive.ZERO);
		Label nameLabel = getLabel(85, Pos.CENTER_LEFT, name,Positive.ZERO);
		Label closeLabel = getLabel(65, Pos.CENTER_RIGHT, Double.toString(close),isPositive(RAF));
		Label RAFLabel = getLabel(75, Pos.CENTER_RIGHT, Double.toString(RAF)+"%",isPositive(RAF));
		Label openLabel = getLabel(65, Pos.CENTER_RIGHT, Double.toString(open),Positive.ZERO);
		Label highLabel = getLabel(65, Pos.CENTER_RIGHT, Double.toString(high),Positive.ZERO);
		Label lowLabel = getLabel(65, Pos.CENTER_RIGHT, Double.toString(low),Positive.ZERO);
		Label volumeLabel = getLabel(100, Pos.CENTER_RIGHT, Integer.toString(volume),Positive.ZERO);
		root.getChildren().addAll(numLabel,codeLabel,nameLabel,closeLabel,RAFLabel,openLabel,highLabel,lowLabel,volumeLabel);
		root.setStyle("-fx-background-color: #38424b");
		//鼠标点击单条信息的效果
		root.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
			root.setStyle("-fx-background-color:rgba(0,0,255,0.2)");
		});
		ContextMenu menu = new ContextMenu();
		MenuItem copy = new MenuItem("添至");
		copy.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getClassLoader().getResource(
					"presentation/singleStockUI/SingleStockUIPopup.fxml"));
				Parent popUp = null;
				try {
					popUp = (AnchorPane)loader.load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Scene scene = new Scene(popUp);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.show();
			}
		});
		MenuItem look = new MenuItem("查看");
		look.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				try {
					controller.setSingleStockUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menu.getItems().addAll(copy,look);
		//menu的监听之后加
		
		//很冗余的代码，想不到解决的办法，因为HBox无法加入上下文菜单，用Button会是比HBox更好的选择，但是时间有限，有机会再改
		nameLabel.setContextMenu(menu);
		codeLabel.setContextMenu(menu);
		numLabel.setContextMenu(menu);
		closeLabel.setContextMenu(menu);
		RAFLabel.setContextMenu(menu);
		openLabel.setContextMenu(menu);
		highLabel.setContextMenu(menu);
		lowLabel.setContextMenu(menu);
		volumeLabel.setContextMenu(menu);
		
		return root;
	}

	
	private void initialMenuAnchorPane(){
		HBox hb = new HBox();
		hb.setPrefSize(597, 25);
		Label numLabel = getLabel4initialMenu(50, Pos.CENTER, "序号");
		Label codeLabel = getLabel4initialMenu(75, Pos.CENTER_RIGHT,"代码");
		Label nameLabel = getLabel4initialMenu(85, Pos.CENTER_LEFT, "名称");
		Label closeLabel = getLabel4initialMenu(65, Pos.CENTER_RIGHT,"最新价");
		Label RAFLabel = getLabel4initialMenu(75, Pos.CENTER_RIGHT, "涨跌幅");
		Label openLabel = getLabel4initialMenu(65, Pos.CENTER_RIGHT, "今开");
		Label highLabel = getLabel4initialMenu(65, Pos.CENTER_RIGHT, "最高");
		Label lowLabel = getLabel4initialMenu(65, Pos.CENTER_RIGHT, "最低");
		Label volumeLabel = getLabel4initialMenu(52, Pos.CENTER_RIGHT, "成交量");
		hb.getChildren().addAll(numLabel,codeLabel,nameLabel,closeLabel,RAFLabel,openLabel,highLabel,lowLabel,volumeLabel);
		menuAnchorPane.getChildren().add(hb);
	}
	
	private Label getLabel4initialMenu(double width, Pos alignment,String text){
		Label label = new Label(text);
		label.setPrefSize(width, 25);
		label.setMaxWidth(width);
		label.setAlignment(alignment);
		label.setStyle("-fx-border-width: 1;"
				+ "-fx-border-color: black;"
				+ "-fx-background-color:rgba(255,255,255,0.1);"
				+ "-fx-text-fill: rgb(255, 255, 255, 0.9);"
				+ "-fx-font-weight:bold");
		return label;
	}
	
	public void setController(MainScreenController controller){
		this.controller = controller;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
//		upTenScrollPane.setStyle("-fx-background-color:transparent;");
//		downTenScrollPane.setStyle("-fx-background-color:transparent;");
		initialMenuAnchorPane();
		Stub stub = new Stub();
		List<SingleStockInfoVO> vo =stub.getStockSetSortedInfo();
		int length =(int) Math.ceil(vo.size()/50);
		Pagination pagination = new Pagination();
		pagePane.getChildren().add(pagination);
		pagination.setStyle("-fx-page-information-visible: false;");
		pagination.setPageCount(length);
		pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				List<SingleStockInfoVO> list = vo.subList(newValue.intValue()*50, newValue.intValue()*50+50);
				initialStocksFlowPane(list);
			}
		});
	}

}
