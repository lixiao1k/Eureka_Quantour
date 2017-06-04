package presentation.singleStockUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import vo.PredictVO;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Created by shelton on 2017/6/3.
 */
public class PredictController implements Initializable {
    @FXML
    Label predict;

    @FXML
    Label price1;

    @FXML
    Label price2;

    @FXML
    Label dof1;

    @FXML
    Label dof2;

    public void set(PredictVO vo){
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        price2.setText(decimalFormat.format(vo.getPredictPrice()));
        dof2.setText(decimalFormat.format(vo.getPredictROD()*100)+"%");
        if(vo.getPredictPrice()>0){
            price2.setStyle("-fx-text-fill:#FF0000;"
                    +"-fx-font-size: 14px;");
            dof2.setStyle("-fx-text-fill:#FF0000;"
                    +"-fx-font-size: 14px;");

        }else{
            price2.setStyle("-fx-text-fill:#00EE00;"
                    +"-fx-font-size: 14px;");
            dof2.setStyle("-fx-text-fill:#00EE00;"
                    +"-fx-font-size: 14px;");
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        predict.setStyle("-fx-text-fill:#E0FFFF;"
                +"-fx-font-size: 20px;");
        price1.setStyle("-fx-text-fill:#E0FFFF;"
                +"-fx-font-size: 14px;");
        dof1.setStyle("-fx-text-fill:#E0FFFF;"
                +"-fx-font-size: 14px;");

    }
}
