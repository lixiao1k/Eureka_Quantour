package presentation.chart.piechart;

import java.text.NumberFormat;

import javafx.animation.TranslateTransitionBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import presentation.chart.chartService;

public class FanChart implements chartService{
	
	private NumberFormat nf = NumberFormat.getPercentInstance();
	
	private AnchorPane pane = new AnchorPane();
	private PieChart chart;
	private Label caption;
	
	private int count = 0;
	
	protected FanChart(String[] nameOfContent, int[] num){
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for( int i=0; i<nameOfContent.length; i++ ){
			pieChartData.add(new PieChart.Data(nameOfContent[i], num[i]));
		}
		
		chart = new PieChart(pieChartData);
		for( int i=0; i<num.length; i++)
			count += num[i];
		
//		caption = new Label("");
//		caption.setTextFill( Color.DARKORANGE );
//		caption.setStyle("-fx-font: 24 arial;");
//		for (final PieChart.Data data : chart.getData()) {
//		    data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
//		        new EventHandler<MouseEvent>() {
//		            @Override public void handle(MouseEvent e) {
//		                caption.setTranslateX(e.getSceneX());
//		                caption.setTranslateY(e.getSceneY());
//		                caption.setText( nf.format(data.getPieValue()/count) );
//		             }
//		        });
//		}
		

        for (PieChart.Data d : pieChartData) {
            d.getNode().setOnMouseEntered(new MouseHoverAnimation(d, chart));
            d.getNode().setOnMouseExited(new MouseExitAnimation());
        }
        chart.setClockwise(false);
		
		chart.getStyleClass().add("-fx-text-fill: #90d7ec");
		chart.setLegendSide( Side.BOTTOM );
	}
	@Override
	public Pane getchart(int width, int height, boolean withdate) {
		// TODO Auto-generated method stub
		if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	
    	chart.setPrefSize(width, height);
    	pane.setPrefSize(width, height);
    	pane.getChildren().add(chart);
//    	pane.getChildren().add(caption);
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/PieChart.css").toExternalForm() );
    	return pane;
	}
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		chart.setTitle(name);
	}
	
	static class MouseHoverAnimation implements EventHandler {
		static final Duration ANIMATION_DURATION = new Duration(500);
		static final double ANIMATION_DISTANCE = 0.15;
		private double cos;
		private double sin;
		private PieChart chart;
		 
		public MouseHoverAnimation(PieChart.Data d, PieChart chart) {
			this.chart = chart;
			double start = 0;
			double angle = calcAngle(d);
			for (PieChart.Data tmp : chart.getData()) {
				if (tmp == d) {
					break;
				}
				start += calcAngle(tmp);
			}
		 
			cos = Math.cos(Math.toRadians(start + angle / 2));
			sin = Math.sin(Math.toRadians(start + angle / 2));
		}
		 
		private static double calcAngle(PieChart.Data d) {
			double total = 0;
			for (PieChart.Data tmp : d.getChart().getData()) {
				total += tmp.getPieValue();
			}
			return 360 * (d.getPieValue() / total);
		}

		@Override
		public void handle(Event arg0) {
			// TODO Auto-generated method stub
			Node n = (Node) arg0.getSource();
			double minX = Double.MAX_VALUE;
			double maxX = Double.MAX_VALUE * -1;
		            
			for (PieChart.Data d : chart.getData()) {
				minX = Math.min(minX, d.getNode().getBoundsInParent().getMinX());
				maxX = Math.max(maxX, d.getNode().getBoundsInParent().getMaxX());
			}
		 
			double radius = maxX - minX;
//			System.out.println("cos:" + cos);
//			System.out.println("sin" + sin);
			TranslateTransitionBuilder.create()
			.toX((radius * ANIMATION_DISTANCE) * cos)
			.toY((radius * ANIMATION_DISTANCE) * (-sin))
			.duration(ANIMATION_DURATION).node(n).build().play();
		}
	}
		 
	static class MouseExitAnimation implements EventHandler {

		@Override
		public void handle(Event event) {
			// TODO Auto-generated method stub
			TranslateTransitionBuilder.create().toX(0).toY(0)
			.duration(new Duration(500)).node((Node) event.getSource())
			.build().play();
		}
	}
	
}
