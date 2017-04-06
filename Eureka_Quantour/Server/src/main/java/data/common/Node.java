package data.common;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6725588505674187217L;
	private boolean isRoot;
	private boolean isLeaf;
	private Node parent;
	private Node previous;
	private Node next;
	private List<Entry<Integer,StockBplusTree>> index;
	private List<Node> children;
	public Node(boolean _isRoot,boolean _isLeaf){
		this(_isLeaf);
		isRoot=_isRoot;
	}
	public Node(boolean _isLeaf){
		setLeaf(_isLeaf);
		index=new ArrayList<Entry<Integer,StockBplusTree>>();
		if(!_isLeaf){
			children=new ArrayList<Node>();
		}
	}
	public StockBplusTree get(int key){
		if(isLeaf()){
			for (Entry<Integer, StockBplusTree> info : index) { 
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
	public void insertNode(int key,Entry<Integer,Integer> info,DateBplusTree tree){
		if(isLeaf()){
			if(contains(key)||index.size()<tree.getOrder()){
				insert_In(key,info);
				if(parent!=null){
					parent.update_In(tree);
				}
			}
			else{
				Node left=new Node(true);
				Node right=new Node(true);
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
                	Node root=new Node(true,false);
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
					children.get(i).insertNode(key, info, tree);
					break;
				}
			}
			children.get(children.size()-1).insertNode(key, info, tree);
		}
	}
	private boolean contains(int key){
		for(Entry<Integer,StockBplusTree> entry:index){
			int tempkey=entry.getKey();
			if(key==tempkey){
				return true;
			}
		}
		return false;
	}
	private void update_In(DateBplusTree tree) throws NullPointerException{
		validate(this,tree);
		if(children.size()>tree.order){
			Node left=new Node(false);
			Node right=new Node(false);
			int totalsize=tree.getOrder()+1;
			int leftSize = totalsize / 2 + (totalsize % 2);  
            int rightSize = totalsize / 2; 
            for(int i=0;i<leftSize;i++){
            	left.getChildren().add(children.get(i));
            	left.getIndex().add(new AbstractMap.SimpleEntry<Integer,StockBplusTree>(children.get(i).getIndex().get(0).getKey(),null));
            	children.get(i).setParent(left);
            }
            for(int i=0;i<rightSize;i++){
            	right.getChildren().add(children.get(leftSize +i));
            	right.getIndex().add(new AbstractMap.SimpleEntry<Integer,StockBplusTree>(children.get(leftSize +i).getIndex().get(0).getKey(),null));
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
        	   Node root=new Node(true,false);
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
	public static void validate(Node node,DateBplusTree tree){
		if(node.getIndex().size()==node.getChildren().size()){
			for(int i=0;i<node.getIndex().size();i++){
				int key1=node.getIndex().get(i).getKey();
				int key2=node.getChildren().get(i).getIndex().get(0).getKey();
				if(key2!=key1){
					node.getIndex().remove(i);
					node.getIndex().add(i,new AbstractMap.SimpleEntry<Integer,StockBplusTree>(key2,null));
				}
			}
			if(!node.isRoot()){
				validate(node.getParent(),tree);
			}
		}
		else if((node.getChildren().size() >= tree.getOrder() / 2  
                && node.getChildren().size() <= tree.getOrder() )
                || (node.getChildren().size() >= 2 &&node.isRoot())){
			node.getIndex().clear();
			for(int i=0;i<node.getChildren().size();i++){
				int key=node.getChildren().get(i).getIndex().get(0).getKey();
				node.getIndex().add(new AbstractMap.SimpleEntry<Integer,StockBplusTree>(key,null));
			}
			if(!node.isRoot()){
				validate(node.getParent(),tree);
			}
		}
	}
	private void insert_In(int key,Entry<Integer,Integer> info){
		try{
		if(this.getIndex().size()==0||this.getIndex().get(this.getIndex().size()-1).getKey()<key){
			StockBplusTree tempmap=new StockBplusTree(16);
			tempmap.insertInfo(info.getKey(), info.getValue());
			this.getIndex().add(new AbstractMap.SimpleEntry<Integer,StockBplusTree>(key, tempmap));
		}
		else if(this.getIndex().get(this.getIndex().size()-1).getKey()==key){
			this.getIndex().get(this.getIndex().size()-1).getValue().insertInfo(info.getKey(), info.getValue());
		}
		else{
			for(int i=0;i<this.getIndex().size()-1;i++){
				int tempminkey=this.getIndex().get(i).getKey();
				int tempmaxkey=this.getIndex().get(i+1).getKey();
				if((tempminkey <= key) && (key < tempmaxkey)){
					if(tempminkey==key){
						this.getIndex().get(i).getValue().insertInfo(info.getKey(), info.getValue());
					}
					else{
						StockBplusTree tempmap=new StockBplusTree(16);
						tempmap.insertInfo(info.getKey(), info.getValue());
						this.getIndex().add(i+1,new AbstractMap.SimpleEntry<Integer,StockBplusTree>(key, tempmap));
					}
					break;
				}
			}
			
		}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(info.getKey()+":"+info.getValue());
			System.out.println(this.getIndex().size());
			System.exit(0);
		}
	}
	public List<Entry<Integer,StockBplusTree>> getIndex(){
		return this.index;
	}
	public void setIndex(List<Entry<Integer,StockBplusTree>> _index){
		this.index=_index;
	}
	public List<Node> getChildren(){
		return children;
	}
	public void setChildren(List<Node> _children){
		this.children=_children;
	}
	public boolean isRoot(){
		return isRoot;
	}
	public Node getParent(){
		return parent;
	}
    public Node getPrevious() { 
        return previous; 
    } 
 
    public void setPrevious(Node previous) { 
        this.previous = previous; 
    } 
    public void setParent(Node _parent){
    	this.parent=_parent;
    }
    public Node getNext() { 
        return next; 
    } 
 
    public void setNext(Node next) { 
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
