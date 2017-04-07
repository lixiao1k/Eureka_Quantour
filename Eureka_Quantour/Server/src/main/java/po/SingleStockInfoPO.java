package po;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vo.SingleStockInfoVO;
/**
 * SingleStockInfo的PO类，用来维护SingleStock的持久对象
 * @author	刘宇翔
 *
 */
public class SingleStockInfoPO 
{
	private String name;//这支股票的名字
	private String code;//这支股票的编号
	
	private Calendar date;//这条股票信息的时间
	
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
			Calendar _date,
			double _open,double _high,double _low,int _volume,
			double _close,double _subclose,double _aftclose,
			double _lclose,double _lsubclose,double _laftclose,
			double _rate,double _subrate,double _aftrate)
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
	}
	public SingleStockInfoPO(String stockinfo){
		String[] info=stockinfo.split("\t");
		setName(info[9]);
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		Calendar cal=Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(info[1]));
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("日期格式有错");
		}
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
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		Calendar cal=Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(info[1]));
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("日期格式有错");
		}
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
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		Calendar cal=Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(info[1]));
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("日期格式有错");
		}
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
	private void setDate(Calendar _date) {
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
	 * 
	 * @Description: 用于将SingleStockInfoPO转化为SingleStockInfoVO
	 * @author: hzp
	 * @time: 2017年3月14日
	 * @return: SingleStockInfoVO
	 */
	public SingleStockInfoVO POToVO(SingleStockInfoPO ssiPO){
		if( ssiPO==null )
			return null;
		SingleStockInfoVO ssiVO = new SingleStockInfoVO();
		ssiVO.setName(ssiPO.getName());
		ssiVO.setDate(ssiPO.getDate());
		ssiVO.setCode(ssiPO.getCode());
		ssiVO.setOpen(ssiPO.getOpen());
		ssiVO.setClose(ssiPO.getClose());
		ssiVO.setHigh(ssiPO.getHigh());
		ssiVO.setLow(ssiPO.getLow());
		ssiVO.setVolume(ssiPO.getVolume());
//		ssiVO.setAdjclose(ssiPO.getAdjclose());
//		ssiVO.setMarket(ssiPO.getMarket());
		return ssiVO;
	}
	/**
	 * 
	 * @Description: 用于将List<SingleStockInfoPO>转化为List<SingleStockInfoVO>
	 * @author: hzp
	 * @time: 2017年3月14日
	 * @return: SingleStockInfoVO
	 */
	public List<SingleStockInfoVO> POToVO(List<SingleStockInfoPO> ssiPO){
		List<SingleStockInfoVO> ssiVO = new ArrayList<SingleStockInfoVO>();
		if( ssiPO==null )
			return null;
		else{
			for( int i=0; i<ssiPO.size(); i++ ){
				ssiVO.add( POToVO(ssiPO.get(i)) );
			}
			return ssiVO;
		}
	}
	/**
	 * @return the close
	 */
	public double getClose() {
		return close;
	}
	/**
	 * @param close the close to set
	 */
	public void setClose(double close) {
		this.close = close;
	}
	/**
	 * @return the subClose
	 */
	public double getSubClose() {
		return subClose;
	}
	/**
	 * @param subClose the subClose to set
	 */
	public void setSubClose(double subClose) {
		this.subClose = subClose;
	}
	/**
	 * @return the aftClose
	 */
	public double getAftClose() {
		return aftClose;
	}
	/**
	 * @param aftClose the aftClose to set
	 */
	public void setAftClose(double aftClose) {
		this.aftClose = aftClose;
	}
	/**
	 * @return the lclose
	 */
	public double getLclose() {
		return lclose;
	}
	/**
	 * @param lclose the lclose to set
	 */
	public void setLclose(double lclose) {
		this.lclose = lclose;
	}
	/**
	 * @return the lsubClose
	 */
	public double getLsubClose() {
		return lsubClose;
	}
	/**
	 * @param lsubClose the lsubClose to set
	 */
	public void setLsubClose(double lsubClose) {
		this.lsubClose = lsubClose;
	}
	/**
	 * @return the laftClose
	 */
	public double getLaftClose() {
		return laftClose;
	}
	/**
	 * @param laftClose the laftClose to set
	 */
	public void setLaftClose(double laftClose) {
		this.laftClose = laftClose;
	}
	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}
	/**
	 * @return the subrate
	 */
	public double getSubrate() {
		return subrate;
	}
	/**
	 * @param subrate the subrate to set
	 */
	public void setSubrate(double subrate) {
		this.subrate = subrate;
	}
	/**
	 * @return the aftrate
	 */
	public double getAftrate() {
		return aftrate;
	}
	/**
	 * @param aftrate the aftrate to set
	 */
	public void setAftrate(double aftrate) {
		this.aftrate = aftrate;
	}
}
