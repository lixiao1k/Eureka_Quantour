package presentation.chart.function;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class CommonSet implements CommonSetService{

	@Override
	public Label beignData(String content, int height) {
		// TODO Auto-generated method stub
		Label begin = new Label();
		begin.setTextFill(Color.WHITE);
		begin.setText(content);
		begin.setLayoutX(40);
		begin.setLayoutY(height-15);
		return begin;
	}

	@Override
	public Label endData(String content, int width, int height) {
		// TODO Auto-generated method stub
		Label end = new Label();
		end.setTextFill(Color.WHITE);
		end.setText(content);
		end.setLayoutX(width-60);
		end.setLayoutY(height-15);
		return end;
	}

}
