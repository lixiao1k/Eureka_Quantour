package presentation.strategyUI;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import logic.service.StockLogicInterface;
import rmi.RemoteHelper;
import vo.SaleVO;
import vo.StrategyConditionVO;
import vo.StrategyVO;

public class StrategyPopUpController implements Initializable{

	@FXML
	TextField name;
	
	@FXML
	RadioButton publicRB;
	
	@FXML
	RadioButton privateRB;
	
	StrategyUIController controller;
	
	@FXML
	ToggleGroup isPublic;
	
	DataContorller dataController;
	
	@FXML
	protected void save(ActionEvent e){
		StrategyConditionVO strategyConditionVO = controller.getStrategyConditionVO();
		SaleVO saleVO = controller.getSaleVO();
		String nameStr = name.getText();
		Boolean ispublic;
		if(nameStr.length()>0){
			if(isPublic.getSelectedToggle().getUserData().equals("公开")){
				ispublic=true;
			}else{
				ispublic=false;
			}
			StrategyVO strategyVO = new StrategyVO(nameStr, strategyConditionVO, saleVO, ispublic);
			RemoteHelper remoteHelper = RemoteHelper.getInstance();
			StockLogicInterface stockLogicInterface = remoteHelper.getStockLogic();
			try {
				stockLogicInterface.saveStragety(strategyVO, (String)dataController.get("UserName"));
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Stage root =(Stage) name.getScene().getWindow();
			root.close();
		}else{
			Notifications.create().title("错误").text("策略名不能为空").showError();
		}

	}
	
	@FXML
	protected void cancel(ActionEvent e){
		Stage root =(Stage) name.getScene().getWindow();
		root.close();
	}
	public void setController(StrategyUIController controller){
		this.controller=controller;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		publicRB.setUserData("公开");
		privateRB.setUserData("私有");
		privateRB.setSelected(true);
		
		dataController = DataContorller.getInstance();
	}

}
