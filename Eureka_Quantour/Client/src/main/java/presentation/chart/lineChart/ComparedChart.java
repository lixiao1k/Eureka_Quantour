package presentation.chart.lineChart;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import presentation.chart.chartService;
import presentation.chart.function.CatchMouseMove;
import presentation.chart.function.CatchMouseMoveService;
import presentation.chart.function.CommonSet;
import presentation.chart.function.CommonSetService;
import presentation.chart.function.ListToArray;
import presentation.chart.function.ListToArrayService;
import vo.ComparedInfoVO;
import vo.YieldChartDataVO;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月31日
 */
public class ComparedChart implements chartService{

	private CatchMouseMoveService catchMouseMove = new CatchMouseMove();
	private ListToArrayService listToArray = new ListToArray();
	private CommonSetService commonSet = new CommonSet();
	
	private DecimalFormat df = new DecimalFormat("#0.00");
	private NumberFormat nf = NumberFormat.getPercentInstance();
	
	private AnchorPane pane = new AnchorPane();
	private StackPane chartpane = new StackPane();
	private AnchorPane infopane = new AnchorPane();
	private Label info = new Label();
	private Label begin = new Label();
	private Label end = new Label();
	
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private static int kind = -1;
    private LineChart<String, Number> lineChart;
    /**
     * dataMap: store every point's information and key is its abscissa which has been format
     */
    private Map<String, String> dataMap = new HashMap<String,String>();
    /**
     * dates: store xAxis's value which has been format
     */
    private String[] dates;
    /**
     * if chart is YieldComparedChart, it stores chart's extra information
     */
    private double[] infodata = new double[8];

    public ComparedChart(){
    	nf.setMinimumFractionDigits(1);
    };
    private ComparedChart(LocalDate[] date, List<Double[]> doubleList, List<String> dataName) {
        xAxis = new CategoryAxis();
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setStartMargin(10);
        xAxis.setOpacity(0.7);

        yAxis = new NumberAxis();
        yAxis.autoRangingProperty().set(true);
        yAxis.setAnimated(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setTickLabelsVisible(false);
        yAxis.setPrefWidth(1);
        yAxis.setOpacity(0.7);
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(true);
        lineChart.setOpacity(0.9);
        lineChart.setPadding(new Insets(10,10,10,10));
        
        dates = listToArray.formatLocalDate(date);
        
        String[] dataStrings = new String[date.length];
        for(int i=0; i<doubleList.size(); i++){
        	XYChart.Series<String, Number> serie = new XYChart.Series<>();
        	Double[] datas = doubleList.get(i);
            String name = "";
        	if( i<dataName.size() ){
        		name = dataName.get(i);
        		serie.setName(name);
        	}
        	for(int j=0; j<date.length; j++){
	        	if( j<datas.length && datas[j]!=0 && datas[j]!=Integer.MIN_VALUE ){
	        		serie.getData().add( new XYChart.Data<>(dates[j], datas[j]) );
	        		String dataFormat = nf.format(datas[j]);
	        		
	        		if( dataStrings[j]!=null )
	        			dataStrings[j] += "/"+name+" : "+dataFormat;
	        		else
	        			dataStrings[j] = name+" : "+dataFormat;
	        	}
        		else{
        			if( dataStrings[j]!=null )
        				dataStrings[j] += "/"+name+" : "+"0";
        			else
        				dataStrings[j] = name+" : "+"0";
        		}

        	}
        	lineChart.getData().add(serie);
        }
        lineChart.setLegendSide(Side.BOTTOM);
        for(int i=0; i<date.length; i++){
        	if( dataStrings[i].length()!=0 )
        		dataMap.put(dates[i], dataStrings[i]);
        }
        
    }
    
    /**
     * @Description: input a YieldChartDataVO and draw a ComparedChart with α etc
     * @author: hzp
     * @time: 2017年4月3日
     * @return: ComparedChart
     */
    public ComparedChart setData(YieldChartDataVO ycd){
    	kind = 0;
    	LocalDate[] date = listToArray.changeLocalDate(ycd.getDatelist());
    	List<Double[]> doubleList = new ArrayList<>();
    	List<String> dataName = new ArrayList<>();
    	doubleList.add( listToArray.changeDouble(ycd.getJizhunlist()) );
    	dataName.add("基础");
    	doubleList.add( listToArray.changeDouble(ycd.getCeluelist()) );
    	dataName.add("策略");
    	
    	ComparedChart chart = new ComparedChart( date, doubleList, dataName ); 	
    	chart.infodata[0] = ycd.getYearreturn();
    	chart.infodata[1] = 0.124;
    	chart.infodata[2] = ycd.getAlpha();
    	chart.infodata[3] = ycd.getBeta();
    	chart.infodata[4] = ycd.getSharpe();
    	chart.infodata[5] = 0.249;
    	chart.infodata[6] = 1.03;
    	chart.infodata[7] = 0.238;
    	return chart;
    }
    
    /**
     * @Description: input a ComparedInfoVO and draw a ComparedChart to compare two stock's situation
     * @author: hzp
     * @time: 2017年4月3日
     * @return: ComparedChart
     */
//    public ComparedChart setData(ComparedInfoVO ci){
//    	this.kind = 1;
//    	LocalDate[] date = ci.getDate();
//    	List<Double[]> doubleList = new ArrayList<>();
//    	List<String> dataName = new ArrayList<>();
//    	Double[] dA = listToArray.dToD(ci.getLogYieldA());
//    	Double[] dB = listToArray.dToD(ci.getLogYieldB());
//    	doubleList.add(dA);
//    	doubleList.add(dB);
//    	dataName.add(ci.getNameA());
//    	dataName.add(ci.getNameB());
//    	return new ComparedChart( date, doubleList, dataName );
//    }
    
    /**
     * @Description: if chart is YieldComparedChart, it needs to initialize other information
     * @author: hzp
     * @time: 2017年4月15日
     * @return: void
     * @param: layout: to set the label's width
     * @param: space: to set the label's height
     * @param: yearyield:年化收益率
     * @param: basicyearyield:基准年化收益率
     * @param: α:阿尔法
     * @param: β:贝塔
     * @param: sharpe:夏普比率
     * @param: yieldbodong:收益波动率
     * @param: infobilve:信息比率
     * @param: maxre:最大回撤
     */
    private void initInfo(double layout, double space){
    	layout = 70;
    	
    	infopane.setPrefSize(layout*8, space);
    	Label yearyield = new Label();
    	Label basicyearyield = new Label();
    	Label alpha = new Label();
    	Label beta = new Label();
    	Label sharpe = new Label();
    	Label yieldbodong = new Label();
    	Label infobilv = new Label();
    	Label maxre = new Label();
    	
    	yearyield.setPrefSize(layout, space);
    	basicyearyield.setPrefSize(layout*9/5, space);
    	alpha.setPrefSize(layout*3/5, space);
    	beta.setPrefSize(layout*3/5, space);
    	sharpe.setPrefSize(layout, space);
    	yieldbodong.setPrefSize(layout, space);
    	infobilv.setPrefSize(layout, space);
    	maxre.setPrefSize(layout, space);
    	
    	yearyield.setText( "年化收益率\n" + nf.format( infodata[0] ) );
    	basicyearyield.setText( "基准年化收益率\n" + nf.format( infodata[1] ));
    	alpha.setText( "α\n" + nf.format( infodata[2] ) );
    	beta.setText( "β\n" + df.format( infodata[3] ) );
    	sharpe.setText( "夏普比率\n" + df.format( infodata[4] ) );
    	yieldbodong.setText( "收益波动率\n" + nf.format( infodata[5] ) );
    	infobilv.setText( "信息比率\n" + df.format( infodata[6] ) );
    	maxre.setText( "最大回撤\n" + nf.format( infodata[7] ) );
    	
    	infopane.getChildren().add(yearyield);
    	infopane.getChildren().add(basicyearyield);
    	infopane.getChildren().add(alpha);
    	infopane.getChildren().add(beta);
    	infopane.getChildren().add(sharpe);
    	infopane.getChildren().add(yieldbodong);
    	infopane.getChildren().add(infobilv);
    	infopane.getChildren().add(maxre);
    	
    	double index = 10;
    	AnchorPane.setLeftAnchor(yearyield, index);
    	
    	index += layout;
    	AnchorPane.setLeftAnchor(basicyearyield, index);
    	
    	index += layout*9/5;
    	AnchorPane.setLeftAnchor(alpha, index);
    	
    	index += layout*3/5;
    	AnchorPane.setLeftAnchor(beta, index);
    	
    	index += layout*3/5;
    	AnchorPane.setLeftAnchor(sharpe, index);
    	
    	index += layout;
    	AnchorPane.setLeftAnchor(yieldbodong, index);
    	
    	index += layout;
    	AnchorPane.setLeftAnchor(infobilv, index);
    	
    	index += layout;
    	AnchorPane.setLeftAnchor(maxre, index);
    	
    	infopane.getStylesheets().add(
    			getClass().getResource("/styles/YieldChartInfoLabel.css").toExternalForm() );
    }
    
    @Override
    public Pane getchart(int width, int height) {
    	double space = 40.0;
    	int heightT = height;
    	
    	if( width<=0 )
    		width = 334;
    	if( height<=0 )
    		height = 200;
    	if( kind==0 ){
    		initInfo( width/8.0, space );
    		heightT -= (int)space;
    	}
    	
    	lineChart.setMaxSize(width, heightT);
    	lineChart.setMinSize(width, heightT);
    	
    	info = catchMouseMove.catchMouseReturnInfoForStackPane(lineChart, dataMap, dates, "date", 10);
    	begin = commonSet.beignDataForAnchorPane(dates[0], height);
    	end = commonSet.endDataForAnchorPane(dates[dates.length-1], width, height);
    	
    	chartpane.getChildren().add(lineChart);
    	chartpane.getChildren().add(info);
    	
    	if( kind==0 ){
    		pane.getChildren().add(infopane);
    		AnchorPane.setTopAnchor(chartpane, space);	
    	}
    	pane.getChildren().add(chartpane);
    	pane.getChildren().add(begin);
    	pane.getChildren().add(end);
    	StackPane.setAlignment(chartpane, Pos.CENTER);
    	
    	info.getStylesheets().add(
    			getClass().getResource("/styles/InfoLabel.css").toExternalForm() );
    	pane.getStylesheets().add(
    			getClass().getResource("/styles/ComparedLineChart.css").toExternalForm() );
    	return pane;
    }
    
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		lineChart.setTitle(name);
	}
	
}
