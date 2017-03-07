package po;

import java.util.Calendar;
/**
 * SingleStockInfo的PO类，用来维护SingleStock的持久对象
 * @author	刘宇翔
 *
 */
public class SingleStockInfoPO 
{
	private String name;//这支股票的名字
	private Calendar date;//这条股票信息的时间
	private String code;//这支股票的编号
	private double open;//这支股票当天的开盘价格
	private double close;//这支股票当天的收盘指数
	private double high;//这支股票当天的最高价格
	private double low;//这支股票当天的最低价格	
	private int volume;//这支股票当天的交易量
	private double adjclose;//这支股票的复权收盘指数
	private String market;//这支股票所处的市场
	/**
	 * 
	 * @param _name String类型，这支股票的名字
	 * @param _date	Calendar类型，这条股票信息的时间
	 * @param _code String类型，这支股票的编号
	 * @param _open	double类型，这支股票当天的开盘价格
	 * @param _close double类型，这支股票当天的收盘指数
	 * @param _high	double类型，这支股票当天的最高价格
	 * @param _low	double类型，这支股票当天的最低价格	
	 * @param _volume int类型，这支股票当天的交易量
	 * @param _adjclose	double类型，这支股票的复权收盘指数
	 * @param _market String类型，这支股票所处的市场
	 */
	public SingleStockInfoPO(String _name,Calendar _date,String _code,double _open,
			double _close,double _high,double _low,int _volume,double _adjclose,String _market)
	{
		setName(_name);
		setDate(_date);
		setCode(_code);
		setOpen(_open);
		setClose(_close);
		setHigh(_high);
		setLow(_low);
		setVolume(_volume);
		setAdjclose(_adjclose);
		setMarket(_market);
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
	public Calendar getDate() {
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
	 * 获得SingleStockInfoPO中的股票收盘价格
	 * @return the close
	 */
	public double getClose() {
		return close;
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
	public int getVolume() {
		return volume;
	}
	
	/**
	 * 获得SingleStockInfoPO中的股票当天复权收盘价格
	 * @return the adjclose
	 */
	public double getAdjclose() {
		return adjclose;
	}
	
	/**
	 * 获得SingleStockInfoPO中的股票所处的市场
	 * @return the market
	 */
	public String getMarket() {
		return market;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票名字
	 * @param name the name to set
	 */
	private void setName(String _name) {
		name = _name;
	}
	
	/**
	 * 设置SingleStockInfoPO中的日期
	 * @param date the date to set
	 */
	private void setDate(Calendar _date) {
		date = _date;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票编号
	 * @param code the code to set
	 */
	private void setCode(String _code) {
		code = _code;
	}
	/**
	 * 设置SingleStockInfoPO中的股票开盘价格
	 * @param open the open to set
	 */
	private void setOpen(double _open) {
		open = _open;
	}
	/**
	 * 设置SingleStockInfoPO中的股票收盘价格
	 * @param close the close to set
	 */
	private void setClose(double _close) {
		close = _close;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票当天最高价格
	 * @param high the high to set
	 */
	private void setHigh(double _high) {
		high = _high;
	}
	
	/**
	 * 设置SingleStockInfoPO中的当天股票最低价格
	 * @param low the low to set
	 */
	private void setLow(double _low) {
		low = _low;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票当天交易量
	 * @param volume the volume to set
	 */
	private void setVolume(int _volume) {
		volume = _volume;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票当天复权收盘价格
	 * @param adjclose the adjclose to set
	 */
	private void setAdjclose(double _adjclose) {
		adjclose = _adjclose;
	}
	
	/**
	 * 设置SingleStockInfoPO中的股票所处的市场
	 * @param market the market to set
	 */
	private void setMarket(String _market) {
		market = _market;
	}
}
