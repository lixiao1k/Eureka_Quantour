package presentation.browseStrategyUI;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.text.DecimalFormat;

import dataController.DataContorller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.service.StockLogicInterface;
import org.controlsfx.control.Notifications;
import presentation.chart.chartService;
import presentation.chart.lineChart.YieldComparedChart;
import presentation.mainScreen.MainScreenController;
import presentation.marketUI.MarketAreaController;
import rmi.RemoteHelper;
import vo.CommentVO;
import vo.StrategyListVO;
import vo.StrategyShowVO;
import vo.StrategyVO;

public class BrowseStrategyController implements Initializable{

	@FXML
	FlowPane strategyFlowPane;
	
	DataContorller dataController;
	@FXML
	FlowPane judgeFlowPane;
	
	@FXML
	AnchorPane anchorPane1;
	
	@FXML
	AnchorPane lineinfoPane;
	
	@FXML
	AnchorPane strategyInfoPane;
	
	@FXML
	TextArea judgeTextArea;

	@FXML
	Button useStrategyButton;

	private  MainScreenController controller;

	@FXML
	protected void useStrategy(ActionEvent e) throws IOException {
		setAndUseStrategy();
	}

	private void setAndUseStrategy() throws IOException {
		controller.setBrowseStrategyUI(true);

	}

	@FXML
	protected void browseMine(ActionEvent e){
		setBrowseMine();
	}

	private void setBrowseMine(){
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		try {
			List<StrategyListVO> list = stockLogicInterface.getStrategyList((String)dataController.get("UserName"));
			setFlowPane(list);
			if(list!=null&&list.size()!=0){
				setJudge(list.get(0));
				setLine(list.get(0));
				dataController.upDate("CreaterName",list.get(0).getCreaterName());
				dataController.upDate("StrategyName",list.get(0).getStrategyName());
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@FXML
	protected void browseAll(ActionEvent e){
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		try {
			List<StrategyListVO> list = stockLogicInterface.getStrategyList();
			setFlowPane(list);
			if(list!=null&&list.size()!=0){
				setJudge(list.get(0));
				setLine(list.get(0));
				dataController.upDate("CreaterName",list.get(0).getCreaterName());
				dataController.upDate("StrategyName",list.get(0).getStrategyName());
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}


	}
	
	@FXML
	protected void judge(ActionEvent e){
		String comment = judgeTextArea.getText();
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		if(comment.length()<=10){
			Notifications.create().title("错误").text("评论字数果断，需大于10个字").showWarning();
		}else if(comment.length()>140){
			Notifications.create().title("错误").text("评论字数超出140字").showWarning();
		}else{
			try {
				stockLogicInterface.comment((String)dataController.get("CreaterName"),(String)dataController.get("StrategyName"),
						(String)dataController.get("UserName"), LocalDateTime.now(),comment);

			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			StrategyShowVO strategyShowVO = null;
			try {

				strategyShowVO = stockLogicInterface.getStrategy((String) dataController.get("CreaterName"),
                        (String)dataController.get("StrategyName"));
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			set4setJudge(strategyShowVO);
			judgeTextArea.clear();
		}
	}
	private void setFlowPane(List<StrategyListVO> list){
		strategyFlowPane.getChildren().clear();
		if(list.size()!=0&&list!=null){
			for(StrategyListVO vo:list){
				strategyFlowPane.getChildren().add(getHBox(vo));
			}
		}
	}
	
	private HBox getHBox(StrategyListVO vo){
		HBox hb = new HBox();
		hb.setPrefSize(245, 30);
		Label name = getLabel(70, Pos.CENTER_LEFT, "  "+vo.getStrategyName());
		Label creater = getLabel(80, Pos.CENTER, vo.getCreaterName());
		Button show = new Button();
		show.setPrefSize(20, 15);
		Image showImage = new Image(getClass().getResourceAsStream("show.png"));
		show.setGraphic(new ImageView(showImage));
		show.getStylesheets().add(getClass().getClassLoader().getResource("styles/buttonFile.css").toExternalForm());
		show.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				dataController.upDate("CreaterName", vo.getCreaterName());
				dataController.upDate("StrategyName", vo.getStrategyName());
				setJudge(vo);
				setLine(vo);
			}
		});
		DecimalFormat df = new DecimalFormat("0.00");	
		Label yearreturn = getLabel(85, Pos.CENTER_LEFT, df.format(vo.getStrategyYearReturn()*100)+"%");
		hb.getChildren().addAll(name,creater,yearreturn,show);
		return hb;
	}
	private Label getLabel(double width, Pos alignment,String text){
		Label label = new Label(text);
		label.setPrefSize(width, 30);
		label.setMaxWidth(width);
		label.setAlignment(alignment);
		label.setStyle("-fx-border-width: 1;"
				+ "-fx-background-color:#6d8187;"
				+"-fx-font-size: 14px;"
				+ "-fx-text-fill:ivory;"
				+ "-fx-font-weight:bold");
		return label;
	}
	private void setJudge(StrategyListVO vo){
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		StrategyShowVO strategyShowVO = null;
		try {
			strategyShowVO = stockLogicInterface.getStrategy(vo.getCreaterName(), vo.getStrategyName());
			dataController.upDate("StrategyShowVO",strategyShowVO);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		set4setJudge(strategyShowVO);
	}

	private void set4setJudge(StrategyShowVO strategyShowVO){
		List<CommentVO> commentVOs = strategyShowVO.getComments();
		judgeFlowPane.getChildren().clear();
		for(CommentVO vo1:commentVOs){
			System.out.println(vo1.getComment());
			VBox vb = getCommentVBox(vo1);
			judgeFlowPane.getChildren().add(vb);
		}
	}
	
	private VBox getCommentVBox(CommentVO vo){
		VBox vb = new VBox();
		vb.setPrefSize(300, 100);
		HBox hb = new HBox();
		hb.setPrefSize(300, 50);
		hb.setSpacing(10);
		Text name = new Text();
		name.setText(vo.getCommenterName());
		name.setStyle(
				"-fx-font-size: 18px;"
						+ "-fx-fill:#ffff00"
						);
		Text date = new Text();
		date.setText(vo.getCommentTime().toString());
		date.setStyle(
				"-fx-font-size:10px;"
						+ "-fx-fill:  linear-gradient(cyan , dodgerblue);"
						+ "-fx-font-smoothing-type: lcd;");
		
		Text commentstr = new Text();
		commentstr.setText(vo.getComment());
		commentstr.setWrappingWidth(300);
		commentstr.setStyle(
				"-fx-font-size: 14px;"
				+ "-fx-fill: rgb(25,25,112);"
						+ "-fx-font-smoothing-type: lcd;");
		hb.getChildren().addAll(name,date);
		vb.getChildren().addAll(hb,commentstr);
		vb.getStylesheets().add(getClass().getClassLoader().getResource("styles/HBox.css").toExternalForm());
		vb.getStyleClass().add("hbox");
		return vb;
	}
	
	private void setLine(StrategyListVO vo){
		String createrName = vo.getCreaterName();
		String strategyName = vo.getStrategyName();
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		StrategyShowVO strategyShowVO = null;
		try {
			strategyShowVO = stockLogicInterface.getStrategy(createrName, strategyName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(strategyShowVO!=null){
			chartService cService = new YieldComparedChart(strategyShowVO.getTimeList(),
					strategyShowVO.getBasicReturn(), strategyShowVO.getStrategyReturn());
			Pane pane = cService.getchart(630, 180, true);
			anchorPane1.getChildren().clear();
			anchorPane1.getChildren().add(pane);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("LineInfo.fxml"));
			Parent lineInfo;
			try {
				lineInfo = (AnchorPane)loader.load();
				LineInfoPaneController controller = loader.getController();
				controller.setInfo(strategyShowVO);
				lineinfoPane.getChildren().clear();
				lineinfoPane.getChildren().add(lineInfo);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(getClass().getResource("StrategyInfo.fxml"));
			Parent strategyInfo;
			try {
				strategyInfo = (AnchorPane)loader1.load();
				strategyInfoPane.getChildren().clear();
				strategyInfoPane.getChildren().add(strategyInfo);
				StrategyInfoPaneController controller = loader1.getController();
				controller.setInfo(createrName,strategyName,strategyShowVO.getStrategyConditionVO(),strategyShowVO.getSaleVO());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void setController(MainScreenController controller){
		this.controller = controller;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		setBrowseMine();
		Image index = new Image(getClass().getResourceAsStream("index.png"));
		useStrategyButton.setGraphic(new ImageView(index));
		useStrategyButton.getStylesheets().add(getClass().getClassLoader().getResource("styles/ButtonFile1.css").toExternalForm());
	}

}
