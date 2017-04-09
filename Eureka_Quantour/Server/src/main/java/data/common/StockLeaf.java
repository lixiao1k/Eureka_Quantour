package data.common;

public class StockLeaf extends TrieNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3847097557621624415L;
	private int cal;
	private TrieNode previous;
	private int dateinfo;
	private TrieNode next;
	private boolean isLeaf;
	public StockLeaf(TrieNode _parent){
		setLeaf(false);	
		setParent(_parent);
	}
	public StockLeaf(int row,int year,int month,int day,TrieNode _parent){
		
		setLayer(4);
		
		dateinfo=row;
		
		setParent(_parent);
		
		setChildsize(0);
		setLeaf(true);
		
		setCal(year * 10000 +month * 100 + day);
	}
	public void activation(int row,int year,int month,int day){
		setLayer(4);
		
		dateinfo=row;
		
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
	public int getDateinfo() {
		return dateinfo;
	}
	/**
	 * @param dateinfo the dateinfo to set
	 */
	public void setDateinfo(int row) {
		this.dateinfo = row;
	}
	public boolean compareTo(StockLeaf leaf){
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
