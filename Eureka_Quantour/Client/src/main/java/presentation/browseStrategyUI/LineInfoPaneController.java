package presentation.browseStrategyUI;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import vo.StrategyShowVO;

public class LineInfoPaneController implements Initializable{
	@FXML
	Label strYear1;
	
	@FXML
	Label strYear2;
	
	@FXML
	Label baseYear1;
	
	@FXML
    Label baseYear2;
	
	@FXML
	Label alpha1;
	
	@FXML
	Label alpha2;
	
	@FXML
	Label beta1;
	
	@FXML
	Label beta2;
	
	@FXML
	Label xiapu1;
	
	@FXML
	Label xiapu2;
	
	@FXML
	Label huice1;
	
	@FXML
	Label huice2;
	
	public void setInfo(StrategyShowVO vo){
		DecimalFormat df = new DecimalFormat("#0.00");
		strYear2.setText(df.format(vo.getStrategyYearReturn()*100)+"%");
        baseYear2.setText(df.format(vo.getJizhunYearReturn()));
		alpha2.setText(df.format(vo.getAlpha()));
		beta2.setText(df.format(vo.getBeta()));
		xiapu2.setText(df.format(vo.getSharp()));
		huice2.setText(df.format(vo.getZuidahuiche()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		strYear1.setStyle("-fx-text-fill:#33ffcc;"
				+"-fx-font-size: 12px;");
		strYear2.setStyle("-fx-text-fill:#33ffcc;"
				+"-fx-font-size: 12px;");
		strYear2.setAlignment(Pos.CENTER);
		baseYear1.setStyle("-fx-text-fill:#ff0033;"
				+"-fx-font-size: 12px;");
		baseYear2.setStyle("-fx-text-fill:#ff0033;"
				+"-fx-font-size: 12px;");
		baseYear2.setAlignment(Pos.CENTER);
		alpha1.setStyle("-fx-text-fill:#ffff00;"
				+"-fx-font-size: 12px;");
		alpha2.setStyle("-fx-text-fill:#ffff00;"
				+"-fx-font-size: 12px;");
		alpha2.setAlignment(Pos.CENTER);
		beta1.setStyle("-fx-text-fill:#ccffff;"
				+"-fx-font-size: 12px;");
		beta2.setStyle("-fx-text-fill:#ccffff;"
				+"-fx-font-size: 12px;");
		beta2.setAlignment(Pos.CENTER);
		xiapu1.setStyle("-fx-text-fill:#ccffff;"
				+"-fx-font-size: 12px;");
		xiapu2.setStyle("-fx-text-fill:#ccffff;"
				+"-fx-font-size: 12px;");
		xiapu2.setAlignment(Pos.CENTER);
		huice1.setStyle("-fx-text-fill:#ccffff;"
				+"-fx-font-size: 12px;");
		huice2.setStyle("-fx-text-fill:#ccffff;"
				+"-fx-font-size: 12px;");
		huice2.setAlignment(Pos.CENTER);
	}

}
