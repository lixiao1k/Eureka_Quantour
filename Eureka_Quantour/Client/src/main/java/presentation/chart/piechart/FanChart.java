package presentation.chart.piechart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import presentation.chart.chartService;

public class FanChart implements chartService{
	
	private AnchorPane pane = new AnchorPane();
	private PieChart chart;
	protected FanChart(String[] nameOfContent, int[] num){
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for( int i=0; i<nameOfContent.length; i++ ){
			pieChartData.add(new PieChart.Data(nameOfContent[i], num[i]));
		}
		
		chart = new PieChart(pieChartData);
		
		Label caption = new Label("");
		caption.setTextFill( Color.DARKORANGE );
		caption.setStyle("-fx-font: 24 arial;");
		for (final PieChart.Data data : chart.getData()) {
		    data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
		        new EventHandler<MouseEvent>() {
		            @Override public void handle(MouseEvent e) {
		                caption.setTranslateX(e.getSceneX());
		                caption.setTranslateY(e.getSceneY());
		                caption.setText(String.valueOf(data.getPieValue()) + "%");
		             }
		        });
		}
		
		chart.setLegendSide( Side.RIGHT );
	}
	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	
    	chart.setPrefSize(width, height);
    	pane.getChildren().add(chart);
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/PieChart.css").toExternalForm() );
    	return pane;
	}
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}
}
