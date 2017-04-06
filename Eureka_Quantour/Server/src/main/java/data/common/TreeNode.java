package data.common;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TreeNode {
	protected boolean isLeaf;
	protected boolean isRoot;
	protected TreeNode parent;
	protected TreeNode previous;
	protected TreeNode next;
	protected List<Entry<Integer, HashMap<String,Integer>>> entries;
	protected List<TreeNode> children;
	public TreeNode(boolean isLeaf) { 
        this.isLeaf = isLeaf; 
        entries = new ArrayList<Entry<Integer, HashMap<String,Integer>>>(); 
        if (!isLeaf) { 
            children = new ArrayList<TreeNode>(); 
        } 
    } 
 
    public TreeNode(boolean isLeaf, boolean isRoot) { 
        this(isLeaf); 
        this.isRoot = isRoot; 
    } 
    public HashMap<String, Integer> get(int key) { 
        
        //如果是叶子节点 
        if (isLeaf) { 
            for (Entry<Integer, HashMap<String,Integer>> info : entries) { 
                if (info.getKey()==key) { 
                    //返回找到的对象 
                    return info.getValue(); 
                } 
            } 
            //未找到所要查询的对象 
            return null; 
             
        //如果不是叶子节点 
        }else { 
        	if(entries.get(entries.size()-1).getKey() <= key){
        		return children.get(entries.size()-1).get(key);
        	}
        	else{
        		for (int i = 0; i < entries.size()-1; i++) { 
            		if (entries.get(i).getKey() <= key &&  key < entries.get(i+1).getKey()) { 
            			return children.get(i).get(key); 
            		} 
            	}
        	}
        	   	
        }     
        return null; 
    } 
    
    public void insert(int key, Entry<String,Integer> entry ,BPlusTree tree){ 
        //如果是叶子节点 
        if (isLeaf){ 
            //不需要分裂，直接插入或更新 
            if (contains(key) || entries.size() < tree.getOrder()){ 
                insertInfo(key, entry); 
                if (parent != null) { 
                    //更新父节点 
                    parent.updateInsert(tree); 
                } 
 
            //需要分裂   
            }
            else { 
                //分裂成左右两个节点 
                TreeNode left = new TreeNode(true); 
                TreeNode right = new TreeNode(true); 
                //设置链接 
                if (previous != null){ 
                    previous.setNext(left); 
                    left.setPrevious(previous); 
                } 
                if (next != null) { 
                    next.setPrevious(right); 
                    right.setNext(next); 
                } 
                if (previous == null){ 
                    tree.setMin(left); 
                }
                left.setNext(right); 
                right.setPrevious(left); 
                previous = null; 
                next = null;                 
                //左右两个节点关键字长度 
                int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;  
                int rightSize = (tree.getOrder() + 1) / 2; 
                //复制原节点关键字到分裂出来的新节点 
                insertInfo(key, entry); 
                for (int i = 0; i < leftSize; i++){ 
                    left.getEntries().add(entries.get(i)); 
                } 
                for (int i = 0; i < rightSize; i++){ 
                    right.getEntries().add(entries.get(leftSize + i)); 
                } 
                 
                //如果不是根节点 
                if (parent != null) { 
                    //调整父子节点关系 
                    int index = parent.getChildren().indexOf(this); 
                    parent.getChildren().remove(this); 
                    left.setParent(parent); 
                    right.setParent(parent); 
                    parent.getChildren().add(index,left); 
                    parent.getChildren().add(index + 1, right); 
                    setEntries(null); 
                    setChildren(null); 
                     
                    //父节点插入或更新关键字 
                    parent.updateInsert(tree); 
                    setParent(null); 
                //如果是根节点     
                }else { 
                    isRoot = false; 
                    TreeNode parent = new TreeNode(false, true); 
                    tree.setRoot(parent); 
                    left.setParent(parent); 
                    right.setParent(parent); 
                    parent.getChildren().add(left); 
                    parent.getChildren().add(right); 
                    setEntries(null); 
                    setChildren(null); 
                     
                    //更新根节点 
                    parent.updateInsert(tree); 
                } 
                 
 
            } 
             
        //如果不是叶子节点 
        }else { 
            if (key >= entries.get(entries.size()-1).getKey() ) { 
                children.get(children.size()-1).insert(key, entry, tree); 
            //否则沿比key大的前一个子节点继续搜索 
            }
            else { 
                for (int i = 0; i < entries.size()-1; i++) { 
                    if (entries.get(i).getKey() <= key && key < entries.get(i+1).getKey()) { 
                        children.get(i).insert(key, entry, tree); 
                        break; 
                    } 
                }    
            } 
        } 
    } 
     
    /** 插入节点后中间节点的更新 */ 
    protected void updateInsert(BPlusTree tree){ 
 
        validate(this, tree); 
         
        //如果子节点数超出阶数，则需要分裂该节点    
        if (children.size() > tree.getOrder()) { 
            //分裂成左右两个节点 
            TreeNode left = new TreeNode(false); 
            TreeNode right = new TreeNode(false); 
            //左右两个节点关键字长度 
            int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2; 
            int rightSize = (tree.getOrder() + 1) / 2; 
            //复制子节点到分裂出来的新节点，并更新关键字 
            for (int i = 0; i < leftSize; i++){ 
                left.getChildren().add(children.get(i)); 
                left.getEntries().add(new AbstractMap.SimpleEntry<Integer,HashMap<String,Integer>>(children.get(i).getEntries().get(0).getKey(), null)); 
                children.get(i).setParent(left); 
            } 
            for (int i = 0; i < rightSize; i++){ 
                right.getChildren().add(children.get(leftSize + i)); 
                right.getEntries().add(new AbstractMap.SimpleEntry<Integer,HashMap<String,Integer>>(children.get(leftSize + i).getEntries().get(0).getKey(), null)); 
                children.get(leftSize + i).setParent(right); 
            } 
             
            //如果不是根节点 
            if (parent != null) { 
                //调整父子节点关系 
                int index = parent.getChildren().indexOf(this); 
                parent.getChildren().remove(this); 
                left.setParent(parent); 
                right.setParent(parent); 
                parent.getChildren().add(index,left); 
                parent.getChildren().add(index + 1, right); 
                setEntries(null); 
                setChildren(null); 
                 
                //父节点更新关键字 
                parent.updateInsert(tree); 
                setParent(null); 
            //如果是根节点     
            }else { 
                isRoot = false; 
                TreeNode parent = new TreeNode(false, true); 
                tree.setRoot(parent); 
                left.setParent(parent); 
                right.setParent(parent); 
                parent.getChildren().add(left); 
                parent.getChildren().add(right); 
                setEntries(null); 
                setChildren(null);  
                //更新根节点 
                parent.updateInsert(tree); 
            } 
        } 
    } 
    /** 调整节点关键字*/ 
    protected static void validate(TreeNode node, BPlusTree tree) { 
         
        // 如果关键字个数与子节点个数相同 
        if (node.getEntries().size() == node.getChildren().size()) { 
            for (int i = 0; i < node.getEntries().size(); i++) { 
                int key = node.getChildren().get(i).getEntries().get(0).getKey(); 
                if (node.getEntries().get(i).getKey() != key) { 
                	node.getEntries().remove(i); 
                    node.getEntries().add(i, new AbstractMap.SimpleEntry<Integer,HashMap<String,Integer>>(key,null)); 
                    if(!node.isRoot()){ 
                        validate(node.getParent(), tree); 
                    } 
                } 
            } 
            // 如果子节点数不等于关键字个数但仍大于M / 2并且小于M，并且大于2 
        }
        else if (node.isRoot() && node.getChildren().size() >= tree.getOrder() / 2  
                && node.getChildren().size() <= tree.getOrder() 
                && node.getChildren().size() >= 2) { 
            node.getEntries().clear(); 
            for (int i = 0; i < node.getChildren().size(); i++) { 
                int key = node.getChildren().get(i).getEntries().get(0).getKey(); 
                node.getEntries().add(new AbstractMap.SimpleEntry<Integer,HashMap<String,Integer>>(key,null)); 
                if (!node.isRoot()) { 
                    validate(node.getParent(), tree); 
                } 
            } 
        } 
    } 
    /** 插入到当前节点的关键字中*/ 
    protected void insertInfo(int key, Entry<String,Integer> info){ 
        //如果关键字列表长度为0，则直接插入 
        if (entries.size() == 0) { 
        	HashMap<String,Integer> map=new HashMap<String,Integer>();
        	map.put(info.getKey(), info.getValue());
            entries.add(new AbstractMap.SimpleEntry<Integer,HashMap<String,Integer>>(key,map)); 
            return; 
        } 
        //否则遍历列表 
        for (int i = 0; i < entries.size(); i++) { 
            //如果该关键字键值已存在，则更新 
            if (entries.get(i).getKey()==key) { 
                entries.get(i).getValue().put(info.getKey(), info.getValue()); 
                return; 
            //否则插入   
            }else if (entries.get(i).getKey() > key){ 
                //插入到链首 
                if (i == 0) { 
                	HashMap<String,Integer> map=new HashMap<String,Integer>();
                	map.put(info.getKey(), info.getValue());
                    entries.add(0, new AbstractMap.SimpleEntry<Integer,HashMap<String,Integer>>(key,map)); 
                    return; 
                //插入到中间 
                }else { 
                	HashMap<String,Integer> map=new HashMap<String,Integer>();
                	map.put(info.getKey(), info.getValue());
                    entries.add(i, new AbstractMap.SimpleEntry<Integer,HashMap<String,Integer>>(key,map)); 
                    return; 
                } 
            } 
        } 
        //插入到末尾 
        HashMap<String,Integer> map=new HashMap<String,Integer>();
    	map.put(info.getKey(), info.getValue());
        entries.add(entries.size(), new AbstractMap.SimpleEntry<Integer,HashMap<String,Integer>>(key,map)); 
    } 
    /** 判断当前节点是否包含该关键字*/ 
    protected boolean contains(int key) { 
        for (Entry<Integer, HashMap<String, Integer>> entry : entries) { 
            if (entry.getKey() == key) { 
                return true; 
            } 
        } 
        return false; 
    } 
    
    public TreeNode getPrevious() { 
        return previous; 
    } 
 
    public void setPrevious(TreeNode previous) { 
        this.previous = previous; 
    } 
 
    public TreeNode getNext() { 
        return next; 
    } 
 
    public void setNext(TreeNode next) { 
        this.next = next; 
    } 
 
    public boolean isLeaf() { 
        return isLeaf; 
    } 
 
    public void setLeaf(boolean isLeaf) { 
        this.isLeaf = isLeaf; 
    } 
 
    public TreeNode getParent() { 
        return parent; 
    } 
 
    public void setParent(TreeNode parent) { 
        this.parent = parent; 
    } 
 
    public List<Entry<Integer, HashMap<String,Integer>>> getEntries() { 
        return entries; 
    } 
 
    public void setEntries(List<Entry<Integer, HashMap<String,Integer>>> entries) { 
        this.entries = entries; 
    } 
 
    public List<TreeNode> getChildren() { 
        return children; 
    } 
 
    public void setChildren(List<TreeNode> children) { 
        this.children = children; 
    } 
     
    public boolean isRoot() { 
        return isRoot; 
    } 
 
    public void setRoot(boolean isRoot) { 
        this.isRoot = isRoot; 
    } 
    public String toString(){ 
        StringBuilder sb = new StringBuilder(); 
        sb.append("isRoot: "); 
        sb.append(isRoot); 
        sb.append(", "); 
        sb.append("isLeaf: "); 
        sb.append(isLeaf); 
        sb.append(", "); 
        sb.append("keys: "); 
        for (Entry entry : entries){ 
            sb.append(entry.getKey()); 
            sb.append(", "); 
        } 
        sb.append(", "); 
        return sb.toString(); 
         
    } 
}
