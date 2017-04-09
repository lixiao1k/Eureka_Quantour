package data.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrieNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8519107065167533342L;
	private TrieNode parent;
	private List<TrieNode> child;
	private int childsize;
	private int layer;
	public TrieNode(){
	}
	public TrieNode(int _layer,TrieNode _parent,boolean stockOrdate){
		setLayer(_layer);
		setParent(_parent);
		child=new ArrayList<TrieNode>();	
		setChildsize(0);
		switch(_layer){
		case 1:
			initYear(stockOrdate);
			break;
		case 2:
			initMonth(stockOrdate);
			break;
		case 3:
			initDay(stockOrdate);
			break;
		}
	}
	
	
	private void initYear(boolean stockOrdate){
		for(int i=5;i<18;i++){
			TrieNode temp=new TrieNode(2,this,stockOrdate);
			child.add(temp);
		}
	}
	private void initMonth(boolean stockOrdate){
		for(int i=1;i<13;i++){
			TrieNode temp=new TrieNode(3,this,stockOrdate);
			child.add(temp);
		}
	}
	private void initDay(boolean stockOrdate){
		if(stockOrdate){
			for(int i=1;i<32;i++){
				DateLeaf temp=new DateLeaf(this);
				child.add(temp);
			}
		}
		else{
			for(int i=1;i<32;i++){
				StockLeaf temp=new StockLeaf(this);
				child.add(temp);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	/**
	 * @return the layer
	 */
	public int getLayer() {
		return layer;
	}
	/**
	 * @param layer the layer to set
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}
	/**
	 * @return the child
	 */
	public List<TrieNode> getChild() {
		return child;
	}
	/**
	 * @param child the child to set
	 */
	public void setChild(List<TrieNode> child) {
		this.child = child;
	}
	/**
	 * @return the parent
	 */
	public TrieNode getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(TrieNode parent) {
		this.parent = parent;
	}
	/**
	 * @return the childsize
	 */
	public int getChildsize() {
		return childsize;
	}
	/**
	 * @param childsize the childsize to set
	 */
	public void setChildsize(int childsize) {
		this.childsize = childsize;
	}
	/**
	 * @return the cal
	 */
}
