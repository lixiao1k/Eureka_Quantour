package presentation.chart.function;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CommonSet implements CommonSetService{

	/**
	 * @Description: according to AnchorPane's feature and chart's size, set label's layoutX and layoutY
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	@Override
	public Label beignDateForAnchorPane(String content, int height) {
		// TODO Auto-generated method stub
		Label begin = new Label();
		begin.setTextFill(Color.WHITE);
		begin.setText(content);
		begin.setLayoutX(20);
		begin.setLayoutY(height-18);
		return begin;
	}

	/**
	 * @Description: according to AnchorPane's feature and chart's size, set label's layoutX and layoutY
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	@Override
	public Label endDateForAnchorPane(String content, int width, int height) {
		// TODO Auto-generated method stub
		Label end = new Label();
		end.setTextFill(Color.WHITE);
		end.setText(content);
		end.setLayoutX(width-60);
		end.setLayoutY(height-18);
		return end;
	}

	/**
	 * @Description: according to StackPane's feature, set label's layoutX as Pos.BOTTOM_LEFT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	@Override
	public Label beignDateForStackPane(String content, int height) {
		// TODO Auto-generated method stub
		Label begin = new Label();
		begin.setTextFill(Color.WHITE);
		begin.setText(content);
		StackPane.setAlignment(begin, Pos.CENTER_LEFT);
		return begin;
	}
	
	/**
	 * @Description: according to StackPane's feature, set label's layoutX as Pos.BOTTOM_LEFT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	@Override
	public Label middleDateForStackPane(String content, int height) {
		// TODO Auto-generated method stub
		Label middle = new Label();
		middle.setTextFill(Color.WHITE);
		middle.setText(content);
		StackPane.setAlignment(middle, Pos.CENTER);
		return middle;
	}

	/**
	 * @Description: according to StackPane's feature, set label's layoutX as Pos.BOTTOM_LEFT
	 * @author: hzp
	 * @time: 2017年4月12日
	 * @return: Label
	 */
	@Override
	public Label endDateForStackPane(String content, int height) {
		// TODO Auto-generated method stub
		Label end = new Label();
		end.setTextFill(Color.WHITE);
		end.setText(content);
		StackPane.setAlignment(end, Pos.CENTER_RIGHT);
		return end;
	}

	/**
	 * @Description: 对于日期直接返回StackPane，日期label已排版好
	 * @author: hzp
	 * @date: 2017年6月10日
	 * @param bdate : begin date
	 * @param mdate : middle date
	 * @param edate : end date
	 */
	@Override
	public Pane dateForStackPane(String bdate, String mdate, String edate) {
		// TODO Auto-generated method stub
		Label begin = new Label();
		Label middle = new Label();
		Label end = new Label();
		begin.setTextFill(Color.WHITE);
		begin.setText(bdate);
		middle.setTextFill(Color.WHITE);
		middle.setText(mdate);
		end.setTextFill(Color.WHITE);
		end.setText(edate);
		
		StackPane pane = new StackPane();
		pane.getChildren().add(begin);
		pane.getChildren().add(middle);
		pane.getChildren().add(end);
		StackPane.setAlignment(begin, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(middle, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(end, Pos.BOTTOM_RIGHT);
		
		return pane;
	}

}
