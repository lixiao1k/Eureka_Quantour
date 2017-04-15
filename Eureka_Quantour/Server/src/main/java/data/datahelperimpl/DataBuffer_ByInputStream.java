package data.datahelperimpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class DataBuffer_ByInputStream {
	private static int BUFFER_SIZE=1024*1024*2;
	private byte[][] queue;
	private HashMap<Integer,Integer> index;
	private int nextClear;
	private int[] codequeue;
	private int size;
	private byte[] dst_BUFFERSIZE;
	public DataBuffer_ByInputStream(int size){
		dst_BUFFERSIZE=new byte[BUFFER_SIZE];
		this.size=size;
		nextClear=0;
		queue=new byte[size][];
		index=new HashMap<Integer,Integer>();
		codequeue=new int[size];
	}
	public void addBuffer(byte[] mbb,int code){
		int removeCode=codequeue[nextClear];
		index.remove(removeCode);
		queue[nextClear]=mbb;
		codequeue[nextClear]=code;
		index.put(code, nextClear);
		nextClear++;
		if(nextClear==size){
			nextClear=0;
		}
	}
	public int getIndex(int code){
		return index.getOrDefault(code, -1);
	}
	public byte[] getMbbwithIndex(int index){
		return queue[index];
	}
	public byte[] getMbb(int code,String path){
		int index=getIndex(code);
		if(getIndex(code)==-1){
			try {
				byte[] mbb=loadStockDateIndex(path);
				addBuffer(mbb,code);
				return mbb;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		else{
			return queue[index];
		}
	}
	private byte[] loadStockDateIndex(String path) throws IOException{
		File file=new File(path);
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file),1024*8);
		byte[] dst1=new byte[(int) (file.length()%BUFFER_SIZE)];
		byte[] main=new byte[(int) file.length()];
		for(int i=0;i<file.length();i+=BUFFER_SIZE){
			if(file.length()-i>BUFFER_SIZE){
				bis.read(dst_BUFFERSIZE);
				System.arraycopy(dst_BUFFERSIZE, 0, main, i, BUFFER_SIZE);
			}
			else{
				bis.read(dst1);
				System.arraycopy(dst1, 0, main, i,(int) (file.length()%BUFFER_SIZE));
			}
		}
		bis.close();
		return main;
	}
	public void print(){
		Iterator<Integer> it=index.keySet().iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	public void clear(){
		for(int i=0;i<size;i++){
			queue[i]=null;
		}
	}
}
