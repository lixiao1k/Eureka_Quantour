package presentation.browseStrategyUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import vo.SaleVO;
import vo.StrategyConditionVO;

/**
 * 设置策略信息的界面
 */

public class StrategyInfoPaneController implements Initializable{
	@FXML
	Label strategyName1;
	
	@FXML
	Label strategyName2;
	
	@FXML
	Label createrName1;
	
	@FXML
	Label createrName2;
	
	@FXML
	Label type1;
	
	@FXML
	Label type2;
	
	@FXML
	Label stocksNum1;
	
	@FXML
	Label stocksNum2;
	
	@FXML
	Label holddays1;
	
	@FXML
	Label holddays2;
	
	@FXML
	Label price1;
	
	@FXML
	Label price2;
	
	@FXML
	Label changeable;
	
	public void setInfo(String creater,String strategyName,StrategyConditionVO conditionVO,SaleVO saleVO){
		createrName2.setText(creater);
		strategyName2.setText(strategyName);
		type2.setText(conditionVO.getName());
		stocksNum2.setText(Integer.toString(conditionVO.getNums()));
		holddays2.setText(Integer.toString(saleVO.getTiaocangqi()));
		price2.setText(saleVO.getTiaocangjiage());
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		strategyName1.setStyle(
				"-fx-font-size: 12px;");
		strategyName2.setStyle(
				"-fx-font-size: 12px;");
		createrName1.setStyle(
				"-fx-font-size: 12px;");
		createrName2.setStyle(
				"-fx-font-size: 12px;");
		type1.setStyle(
				"-fx-font-size: 12px;");
		type2.setStyle(
				"-fx-font-size: 12px;");
		stocksNum1.setStyle(
				"-fx-font-size: 12px;");
		stocksNum2.setStyle(
				"-fx-font-size: 12px;");
		holddays1.setStyle(
				"-fx-font-size: 12px;");
		holddays2.setStyle(
				"-fx-font-size: 12px;");
		price1.setStyle(
				"-fx-font-size: 12px;");
		price2.setStyle(
				"-fx-font-size: 12px;");
		changeable.setStyle(
				"-fx-font-size: 12px;");

	}

}
