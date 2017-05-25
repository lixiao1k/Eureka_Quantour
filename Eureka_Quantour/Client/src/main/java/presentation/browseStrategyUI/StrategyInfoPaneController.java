package presentation.browseStrategyUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import vo.SaleVO;
import vo.StrategyConditionVO;

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
		
	}

}
