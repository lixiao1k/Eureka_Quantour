package presentation.stockSetUI;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.sound.midi.Sequence;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.service.Stub;

public class StockSetUIController implements Initializable {
	@FXML
	AnchorPane stockSetMainAnchorPane;
	
	@FXML
	ScrollPane stockSetScrollPane;
	
	@FXML
	ScrollPane stocksScrollPane;
	
	@FXML
	FlowPane stockSetFlowPane;
	
	@FXML
	FlowPane stocksFlowPane;
	
	@FXML
	AnchorPane stockBasicInfoAnchorPane;
	
	@FXML
	AnchorPane kChartAnchorPane;
	
	@FXML
	AnchorPane emaChartAnchorPane;
	//关联与弹窗之间的controller
	@FXML
	protected void creatStockSet(ActionEvent e) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("StockSetPopup.fxml"));
		Parent popup = (AnchorPane)loader.load();
		StockSetPopupController controller = loader.getController();
		controller.setUpController(this);
		Scene scene = new Scene(popup);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}
	
	
	private void addSet(List<String> list){
		for(String item:list){
            creatSet(item);
		}	
	}
	
	//用户新建股池,给StockSetPopupController的供接口
	public void creatSet(String name){
		Button button = new Button(name);
        button.setPrefWidth(244);
        button.setMinWidth(244);
        button.getStylesheets().add(getClass().getClassLoader().getResource("styles/StockSetButton.css").toExternalForm());
        button.getProperties().put("NAME", button.getText());
        stockSetFlowPane.getChildren().add(button);
        //加入删除的上下文菜单
        final ContextMenu menu = new ContextMenu();
        MenuItem item = new MenuItem("删除");
        menu.getItems().add(item);
        button.setContextMenu(menu);
        item.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
			    ObservableList<Node> list = stockSetFlowPane.getChildren();
			    for(Node button1:list){
			    	String name = (String) button1.getProperties().get("NAME");
			    	if(name!=null&&name==button.getText()){
			    		list.remove(button1);
			    		break;
			    	}
			    }
			    //之后加入更新保存数据
			    
			}
		});
        
        //之后加入更新保存数据
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
//		javafx8之后这条语句单独使用没有效果，需要在css上加上
//		.scroll-pane > .viewport {
//		   -fx-background-color: transparent;
//	    }
		stockSetScrollPane.setStyle("-fx-background-color:transparent;");
		stocksScrollPane.setStyle("-fx-background-color:transparent;");
		Stub stub = new Stub();
		addSet(stub.getStockSet("username"));

	}

}
