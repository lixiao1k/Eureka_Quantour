package presentation.chart.lineChart;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Main extends Application {

  public Parent createContent() {
    NumberAxis xAxis = new NumberAxis("X-Axis", 0d, 8.0d, 1.0d);
    NumberAxis yAxis = new NumberAxis("Y-Axis", 0.0d, 5.0d, 1.0d);
    ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList(
        new LineChart.Series<>("Series 1", FXCollections.<ScatterChart.Data<Number, Number>>observableArrayList(
            new XYChart.Data<>(0.2, 3.5),
            new XYChart.Data<>(4.0, 2.2),
            new XYChart.Data<>(7.8, 4.0)
        )),
        new LineChart.Series<>("Series 2", FXCollections.<ScatterChart.Data<Number, Number>>observableArrayList(
            new XYChart.Data<>(0.2, 3.5),
            new XYChart.Data<>(0.7, 4.6),
            new XYChart.Data<>(1.8, 1.7),
            new XYChart.Data<>(2.1, 2.8),
            new XYChart.Data<>(4.0, 2.2),
            new XYChart.Data<>(4.1, 2.6),
            new XYChart.Data<>(4.5, 2.0),
            new XYChart.Data<>(6.0, 3.0),
            new XYChart.Data<>(7.0, 2.0),
            new XYChart.Data<>(7.8, 4.0)
        ))
    );
    LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis, data);
    lineChart.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
    return lineChart;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setScene(new Scene(createContent()));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
