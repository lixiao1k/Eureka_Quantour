package data.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class BufferLeaf extends BufferNode{
	private static String path="config/resources/date/calendarDate/";
	private static int BUFFER_SIZE=1024*8;
	private static byte[] dst=new byte[BUFFER_SIZE];
	private int index;
	private int cal;
	private int row;
	private boolean isLeaf;
	private MappedByteBuffer mbb;
	private boolean isMbb;
	public BufferLeaf(){
		setLeaf(false);	
		setMbb(false);
	}
	public void activation(int index,int cal,int row){
		setIndex(index);
		setLayer(4);
		setLeaf(true);
		setCal(cal);
		setRow(row);
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
	public MappedByteBuffer activationMbb() throws IOException{
		setMbb(true);
		FileInputStream is=new FileInputStream(path+this.cal);
		FileChannel fc=is.getChannel();
		MappedByteBuffer mbb=fc.map(MapMode.READ_ONLY, 0, fc.size());
		long size=fc.size();
		byte[] tempdst=new byte[(int) (size%BUFFER_SIZE)];
		for(int i=0;i<size;i+=BUFFER_SIZE){
			if(size-i>BUFFER_SIZE){
				mbb.get(dst);
			}
			else{
				mbb.get(tempdst);
			}
		}
		is.close();
		setMbb(mbb);
		return mbb;
	}
	public boolean compareTo(DateLeaf leaf){
		if(this.cal>leaf.getCal()){
			return false;
		}
		else if(this.cal<leaf.getCal()){
			return true;
		}
		else{
			throw new NullPointerException();
		}
	}
	/**
	 * @return the cal
	 */
	public int getCal() {
		return cal;
	}
	/**
	 * @param cal the cal to set
	 */
	public void setCal(int cal) {
		this.cal = cal;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}
	/**
	 * @return the mbb
	 */
	public MappedByteBuffer getMbb() {
		return mbb;
	}
	/**
	 * @param mbb the mbb to set
	 */
	public void setMbb(MappedByteBuffer mbb) {
		this.mbb = mbb;
	}
	/**
	 * @return the isMbb
	 */
	public boolean isMbb() {
		return isMbb;
	}
	/**
	 * @param isMbb the isMbb to set
	 */
	public void setMbb(boolean isMbb) {
		this.isMbb = isMbb;
	}
}
