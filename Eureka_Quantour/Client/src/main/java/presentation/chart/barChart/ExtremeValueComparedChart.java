package presentation.chart.barChart;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ExtremeValueComparedChart implements Initializable {

    public BarChart<String, Number> createChart(String stockA,String stockB,double highA,double highB,double lowA,double lowB) {
        final String[] years = {"最高值", "最低值"};
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        // setup chart
        String title = stockA+" VS "+stockB;
        bc.setTitle(title);
        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(years)));
        XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
        series1.setName(stockA);
        XYChart.Series<String,Number> series2 = new XYChart.Series<String,Number>();
        series2.setName(stockB);
        series1.getData().add(new XYChart.Data<String,Number>(years[0], highA));
        series1.getData().add(new XYChart.Data<String,Number>(years[1], lowA));
        series2.getData().add(new XYChart.Data<String,Number>(years[0], highB));
        series2.getData().add(new XYChart.Data<String,Number>(years[1], lowB));  

        bc.getData().add(series1);
        bc.getData().add(series2);
        for(XYChart.Series<String, Number> s:bc.getData()){
        	for(XYChart.Data<String, Number> data:s.getData()){
        	    StackPane bar = (StackPane)data.getNode();
        	    final Text dataText = new Text(data.getYValue()+"");
        	    bar.getChildren().add(dataText);
                dataText.relocate(bar.getBoundsInParent().getMinX(), bar.getBoundsInParent().getMinY() - 30);
        	}
        }        
        bc.setCategoryGap(50);
        bc.getStylesheets().add(getClass().getResource("/styles/ComparedChart.css").toExternalForm());
        bc.setPrefSize(277, 238);
        return bc;
    }

@Override
public void initialize(URL location, ResourceBundle resources) {
	// TODO Auto-generated method stub
	
}

//    @Override public void start(Stage primaryStage) throws Exception {
//        init(primaryStage);
//        primaryStage.show();
//    }
//    public static void main(String[] args) { launch(args); }
}
