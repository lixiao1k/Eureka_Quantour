package presentation.chart.function;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CommonSet implements CommonSetService{

	@Override
	public Label beignDataForAnchorPane(String content, int height) {
		// TODO Auto-generated method stub
		Label begin = new Label();
		begin.setTextFill(Color.WHITE);
		begin.setText(content);
		begin.setLayoutX(20);
		begin.setLayoutY(height-18);
		return begin;
	}

	@Override
	public Label endDataForAnchorPane(String content, int width, int height) {
		// TODO Auto-generated method stub
		Label end = new Label();
		end.setTextFill(Color.WHITE);
		end.setText(content);
		end.setLayoutX(width-60);
		end.setLayoutY(height-18);
		return end;
	}

	@Override
	public Label beignDataForStackPane(String content, int height) {
		// TODO Auto-generated method stub
		Label begin = new Label();
		begin.setTextFill(Color.WHITE);
		begin.setText(content);
		StackPane.setAlignment(begin, Pos.BOTTOM_LEFT);
		return begin;
	}

	@Override
	public Label endDataForStackPane(String content, int width, int height) {
		// TODO Auto-generated method stub
		Label end = new Label();
		end.setTextFill(Color.WHITE);
		end.setText(content);
		StackPane.setAlignment(end, Pos.BOTTOM_RIGHT);
		return end;
	}

}
