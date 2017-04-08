package data.common;

import java.util.HashMap;

public class StockHashTree {
	private HashMap<Integer,StockTrie> hashtree;
	public StockHashTree(){
		setHashtree(new HashMap<Integer,StockTrie>());
	}
	public void add(int year,int month,int day,int code , int row){
		if(hashtree.containsKey(code)){
			hashtree.get(code).add(year, month, day, row);
		}
		else{
			StockTrie temp=new StockTrie(code);
			temp.add(year, month, day, row);
			hashtree.put(code, temp);
		}
	}
	public StockTrie get(int code){
		if(hashtree.containsKey(code)){
			return hashtree.get(code);
		}
		else{
			return null;
		}
	}
	public StockLeaf get(int cal,int code){
		if(hashtree.containsKey(code)){
			return hashtree.get(code).get(cal);
		}
		else{
			return null;
		}
	}
	/**
	 * @return the hashtree
	 */
	public HashMap<Integer,StockTrie> getHashtree() {
		return hashtree;
	}
	/**
	 * @param hashtree the hashtree to set
	 */
	private void setHashtree(HashMap<Integer,StockTrie> hashtree) {
		this.hashtree = hashtree;
	}
	
}
