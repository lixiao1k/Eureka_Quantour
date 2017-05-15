package po;

import java.time.LocalDate;
import data.parse.Parse;
/**
 * SingleStockInfo的PO类，用来维护SingleStock的持久对象
 * @author	刘宇翔
 *
 */
public class SingleStockInfoPO 
{
	private String name;//这支股票的名字
	private String code;//这支股票的编号
	
	private LocalDate date;//这条股票信息的时间
	
	private double open;//这支股票当天的开盘价格
	private double high;//这支股票当天的最高价格
	private double low;//这支股票当天的最低价格	
	private long volume;//这支股票当天的交易量
	
	private double close;//这支股票当天的收盘指数
	private double subClose;//今日前复权收盘价格
	private double aftClose;//今日后复权收盘价格
			
	private double lclose;//昨日收盘价格
	private double lsubClose;//昨日前复权收盘价格
	private double laftClose;//昨日后复权收盘价格
	
	private double rate;//未复权涨跌幅
	private double subrate;//前复权涨跌幅
	private double aftrate;//后复权涨跌幅
	
//	private double subavg_5;//前复权5日均线
//	private double subavg_10;//前复权10日均线
//	private double subavg_20;//前复权20日均线
//	private double subavg_30;//前复权30日均线
//	private double subavg_60;//前复权60日均线
//	
	private double aftavg_5;//后复权5日均线
//	private double aftavg_10;//后复权10日均线
//	private double aftavg_20;//后复权20日均线
//	private double aftavg_30;//后复权30日均线
//	private double aftavg_60;//后复权60日均线
	
	public SingleStockInfoPO(){
	}
	/**
	 * 
	 * @param _name String类型，这支股票的名字
	 * @param _code String类型，这支股票的编号
	 * 
	 * @param _date	Calendar类型，这条股票信息的时间
	 *
	 * @param _open	double类型，这支股票当天的开盘价格
	 * @param _high	double类型，这支股票当天的最高价格
	 * @param _low	double类型，这支股票当天的最低价格	
	 * @param _volume int类型，这支股票当天的交易量
	 * 
	 * @param _close double类型，这支股票当天的收盘指数
	 * @param _subclose double类型，这支股票当天的前复权收盘指数
	 * @param _aftclose double类型，这支股票当天的后复权收盘指数
	 * 
	 * @param _lclose double类型，这支股票昨天的收盘指数
	 * @param _lsubclose double类型，这支股票昨天的前复权收盘指数
	 * @param _laftclose double类型，这支股票昨天的后复权收盘指数
	 * 
	 * @param _rate double类型，这支股票当天的涨跌幅
	 * @param _subrate double类型，这支股票当天的前复权涨跌幅
	 * @param _aftrate double类型，这支股票当天的后复权涨跌幅
	 */
	public SingleStockInfoPO(
			String _name,String _code,
			LocalDate _date,
			double _open,double _high,double _low,long _volume,
			double _close,double _subclose,double _aftclose,
			double _lclose,double _lsubclose,double _laftclose,
			double _rate,double _subrate,double _aftrate,
			double _subavg_5,double _subavg_10,double _subavg_20,double _subavg_30,double _subavg_60,
			double _aftavg_5,double _aftavg_10,double _aftavg_20,double _aftavg_30,double _aftavg_60)
	{
		setName(_name);
		setCode(_code);
		
		setDate(_date);
		
		setOpen(_open);
		setHigh(_high);
		setLow(_low);
		setVolume(_volume);
		
		setClose(_close);
		setSubClose(_subclose);
		setAftClose(_aftclose);
		
		setLclose(_lclose);
		setLsubClose(_lsubclose);
		setLaftClose(_laftclose);
		
		setRate(_rate);
		setSubrate(_subrate);
		setAftrate(_aftrate);
		
//		setSubavg_5(_subavg_5);
//		setSubavg_10(_subavg_10);
//		setSubavg_20(_subavg_20);
//		setSubavg_30(_subavg_30);
//		setSubavg_60(_subavg_60);
//		
//		setAftavg_5(_aftavg_5);
//		setAftavg_10(_aftavg_10);
//		setAftavg_20(_aftavg_20);
//		setAftavg_30(_aftavg_30);
//		setAftavg_60(_aftavg_60);
	}
	public String toString(){
		return "名字："+name+","+
				"编号："+code+","+
				"日期："+Parse.getInstance().getIntDate(date)+","+
				"开盘："+open+","+
				"最高："+high+","+
				"最低："+low+","+
				"交易量："+volume+","+
				"收盘："+close+","+
				"前复权收盘："+subClose+","+
				"后复权收盘："+aftClose+","+
				"昨日收盘："+lclose+","+
				"昨日前复权收盘："+lsubClose+","+
				"昨日后复权收盘："+laftClose+","+
				"涨跌幅："+rate+","+
				"前复权涨跌幅："+subrate+","+
				"后复权涨跌幅："+aftrate+","
				;
	}
	/**
	 * 新的构造方法，其余的全作废
	 * @param info
	 * @param _name
	 * @param _code
	 * @param _date
	 */
	public SingleStockInfoPO(String info,String _name,String _code,LocalDate _date){
		setName(_name);
		setCode(_code);
		
		setDate(_date);
		
		String[] input=info.split(",");
		setOpen(strTodouble1(input[0]));
		setHigh(strTodouble1(input[1]));
		setLow(strTodouble1(input[2]));
		setVolume(Long.parseLong(input[6]));
		
		setClose(strTodouble1(input[3]));
		setSubClose(strTodouble1(input[7]));
		setAftClose(strTodouble1(input[10]));
		
		setLclose(strTodouble1(input[4]));
		setLsubClose(strTodouble1(input[8]));
		setLaftClose(strTodouble1(input[11]));
		
		setRate(strTodouble2(input[5]));
		setSubrate(strTodouble2(input[9]));
		setAftrate(strTodouble2(input[12]));
	
//		setSubavg_5(strTodouble2(input[13]));
//		setSubavg_10(strTodouble(input[15]));
//		setSubavg_20(strTodouble(input[17]));
//		setSubavg_30(strTodouble(input[19]));
//		setSubavg_60(strTodouble(input[21]));
//		
//		setAftavg_5(strTodouble2(input[13]));
//		setAftavg_10(strTodouble(input[16]));
//		setAftavg_20(strTodouble(input[18]));
//		setAftavg_30(strTodouble(input[20]));
//		setAftavg_60(strTodouble(input[22]));
	}
	public SingleStockInfoPO(String stockinfo){
		String[] info=stockinfo.split("\t");
		setName(info[9]);
		LocalDate cal=LocalDate.parse(info[1]);
		setDate(cal);
		setCode(info[8]);
		setOpen(Double.parseDouble(info[2]));
		setClose(Double.parseDouble(info[5]));
		setHigh(Double.parseDouble(info[3]));
		setLow(Double.parseDouble(info[4]));
		setVolume(Long.parseLong(info[6]));
	}
	public SingleStockInfoPO(String stockinfo,double flag,double flag1){
		String[] info=stockinfo.split("\t");
		setName(info[9]);
		LocalDate cal=LocalDate.parse(info[1]);
		setDate(cal);
		setCode(info[8]);
		setOpen(Double.parseDouble(info[2]));
		setClose(Double.parseDouble(info[5]));
		setHigh(Double.parseDouble(info[3]));
		setLow(Double.parseDouble(info[4]));
		setVolume(Long.parseLong(info[6]));
//		setAdjclose(Double.parseDouble(info[7]));
//		setMarket(info[10]);
//		setStr(stockinfo);
//		last_close=flag;
	}
	
	public SingleStockInfoPO(String stockinfo,int flag){
		String[] info=stockinfo.split("\t");
		setName(info[9]);
		LocalDate cal=LocalDate.parse(info[1]);
		setDate(cal);
		setCode(info[8]);
		setOpen(Double.parseDouble(info[2]));
		setClose(Double.parseDouble(info[5]));
		setHigh(Double.parseDouble(info[3]));
		setLow(Double.parseDouble(info[4]));
		setVolume(Long.parseLong(info[6]));
//		setAdjclose(Double.parseDouble(info[7]));
//		setMarket(info[10]);
//		setStr(stockinfo);
//		last_close=Double.parseDouble(info[11]);
//		setLast_subadjclose(Double.parseDouble(info[12]));
	}
	/**
	 * 获得SingleStockInfoPO中的股票名字
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 获得SingleStockInfoPO中的日期
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
	
	/**
	 * 获得SingleStockInfoPO中的股票编号
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 获得SingleStockInfoPO中的股票开盘价格
	 * @return the open
	 */
	public double getOpen() {
		return open;
	}
	
	
	/**
	 * 获得SingleStockInfoPO中的股票当天最高价格
	 * @return the high
	 */
	
	public double getHigh() {
		return high;
	}
	
	/**
	 * 获得SingleStockInfoPO中的当天股票最低价格
	 * @return the low
	 */
	public double getLow() {
		return low;
	}
	
	/**
	 * 获得SingleStockInfoPO中的股票当天交易量
	 * @return the volume
	 */
	public long getVolume() {
		return volume;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票名字
	 * @param _name the name to set
	 */
	private void setName(String _name) {
		name = _name;
	}
	
	/**
	 * 设置SingleStockInfoPO中的日期
	 * @param _date the date to set
	 */
	private void setDate(LocalDate _date) {
		date = _date;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票编号
	 * @param _code the code to set
	 */
	private void setCode(String _code) {
		code = _code;
	}
	/**
	 * 设置SingleStockInfoPO中的股票开盘价格
	 * @param _open the open to set
	 */
	private void setOpen(double _open) {
		open = _open;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票当天最高价格
	 * @param _high the high to set
	 */
	private void setHigh(double _high) {
		high = _high;
	}
	
	/**
	 * 设置SingleStockInfoPO中的当天股票最低价格
	 * @param _low the low to set
	 */
	private void setLow(double _low) {
		low = _low;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票当天交易量
	 * @param _volume the volume to set
	 */
	private void setVolume(long _volume) {
		volume = _volume;
	}
	/**
	 * 获取未复权今日收盘价
	 * @return the close
	 */
	public double getClose() {
		return close;
	}
	/**
	 * 设置未复权今日收盘价
	 * @param close the close to set
	 */
	private void setClose(double close) {
		this.close = close;
	}
	/**
	 * 获取前复权今日收盘价
	 * @return the subClose
	 */
	public double getSubClose() {
		return subClose;
	}
	/**
	 * 设置前复权今日收盘价
	 * @param subClose the subClose to set
	 */
	private void setSubClose(double subClose) {
		this.subClose = subClose;
	}
	/**
	 * 获取后复权今日收盘价
	 * @return the aftClose
	 */
	public double getAftClose() {
		return aftClose;
	}
	/**
	 * 设置后复权今日收盘价
	 * @param aftClose the aftClose to set
	 */
	private void setAftClose(double aftClose) {
		this.aftClose = aftClose;
	}
	/**
	 * 获取未复权昨日收盘价
	 * @return the lclose
	 */
	public double getLclose() {
		return lclose;
	}
	/**
	 * 设置未复权昨日收盘价
	 * @param lclose the lclose to set
	 */
	private void setLclose(double lclose) {
		this.lclose = lclose;
	}
	/**
	 * 获取前复权昨日收盘价
	 * @return the lsubClose
	 */
	public double getLsubClose() {
		return lsubClose;
	}
	/**
	 * 设置前复权昨日收盘价
	 * @param lsubClose the lsubClose to set
	 */
	private void setLsubClose(double lsubClose) {
		this.lsubClose = lsubClose;
	}
	/**
	 * 获取后复权昨日收盘价
	 * @return the laftClose
	 */
	public double getLaftClose() {
		return laftClose;
	}
	/**
	 * 设置后复权昨日收盘价
	 * @param laftClose the laftClose to set
	 */
	private void setLaftClose(double laftClose) {
		this.laftClose = laftClose;
	}
	/**
	 * 获取未复权涨跌幅
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}
	/**
	 * 设置未复权涨跌幅
	 * @param rate the rate to set
	 */
	private void setRate(double rate) {
		this.rate = rate;
	}
	/**
	 * 获取前复权涨跌幅
	 * @return the subrate
	 */
	public double getSubrate() {
		return subrate;
	}
	/**
	 * 设置前复权涨跌幅
	 * @param subrate the subrate to set
	 */
	private void setSubrate(double subrate) {
		this.subrate = subrate;
	}
	/**
	 * 获取后复权涨跌幅
	 * @return the aftrate
	 */
	public double getAftrate() {
		return aftrate;
	}
	/**
	 * 设置后复权涨跌幅
	 * @param aftrate the aftrate to set
	 */
	private void setAftrate(double aftrate) {
		this.aftrate = aftrate;
	}
	private double strTodouble1(String str){
		double result=Double.parseDouble(str);
		int i=(int)(result*100+0.5);
		return i/100.0;
	}
	private double strTodouble2(String str){
		double result=Double.parseDouble(str);
		int i=(int)(result*10000+0.5);
		return i/10000.0;
	}
//	/**
//	 * @return the subavg_5
//	 */
//	public double getSubavg_5() {
//		return subavg_5;
//	}
//	/**
//	 * @param subavg_5 the subavg_5 to set
//	 */
//	public void setSubavg_5(double subavg_5) {
//		this.subavg_5 = subavg_5;
//	}
//	/**
//	 * @return the subavg_10
//	 */
//	public double getSubavg_10() {
//		return subavg_10;
//	}
//	/**
//	 * @param subavg_10 the subavg_10 to set
//	 */
//	public void setSubavg_10(double subavg_10) {
//		this.subavg_10 = subavg_10;
//	}
//	/**
//	 * @return the subavg_20
//	 */
//	public double getSubavg_20() {
//		return subavg_20;
//	}
//	/**
//	 * @param subavg_20 the subavg_20 to set
//	 */
//	public void setSubavg_20(double subavg_20) {
//		this.subavg_20 = subavg_20;
//	}
//	/**
//	 * @return the subavg_30
//	 */
//	public double getSubavg_30() {
//		return subavg_30;
//	}
//	/**
//	 * @param subavg_30 the subavg_30 to set
//	 */
//	public void setSubavg_30(double subavg_30) {
//		this.subavg_30 = subavg_30;
//	}
//	/**
//	 * @return the subavg_60
//	 */
//	public double getSubavg_60() {
//		return subavg_60;
//	}
//	/**
//	 * @param subavg_60 the subavg_60 to set
//	 */
//	public void setSubavg_60(double subavg_60) {
//		this.subavg_60 = subavg_60;
//	}
	/**
	 * @return the aftavg_5
	 */
	public double getAftavg_5() {
		return aftavg_5;
	}
	/**
	 * @param aftavg_5 the aftavg_5 to set
	 */
	public void setAftavg_5(double aftavg_5) {
		this.aftavg_5 = aftavg_5;
	}
//	/**
//	 * @return the aftavg_10
//	 */
//	public double getAftavg_10() {
//		return aftavg_10;
//	}
//	/**
//	 * @param aftavg_10 the aftavg_10 to set
//	 */
//	public void setAftavg_10(double aftavg_10) {
//		this.aftavg_10 = aftavg_10;
//	}
//	/**
//	 * @return the aftavg_20
//	 */
//	public double getAftavg_20() {
//		return aftavg_20;
//	}
//	/**
//	 * @param aftavg_20 the aftavg_20 to set
//	 */
//	public void setAftavg_20(double aftavg_20) {
//		this.aftavg_20 = aftavg_20;
//	}
//	/**
//	 * @return the aftavg_30
//	 */
//	public double getAftavg_30() {
//		return aftavg_30;
//	}
//	/**
//	 * @param aftavg_30 the aftavg_30 to set
//	 */
//	public void setAftavg_30(double aftavg_30) {
//		this.aftavg_30 = aftavg_30;
//	}
//	/**
//	 * @return the aftavg_60
//	 */
//	public double getAftavg_60() {
//		return aftavg_60;
//	}
//	/**
//	 * @param aftavg_60 the aftavg_60 to set
//	 */
//	public void setAftavg_60(double aftavg_60) {
//		this.aftavg_60 = aftavg_60;
//	}
}
