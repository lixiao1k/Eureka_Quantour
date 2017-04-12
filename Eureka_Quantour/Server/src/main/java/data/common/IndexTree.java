package data.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexTree {
	public static void main(String[] args){
		new IndexTree();
	}
	private List<List<List<Integer>>> datetrie;
	private List<HashMap<Integer,Integer>> dateinfomap;
	private int mapnumber;
	public List<Integer> datesort;
	private Integer remain;
	
	public HashMap<Integer,Integer> stockindex;
	public List<List<Integer>> stockLinkedList;
	private List<Integer> stockremain;
	private int stocknumber;
	
	public List<HashMap<Integer,Integer>> i;
	
	public IndexTree(){
		mapnumber=0;
		dateinfomap=new ArrayList<HashMap<Integer,Integer>>();
		datetrie=new ArrayList<List<List<Integer>>>();
		for(int i=5;i<18;i++){
			List<List<Integer>> temp1=new ArrayList<List<Integer>>();
			for(int j=0;j<12;j++){
				List<Integer> temp2=new ArrayList<Integer>();
				for(int m=0;m<31;m++){
					Integer temp3=null;
					temp2.add(temp3);
				}
				temp1.add(temp2);
			}
			datetrie.add(temp1);
		}
		datesort=new ArrayList<Integer>();
		remain=null;
		setStocknumber(0);
		setStockindex(new HashMap<Integer,Integer>());
		setStockremain(new ArrayList<Integer>());
		setStockLinkedList(new ArrayList<List<Integer>>());
	}
	public void end(){
		List<HashMap<Integer,Integer>> copy=new ArrayList<HashMap<Integer,Integer>>();
		for(int i=0;i<datesort.size();i++){
			int cal=datesort.get(i);
			int year= cal / 10000;
			int month= (cal -year * 10000 ) / 100 ;
			int day=cal - year * 10000 - month * 100 ;
			copy.add(dateinfomap.get(datetrie.get(year-2005).get(month-1).get(day-1)));
			datetrie.get(year-2005).get(month-1).remove(day-1);
			datetrie.get(year-2005).get(month-1).add(day-1,i);
		}
		dateinfomap=copy;
		i=new ArrayList<HashMap<Integer,Integer>>();
		for(int k=0;k<stockLinkedList.size();k++){
			HashMap<Integer,Integer> temp=new HashMap<Integer,Integer>();
			for(int m=0;m<stockLinkedList.get(k).size();m++){
				temp.put(stockLinkedList.get(k).get(m), m);
			}
			i.add(temp);
		}
	}
	public void add(int year,int month,int day,int code,int row){
		int cal=year * 10000 + month * 100 + day;
		year=year-2005;
		month=month-1;
		day=day-1;
		Integer index=datetrie.get(year).get(month).get(day);	
		if(index==null){
			datetrie.get(year).get(month).remove(day);
			datetrie.get(year).get(month).add(day, mapnumber);
			HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
			map.put(code, row);
			dateinfomap.add(map);
			validateDateLeaf(cal,false);
			validateStockLeaf(code,cal);
			mapnumber++;
		}
		else{
			validateStockLeaf(code,cal);
			dateinfomap.get(datetrie.get(year).get(month).get(day)).put(code, row);
		}
	}
	private void validateDateLeaf(int cal,boolean flag){
		if(remain==null){
			remain=0;
			datesort.add(cal);
		}
		else{

			if(datesort.get(remain) < cal){
				if((datesort.size()-1)<=remain){
					datesort.add(cal);
				}
				else{
					remain++;
					validateDateLeaf(cal,true);
				}
			}
			else{
				if(flag){
					datesort.add(remain,cal);
				}
				else{
					remain=0;
					validateDateLeaf(cal,false);
				}
			}
		}
	}
	private void validateStockLeaf(int code,int cal){
		Integer judge=stockindex.putIfAbsent(code, stocknumber);
		if(judge==null){
			stockLinkedList.add(null);
			stockremain.add(null);
			stocknumber++;
		}
		int spindex=stockindex.get(code);
		Integer remain=stockremain.get(spindex);
		List<Integer> datelist=stockLinkedList.get(spindex);
		setLeafPrevious(remain,datelist,false,cal);
	}
	private void setLeafPrevious(Integer remain,List<Integer> datelist,boolean bigflag,int day){
		if(remain==null){
			remain=0;
			datelist=new ArrayList<Integer>();
			datelist.add(day);
		}
		else{
			if(datelist.get(remain) < day){
				if((datelist.size()-1)<=remain){
					datelist.add(day);
					remain++;
				}
				else{
					remain=remain++;
					bigflag=true;
					setLeafPrevious(remain,datelist,bigflag,day);
				}
			}
			else{
				if(bigflag){
					datelist.add(remain,day);
				}
				else{
					remain=0;
					setLeafPrevious(remain,datelist,bigflag,day);
				}
			}
		}
	}
	/**
	 * @return the stockindex
	 */
	public HashMap<Integer,Integer> getStockindex() {
		return stockindex;
	}
	/**
	 * @param stockindex the stockindex to set
	 */
	public void setStockindex(HashMap<Integer,Integer> stockindex) {
		this.stockindex = stockindex;
	}
	/**
	 * @return the stockLinkedList
	 */
	public List<List<Integer>> getStockLinkedList() {
		return stockLinkedList;
	}
	/**
	 * @param stockLinkedList the stockLinkedList to set
	 */
	public void setStockLinkedList(List<List<Integer>> stockLinkedList) {
		this.stockLinkedList = stockLinkedList;
	}
	/**
	 * @return the stockremain
	 */
	public List<Integer> getStockremain() {
		return stockremain;
	}
	/**
	 * @param stockremain the stockremain to set
	 */
	public void setStockremain(List<Integer> stockremain) {
		this.stockremain = stockremain;
	}
	/**
	 * @return the stocknumber
	 */
	public int getStocknumber() {
		return stocknumber;
	}
	/**
	 * @param stocknumber the stocknumber to set
	 */
	public void setStocknumber(int stocknumber) {
		this.stocknumber = stocknumber;
	}
}
