package po;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StockSetInfoPO {
	private Calendar date;//该天日期
	private List<String> haltList;//停牌股票的股票编号列表
	private List<String> haltNameList;//停牌股票的股票编号列表
	private List<SingleStockInfoPO> stockList;//股票池中股票当天的信息
	private int haltNumber;//停牌股票的数量
	private boolean allhalt;//
	/**
	 * 构造方法
	 * @param _date 日期
	 */
	public StockSetInfoPO(Calendar _date){
		setDate(_date);
		setHaltList(null);
		setHaltNameList(null);
		setHaltNumber(0);
		stockList=new ArrayList<SingleStockInfoPO>();
	}
	/**
	 * 构造方法
	 * @param _date 日期
	 * @param _haltlist 停牌股票编号列表
	 * @param _haltlist 停牌股票名字列表
	 * @param _stocklist 股票池中股票信息的列表
	 * @param _haltnum 股票池中停牌股票的数量
	 */
	public StockSetInfoPO(Calendar _date,
			List<String> _haltlist,List<String> _haltnamelist,
			List<SingleStockInfoPO> _stocklist,int _haltnum){
		setDate(_date);
		setHaltList(_haltlist);
		setHaltNameList(_haltnamelist);
		setStockList(_stocklist);
		setHaltNumber(_haltnum);
	}
	
	/**
	 * 不存在停牌股票时的构造方法
	 * @param _date 日期
	 * @param _stocklist 股票池中股票信息的列表
	 */
	public StockSetInfoPO(Calendar _date,List<SingleStockInfoPO> _stocklist){
		setDate(_date);
		setHaltList(null);
		setHaltNameList(null);
		setStockList(_stocklist);
		setHaltNumber(0);
	}
	
	public void addStockInfo(String str,String code,String name){
		stockList.add(new SingleStockInfoPO(str,name,code,date));
	}
	public void addHalt(String code,String name){
		if(haltNumber==0){
			haltList=new ArrayList<String>();
			haltNameList=new ArrayList<String>();
		}
		haltList.add(code);
		haltNameList.add(name);
		haltNumber=haltNumber+1;
	}
	
	/**
	 *获取日期
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * 设置日期
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * 获取停牌股票列表
	 * @return the haltList
	 */
	public List<String> getHaltList() {
		return haltList;
	}

	/**
	 * 设置停牌股票列表
	 * @param haltList the haltList to set
	 */
	public void setHaltList(List<String> haltList) {
		this.haltList = haltList;
	}

	/**
	 * 获取股票信息列表
	 * @return the stockList
	 */
	public List<SingleStockInfoPO> getStockList() {
		return stockList;
	}

	/**
	 * 设置股票信息列表
	 * @param stockList the stockList to set
	 */
	public void setStockList(List<SingleStockInfoPO> stockList) {
		this.stockList = stockList;
	}

	/**
	 * 获取停牌股票数量
	 * @return the haltNumber
	 */
	public int getHaltNumber() {
		return haltNumber;
	}

	/**
	 * 设置停牌股票数量
	 * @param haltNumber the haltNumber to set
	 */
	public void setHaltNumber(int haltNumber) {
		this.haltNumber = haltNumber;
	}
	/**
	 * @return the haltNameList
	 */
	public List<String> getHaltNameList() {
		return haltNameList;
	}
	/**
	 * @param haltNameList the haltNameList to set
	 */
	public void setHaltNameList(List<String> haltNameList) {
		this.haltNameList = haltNameList;
	}
}
