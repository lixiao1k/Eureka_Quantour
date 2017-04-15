package data.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.parse.Parse;

public class DateLeaf extends TrieNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -689936797450910210L;
	private int cal;
	private TrieNode previous;
	private HashMap<Integer,Integer> dateinfo;
	private List<String> stockinfo;
	private List<String> namelist;
	private int nownumber;
	private TrieNode next;
	private boolean isLeaf;
	public DateLeaf(TrieNode _parent){
		setLeaf(false);	
		setParent(_parent);
		nownumber=0;
	}
	public DateLeaf(int code,String row,int year,int month,int day,TrieNode _parent){
		
		setLayer(4);
		
		dateinfo=new HashMap<Integer,Integer>();
		dateinfo.put(code, nownumber);
		stockinfo=new ArrayList<String>();
		namelist=new ArrayList<String>();
		namelist.add(Parse.getInstance().supCode(code));
		stockinfo.add(row);
		setParent(_parent);
		nownumber=1;
		setChildsize(0);
		setLeaf(true);
		
		setCal(year * 10000 +month * 100 + day);
	}
	public void activation(int code,String row,int year,int month,int day){
		setLayer(4);
		
		dateinfo=new HashMap<Integer,Integer>();
		dateinfo.put(code, nownumber);
		stockinfo=new ArrayList<String>();
		stockinfo.add(row);
		namelist=new ArrayList<String>();
		namelist.add(Parse.getInstance().supCode(code));
		nownumber=1;
		setChildsize(0);
		setLeaf(true);
		
		setCal(year * 10000 +month * 100 + day);
		
	}
	public void addstock(int code,String row){
		dateinfo.put(code, nownumber);
		stockinfo.add(row);
		namelist.add(Parse.getInstance().supCode(code));
		nownumber++;
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
	public HashMap<Integer,Integer> getDateinfo() {
		return dateinfo;
	}
	/**
	 * @param dateinfo the dateinfo to set
	 */
	public void setDateinfo(HashMap<Integer,Integer> dateinfo) {
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
	/**
	 * @return the stockinfo
	 */
	public List<String> getStockinfo() {
		return stockinfo;
	}
	/**
	 * @param stockinfo the stockinfo to set
	 */
	public void setStockinfo(List<String> stockinfo) {
		this.stockinfo = stockinfo;
	}
	/**
	 * @return the nownumber
	 */
	public int getNownumber() {
		return nownumber;
	}
	/**
	 * @param nownumber the nownumber to set
	 */
	public void setNownumber(int nownumber) {
		this.nownumber = nownumber;
	}
	/**
	 * @return the namelist
	 */
	public List<String> getNamelist() {
		return namelist;
	}
	/**
	 * @param namelist the namelist to set
	 */
	public void setNamelist(List<String> namelist) {
		this.namelist = namelist;
	}
}
