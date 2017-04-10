package data.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DateTrie implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8358562782181512677L;
	private TrieNode root;
	private DateLeaf min;
	private DateLeaf max;
	private DateLeaf remain;
	private boolean bigflag;
	private HashMap<Integer,Integer> stockindex;
	private List<StockLeaf> stockmax;
	private List<StockLeaf> stockmin;
	private List<StockLeaf> stockremain;
	private List<Boolean> stockbigflag;
	private int stocknumber;
	public DateTrie(){
		root=new TrieNode(1,null,true);
		min=null;
		remain=null;
		bigflag=false;
		setStocknumber(0);
		setStockindex(new HashMap<Integer,Integer>());
		setStockmax(new ArrayList<StockLeaf>());
		setStockmin(new ArrayList<StockLeaf>());
		setStockremain(new ArrayList<StockLeaf>());
		setStockbigflag(new ArrayList<Boolean>());
	}
	public void clear(){
		stockremain=null;
		stockbigflag=null;
		remain=null;
		System.gc();
	}
	public boolean add(int year,int month,int day,int code,int row){
		TrieNode temp;
		if(year>2004){
			temp=root.getChild().get(year-2005);
			if(month>0){
				temp=temp.getChild().get(month-1);
				if(day>0){
					DateLeaf t=(DateLeaf)temp.getChild().get(day-1);
					if(t.isLeaf()){
						StockLeaf leaf=new StockLeaf(row,t);
//						Integer judge=stockindex.putIfAbsent(code, stocknumber);
//						if(judge==null){
//							stockmax.add(null);
//							stockmin.add(null);
//							stockremain.add(null);
//							stockbigflag.add(false);
//							stocknumber++;
//						}
//						int index=stockindex.get(code);
//						StockLeaf remain=stockremain.get(index);
//						StockLeaf min=stockmin.get(index);
//						StockLeaf max=stockmax.get(index);
//						boolean bigflag=stockbigflag.get(index);
						t.getDateinfo().put(code, leaf);
//						setLeafPrevious(remain,min,max,bigflag,leaf);
						return true;
					}
					else{
						t.activation(code, row, year, month, day);
//						Integer judge=stockindex.putIfAbsent(code, stocknumber);
//						if(judge==null){
//							stockmax.add(null);
//							stockmin.add(null);
//							stockremain.add(null);
//							stockbigflag.add(false);
//							stocknumber++;
//						}
						setPrevious(t);
//						int index=stockindex.get(code);
//						StockLeaf remain=stockremain.get(index);
//						StockLeaf min=stockmin.get(index);
//						StockLeaf max=stockmax.get(index);
//						boolean bigflag=stockbigflag.get(index);
//						setLeafPrevious(remain,min,max,bigflag,t.getDateinfo().get(code));
						bigflag=false;
						return true;
					}
				}
			}
		}
		return false;
	}
	private void setPrevious(DateLeaf leaf){
		if(remain==null){
			remain=leaf;
			min=leaf;
			max=leaf;
		}
		else{
			if(remain.compareTo(leaf)){
				if(remain.getNext()==null){
					remain.setNext(leaf);
					leaf.setPrevious(remain);
					max=leaf;
				}
				else{
					remain=(DateLeaf) remain.getNext();
					bigflag=true;
					setPrevious(leaf);
				}
			}
			else{
				if(bigflag){
					leaf.setNext(remain);
					leaf.setPrevious(remain.getPrevious());
					((DateLeaf)remain.getPrevious()).setNext(leaf);
					remain.setPrevious(leaf);
				}
				else{
					remain=min;
					setPrevious(leaf);
				}
			}
		}
	}
	private void setLeafPrevious(StockLeaf remain,StockLeaf min,StockLeaf max,boolean bigflag,StockLeaf leaf){
		if(remain==null){
			remain=leaf;
			min=leaf;
			max=leaf;
		}
		else{
			if(remain.compareTo(leaf)){
				if(remain.getNext()==null){
					remain.setNext(leaf);
					leaf.setPrevious(remain);
					max=leaf;
				}
				else{
					remain=(StockLeaf) remain.getNext();
					bigflag=true;
					setLeafPrevious(remain,min,max,bigflag,leaf);
				}
			}
			else{
				if(bigflag){
					leaf.setNext(remain);
					leaf.setPrevious(remain.getPrevious());
					((StockLeaf)remain.getPrevious()).setNext(leaf);
					remain.setPrevious(leaf);
				}
				else{
					remain=min;
					setLeafPrevious(remain,min,max,bigflag,leaf);
				}
			}
		}
	}
	
	public DateLeaf get(int cal){
		int year= cal / 10000;
		int month= (cal -year * 10000 ) / 100;
		int day=cal - year * 10000 - month * 100;
		return get(year,month,day);
	}
	public DateLeaf get(int year,int month,int day){
		if(year>2004){
			return get(month,day,root.getChild().get(year-2005));
		}
		return null;
	}
	private DateLeaf get(int month,int day,TrieNode node){
		if(month>0){
			return get(day,node.getChild().get(month-1));
		}
		return null;
	}
	private DateLeaf get(int day,TrieNode node){
		if(day>0){
			DateLeaf leaf=(DateLeaf) node.getChild().get(day-1);
			return leaf;
		}
		return null;
	}
	
	public DateLeaf getMin(){
		return min;
	}
	/**
	 * @return the max
	 */
	public DateLeaf getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(DateLeaf max) {
		this.max = max;
	}
	/**
	 * @return the stockindex
	 */
	public HashMap<Integer,Integer> getStockindex() {
		return stockindex;
	}
	/**
	 * @param stockindex the stockindex to set
	 */
	public void setStockindex(HashMap<Integer,Integer> stockindex) {
		this.stockindex = stockindex;
	}
	/**
	 * @return the stockmax
	 */
	public List<StockLeaf> getStockmax() {
		return stockmax;
	}
	/**
	 * @param stockmax the stockmax to set
	 */
	public void setStockmax(List<StockLeaf> stockmax) {
		this.stockmax = stockmax;
	}
	/**
	 * @return the stockmin
	 */
	public List<StockLeaf> getStockmin() {
		return stockmin;
	}
	/**
	 * @param stockmin the stockmin to set
	 */
	public void setStockmin(List<StockLeaf> stockmin) {
		this.stockmin = stockmin;
	}
	/**
	 * @return the stockremain
	 */
	public List<StockLeaf> getStockremain() {
		return stockremain;
	}
	/**
	 * @param stockremain the stockremain to set
	 */
	public void setStockremain(List<StockLeaf> stockremain) {
		this.stockremain = stockremain;
	}
	/**
	 * @return the stocknumber
	 */
	public int getStocknumber() {
		return stocknumber;
	}
	/**
	 * @param stocknumber the stocknumber to set
	 */
	public void setStocknumber(int stocknumber) {
		this.stocknumber = stocknumber;
	}
	/**
	 * @return the stockbigflag
	 */
	public List<Boolean> getStockbigflag() {
		return stockbigflag;
	}
	/**
	 * @param stockbigflag the stockbigflag to set
	 */
	public void setStockbigflag(List<Boolean> stockbigflag) {
		this.stockbigflag = stockbigflag;
	}
}
