package presentation.mainScreen;

import java.io.IOException;
import java.net.URL;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dataController.DataContorller;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import presentation.browseStrategyUI.BrowseStrategyController;
import presentation.marketUI.MarketUIController;
import presentation.stockSetUI.StockSetUIController;
import presentation.strategyUI.StrategyUIController;
import rmi.RemoteHelper;
import vo.StrategyShowVO;

public class MainScreenController implements Initializable{
	
	@FXML
	DatePicker nowDatePicker;
	
	@FXML
	Button saveButton;
	
	@FXML
	Button exitButton;
	
	@FXML
	Label nameLabel;
	
	@FXML
	Button stockSetButton;
	
	@FXML
	Button marketButton;
	
	@FXML
	Button singleStockButton;
	
	@FXML
	Button strategyButton;
	
	@FXML
	Button statisticsButton;
	
	@FXML
	Button browseStrategyButton;
	
	@FXML
	AnchorPane mainAnchorPane;
	
	@FXML
	Label timeLabel;
	
	private DataContorller dataController;
	
	@FXML
	public void browseStockSet(ActionEvent e) throws IOException{
		setStockSet();
	}
	
	public void setStockSet() throws IOException{
		ObservableList<Node> nodeList = mainAnchorPane.getChildren();
		nodeList.clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("presentation/stockSetUI/StockSetUI.fxml"));
		AnchorPane stockSetPane = (AnchorPane)loader.load();
//		AnchorPane stockSetPane = (AnchorPane)FXMLLoader.load(getClass().getClassLoader().getResource("presentation/stockSetUI/StockSetUI.fxml"));
		StockSetUIController controller = loader.getController();
		controller.setController(this);
		mainAnchorPane.getChildren().add(stockSetPane);
	}
	@FXML
	protected void browseMarket(ActionEvent e) throws IOException{
		setMarketUI();
	}
	
	public void setMarketUI() throws IOException{
		ObservableList<Node> nodeList = mainAnchorPane.getChildren();
		nodeList.clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("presentation/marketUI/MarketUI.fxml"));
		AnchorPane marketPane = (AnchorPane)loader.load();
		MarketUIController controller = loader.getController();
		controller.setController(this);
//		AnchorPane marketPane = (AnchorPane)FXMLLoader.load(getClass().getClassLoader().getResource("presentation/marketUI/MarketUI.fxml"));
		mainAnchorPane.getChildren().add(marketPane);
	}
	
	@FXML
	protected void browseSingleStock(ActionEvent e) throws IOException{
		setSingleStockUI();
	}
	public void setSingleStockUI() throws IOException{
		ObservableList<Node> nodeList = mainAnchorPane.getChildren();
		nodeList.clear();
		AnchorPane singleStockPane = (AnchorPane)FXMLLoader.load(getClass().getClassLoader().getResource("presentation/singleStockUI/SingleStockUI.fxml"));
		mainAnchorPane.getChildren().add(singleStockPane);
	}
	
	@FXML
	protected void browseStrategy(ActionEvent e) throws IOException{
		setBrowseStrategyUI(false);
	}

	/**
	 *
	 * @param isInitialStrategy  如果是false，就不用初始化策略，是true，初始化策略，用于策略运用中
	 * @throws IOException
	 */
	public void setBrowseStrategyUI(boolean isInitialStrategy) throws IOException{
		ObservableList<Node> nodeList = mainAnchorPane.getChildren();
		nodeList.clear();
		AnchorPane strategyPane;
		if(isInitialStrategy){
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("presentation/strategyUI/StrategyUI.fxml"));
			strategyPane = (AnchorPane)loader.load();
			StrategyUIController controller = loader.getController();
			controller.setStrategy((StrategyShowVO) dataController.get("StrategyShowVO"));
		}else{
			strategyPane = (AnchorPane)FXMLLoader.load(getClass().getClassLoader().getResource("presentation/strategyUI/StrategyUI.fxml"));

		}
		mainAnchorPane.getChildren().add(strategyPane);
	}

	
	@FXML
	protected void browseStatistics(ActionEvent e) throws IOException{
		setBrowseStatisticsUI();
	}
	
	public void setBrowseStatisticsUI() throws IOException{
		ObservableList<Node> nodeList = mainAnchorPane.getChildren();
		nodeList.clear();
		AnchorPane statisticsPane = (AnchorPane)FXMLLoader.load(getClass().getClassLoader().getResource("presentation/statisticsUI/StatisticsUI.fxml"));
		mainAnchorPane.getChildren().add(statisticsPane);
	}
	
	@FXML
	protected void browseStrategySet(ActionEvent e) throws IOException{
		setBrowseStrategySetUI();
	}
	/**
	 * 发现策略界面
	 * @throws IOException
	 */
	public void setBrowseStrategySetUI() throws IOException{
		ObservableList<Node> nodeList = mainAnchorPane.getChildren();
		nodeList.clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("presentation/browseStrategyUI/BrowseStrategy.fxml"));
		AnchorPane strategyPane = (AnchorPane)loader.load();
		BrowseStrategyController controller = loader.getController();
		controller.setController(this);
		mainAnchorPane.getChildren().add(strategyPane);
	}

	@FXML
	protected void saveTime(ActionEvent e){
		LocalDate date = nowDatePicker.getValue();
		if(date!=null&&!date.isAfter(LocalDate.now())){
			dataController.upDate("SystemTime", date);
			timeLabel.setText(date.toString());
			Notifications.create().title("成功").text("系统时间修改成功").showInformation();
		}else if(date==null){
			Notifications.create().title("异常").text("没有时间").showWarning();
		}else if(date.isAfter(LocalDate.now())){
			Notifications.create().title("异常").text("系统时间设置为今日之后").showWarning();
		}
	}
	
	@FXML
	protected void exit(ActionEvent e){
		Stage root = (Stage) exitButton.getScene().getWindow();
		try {
			RemoteHelper.getInstance().getClientLogic().signOut((String) dataController.get("UserName"));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		root.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dataController = DataContorller.getInstance();
		String username = (String)dataController.get("UserName");
		nameLabel.setText(username);
		Image exitImage = new Image(getClass().getResourceAsStream("exit.png"));
		exitButton.setGraphic(new ImageView(exitImage));
		Image saveImage = new Image(getClass().getResourceAsStream("save.png"));
		saveButton.setGraphic(new ImageView(saveImage));
		stockSetButton.setText("股池");
		marketButton.setText("市场");
		singleStockButton.setText("个股");
		strategyButton.setText("策略");
		statisticsButton.setText("统计");
		browseStrategyButton.setText("发现");
		
		Locale.setDefault(Locale.ENGLISH);
		
		timeLabel.setText(LocalDate.now().toString());
		dataController.upDate("SystemTime", LocalDate.now());
	}

}
