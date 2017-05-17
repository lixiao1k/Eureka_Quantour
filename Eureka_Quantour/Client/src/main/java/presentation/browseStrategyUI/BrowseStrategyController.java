package presentation.browseStrategyUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

import dataController.DataContorller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import logic.service.StockLogicInterface;
import rmi.RemoteHelper;
import vo.StrategyListVO;

public class BrowseStrategyController implements Initializable{

	@FXML
	FlowPane strategyFlowPane;
	
	DataContorller dataController;
	
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
		hb.setPrefSize(255, 30);
		Label name = getLabel(100, Pos.CENTER_LEFT, vo.getStrategyName());
		Label creater = getLabel(100, Pos.CENTER_LEFT, vo.getCreaterName());
		Label yearreturn = getLabel(55, Pos.CENTER_RIGHT, Double.toString(vo.getStrategyYearReturn()*100)+"%");
		hb.getChildren().addAll(name,creater,yearreturn);
		return hb;
	}
	private Label getLabel(double width, Pos alignment,String text){
		Label label = new Label(text);
		label.setPrefSize(width, 30);
		label.setMaxWidth(width);
		label.setAlignment(alignment);
		label.setStyle("-fx-border-width: 1;"
				+ "-fx-border-color: black;"
				+ "-fx-background-color:rgba(255,255,255,0.1);"
				+ "-fx-text-fill: rgb(255, 255, 255, 0.9);"
				+ "-fx-font-weight:bold");
		return label;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
	}

}
