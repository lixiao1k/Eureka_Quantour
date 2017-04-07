package data.common;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

public class DateBplusTree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3378019605548410766L;
	protected Node root;
	protected int order;
	protected Node minnode;
	boolean check;
	public Node getMin(){
		return minnode;
	}
	public void setMin(Node node){
		minnode=node;
	}
	public Node getRoot() { 
        return root; 
    } 
 
    public void setRoot(Node root) { 
        this.root = root; 
    } 
 
    public int getOrder() { 
        return order; 
    } 
    public StockBplusTree getMarket(int key) { 
        return root.get(key); 
    } 
    public int getSingleInfot(int key,int code){
    	return root.get(key).getMarket(code);
    }
    public void insertInfo(int key, Entry<Integer,Integer> obj) { 
        root.insertNode(key, obj, this);
    } 
    public void insertInfo(int key, int code,int row) { 
    	root.insertNode(key, new AbstractMap.SimpleEntry<Integer,Integer>(code,row), this);
    } 
    public void setOrder(int order) { 
        this.order = order; 
    } 
    public void toString(Node node){
    	if(node.isLeaf()){
    		for(int i=0;i<node.getIndex().size();i++){
    			System.out.println(node.getIndex().get(i).getKey());
    		}
    	}
    	else{
    		for(int i=0;i<node.getChildren().size();i++){
    			toString(node.getChildren().get(i));
    		}	
    	}
    }
    public DateBplusTree(int order){ 
        if (order < 3) { 
            System.out.print("order must be greater than 2"); 
            System.exit(0); 
        } 
        this.order = order; 
        root = new Node(true, true); 
        minnode = root; 
        check=false;
    }
}
