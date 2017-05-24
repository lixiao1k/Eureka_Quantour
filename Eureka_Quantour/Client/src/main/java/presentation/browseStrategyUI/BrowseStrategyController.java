package presentation.browseStrategyUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.text.DecimalFormat;

import dataController.DataContorller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.service.StockLogicInterface;
import rmi.RemoteHelper;
import vo.CommentVO;
import vo.StrategyListVO;
import vo.StrategyShowVO;

public class BrowseStrategyController implements Initializable{

	@FXML
	FlowPane strategyFlowPane;
	
	DataContorller dataController;
	@FXML
	FlowPane judgeFlowPane;
	
	@FXML
	AnchorPane anchorPane1;
	
	@FXML
	protected void browseMine(ActionEvent e){
		RemoteHelper remoteHelper = RemoteHelper.getInstance();
		StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
		try {
			List<StrategyListVO> list = stockLogicInterface.getStrategyList((String)dataController.get("UserName"));
			System.out.println((String)dataController.get("UserName"));
			System.out.println(list);
			setFlowPane(list);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@FXML
	protected void browseAll(ActionEvent e){
		
	}
	
	@FXML
	protected void judge(ActionEvent e){
		
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
		Label yearreturn = getLabel(85, Pos.CENTER_RIGHT, Double.toString(vo.getStrategyYearReturn()*100)+"%");
		Button show = new Button();
		show.setPrefSize(20, 15);
		Image showImage = new Image(getClass().getResourceAsStream("show.png"));
		show.setGraphic(new ImageView(showImage));
		show.getStylesheets().add(getClass().getClassLoader().getResource("styles/buttonFile.css").toExternalForm());
		show.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				setJudge(vo);
			}
		});
		hb.getChildren().addAll(name,creater,yearreturn,show);

		DecimalFormat df = new DecimalFormat("0.00");
		
		Label yearreturn = getLabel(85, Pos.CENTER_RIGHT, df.format(vo.getStrategyYearReturn()*100)+"%");
		hb.getChildren().addAll(name,creater,yearreturn);
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
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<CommentVO> commentVOs = strategyShowVO.getComments();
		for(CommentVO vo1:commentVOs){
			System.out.println("sda");
			System.out.println(vo1.getComment());
			VBox vb = getCommentVBox(vo1);
			judgeFlowPane.getChildren().add(vb);
		}
		
	}
	
	private VBox getCommentVBox(CommentVO vo){
		VBox vb = new VBox();
		vb.setPrefSize(300, 130);
		HBox hb = new HBox();
		hb.setPrefSize(300, 50);
		hb.setSpacing(10);
		Text name = new Text();
		name.setText(vo.getCommenterName());
		name.setStyle(
				"-fx-font-size: 18px;"
						+ "-fx-fill:  linear-gradient(cyan , dodgerblue);"
						+ "-fx-font-smoothing-type: lcd;");
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
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
	}

}
