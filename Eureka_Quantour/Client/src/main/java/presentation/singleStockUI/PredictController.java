package presentation.singleStockUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import vo.CompanyInfoVO;
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

    @FXML
    Label caikuang;

    @FXML
    Label stockcode1;

    @FXML
    Label stockcode2;

    @FXML
    Label date1;

    @FXML
    Label date2;

    @FXML
    Label shouyi1;

    @FXML
    Label shouyi2;

    @FXML
    Label jingzichan1;

    @FXML
    Label jingzichan2;

    @FXML
    Label guben1;

    @FXML
    Label guben2;

    @FXML
    Label liutong1;

    @FXML
    Label liutong2;

    @FXML
    Label shiyinglv1;

    @FXML
    Label shiyinglv2;

    @FXML
    Label shijinlv1;

    @FXML
    Label shijinlv2;

    @FXML
    Label huanshoulv1;

    @FXML
    Label huanshoulv2;
//设置预测信息和公司财况
    public void set(PredictVO vo, CompanyInfoVO vo1){
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        price2.setText(decimalFormat.format(vo.getPredictPrice()));
        dof2.setText(decimalFormat.format(vo.getPredictROD()*100)+"%");
        if(vo.getPredictROD()>0){
            price2.setStyle("-fx-text-fill:#FF0000;"
                    +"-fx-font-size: 18px;");
            dof2.setStyle("-fx-text-fill:#FF0000;"
                    +"-fx-font-size: 18px;");

        }else{
            price2.setStyle("-fx-text-fill:#00EE00;"
                    +"-fx-font-size: 18px;");
            dof2.setStyle("-fx-text-fill:#00EE00;"
                    +"-fx-font-size: 18px;");
        }
        stockcode2.setText(vo1.getCode());
        date2.setText(vo1.getDate().toString());
        shouyi2.setText(decimalFormat.format(vo1.getBasicIncome()));
        jingzichan2.setText(decimalFormat.format(vo1.getNetAsset()));
        guben2.setText(Long.toString(vo1.getTotalCapitalization()));
        liutong2.setText(Long.toString(vo1.getFluCapitalization()));
        shiyinglv2.setText(decimalFormat.format(vo1.getShiYing()));
        shijinlv2.setText(decimalFormat.format(vo1.getShiJing()));
        huanshoulv2.setText(decimalFormat.format(vo1.getHuanShou())+"%");
    }
    //为标签设样式
    private void setStyle(Label label){
        label.setStyle("-fx-text-fill:#E0FFFF;"
                +"-fx-font-size: 13px;");
    }
    //为标签内容设样式
    private void setStyle1(Label label,String color){
        label.setStyle("-fx-text-fill:"+color+";"+"-fx-font-size: 13px;");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        predict.setStyle("-fx-text-fill:#E0FFFF;"
                +"-fx-font-size: 18px;");
        caikuang.setStyle("-fx-text-fill:#E0FFFF;"
                +"-fx-font-size: 18px;");
        price1.setStyle("-fx-text-fill:#E0FFFF;"
                +"-fx-font-size: 18px;");
        dof1.setStyle("-fx-text-fill:#E0FFFF;"
                +"-fx-font-size: 18px;");
        setStyle(stockcode1);
        setStyle(date1);
        setStyle(jingzichan1);
        setStyle(shouyi1);
        setStyle(guben1);
        setStyle(liutong1);
        setStyle(shiyinglv1);
        setStyle(shijinlv1);
        setStyle(huanshoulv1);
        setStyle1(stockcode2,"98F5FF");
        setStyle1(date2,"98F5FF");
        setStyle1(shouyi2,"98F5FF");
        setStyle1(jingzichan2,"98F5FF");
        setStyle1(guben2,"98F5FF");
        setStyle1(liutong2,"98F5FF");
        setStyle1(shiyinglv2,"98F5FF");
        setStyle1(shijinlv2,"98F5FF");
        setStyle1(huanshoulv2,"98F5FF");

    }
}
