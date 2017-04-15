package data.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
	private List<List<Integer>> stockLinkedList;
	private List<Integer> stockremain;
	private List<Boolean> stockbigflag;
	private int stocknumber;
	public DateTrie(){
		root=new TrieNode(1,null,true);
		min=null;
		remain=null;
		bigflag=false;
		setStocknumber(0);
		setStockindex(new HashMap<Integer,Integer>());
		setStockremain(new ArrayList<Integer>());
		setStockbigflag(new ArrayList<Boolean>());
		stockLinkedList=new ArrayList<List<Integer>>();
	}

	public void clear(){
		stockremain=null;
		stockbigflag=null;
		remain=null;
		System.gc();
	}
	public void add(int cal,int code){
		int year= cal / 10000;
		int month= (cal -year * 10000 ) / 100;
		int day=cal - year * 10000 - month * 100;
		add(year,month,day,code);
	}
	public boolean add(int year,int month,int day,int code){
		TrieNode temp;
		int cal=year * 10000 + month * 100 + day;
		if(year>2004){
			temp=root.getChild().get(year-2005);
			if(month>0){
				temp=temp.getChild().get(month-1);
				if(day>0){
					DateLeaf t=(DateLeaf)temp.getChild().get(day-1);
					if(t.isLeaf()){
						t.addstock(code);
						return true;
					}
					else{
						t.activation(code, year, month, day);
						setPrevious(t);
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
	private void setLeafPrevious(Integer remain,List<Integer> datelist,boolean bigflag,int day){
		if(remain==null){
			remain=0;
			datelist=new ArrayList<Integer>();
			datelist.add(day);
		}
		else{
			if(datelist.get(remain) < day){
				if(datelist.size()<=remain){
					datelist.add(day);
					remain++;
				}
				else{
					remain=remain++;
					bigflag=true;
					setLeafPrevious(remain,datelist,bigflag,day);
				}
			}
			else{
				if(bigflag){
					datelist.add(remain,day);
				}
				else{
					remain=0;
					setLeafPrevious(remain,datelist,bigflag,day);
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
	private void setStockremain(ArrayList<Integer> arrayList) {
		stockremain=arrayList;
	}
}
