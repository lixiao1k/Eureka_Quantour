package presentation.stockSetUI;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.sound.midi.Sequence;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import en_um.Positive;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.service.StockLogicInterface;
import logic.service.Stub;
import presentation.chart.klineChart.KLineChart;
import presentation.mainScreen.MainScreenController;
import rmi.RemoteHelper;
import vo.SingleStockInfoVO;

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
	AnchorPane basicInfoAnchorPane;
	
	@FXML
	AnchorPane kChartAnchorPane;
	
	@FXML
	AnchorPane emaChartAnchorPane;
	
	@FXML
	AnchorPane menuAnchorPane;
	
	@FXML
	Label codeLabel;
	
	@FXML
	Label nameLabel;
	
	@FXML
	Label closeLabel;
	
	@FXML
	Label RAFLabel;
	
	@FXML
	Label highLabel;
	
	@FXML
	Label openLabel;
	
	@FXML
	Label lowLabel;
	
	@FXML
	Label volumeLabel;
	
	private DataContorller dataController;
	
	//关联与弹窗之间的controller,以便查看股票详细信息时跳转至个股界面	
	private MainScreenController controller;
	
/*
 * @description 新建股池时跳出相应界面
 */
	@FXML
	protected void creatStockSet(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("StockSetPopup.fxml"));
		Parent popup = (AnchorPane)loader.load();
		StockSetPopupController controller = loader.getController();
		controller.setUpController(this);
		Scene scene = new Scene(popup);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}
	
	/*
	 * @description 初始化股池界面
	 */
	private void addSet(List<String> list){
		if(list!=null){
			for(String item:list){
	            creatSet(item);
			}
		}
	
	}
	
	//用户新建股池,给StockSetPopupController的供接口
	public void creatSet(String name){
		Button button = new Button(name);
        button.setPrefWidth(244);
        button.setMinWidth(244);
        button.getStylesheets().add(getClass().getClassLoader().getResource("styles/StockSetButton.css").toExternalForm());
        button.getProperties().put("NAME", button.getText());
        button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				String stockSetName = (String) button.getProperties().get("NAME");
				System.out.println(stockSetName);
				dataController.upDate("StockSetNow",stockSetName);
				//....
			}
		});
        stockSetFlowPane.getChildren().add(button);
        //加入删除的上下文菜单
        final ContextMenu menu = new ContextMenu();
        MenuItem item = new MenuItem("删除");
        menu.getItems().add(item);
        button.setContextMenu(menu);
        item.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
			    ObservableList<Node> list = stockSetFlowPane.getChildren();
			    for(Node button1:list){
			    	String name = (String) button1.getProperties().get("NAME");
			    	if(name!=null&&name==button.getText()){
			    		list.remove(button1);
			    		break;
			    	}
			    }
			    RemoteHelper remote = RemoteHelper.getInstance();
			    StockLogicInterface stockLogicInterface = remote.getStockLogic();
			    try {
					stockLogicInterface.deleteStockSet(button.getText(), (String)dataController.get("UserName"));
					Notifications.create().title("成功").text(button.getText()+"--股池删除成功！").showInformation();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Notifications.create().title("网络连接异常").text(e.toString()).showWarning();
					e.printStackTrace();
				}
			}
		});
        
        //之后加入更新保存数据
	}
	/*
	 * @description 显示股票池中的所有股票信息
	 */
	private void setStockSetSortedInfo(List<SingleStockInfoVO> list){
		int length = list.size();
		for(int i=0;i<length;i++){
			SingleStockInfoVO vo = list.get(i);
			HBox hb = getHBox(i+1, vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu()
					, vo.getOpen(), vo.getHigh(), vo.getLow(),(int)vo.getVolume());
			stocksFlowPane.getChildren().add(hb);
		}
	}
	/*
	 * @description setStockSetSortedInfo的供方法，获取股票信息条组件
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
		
		/*
		 * 单击股票时的效果，包括股票栏变色，以及显示详细信息
		 */
		root.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
			root.setStyle("-fx-background-color:rgba(0,0,255,0.2)");//鼠标点击单条信息的效果
		});
		
		ContextMenu menu = new ContextMenu();
		MenuItem delete = new MenuItem("移除");
		MenuItem show = new MenuItem("细节");
		show.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				dataController.upDate("StockNow", code);
				setDetailInfo();
				
			}
		});
		
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
		look.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
                try {
                	dataController.upDate("SingleStockNow", code);
					controller.setSingleStockUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		menu.getItems().addAll(delete,copy,look,show);
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
	 *@description method for the eventHandler in stockInfo to show the chart
	 *
	 */
	public void showDetailInfo(){
		/*
		 * 测试代码K线图
		 */
//	    KLineChart klineChart;
//	    List<SingleStockInfoVO> stocklist = new ArrayList<>();
//		SingleStockInfoVO ssi = new SingleStockInfoVO();
//    	Calendar cal = Calendar.getInstance();
//    	int j = 2;
//    	for(int i=0; i<20; i++, j++){
//    		ssi = new SingleStockInfoVO();
//    		cal.set(2014, 3, j);
//    		ssi.setDate( (Calendar)cal.clone() );
//    		double d = Math.random();
//    		if( d>0.5 ){
//    			ssi.setOpen(d*7);
//    			ssi.setClose(d*9);
//    			ssi.setHigh(d*10);
//    			ssi.setLow(d*6);
//    			ssi.setVolume((long)(d*8000));
//    		}
//    		else{
//    			ssi.setOpen(d*9);
//    			ssi.setClose(d*7);
//    			ssi.setHigh(d*11);
//    			ssi.setLow(d*5.5);
//    			ssi.setVolume((long)(d*11000));
//    		}
//    		stocklist.add(ssi);
//    	}
//    	klineChart = new KLineChart(stocklist);
//		Pane klinePane = klineChart.getchart(344,200);
//		kChartAnchorPane.getChildren().clear();
//		kChartAnchorPane.getChildren().add(klinePane);
//		
//		
	}
	/*
	 * @description 判断正负性
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
	 * @description 获取股票信息条中的组件Label 为getHBox方法所调用
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
	/*
	 * @description初始化股票列表的表头
	 */
	private void initialMenuAnchorPane(){
		HBox hb = new HBox();
		hb.setPrefSize(645, 25);
		Label numLabel = getLabel4initialMenu(50, Pos.CENTER, "序号");
		Label codeLabel = getLabel4initialMenu(75, Pos.CENTER_RIGHT,"代码");
		Label nameLabel = getLabel4initialMenu(85, Pos.CENTER_LEFT, "名称");
		Label closeLabel = getLabel4initialMenu(65, Pos.CENTER_RIGHT,"最新价");
		Label RAFLabel = getLabel4initialMenu(75, Pos.CENTER_RIGHT, "涨跌幅");
		Label openLabel = getLabel4initialMenu(65, Pos.CENTER_RIGHT, "今开");
		Label highLabel = getLabel4initialMenu(65, Pos.CENTER_RIGHT, "最高");
		Label lowLabel = getLabel4initialMenu(65, Pos.CENTER_RIGHT, "最低");
		Label volumeLabel = getLabel4initialMenu(100, Pos.CENTER_RIGHT, "成交量");
		hb.getChildren().addAll(numLabel,codeLabel,nameLabel,closeLabel,RAFLabel,openLabel,highLabel,lowLabel,volumeLabel);
		menuAnchorPane.getChildren().add(hb);
	}
	/*
	 * @description提供表头组件的Label组件，为initialMenuAnchorPane所调用
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
	 * @description 设置界面右侧的股票详细信息
	 */
   private void setDetailInfo(){
	   String code = (String) dataController.get("StockNow");
	   //...
   }
	/*
	 * @description 设置主界面的Controller
	 */
	public void setController(MainScreenController controller){
		this.controller = controller;
	}
	/*
	 * @description初始化股票基本信息界面
	 */
	private void setStockInfoPane(String code,String name,double close,double RAF,double high
			,double low,double open,long volume){
		codeLabel.setText(code);
		codeLabel.setStyle("-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold; -fx-font-size: 18;");
		nameLabel.setText(name);
		nameLabel.setStyle("-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold; -fx-font-size: 18;");
		closeLabel.setText(Double.toString(close));
		addLabelColor(closeLabel, close,28);
		if(RAF>0){
			RAFLabel.setText("+"+Double.toString(RAF)+"%");
		}else if(RAF<0){
			RAFLabel.setText("-"+Double.toString(RAF)+"%");
		}else{
			RAFLabel.setText(Double.toString(RAF)+"%");
		}
	    addLabelColor(RAFLabel, RAF,0);
	    highLabel.setText(Double.toString(high));
	    addLabelColor(highLabel, high,0);
	    lowLabel.setText(Double.toString(low));
	    addLabelColor(lowLabel, -1,0);
	    openLabel.setText(Double.toString(open));
	    addLabelColor(openLabel, open,0);
	    volumeLabel.setText(Long.toString(volume));		
	}
	/*
	 * @description设置字体颜色，为setStockInfoPane方法所调用
	 */
	public void addLabelColor(Label label,double num,int size){
		if(size==0){
			if(num>0){
				label.setStyle("-fx-text-fill: rgb(255, 0, 0, 1);-fx-font-weight:bold");
			}else if(num<0){
				label.setStyle("-fx-text-fill: rgb(0, 255, 0, 1);-fx-font-weight:bold");
			}else{
				label.setStyle("-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold");
			}
		}else{
			if(num>0){
				label.setStyle("-fx-text-fill: rgb(255, 0, 0, 1);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}else if(num<0){
				label.setStyle("-fx-text-fill: rgb(0, 255, 0, 1);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}else{
				label.setStyle("-fx-text-fill: rgb(255, 255, 255, 1);-fx-font-weight:bold"
						+ ";-fx-font-size:28");
			}
		}

	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
//		javafx8之后这条语句单独使用没有效果，需要在css上加上
//		.scroll-pane > .viewport {
//		   -fx-background-color: transparent;
//	    }
		stockSetScrollPane.setStyle("-fx-background-color:transparent;");
		stocksScrollPane.setStyle("-fx-background-color:transparent;");
		Stub stub = new Stub();
		String username = (String)dataController.get("UserName");
		RemoteHelper remote = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remote.getStockLogic();
		try {
			List<String> stocksets = stockLogicInterface.getStockSet(username);
			addSet(stocksets);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Notifications.create().title("网络连接提示").text(e.toString()).showWarning();
			e.printStackTrace();
		}
		setStockSetSortedInfo(stub.getStockSetSortedInfo());
		initialMenuAnchorPane();
		SingleStockInfoVO vo = stub.getStockSetSortedInfo().get(0);
		System.out.println(vo.getCode());
		setStockInfoPane(vo.getCode(), vo.getName(), vo.getClose(), vo.getFudu(), vo.getHigh()
				, vo.getLow(), vo.getOpen(), vo.getVolume());
//		showDetailInfo();

	}

}
