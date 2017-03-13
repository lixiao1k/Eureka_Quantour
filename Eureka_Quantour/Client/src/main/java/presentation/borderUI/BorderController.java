package presentation.borderUI;

import java.net.URL;
import java.util.Calendar;
import java.util.Observable;
import java.util.ResourceBundle;

import stub.CompareInfoVOStub;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import presentation.chart.klineChart.CandleStickChart;
//import presentation.chart.klineChart.kChartStub;
import vo.ComparedInfoVO;

public class BorderController implements Initializable {
	
	private Calendar beginDate;
	private Calendar endDate;
	private String stockA;
	private String stockB;
	private CandleStickChart candleStickChart;
	
	@FXML
	ImageView imageView;
	
	@FXML
	ImageView logoImageView;
	
	@FXML
	AnchorPane setInfoAnchorPane;
	
	@FXML
	BorderPane borderPane;
	
	private Image logo = new Image(getClass().getResourceAsStream("Title.png"));
	
	@FXML
	protected void browseKLine(ActionEvent e){
		ObservableList<Node> nodeList = setInfoAnchorPane.getChildren();
		for(Node node:nodeList){
			String name = (String)node.getProperties().get("Name");
			if(name!=null&&name.contains("HBox")){
				nodeList.remove(node);
				break;
			}
		}
		setInfoAnchorPane.getChildren().add(searchSingleStockHBox());
	}
	
	@FXML
	protected void browseAverageLine(ActionEvent e){
		
	}
	
	@FXML
	protected void goCompare(ActionEvent e){
		ObservableList<Node> nodeList = setInfoAnchorPane.getChildren();
		for(Node node:nodeList){
			String name = (String)node.getProperties().get("Name");
			if(name!=null&&name.contains("HBox")){
				nodeList.remove(node);
				break;
			}
		}
		setInfoAnchorPane.getChildren().add(compareHBox());
	}
	
	@FXML
	protected void browseMarket(ActionEvent e){
		
	}
	
	@FXML
	protected void exit(ActionEvent e){
		Stage stage = (Stage)imageView.getScene().getWindow();
		stage.close();
	}
	
	private HBox searchSingleStockHBox (){
		HBox hb = new HBox();
		hb.setPadding(new Insets(25, 5, 5, 25));
		hb.setSpacing(5);
		Label beginLabel = new Label("起始时间：");
		DatePicker beginDatePicker = new DatePicker();
		Label endLabel = new Label("结束时间：");
		DatePicker endDatePicker = new DatePicker();
		Label blank = new Label();
	    blank.setPrefSize(40, 10);
		TextField stockName = new TextField();
		stockName.setPrefSize(100,5);
		stockName.setPromptText("股票名称");
		Button searchButton = new Button("搜索");
		
		
		searchButton.setOnAction((ActionEvent e)->{
			ObservableList<Node> nodelist = borderPane.getChildren();
			nodelist.clear();
//			kChartStub kchart = new kChartStub();
//			candleStickChart = kchart.getKChart();
//			borderPane.setCenter(candleStickChart);
		});
		
		hb.getChildren().addAll(beginLabel,beginDatePicker,endLabel,endDatePicker,blank,stockName,searchButton);
		hb.getProperties().put("Name","searchSingleStockHBox");
		return hb;
	}
	
	private HBox compareHBox(){
		HBox hb = new HBox();
		hb.setPadding(new Insets(25, 5, 5, 25));
		hb.setSpacing(5);
		
		Label beginLabel = new Label("起始时间：");
		DatePicker beginDatePicker = new DatePicker();
		Label endLabel = new Label("结束时间：");
		DatePicker endDatePicker = new DatePicker();
		Label blank = new Label();
		blank.setPrefSize(40, 10);
		TextField stockNameA = new TextField();
		stockNameA.setPrefSize(100, 5);
		stockNameA.setPromptText("股票A");
		TextField stockNameB = new TextField();
		stockNameB.setPrefSize(100, 5);
		stockNameB.setPromptText("股票B");
		Button compareButton = new Button("比较");
		compareButton.setOnAction((ActionEvent e)->{
			ObservableList<Node> nodelist = borderPane.getChildren();
			nodelist.clear();
			borderPane.setRight(getCompareResultFlowPane());
			
		});
		
		hb.getChildren().addAll(beginLabel,beginDatePicker,endLabel,endDatePicker,blank,stockNameA,stockNameB,compareButton);
		hb.getProperties().put("Name","compareHBox");
		return hb;
	}
	
	
	private FlowPane getCompareResultFlowPane(){
		FlowPane fp = new FlowPane();
		fp.setPrefWidth(200);
		fp.setVgap(20);
		ComparedInfoVO vo = new CompareInfoVOStub();
		VBox vb1 = getCompareResultVBox(vo.getNameA(),vo.getHighA(),vo.getLowA()
				,vo.getLogYieldA(),vo.getLogYieldVarianceA(),"2017/11/19~2017/11/20");
		VBox vb2 = getCompareResultVBox(vo.getNameB(),vo.getHighB(),vo.getLowB()
				,vo.getLogYieldB(),vo.getLogYieldVarianceB(),"2017/11/19~2017/11/20");
		fp.getChildren().addAll(vb1,vb2);
		return fp;
	}
	/*
	 * @param variance 对数收益率方差
	 * @param change 涨/跌幅
	 */
	public VBox getCompareResultVBox (String stockName,double high,double low,double change,double variance,
			String timeRange){
		VBox result = new VBox();
		result.setPadding(new Insets(4,4,4,4));
		result.setSpacing(5);
		
		Label time = new Label(timeRange);
		
		Label name = new Label(stockName);
		
		Label highLabel = new Label("最高值: "+Double.toString(high));
		
		Label lowLabel = new Label("最低值: "+Double.toString(low));
		
		HBox changeHBox = setHBoxStyle(new Insets(4,4,4,4), 5);
		Label changeLabel = new Label();
		Label changeValueLabel = new Label();
		if(change>=0){
			changeLabel.setText("涨幅：");
			changeValueLabel.setText(Double.toString(change));
			changeValueLabel.setTextFill(Color.RED);
		}else{
			changeLabel.setText("跌幅: ");
			changeValueLabel.setText(Double.toString(change));
			changeValueLabel.setTextFill(Color.GREEN);
		}
		changeHBox.getChildren().addAll(changeLabel,changeValueLabel);
		
		Label varianceLabel = new Label("对数收益率方差: "+Double.toString(variance));
		
		result.getChildren().addAll(time,name,highLabel,lowLabel,changeHBox,varianceLabel);
		return result;
	}
	//设置HBox的简单样式
	private HBox setHBoxStyle(Insets insets,int spacing){
		HBox hb = new HBox();
		hb.setPadding(insets);
		hb.setSpacing(spacing);
		return hb;
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		logoImageView.setImage(logo);
		setInfoAnchorPane.getChildren().add(searchSingleStockHBox());
	}
}
