package data.common;

public class StockLeaf extends TrieNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3847097557621624415L;
	
	private TrieNode previous;
	private int dateinfo;
	private TrieNode next;
	private boolean isLeaf;
	
	public StockLeaf(TrieNode _parent){
		setLeaf(false);	
		setParent(_parent);
	}
	public StockLeaf(int row,TrieNode _parent){
		
		setLayer(5);
		
		dateinfo=row;
		
		setParent(_parent);
		
		setChildsize(0);
		setLeaf(true);
	}
	public void activation(int row,int day){
		setLayer(5);
		
		dateinfo=row;
		
		setChildsize(0);
		setLeaf(true);
		
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
		if(((DateLeaf) this.getParent()).getCal()>((DateLeaf) leaf.getParent()).getCal()){
			return false;
		}
		else if(((DateLeaf) this.getParent()).getCal()<((DateLeaf) leaf.getParent()).getCal()){
			return true;
		}
		else{
			throw new NullPointerException();
		}
	}
}
