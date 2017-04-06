package data.common;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class StockNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7650729202614241733L;
	private boolean isRoot;
	private boolean isLeaf;
	private StockNode parent;
	private StockNode previous;
	private StockNode next;
	private List<Entry<Integer,Integer>> index;
	private List<StockNode> children;
	public StockNode(boolean _isRoot,boolean _isLeaf){
		this(_isLeaf);
		isRoot=_isRoot;
	}
	public StockNode(boolean _isLeaf){
		setLeaf(_isLeaf);
		index=new ArrayList<Entry<Integer,Integer>>();
		if(!_isLeaf){
			children=new ArrayList<StockNode>();
		}
	}
	public Integer get(int key){
		if(isLeaf()){
			for (Entry<Integer,Integer> info : index) { 
                if (info.getKey()==key) { 
                    //返回找到的对象 
                    return info.getValue(); 
                } 
            } 
			return null;
		}
		else{
			for(int i=0;i<index.size()-1;i++){
				int tempminkey=index.get(i).getKey();
				int tempmaxkey=index.get(i+1).getKey();
				if((tempminkey <= key)	&&	(key<tempmaxkey)){
					return children.get(i).get(key);
				}
			}
			return children.get(children.size()-1).get(key);
		}
	}
	public void insertStockNode(int key,int info,StockBplusTree tree){
		if(isLeaf()){
			if(contains(key)||index.size()<tree.getOrder()){
				insert_In(key,info);
				if(parent!=null){
					parent.update_In(tree);
				}
			}
			else{
				StockNode left=new StockNode(true);
				StockNode right=new StockNode(true);
				if(previous==null){
					tree.setMin(left);
				}
				if(previous!=null){
					left.setPrevious(previous);
				}
				if(next!=null){
					right.setNext(next);
				}
				right.setPrevious(left);
				left.setNext(right);
				previous=null;
				next=null;
				int totalsize=tree.getOrder()+1;
				int leftSize = totalsize / 2 + (totalsize % 2);  
                int rightSize = totalsize / 2; 
                insert_In(key,info);
                for(int i=0;i<leftSize;i++){
                	left.getIndex().add(index.get(i));
                }
                for(int i=0;i<rightSize;i++){
                	right.getIndex().add(index.get(leftSize+i));
                }
                if(parent!=null){
                	 int locate = parent.getChildren().indexOf(this); 
                	 parent.getChildren().remove(locate);
                	 parent.getChildren().add(locate,right);
                	 parent.getChildren().add(locate,left);
                	 left.setParent(parent); 
                     right.setParent(parent);
                     setIndex(null); 
                     setChildren(null); 
                     parent.update_In(tree); 
                     setParent(null); 
                }
                else{
                	isRoot=false;
                	StockNode root=new StockNode(true,false);
                	left.setParent(root); 
                    right.setParent(root); 
                    root.getChildren().add(left); 
                    root.getChildren().add(right); 
                    setIndex(null); 
                    setChildren(null); 
                    tree.setRoot(root);
                    root.update_In(tree); 
                }
			}
		}
		else{
			for(int i=0;i<index.size()-1;i++){
				int tempminkey=index.get(i).getKey();
				int tempmaxkey=index.get(i+1).getKey();
				if((tempminkey <= key)	&&	(key<tempmaxkey)){
					children.get(i).insertStockNode(key, info, tree);
					break;
				}
			}
			children.get(children.size()-1).insertStockNode(key, info, tree);
		}
	}
	private boolean contains(int key){
		for(Entry<Integer,Integer> entry:index){
			int tempkey=entry.getKey();
			if(key==tempkey){
				return true;
			}
		}
		return false;
	}
	private void update_In(StockBplusTree tree) throws NullPointerException{
		validate(this,tree);
		if(children.size()>tree.order){
			StockNode left=new StockNode(false);
			StockNode right=new StockNode(false);
			int totalsize=tree.getOrder()+1;
			int leftSize = totalsize / 2 + (totalsize % 2);  
            int rightSize = totalsize / 2; 
            for(int i=0;i<leftSize;i++){
            	left.getChildren().add(children.get(i));
            	left.getIndex().add(new AbstractMap.SimpleEntry<Integer,Integer>(children.get(i).getIndex().get(0).getKey(),null));
            	children.get(i).setParent(left);
            }
            for(int i=0;i<rightSize;i++){
            	right.getChildren().add(children.get(leftSize +i));
            	right.getIndex().add(new AbstractMap.SimpleEntry<Integer,Integer>(children.get(leftSize +i).getIndex().get(0).getKey(),null));
            	children.get(leftSize +i).setParent(right);
            }
            if(parent!=null){
            	int locate = parent.getChildren().indexOf(this); 
            	parent.getChildren().remove(locate);
            	parent.getChildren().add(locate,right);
            	parent.getChildren().add(locate,left);
            	left.setParent(parent); 
            	right.setParent(parent);
                setIndex(null); 
                setChildren(null); 
                parent.update_In(tree); 
                setParent(null); 
           }
           else{
        	   isRoot=false;
        	   StockNode root=new StockNode(true,false);
        	   left.setParent(root); 
               right.setParent(root); 
               root.getChildren().add(left); 
               root.getChildren().add(right); 
               setIndex(null); 
               setChildren(null); 
               tree.setRoot(root);
               root.update_In(tree); 
           }
		}
	}
	public static void validate(StockNode StockNode,StockBplusTree tree){
		if(StockNode.getIndex().size()==StockNode.getChildren().size()){
			for(int i=0;i<StockNode.getIndex().size();i++){
				int key1=StockNode.getIndex().get(i).getKey();
				int key2=StockNode.getChildren().get(i).getIndex().get(0).getKey();
				if(key2!=key1){
					StockNode.getIndex().remove(i);
					StockNode.getIndex().add(i,new AbstractMap.SimpleEntry<Integer,Integer>(key2,null));
				}
			}
			if(!StockNode.isRoot()){
				validate(StockNode.getParent(),tree);
			}
		}
		else if((StockNode.getChildren().size() >= tree.getOrder() / 2  
                && StockNode.getChildren().size() <= tree.getOrder() )
                || (StockNode.getChildren().size() >= 2 &&StockNode.isRoot())){
			StockNode.getIndex().clear();
			for(int i=0;i<StockNode.getChildren().size();i++){
				int key=StockNode.getChildren().get(i).getIndex().get(0).getKey();
				StockNode.getIndex().add(new AbstractMap.SimpleEntry<Integer,Integer>(key,null));
			}
			if(!StockNode.isRoot()){
				validate(StockNode.getParent(),tree);
			}
		}
	}
	private void insert_In(int key,int info){
		try{
		if(this.getIndex().size()==0||this.getIndex().get(this.getIndex().size()-1).getKey()<key){
			this.getIndex().add(new AbstractMap.SimpleEntry<Integer,Integer>(key, info));
		}
		else if(this.getIndex().get(this.getIndex().size()-1).getKey()==key){
			System.out.println("this"+key+";"+info);
			System.exit(0);
		}
		else{
			for(int i=0;i<this.getIndex().size()-1;i++){
				int tempminkey=this.getIndex().get(i).getKey();
				int tempmaxkey=this.getIndex().get(i+1).getKey();
				if((tempminkey <= key) && (key < tempmaxkey)){
					if(tempminkey==key){
						System.out.println("this"+key+";"+info);
						System.exit(0);
					}
					else{
						this.getIndex().add(i+1,new AbstractMap.SimpleEntry<Integer,Integer>(key, info));
					}
					break;
				}
			}
			
		}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(this.getIndex().size());
			System.exit(0);
		}
	}
	public List<Entry<Integer,Integer>> getIndex(){
		return this.index;
	}
	public void setIndex(List<Entry<Integer,Integer>> _index){
		this.index=_index;
	}
	public List<StockNode> getChildren(){
		return children;
	}
	public void setChildren(List<StockNode> _children){
		this.children=_children;
	}
	public boolean isRoot(){
		return isRoot;
	}
	public StockNode getParent(){
		return parent;
	}
    public StockNode getPrevious() { 
        return previous; 
    } 
 
    public void setPrevious(StockNode previous) { 
        this.previous = previous; 
    } 
    public void setParent(StockNode _parent){
    	this.parent=_parent;
    }
    public StockNode getNext() { 
        return next; 
    } 
 
    public void setNext(StockNode next) { 
        this.next = next; 
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
}
