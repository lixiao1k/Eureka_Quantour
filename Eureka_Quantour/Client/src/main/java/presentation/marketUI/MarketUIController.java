package presentation.marketUI;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
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
import logic.service.StockLogicInterface;
import logic.service.Stub;
import presentation.mainScreen.MainScreenController;
import presentation.singleStockUI.SingleStockUIPopupController;
import rmi.RemoteHelper;
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
	
	private DataContorller dataController;
	
	//pagnition 是浏览股票信息时的分页组件
	private Pagination pagination;
	
	/*
	 * @param controller 获得主界面的Controller后以便之后跳转至个股界面浏览单只股票详细信息
	 */
	private MainScreenController controller;
	
	private List<SingleStockInfoVO> setStocks;
	private LocalDate systime;
	
	/*
	 * @description 初始化沪市板块栏的组件
	 */
	@FXML
	protected void goSetHSButtons(ActionEvent e){
		buttonHBox.getChildren().clear();
		Button HSAButton = new Button("沪市A股");
		HSAButton.setPrefHeight(37);
		HSAButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				systime = (LocalDate) dataController.get("SystemTime");
				System.out.println(systime.toString());
				// TODO Auto-generated method stub
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					setStocks = stockLogicInterface.getStockSetSortedInfo("SHA",systime, null);
					System.out.println(setStocks.size());
					initialAllStocksPane(setStocks);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
					e.printStackTrace();
				}
			}
		});
		HSAButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		Button HSBButton = new Button("沪市B股");
		HSBButton.setPrefHeight(37);
		HSBButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				systime = (LocalDate) dataController.get("SystemTime");
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					setStocks = stockLogicInterface.getStockSetSortedInfo("SHB",systime, null);
					System.out.println(setStocks.size());
					initialAllStocksPane(setStocks);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
					e.printStackTrace();
				}
			}
		});
		HSBButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		buttonHBox.getChildren().addAll(HSAButton,HSBButton);
	}
	
	/*
	 * @description 初始化深市板块栏的组件
	 */	
	@FXML
	protected void goSetSSButtons(ActionEvent e){
		buttonHBox.getChildren().clear();
		Button SZAButton = new Button("深市A股");
		SZAButton.setPrefHeight(37);

		SZAButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				systime = (LocalDate) dataController.get("SystemTime");
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					setStocks = stockLogicInterface.getStockSetSortedInfo("SZA",systime, null);
					initialAllStocksPane(setStocks);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
					e.printStackTrace();
				}
			}
		});
		SZAButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		Button SZBButton = new Button("深市B股");
		SZBButton.setPrefHeight(37);
		SZBButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				systime = (LocalDate) dataController.get("SystemTime");
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					setStocks = stockLogicInterface.getStockSetSortedInfo("SZB",systime, null);
					initialAllStocksPane(setStocks);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
					e.printStackTrace();
				}
			}
		});
		SZBButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		Button CYBButton = new Button("创业板");
		CYBButton.setPrefHeight(37);
		CYBButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				systime = (LocalDate) dataController.get("SystemTime");
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					setStocks = stockLogicInterface.getStockSetSortedInfo("CYB",systime , null);
					initialAllStocksPane(setStocks);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
					e.printStackTrace();
				}
			}
		});
		CYBButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		Button ZXBButton = new Button("中小板");
		ZXBButton.setPrefHeight(37);
		ZXBButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				systime = (LocalDate) dataController.get("SystemTime");
				RemoteHelper remote = RemoteHelper.getInstance();
				StockLogicInterface stockLogicInterface = remote.getStockLogic();
				try {
					setStocks = stockLogicInterface.getStockSetSortedInfo("ZXB",systime , null);
					initialAllStocksPane(setStocks);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
					e.printStackTrace();
				}
			}
		});
		ZXBButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/MarketButton.css").toExternalForm());
		buttonHBox.getChildren().addAll(SZAButton,SZBButton,CYBButton,ZXBButton);
	}
	/*
	 * @description 浏览沪深300的监听
	 */
	@FXML
	protected void goBrowseHS300(ActionEvent e){
		systime = (LocalDate) dataController.get("SystemTime");
		buttonHBox.getChildren().clear();
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		try {
			setStocks = stockLogicInterface.getStockSetSortedInfo("HS300", systime, null);
			initialAllStocksPane(setStocks);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
			e1.printStackTrace();
		}
	}
	/*
	 * 初始化所有stocks信息
	 */
	private void initialAllStocksPane(List<SingleStockInfoVO> list){
		initialTenScroll(list);
		initialStocksFlowPane(list);
	}
	
	/*
	 * @description 初始化板块内排名前后十的界面
	 */
	private void initialTenScroll(List<SingleStockInfoVO> list){
		upTenFlowPane.getChildren().clear();
		downTenFlowPane.getChildren().clear();
		int length = list.size();
		for(int i =0;i<10;i++){
			SingleStockInfoVO vo = list.get(i);
			HBox hb = getHBox4TenScoll(vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu());
			upTenFlowPane.getChildren().add(hb);
		}
		for(int i=0;i<10;i++){
			SingleStockInfoVO vo = list.get(length-1-i);
			HBox hb = getHBox4TenScoll(vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu());
			downTenFlowPane.getChildren().add(hb);
		}
	}
	/*
	 * @description 获取前后十排名中单只股票信息的信息条
	 */
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
		MenuItem show = new MenuItem("细节");
		show.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				dataController.upDate("Market_StockNow", code);
			}
		});
		menu.getItems().addAll(copy,look,show);
		//menu的监听之后加
		
		//很冗余的代码，想不到解决的办法，因为HBox无法加入上下文菜单，用Button会是比HBox更好的选择，但是时间有限，有机会再改
		nameLabel.setContextMenu(menu);
		codeLabel.setContextMenu(menu);
		closeLabel.setContextMenu(menu);
		RAFLabel.setContextMenu(menu);	
		return root;
	}
	/*
	 * @description 用以判断数的正负性
	 */
	public Positive isPositive(Double num){
		if(num>0){
			return Positive.POSITIVE;
		}else if(num==0){
			return Positive.ZERO;
		}else{
			return Positive.NEGATIVE;
		}
	}
	/*
	 *@description 获得单只股票信息条中的Label组件,为getHBox方法调用
	 */
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
		System.out.println(list.size());
		int length = (int) Math.ceil(list.size()/50.0);
		System.out.println(length);
		initialPagination(length);
		if(length==1){
			int sublength = list.size();
			List<SingleStockInfoVO> sublist = list.subList(0, sublength-1);
			setStocksFlowPane(sublist,0);
		}else{
			List<SingleStockInfoVO> sublist = list.subList(0, 50);
			setStocksFlowPane(sublist,0);
		}
		pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub

				if(newValue.intValue()!=length-1){
					List<SingleStockInfoVO> sublist = list.subList(newValue.intValue()*50, newValue.intValue()*50+50);
					setStocksFlowPane(sublist,newValue.intValue());
				}else{
					List<SingleStockInfoVO> sublist = list.subList(newValue.intValue()*50, list.size()-1);
					setStocksFlowPane(sublist,newValue.intValue());
				}
			}
		});
		
		
	}
	/*
	 * @description 初始化显示股市所有股票信息的界面,为setSocksFlowPane调用
	 */
	private void setStocksFlowPane(List<SingleStockInfoVO> list, int vernier){
		stocksFlowPane.getChildren().clear();
		int length = list.size();
		for(int i=0;i<length;i++){
			SingleStockInfoVO vo = list.get(i);
			HBox hb = getHBox(i+1+vernier*50, vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu()
					, vo.getOpen(), vo.getHigh(), vo.getLow(),(int)vo.getVolume());
			stocksFlowPane.getChildren().add(hb);
		}
	}
	/*
	 * @description 获取股票信息条组件
	 */
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
		MenuItem show = new MenuItem("细节");
		show.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				dataController.upDate("Market_StockNow", code);
			}
		});
		menu.getItems().addAll(copy,look,show);
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

	/*
	 * @description 初始化股票信息表的表头
	 */
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
	/*
	 * @description 获取表头组件中的Label组件 为initialMenuAnchorPane调用
	 */
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
	/*
	 * @description 初始化分页控件
	 */
	private void initialPagination(int length){
		pagination = new Pagination();
		pagePane.getChildren().clear();
		pagePane.getChildren().add(pagination);
		pagination.setStyle("-fx-page-information-visible: false;");
		pagination.setPageCount(length);
	}
	/*
	 * @description 设置主界面的Controller
	 */
	public void setController(MainScreenController controller){
		this.controller = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		
//		upTenScrollPane.setStyle("-fx-background-color:transparent;");
//		downTenScrollPane.setStyle("-fx-background-color:transparent;");
		initialMenuAnchorPane();
//		Stub stub = new Stub();
//		List<SingleStockInfoVO> vo =stub.getStockSetSortedInfo();
//		int length =(int) Math.ceil(vo.size()/50);
//		Pagination pagination = new Pagination();
//		pagePane.getChildren().add(pagination);
//		pagination.setStyle("-fx-page-information-visible: false;");
//		pagination.setPageCount(length);
//		pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				// TODO Auto-generated method stub
//				List<SingleStockInfoVO> list = vo.subList(newValue.intValue()*50, newValue.intValue()*50+50);
//				initialStocksFlowPane(list);
//			}
//		});
	}

}
