package data.common;

import java.util.Map.Entry;

public class BPlusTree {
	protected TreeNode root;
	protected int order;
	protected TreeNode minnode;
	public TreeNode getMin(){
		return minnode;
	}
	public void setMin(TreeNode node){
		minnode=node;
	}
	public TreeNode getRoot() { 
        return root; 
    } 
 
    public void setRoot(TreeNode root) { 
        this.root = root; 
    } 
 
    public int getOrder() { 
        return order; 
    } 
    public Object get(int key) { 
        return root.get(key); 
    } 
    public void insertOrUpdate(int key, Entry<String,Integer> obj) { 
        root.insert(key, obj, this);
    } 
    public void setOrder(int order) { 
        this.order = order; 
    } 
    public void toString(TreeNode node){
    	if(node.isLeaf){
    		for(int i=0;i<node.entries.size();i++){
    			System.out.println(node.entries.get(i).getValue()+":"+node.entries.get(i).getKey());
    		}
    	}
    	else{
    		for(int i=0;i<node.children.size();i++){
    			toString(node.children.get(i));
    		}	
    	}
    }
    public BPlusTree(int order){ 
        if (order < 3) { 
            System.out.print("order must be greater than 2"); 
            System.exit(0); 
        } 
        this.order = order; 
        root = new TreeNode(true, true); 
        minnode = root; 
    }
}
