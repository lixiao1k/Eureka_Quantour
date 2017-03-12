package presentation.chart.barChart;

/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
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

/**
 *
 * An advanced bar chart with a variety of controls.
 *
 * @see javafx.scene.chart.BarChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.NumberAxis
 * @see javafx.scene.chart.XYChart
 */
public class ExtremeValueComparedChart implements Initializable {

//    private void init(Stage primaryStage) {
//        Group root = new Group();
//        primaryStage.setScene(new Scene(root));
//        root.getChildren().add(createChart("A","B",50,60,30,40));
//    }

    public BarChart<String, Number> createChart(String stockA,String stockB,int highA,int highB,int lowA,int lowB) {
        final String[] years = {"最高值", "最低值"};
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        // setup chart
        bc.setTitle("2017.11.19~20.17.11.20");
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
