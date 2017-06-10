package presentation.chart.barChart;

import javafx.scene.chart.*;

import java.util.*;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import presentation.chart.chartService;

/**
 * 两支股票最高值和最低值绘制柱状图进行比较
 */
public class ExtremeValueComparedChart implements chartService {

	private AnchorPane pane = new AnchorPane();
    private BarChartExt<String,Number> bc;

    public ExtremeValueComparedChart(String stockA,String stockB,double highA,double highB,double lowA,double lowB){
        final String[] years = {"最高值", "最低值"};
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc=new BarChartExt<>(xAxis,yAxis);
        bc.setHorizontalGridLinesVisible(false);
        bc.setVerticalGridLinesVisible(false);
        bc.setTitle("对比图");
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

        bc.setCategoryGap(50);
        bc.getStylesheets().add(getClass().getResource("/styles/ComparedChart.css").toExternalForm());
        bc.setPrefSize(277, 238);
    }

    @Override
    public Pane getchart(int width, int height, boolean withdate) {
    	if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	bc.setMaxSize(width, height);
    	bc.setMinSize(width, height);
    	
    	pane.getChildren().add(bc);
        return pane;
    }

    @Override
    public void setName(String name) {
        bc.setTitle(name);

    }


    private static class BarChartExt<X, Y> extends BarChart<X, Y> {

        /**
         * Registry for text nodes of the bars
         */
        Map<Node, Node> nodeMap = new HashMap<>();

        public BarChartExt(Axis xAxis, Axis yAxis) {
            super(xAxis, yAxis);
        }

        /**
         * Add text for bars
         */
        @Override
        protected void seriesAdded(Series<X, Y> series, int seriesIndex) {

            super.seriesAdded(series, seriesIndex);

            for (int j = 0; j < series.getData().size(); j++) {

                Data<X, Y> item = series.getData().get(j);

                Node text = new Text(String.valueOf(item.getYValue()));
                ((Text)text).setFill(Color.WHITE);
                nodeMap.put(item.getNode(), text);
                getPlotChildren().add(text);

            }

        }

        /**
         * Remove text of bars
         */
        @Override
        protected void seriesRemoved(final Series<X, Y> series) {

            for (Node bar : nodeMap.keySet()) {

                Node text = nodeMap.get(bar);
                getPlotChildren().remove(text);

            }

            nodeMap.clear();

            super.seriesRemoved(series);
        }

        /**
         * Adjust text of bars, position them on top
         */
        @Override
        protected void layoutPlotChildren() {

            super.layoutPlotChildren();

            for (Node bar : nodeMap.keySet()) {

                Node text = nodeMap.get(bar);

                text.relocate(bar.getBoundsInParent().getMinX(), bar.getBoundsInParent().getMinY() -15);

            }

        }
    }


}
