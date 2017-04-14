package data.datahelperimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class MainDataBuffer {
	private static int BUFFER_SIZE=1024*1024*4;
	private static int[] block={1,2,3,4};
	private long[] seperate;
	private byte[] dst_BUFFERSIZE;
	private String path;
	private MappedByteBuffer mainData;
	private long size;
	private int nowpart;
	private long partSize;
	public MainDataBuffer(){
		path="config/resources/mainData";
		File main=new File(path);
		size=main.length();
		partSize=size/4;
		dst_BUFFERSIZE=new byte[BUFFER_SIZE];
		seperate=new long[5];
		seperate[0]=0;
		seperate[1]=partSize;
		seperate[2]=partSize*2;
		seperate[3]=partSize*3;
		seperate[4]=partSize*4;
	}
	public String read(int position,int length){
		int thispart=0;
		for(int i=0;i<4;i++){
			if(seperate[i+1]>position){
				thispart=block[i];
				break;
			}
		}
		if(nowpart==thispart){
			if((position+length)>=seperate[thispart+1]){
				try {
					byte[] dst=new byte[length];
					loadStockDateIndex(path,position,length).get(dst);
					return new String(dst);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
			else{
				byte[] dst=new byte[length];
				mainData.position((int) (position-seperate[thispart]));
				mainData.get(dst);
				return new String(dst);
			}
		}
		else{
			mainData=null;
			System.gc();
			try {
				byte[] dst=new byte[length];
				loadStockDateIndex(path,(int)seperate[thispart],(int)partSize).get(dst);
				return new String(dst);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	private MappedByteBuffer loadStockDateIndex(String path,int position,int length) throws IOException{
		FileInputStream is=new FileInputStream(path);
		FileChannel fc=is.getChannel();
		mainData=fc.map(MapMode.READ_ONLY, position, length);
		byte[] tempdst=new byte[(int) (fc.size()%BUFFER_SIZE)];
		for(int i=0;i<fc.size();i+=BUFFER_SIZE){
			if(fc.size()-i>BUFFER_SIZE){
				mainData.get(dst_BUFFERSIZE);
			}
			else{
				mainData.get(tempdst);
			}
		}
		is.close();
		return mainData;
	}
}
