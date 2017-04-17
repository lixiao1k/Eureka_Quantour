package presentation.chart.function;

import java.util.Map;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class CatchMouseMove implements CatchMouseMoveService{
	
	@Override
	public Label catchMouseReturnInfoForAnchorPane(XYChart<String, Number> chart, Map<String, String> dataMap,
			String[] dates, String name, int index) {
		// TODO Auto-generated method stub

		Axis<String> xAxis = chart.getXAxis();
	    Axis<Number> yAxis = chart.getYAxis();
	
	    Label cursorCoords = new Label();
	    cursorCoords.setVisible(false);
	
	    final Node chartBackground = chart.lookup(".chart-plot-background");
	    for( Node n: chartBackground.getParent().getChildrenUnmodifiable() ) {
	    	if ( n!=chartBackground && n!=xAxis && n!=yAxis) {
	    		n.setMouseTransparent(true);
	    	}
	    }

	    chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    	@Override 
	    	public void handle(MouseEvent mouseEvent) {
	    		cursorCoords.setVisible(true);
	    	}
	    });
	
	    chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
	    	@Override 
	    	public void handle(MouseEvent mouseEvent) {
	    		double temp = (mouseEvent.getX()-index)*(dates.length-1)/(xAxis.getWidth()-index);
	    		
	    		if( mouseEvent.getX()>chart.getXAxis().getWidth()/2 )
	    			cursorCoords.setLayoutX(5);			
	    		else
	    			cursorCoords.setLayoutX(xAxis.getWidth()-30);
	    		
	    		int index = 0;
	    		if( xAxis.getWidth()/(dates.length-1)<8 ){
		    		if( (temp%1)<0.49 )
		    			index = (int)(temp/1);
		    		else if( (temp%1)>0.51 )
		    			index = (int)(temp/1)+1;
		    		else
		    			index = -1;
	    		}
	    		else{
	    			if( (temp%1)<0.4 )
		    			index = (int)(temp/1);
		    		else if( (temp%1)>0.6 )
		    			index = (int)(temp/1)+1;
		    		else
		    			index = -1;
	    		}
	    		if( index>-1){
		    		String dataInfo = dataMap.get(dates[index]);
		    		String infos[] = dataInfo.split("/");
		    		String info = name+" : "+dates[index]+"\n";
		    		for(int i=0; i<infos.length; i++){
		    			info += infos[i]+"\n";
		    		}
		    		cursorCoords.setVisible(true);
		    		cursorCoords.setText(info);
	    		}
	    		else{
	    			cursorCoords.setVisible(false);
	    			cursorCoords.setText("");
	    		}
	    	}
	    });

	    chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
	    	@Override 
	    	public void handle(MouseEvent mouseEvent) {
	    		cursorCoords.setVisible(false);
	    	}
	    });
		
		return cursorCoords;
	}

	@Override
	public Label catchMouseReturnInfoForStackPane(XYChart<String, Number> chart, Map<String, String> dataMap,
			String[] dates, String name, int index) {
		// TODO Auto-generated method stub
		
		Axis<String> xAxis = chart.getXAxis();
	    Axis<Number> yAxis = chart.getYAxis();
	
	    Label cursorCoords = new Label();
	    cursorCoords.setVisible(false);
	
	    final Node chartBackground = chart.lookup(".chart-plot-background");
	    for( Node n: chartBackground.getParent().getChildrenUnmodifiable() ) {
	    	if ( n!=chartBackground && n!=xAxis && n!=yAxis) {
	    		n.setMouseTransparent(true);
	    	}
	    }

	    chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    	@Override 
	    	public void handle(MouseEvent mouseEvent) {
	    		cursorCoords.setVisible(true);
	    	}
	    });
		    
		chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
			
	    	public void handle(MouseEvent mouseEvent) {
	    		double temp = (mouseEvent.getX()-index)*(dates.length-1)/(xAxis.getWidth()-index);
	    		
	    		if( mouseEvent.getX()>chart.getXAxis().getWidth()/2 )
	    			StackPane.setAlignment(cursorCoords, Pos.TOP_LEFT);			
	    		else
	    			StackPane.setAlignment(cursorCoords, Pos.TOP_RIGHT);
	    		
	    		int index = 0;
	    		if( xAxis.getWidth()/(dates.length-1)<8 ){
		    		if( (temp%1)<0.49 )
		    			index = (int)(temp/1);
		    		else if( (temp%1)>0.51 )
		    			index = (int)(temp/1)+1;
		    		else
		    			index = -1;
	    		}
	    		else{
	    			if( (temp%1)<0.4 )
		    			index = (int)(temp/1);
		    		else if( (temp%1)>0.6 )
		    			index = (int)(temp/1)+1;
		    		else
		    			index = -1;
	    		}
	    		if( index>-1){
		    		String dataInfo = dataMap.get(dates[index]);
		    		String infos[] = dataInfo.split("/");
		    		String info = name+" : "+dates[index]+"\n";
		    		for(int i=0; i<infos.length; i++){
		    			info += infos[i]+"\n";
		    		}
		    		cursorCoords.setVisible(true);
		    		cursorCoords.setText(info);
	    		}
	    		else{
	    			cursorCoords.setVisible(false);
	    			cursorCoords.setText("");
	    		}
	    	}
	    });
		
		 chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
		    	@Override 
		    	public void handle(MouseEvent mouseEvent) {
		    		cursorCoords.setVisible(false);
		    	}
		    });
			
		return cursorCoords;
	}

}
