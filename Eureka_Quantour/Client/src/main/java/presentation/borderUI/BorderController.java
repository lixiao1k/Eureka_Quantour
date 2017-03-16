package presentation.borderUI;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.sun.javafx.tk.Toolkit;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import logic.service.StockLogicInterface;
import presentation.chart.chartService;
import presentation.chart.barChart.ExtremeValueComparedChart;
import presentation.chart.barChart.VolumeChart;
import presentation.chart.klineChart.CandleStickChart;
import presentation.chart.klineChart.KLineChart;
import presentation.chart.lineChart.EMAChart;
import resultmessage.BeginInvalidException;
import resultmessage.DateInvalidException;
import resultmessage.EndInvalidException;
import dataController.DataContorller;
import rmi.RemoteHelper;
import vo.ComparedInfoVO;
import vo.EMAInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;

public class BorderController implements Initializable {
//	
//	private Calendar beginDate;
//	private Calendar endDate;
//	private String stockA;
//	private String stockB;
//	private CandleStickChart candleStickChart;
//	
	public DataContorller dataController = DataContorller.getInstance();
	
	@FXML
	ImageView imageView;
	
	@FXML
	ImageView logoImageView;
	
	@FXML
	AnchorPane setInfoAnchorPane;
	
	@FXML
	BorderPane borderPane;
	
	private Image logo = new Image(getClass().getResourceAsStream("Title.png"));
	/*
	 * 点击左侧K线均线按钮，出现相应搜索栏
	 */
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
		borderPane.getChildren().clear();
	}
	/*
	 * 点击左侧比较按钮，出现相应比较搜索栏
	 */
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
		borderPane.getChildren().clear();
	}
	/*
	 * 点击左侧市场情况按钮，出现相应搜索栏
	 */
	@FXML
	protected void browseMarket(ActionEvent e){
		ObservableList<Node> nodeList = setInfoAnchorPane.getChildren();
		for(Node node:nodeList){
			String name = (String)node.getProperties().get("Name");
			if(name!=null&&name.contains("HBox")){
				nodeList.remove(node);
				break;
			}
		}
		setInfoAnchorPane.getChildren().add(marketHBox());
		borderPane.getChildren().clear();
	}
	/*
	 * 登出按钮
	 */
	@FXML
	protected void exit(ActionEvent e){
		Stage stage = (Stage)imageView.getScene().getWindow();
		stage.close();
	}
	/*
	 * k线均线搜索栏
	 */
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
		stockName.setPromptText("股票代码");
		Button searchKButton = new Button("K线");
		//K线浏览的监听
		searchKButton.setOnAction((ActionEvent e)->{
			List<SingleStockInfoVO> stockInfoList=null;
			String stockCode = stockName.getText();
			Calendar beginTime = localDate2Calendar(beginDatePicker.getValue());
			Calendar endTime = localDate2Calendar(endDatePicker.getValue());
			RemoteHelper remote = RemoteHelper.getInstance();
			StockLogicInterface slinterface = remote.getStockLogic();
			//开始时间必须小于结束时间
			if(beginTime.after(endTime)){
				java.awt.Toolkit.getDefaultToolkit().beep(); 
				Notifications.create().text("开始日期不能大于结束日期").title("提示").show();
			}else if(stockCode.isEmpty()){
				java.awt.Toolkit.getDefaultToolkit().beep(); 
				Notifications.create().text("股票代码处不能为空").title("提示").show();
			}else{
				try {
				    stockInfoList = slinterface.getSingleStockInfoByTime(stockCode, beginTime, endTime);
				}catch(DateInvalidException ed){
					Notifications.create().text(ed.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				} catch(BeginInvalidException eb){
					Notifications.create().text(eb.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}catch(EndInvalidException ee){
					Notifications.create().text(ee.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}
				String title = stockInfoList.get(0).getName()+"  K线图";
				chartService service = new KLineChart(stockInfoList);
				service.setName(title);
				XYChart<String, Number> klchart = service.getchart();
				chartService service1 = new VolumeChart(stockInfoList);
				XYChart<String, Number> vlchart = service1.getchart();
				ObservableList<Node> nodelist = borderPane.getChildren();
				nodelist.clear();
				
				borderPane.setCenter(klchart);
//				borderPane.setBottom(vlchart);
			}
			
		});
		

		Button searchEMAButton = new Button("均线");
		//均线浏览的监听
		searchEMAButton.setOnAction((ActionEvent e)->{
			List<List<EMAInfoVO>> emaInfo = null;
			String stockCode = stockName.getText();
			Calendar beginTime = localDate2Calendar(beginDatePicker.getValue());
			Calendar endTime = localDate2Calendar(endDatePicker.getValue());
			RemoteHelper remote = RemoteHelper.getInstance();
			StockLogicInterface slinterface = remote.getStockLogic();
			if(beginTime.after(endTime)){
				java.awt.Toolkit.getDefaultToolkit().beep(); 
				Notifications.create().text("开始日期不能大于结束日期").title("提示").show();
			}else if(stockCode.isEmpty()){
				java.awt.Toolkit.getDefaultToolkit().beep(); 
				Notifications.create().text("股票代码处不能为空").title("提示").show();
			}else{
				try {
					emaInfo = remote.getStockLogic().getEMAInfo(stockCode, beginTime, endTime);
				}catch(DateInvalidException ed){
					Notifications.create().text(ed.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				} catch(BeginInvalidException eb){
					Notifications.create().text(eb.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}catch(EndInvalidException ee){
					Notifications.create().text(ee.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				chartService service = new EMAChart(emaInfo);
				service.setName("标准均线图");
				XYChart<String, Number> EMAchart = service.getchart();
				ObservableList<Node> nodelist = borderPane.getChildren();
				nodelist.clear();
				borderPane.setCenter(EMAchart);
			}
			

		});
		
		hb.getChildren().addAll(beginLabel,beginDatePicker,endLabel,endDatePicker,blank,stockName,searchKButton,searchEMAButton);
		hb.getProperties().put("Name","searchSingleStockHBox");
		return hb;
	}
	
	
	
	/*
	 * 股票比较的搜索栏
	 */
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
			ComparedInfoVO comparedInfoVO = null;
			Calendar beginTime = localDate2Calendar(beginDatePicker.getValue());
			Calendar endTime = localDate2Calendar(endDatePicker.getValue());
			String stockA = stockNameA.getText();
			String stockB = stockNameB.getText();
			RemoteHelper remote = RemoteHelper.getInstance();
			StockLogicInterface slinterface = remote.getStockLogic();
			if(beginTime.after(endTime)){
				java.awt.Toolkit.getDefaultToolkit().beep(); 
				Notifications.create().text("开始日期不能大于结束日期").title("提示").show();
			}else if(stockA.isEmpty()||stockB.isEmpty()){
				java.awt.Toolkit.getDefaultToolkit().beep(); 
				Notifications.create().text("请输入股票A或股票B名称或代号").title("提示").show();
			}else{
				try {
					comparedInfoVO = slinterface.getComparedInfo(stockA, stockB, beginTime, endTime);
					System.out.println(comparedInfoVO);
					dataController.upDate("COMPAREDINFO", comparedInfoVO);
				} catch(DateInvalidException ed){
					Notifications.create().text(ed.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				} catch(BeginInvalidException eb){
					Notifications.create().text(eb.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}catch(EndInvalidException ee){
					Notifications.create().text(ee.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				ObservableList<Node> nodelist = borderPane.getChildren();
				nodelist.clear();
				borderPane.setRight(getCompareResultFlowPane());
				borderPane.setCenter(getCompareResultChart());
			}
			
			
			
			
		});
		
		hb.getChildren().addAll(beginLabel,beginDatePicker,endLabel,endDatePicker,blank,stockNameA,stockNameB,compareButton);
		hb.getProperties().put("Name","compareHBox");
		return hb;
	}
	
	private HBox marketHBox(){
		HBox hb = new HBox();
		hb.setPadding(new Insets(25,5,5,25));
		hb.setSpacing(5);
		Label timeLabel = new Label("查询时间: ");
		DatePicker timeDatePicker = new DatePicker();
		Label blank = new Label();
		blank.setPrefSize(40, 10);
		TextField marketName = new TextField();
		marketName.setPrefSize(100, 5);
		marketName.setPromptText("市场名");
		Button marketThermometer = new Button("市场温度计");
		marketThermometer.setOnAction((ActionEvent e)->{
			MarketInfoVO marketInfoVO = null;
			RemoteHelper remote = RemoteHelper.getInstance();
			Calendar date = localDate2Calendar(timeDatePicker.getValue());
			StockLogicInterface slinterface = remote.getStockLogic();
			if(timeDatePicker.getValue()==null){
				java.awt.Toolkit.getDefaultToolkit().beep(); 
				Notifications.create().text("开始日期不能大于结束日期").title("提示").show();
			}else if(marketName.getText().isEmpty()){
				java.awt.Toolkit.getDefaultToolkit().beep(); 
				Notifications.create().text("请输入市场名，任意字符即可").title("提示").show();
			}else{
				try {
					marketInfoVO = slinterface.getMarketInfo(date);
					ObservableList<Node> nodeList = borderPane.getChildren();
					nodeList.clear();
					borderPane.setTop(getMarketThermometerResultVBox(marketInfoVO.getVolume(), marketInfoVO.getNumOfRiseStop()
							,marketInfoVO.getNumOfDropStop(),marketInfoVO.getNumOfRiseEFP(),marketInfoVO.getNumOfDropEFP(), 
							marketInfoVO.getNumOfOMCEFP(), marketInfoVO.getNumOfOMCLTFP()));
				}catch(DateInvalidException ed){
					Notifications.create().text(ed.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				} catch(BeginInvalidException eb){
					Notifications.create().text(eb.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}catch(EndInvalidException ee){
					Notifications.create().text(ee.toString()).title("提示").show();
					java.awt.Toolkit.getDefaultToolkit().beep(); 
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		hb.getChildren().addAll(timeLabel,timeDatePicker,blank,marketName,marketThermometer);
		hb.getProperties().put("Name","marketHBox");
		return hb;
	}
	
	private FlowPane getCompareResultFlowPane(){
		FlowPane fp = new FlowPane();
		fp.setPrefWidth(200);
		fp.setVgap(20);
		ComparedInfoVO vo = (ComparedInfoVO) dataController.get("COMPAREDINFO");
		int dateLength =vo.getDate().length;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String timeRange = df.format(vo.getDate()[0].getTime())+"~"+df.format(vo.getDate()[dateLength-1].getTime());
		VBox vb1 = getCompareResultVBox(vo.getNameA(), vo.getHighA(), vo.getLowA(),
				vo.getRODA(), vo.getLogYieldVarianceA(), timeRange);
		VBox vb2 = getCompareResultVBox(vo.getNameB(), vo.getHighB(), vo.getLowB(), 
				vo.getRODB(), vo.getLogYieldVarianceB(), timeRange);
		VBox buttonVBox = new VBox();//拓展
		buttonVBox.setSpacing(5);
		buttonVBox.setPadding(new Insets(4,4,4,4));
		fp.getChildren().addAll(vb1,vb2);
		return fp;
	}
	
	private AnchorPane getCompareResultChart(){
		AnchorPane resultAnchorPane=null;
		try {
             resultAnchorPane = FXMLLoader.load(getClass().getResource("CompareChartPane.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultAnchorPane;
	}
	/*
	 * @param A  A指数，开盘‐收盘大于5%*上一个交易日收盘价的股票个数
	 * @param B  B指数，开盘‐收盘小于-5%*上一个交易日收盘价的股票个数
	 */
	private VBox getMarketThermometerResultVBox(Long sumOfVolume,int numOfUp,int numOfDown,int numOfRateIncrease,int numOfRateDecrease,
			int A,int B){
		
		VBox result = new VBox();
		result.setPadding(new Insets(4,4,4,4));
		result.setSpacing(5);
		HBox hb1 = setHBoxStyle(new Insets(4,4,4,4),10);
		HBox hb2 = setHBoxStyle(new Insets(4,4,4,4),10);
		Label sumOfVolumeLabel = new Label("当日总交易量: "+Long.toString(sumOfVolume));
		Label numOfUpLabel = new Label("涨停股票数: "+Integer.toString(numOfUp));
		Label numOfDownLabel = new Label("跌停股票数: "+Integer.toString(numOfDown));
		Label numOfRateIncreaseLabel = new Label("涨幅超5%股票数: "+Integer.toString(numOfRateIncrease));
		Label numOfRateDecreaseLabel = new Label("跌幅超5%股票数: "+Integer.toString(numOfRateDecrease));
		Label aLabel = new Label("A指数: "+Integer.toString(A));
		Tooltip aTooltip = new Tooltip("开盘‐收盘大于5%*上一个交易日收盘价的股票个数");
		aLabel.setTooltip(aTooltip);
		Label bLabel = new Label("B指数: "+Integer.toString(B));
		Tooltip bTooltip = new Tooltip("开盘‐收盘小于-5%*上一个交易日收盘价的股票个数");
		bLabel.setTooltip(bTooltip);
		hb1.getChildren().addAll(sumOfVolumeLabel,numOfUpLabel,numOfDownLabel);
		hb2.getChildren().addAll(numOfRateIncreaseLabel,numOfRateDecreaseLabel,aLabel,bLabel);
		result.getChildren().addAll(hb1,hb2);
		return result;
	}
	
	
	/*
	 * @param variance 对数收益率方差
	 * @param change 涨/跌幅
	 */
	private VBox getCompareResultVBox (String stockName,double high,double low,double change,double variance,
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
	
	/*
	 * 将LocalDate转换为Calendar
	 */
	private Calendar localDate2Calendar(LocalDate date){
		Calendar calendar = Calendar.getInstance();
		calendar.set(date.getYear(), date.getMonthValue()-1, date.getDayOfMonth());
		return calendar;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		logoImageView.setImage(logo);
		setInfoAnchorPane.getChildren().add(searchSingleStockHBox());
	}
}
