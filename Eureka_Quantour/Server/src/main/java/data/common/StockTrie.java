package data.common;

public class StockTrie {
	private TrieNode root;
	private StockLeaf min;
	private StockLeaf max;
	private StockLeaf remain;
	private boolean bigflag;
	private int stockCode;
	public StockTrie(int stoCode){
		root=new TrieNode(1,null,false);
		min=null;
		remain=null;
		bigflag=false;
		setStockCode(stoCode);
	}
	public boolean add(int year,int month,int day,int row){
		TrieNode temp;
		if(year>2004){
			temp=root.getChild().get(year-2005);
			if(month>0){
				temp=temp.getChild().get(month-1);
				if(day>0){
					StockLeaf t=(StockLeaf)temp.getChild().get(day-1);
					if(t.isLeaf()){
						return false;
					}
					else{
						t.activation(row, year, month, day);
						setPrevious(t);
						bigflag=false;
						return true;
					}
				}
			}
		}
		return false;
	}
	private void setPrevious(StockLeaf leaf){
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
					setPrevious(leaf);
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
					setPrevious(leaf);
				}
			}
		}
	}
	
	
	
	public StockLeaf get(int cal){
		int year= cal / 10000;
		int month= (cal -year * 10000 ) / 100;
		int day=cal - year * 10000 - month * 100;
		return get(year,month,day);
	}
	public StockLeaf get(int year,int month,int day){
		if(year>2004){
			return get(month,day,root.getChild().get(year-2005));
		}
		return null;
	}
	private StockLeaf get(int month,int day,TrieNode node){
		if(month>0){
			return get(day,node.getChild().get(month-1));
		}
		return null;
	}
	private StockLeaf get(int day,TrieNode node){
		if(day>0){
			StockLeaf leaf=(StockLeaf) node.getChild().get(day-1);
			if(leaf.isLeaf()){
				return leaf;
			}
			else{
				return null;
			}
		}
		return null;
	}
	
	public StockLeaf getMin(){
		return min;
	}
	/**
	 * @return the max
	 */
	public StockLeaf getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(StockLeaf max) {
		this.max = max;
	}
	/**
	 * @return the stockCode
	 */
	public int getStockCode() {
		return stockCode;
	}
	/**
	 * @param stockCode the stockCode to set
	 */
	public void setStockCode(int stockCode) {
		this.stockCode = stockCode;
	}
}
