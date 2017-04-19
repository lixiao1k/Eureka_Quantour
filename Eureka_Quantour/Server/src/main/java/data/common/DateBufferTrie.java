package data.common;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DateBufferTrie {
	/**
	 * 
	 */
	private BufferNode root;
	private int nownumber;
	private List<Integer> datesort;
	private int[] workingmbb;
	private int maxsize;
	private int nowptr;
	private boolean full;
	public DateBufferTrie(int _maxsize){
		setNownumber(0);
		datesort=new ArrayList<Integer>();
		root=new BufferNode(1);
		maxsize=_maxsize;
		workingmbb=new int[_maxsize];
		nowptr=0;
		full=false;
	}
	public boolean add(int cal,int row){
		int year= cal / 10000;
		int month= (cal -year * 10000 ) / 100;
		int day=cal - year * 10000 - month * 100;
		return add(year,month,day,row);
	}
	public boolean add(int year,int month,int day,int row){
		BufferNode temp;
		int cal=year * 10000 + month * 100 + day;
		if(year>2004){
			temp=root.getChild().get(year-2005);
			if(month>0){
				temp=temp.getChild().get(month-1);
				if(day>0){
					BufferLeaf t=(BufferLeaf)temp.getChild().get(day-1);
					if(t.isLeaf()){
						return false;
					}
					else{
						t.activation(nownumber,cal,row);
						nownumber++;
						datesort.add(cal);
						return true;
					}
				}
			}
		}
		return false;
	}
	public int get(int cal){
		int year= cal / 10000;
		int month= (cal -year * 10000 ) / 100;
		int day=cal - year * 10000 - month * 100;
		BufferLeaf temp=get(year,month,day);
		if(temp==null||!temp.isLeaf()){
			return 0;
		}
		else{
			return temp.getRow();
		}
	}
	public boolean hasday(int cal){
		int year= cal / 10000;
		int month= (cal -year * 10000 ) / 100;
		int day=cal - year * 10000 - month * 100;
		BufferLeaf temp=get(year,month,day);
		if(temp==null){
			return false;
		}
		else{
			return temp.isLeaf();
		}
	}
	public int getIndex(int cal){
		int year= cal / 10000;
		int month= (cal -year * 10000 ) / 100;
		int day=cal - year * 10000 - month * 100;
		BufferLeaf temp=get(year,month,day);
		if(temp==null||!temp.isLeaf()){
			return -1;
		}
		else{
			return temp.getIndex();
		}
	}
	private BufferLeaf get(int year,int month,int day){
		if(year>2004){
			if(month>0){
				if(day>0){
					BufferLeaf temp=(BufferLeaf) root.getChild().get(year-2005).getChild().get(month-1)
							.getChild().get(day-1);
					return temp;
				}
				else{
					return null;
				}
			}
			else{
				return null;
			}
			
		}
		else{
			return null;
		}
	}
	public BufferLeaf get_leaf(int cal){
		int year= cal / 10000;
		int month= (cal -year * 10000 ) / 100;
		int day=cal - year * 10000 - month * 100;
		BufferLeaf temp=get(year,month,day);
		if(temp==null||!temp.isLeaf()){
			return null;
		}
		else{
			return temp;
		}
	}
	public MappedByteBuffer getMbbbyCal(int cal) throws IOException{
		BufferLeaf temp=get_leaf(cal);
		if(temp.isMbb()){
			return temp.getMbb();
		}
		else{
			if(full){
				get_leaf(workingmbb[nowptr]).setMbb(null);
				get_leaf(workingmbb[nowptr]).setMbb(false);
				workingmbb[nowptr]=cal;
				nowptr++;
				if(nowptr==maxsize){
					nowptr=0;
				}
				return temp.activationMbb();
			}
			else{
				workingmbb[nowptr]=cal;
				nowptr++;
				if(nowptr==maxsize){
					nowptr=0;
					full=true;
				}
				return temp.activationMbb();
			}
		}
	}
	/**
	 * @return the nownumber
	 */
	public int getNownumber() {
		return nownumber;
	}
	/**
	 * @param nownumber the nownumber to set
	 */
	public void setNownumber(int nownumber) {
		this.nownumber = nownumber;
	}
	/**
	 * @return the datesort
	 */
	public List<Integer> getDatesort() {
		return datesort;
	}
	/**
	 * @param datesort the datesort to set
	 */
	public void setDatesort(List<Integer> datesort) {
		this.datesort = datesort;
	}
	/**
	 * @return the maxsize
	 */
	public int getMaxsize() {
		return maxsize;
	}
	/**
	 * @param maxsize the maxsize to set
	 */
	public void setMaxsize(int maxsize) {
		this.maxsize = maxsize;
	}
	/**
	 * @return the nowptr
	 */
	public int getNowptr() {
		return nowptr;
	}
	/**
	 * @param nowptr the nowptr to set
	 */
	public void setNowptr(int nowptr) {
		this.nowptr = nowptr;
	}

}
