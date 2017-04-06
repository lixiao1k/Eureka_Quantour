package presentation.marketUI;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import en_um.Positive;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import logic.service.Stub;
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
		MenuItem delete = new MenuItem("移除");
		MenuItem copy = new MenuItem("添至");
		menu.getItems().addAll(delete,copy);
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
		int length = list.size();
		System.out.println(length);
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
		MenuItem delete = new MenuItem("移除");
		MenuItem copy = new MenuItem("添至");
		menu.getItems().addAll(delete,copy);
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
//		upTenScrollPane.setStyle("-fx-background-color:transparent;");
//		downTenScrollPane.setStyle("-fx-background-color:transparent;");
		Stub stub = new Stub();
		System.out.println(stub.getStockSetSortedInfo().size());
		initialTenScroll(stub.getStockSetSortedInfo());
		System.out.println(stub.getStockSetSortedInfo().size());
		System.out.println(stub.getStockSetSortedInfo().size());
		System.out.println(stub.getStockSetSortedInfo().size());
		System.out.println(stub.getStockSetSortedInfo().size());
		System.out.println(stub.getStockSetSortedInfo().size());
		for(int i = 0;i<25;i++){
			System.out.println(stub.getStockSetSortedInfo().size());
		}
		initialStocksFlowPane(stub.getStockSetSortedInfo());
	}

}
