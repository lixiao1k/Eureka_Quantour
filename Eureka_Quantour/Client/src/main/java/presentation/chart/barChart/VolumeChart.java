package presentation.chart.barChart;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import vo.SingleStockInfoVO;

import java.util.List;

/**
 * Created by huihantao on 2017/3/9.
 */
public class VolumeChart{
	
    protected NumberAxis yAxis;
    protected CategoryAxis xAxis;

    private BarChart<String, Number> volumechart ;

    public VolumeChart(List<SingleStockInfoVO> list){
        xAxis = new CategoryAxis();
        xAxis.setGapStartAndEnd(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setStartMargin(5);
        xAxis.setOpacity(0.5);
        
        yAxis = new NumberAxis();
        yAxis.setPrefWidth(1);
        yAxis.autoRangingProperty().set(true);
        yAxis.forceZeroInRangeProperty().setValue(Boolean.FALSE);
        yAxis.setOpacity(0);
        yAxis.setTickLabelsVisible(false);

        volumechart = new BarChart<>(xAxis,yAxis);
        volumechart.setHorizontalGridLinesVisible(false);
        volumechart.setVerticalGridLinesVisible(false);
        volumechart.setLegendVisible(false);
        volumechart.setBarGap(0);
        volumechart.setCategoryGap(0);
        volumechart.setOpacity(1);

        XYChart.Series<String,Number> series = new XYChart.Series<>();

        for (SingleStockInfoVO info:list){ 
            String label = info.getDate().toString();
            XYChart.Data<String,Number> s= new XYChart.Data<>(label,info.getVolume());
            s.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                    if (newValue != null) {
                        if ( info.getClose()>info.getOpen() ) {
                            newValue.getStyleClass().add("barp");
                        } else  {
                            newValue.getStyleClass().add("barm");
                        }
                    }
                }
            });
            series.getData().add(s);
        }

        volumechart.getData().add(series);
        volumechart.getStylesheets().add(getClass().getResource("/styles/VolumeChart.css").toExternalForm());
        
        for (XYChart.Series<String, Number> s : volumechart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "   " +d.getYValue()));

            }
        }

    }

    public XYChart<String, Number> getchart(int width, int height) {
    	if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	volumechart.setMaxSize(width, height);
    	volumechart.setMinSize(width, height);
    	
        return volumechart;
    }

    public void setName(String name) {
        volumechart.setTitle(name);
    }


}
