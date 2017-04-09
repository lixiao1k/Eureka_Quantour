package data.common;

import java.io.Serializable;

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
	public DateTrie(){
		root=new TrieNode(1,null,true);
		min=null;
		remain=null;
		bigflag=false;
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
						t.getDateinfo().put(code, row);
						return true;
					}
					else{
						t.activation(code, row, year, month, day);
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
}
