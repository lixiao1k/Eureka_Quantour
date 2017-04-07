package data.common;

import java.io.Serializable;

public class StockBplusTree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8165074587874035276L;

	protected StockNode root;
	protected int order;
	protected StockNode minnode;
	public StockNode getMin(){
		return minnode;
	}
	public void setMin(StockNode node){
		minnode=node;
	}
	public StockNode getRoot() { 
        return root; 
    } 
 
    public void setRoot(StockNode root) { 
        this.root = root; 
    } 
 
    public int getOrder() { 
        return order; 
    } 
    public int getMarket(int key) { 
    	if(root==null){
    		System.out.println("a");
    	}
    	int i=root.get(key);
        return i; 
    } 
    public void insertInfo(int key, int code) { 
        root.insertStockNode(key, code, this);
    } 
    public void setOrder(int order) { 
        this.order = order; 
    } 
    public void toString(StockNode node){
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
    public StockBplusTree(int order){ 
        if (order < 3) { 
            System.out.print("order must be greater than 2"); 
            System.exit(0); 
        } 
        this.order = order; 
        root = new StockNode(true, true); 
        minnode = root; 
    }
}
