package data.datahelperimpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Iterator;

public class StockIndexBuffer {
	private static int BUFFER_SIZE=1024*1024*2;
	private MappedByteBuffer[] queue;
	private HashMap<Integer,Integer> index;
	private int nextClear;
	private int[] codequeue;
	private int size;
	private byte[] dst_BUFFERSIZE;
	public StockIndexBuffer(int size){
		dst_BUFFERSIZE=new byte[BUFFER_SIZE];
		this.size=size;
		nextClear=0;
		queue=new MappedByteBuffer[size];
		index=new HashMap<Integer,Integer>();
		codequeue=new int[size];
	}
	public void addBuffer(MappedByteBuffer mbb,int code){
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
	public MappedByteBuffer getMbbwithIndex(int index){
		return queue[index];
	}
	public MappedByteBuffer getMbb(int code,String path){
		int index=getIndex(code);
		if(getIndex(code)==-1){
			try {
				MappedByteBuffer mbb=loadStockDateIndex(path);
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
	private MappedByteBuffer loadStockDateIndex(String path) throws IOException{
		FileInputStream is=new FileInputStream(path);
		FileChannel fc=is.getChannel();
		MappedByteBuffer mbb=fc.map(MapMode.READ_ONLY, 0, fc.size());
		byte[] tempdst=new byte[(int) (fc.size()%BUFFER_SIZE)];
		for(int i=0;i<fc.size();i+=BUFFER_SIZE){
			if(fc.size()-i>BUFFER_SIZE){
				mbb.get(dst_BUFFERSIZE);
			}
			else{
				mbb.get(tempdst);
			}
		}
		is.close();
		return mbb;
	}
	public void print(){
		Iterator<Integer> it=index.keySet().iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
}
