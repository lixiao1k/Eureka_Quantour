package data.common;

import java.util.ArrayList;
import java.util.List;

public class BufferNode {
	private List<BufferNode> child;
	private int layer;
	public BufferNode(){
	}
	public BufferNode(int _layer){
		setLayer(_layer);
		child=new ArrayList<BufferNode>();	
		switch(_layer){
		case 1:
			initYear();
			break;
		case 2:
			initMonth();
			break;
		case 3:
			initDay();
			break;
		}
	}
	
	
	private void initYear(){
		for(int i=5;i<18;i++){
			BufferNode temp=new BufferNode(2);
			child.add(temp);
		}
	}
	private void initMonth(){
		for(int i=1;i<13;i++){
			BufferNode temp=new BufferNode(3);
			child.add(temp);
		}
	}
	private void initDay(){
		for(int i=1;i<32;i++){
			BufferLeaf temp=new BufferLeaf();
			child.add(temp);
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
	public List<BufferNode> getChild() {
		return child;
	}
	/**
	 * @param child the child to set
	 */
	public void setChild(List<BufferNode> child) {
		this.child = child;
	}
}
