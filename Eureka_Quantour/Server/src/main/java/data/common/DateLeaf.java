package data.common;

import java.util.HashMap;

public class DateLeaf extends TrieNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -689936797450910210L;
	private int cal;
	private TrieNode previous;
	private HashMap<Integer,StockLeaf> dateinfo;
	private TrieNode next;
	private boolean isLeaf;
	public DateLeaf(TrieNode _parent){
		setLeaf(false);	
		setParent(_parent);
	}
	public DateLeaf(int code,int row,int year,int month,int day,TrieNode _parent){
		
		setLayer(4);
		
		dateinfo=new HashMap<Integer,StockLeaf>();
		StockLeaf leaf=new StockLeaf(row,this);
		dateinfo.put(code, leaf);
		
		setParent(_parent);
		
		setChildsize(0);
		setLeaf(true);
		
		setCal(year * 10000 +month * 100 + day);
	}
	public void activation(int code,int row,int year,int month,int day){
		setLayer(4);
		
		dateinfo=new HashMap<Integer,StockLeaf>();
		StockLeaf leaf=new StockLeaf(row,this);
		dateinfo.put(code, leaf);
		
		setChildsize(0);
		setLeaf(true);
		
		setCal(year * 10000 +month * 100 + day);
		
	}
	/**
	 * @return the next
	 */
	public TrieNode getNext() {
		return next;
	}
	/**
	 * @param next the next to set
	 */
	public void setNext(TrieNode next) {
		this.next = next;
	}
	/**
	 * @return the previous
	 */
	public TrieNode getPrevious() {
		return previous;
	}
	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(TrieNode previous) {
		this.previous = previous;
	}
	/**
	 * @return the isLeaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}
	/**
	 * @param isLeaf the isLeaf to set
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	/**
	 * @return the dateinfo
	 */
	public HashMap<Integer,StockLeaf> getDateinfo() {
		return dateinfo;
	}
	/**
	 * @param dateinfo the dateinfo to set
	 */
	public void setDateinfo(HashMap<Integer,StockLeaf> dateinfo) {
		this.dateinfo = dateinfo;
	}
	public boolean compareTo(DateLeaf leaf){
		if(this.cal>leaf.getCal()){
			return false;
		}
		else if(this.cal<leaf.getCal()){
			return true;
		}
		else{
			throw new NullPointerException();
		}
	}
	/**
	 * @return the cal
	 */
	public int getCal() {
		return cal;
	}
	/**
	 * @param cal the cal to set
	 */
	public void setCal(int cal) {
		this.cal = cal;
	}
}
