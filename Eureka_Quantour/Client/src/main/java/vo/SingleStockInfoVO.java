package vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 
 * @Description: gather the data of stock in one day
 * @author: hzp
 * @time: 2017.3.6
 */
public class SingleStockInfoVO implements Serializable, Comparable<SingleStockInfoVO>{

	private static final long serialVersionUID = -185701255295387970L;
	
	/**
	 *@param: name: stock's name
	 *@param: date: the time of these data
	 *@param: open: the price of stock when market is open
	 *@param: close: the price of stock when market is closed
	 *@param: high: the highest price of stock today
	 *@param: low: the lowest price of stock today
	 *@param: volume: the total trade of stock today
	 *@param: adjclose: 
	 *@param: market: the name of where stock list
	 */
	private String name = "";
	private LocalDate date ;
	private String code = "";
	private double open = 0.0;
	private double close = 0.0;
	private double high = 0.0;
	private double low = 0.0;
	private long volume = 0;
	private double adjclose = 0.0;
	private double fudu=0.0;
	
	public String getName(){
		return this.name;
	}
	public LocalDate getDate(){
		return this.date;
	}
	public String getCode(){
		return this.code;
	}
	public double getOpen(){
		return this.open;
	}
	public double getClose(){
		return this.close;
	}
	public double getHigh(){
		return this.high;
	}
	public double getLow(){
		return this.low;
	}
	public long getVolume(){
		return this.volume;
	}
	public double getAdjclose(){
		return this.adjclose;
	}

	public void setName(String name){
		this.name = name;
	}
	public void setDate(LocalDate date){
		this.date = date;
	}
	public void setCode(String code){
		this.code = code;
	}
	public void setOpen(double open){
		this.open = open;
	}
	public void setClose(double close){
		this.close = close;
	}
	public void setHigh(double high){
		this.high = high;
	}
	public void setLow(double low){
		this.low = low;
	}
	public void setVolume(long volume){
		this.volume = volume;
	}
	public void setAdjclose(double adjclose){
		this.adjclose = adjclose;
	}
	public double getFudu() {
		return fudu;
	}
	public void setFudu(double fudu) {
		this.fudu = fudu;
	}

	public SingleStockInfoVO initObject(String name, LocalDate date, String code, double open, double close,
										double high, double low, int volume, double adjclose, String market,double fudu){
		SingleStockInfoVO ssi = new SingleStockInfoVO();
		ssi.setName(name);
		ssi.setDate(date);
		ssi.setCode(code);
		ssi.setOpen(open);
		ssi.setClose(close);
		ssi.setHigh(high);
		ssi.setLow(low);
		ssi.setVolume(volume);
		ssi.setAdjclose(adjclose);
		ssi.setFudu(fudu);
		return ssi;
	}
	public SingleStockInfoVO(){}
	public SingleStockInfoVO(String name){
		this.name=name;
	}


	@Override
	public int compareTo(SingleStockInfoVO o) {

		return  (int) (Math.rint((this.getFudu()-o.getFudu())*100));
	}
}
