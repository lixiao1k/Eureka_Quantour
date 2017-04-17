package data.fetchdataimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import data.common.DateLeaf;
import data.common.DateTrie;
import data.common.IndexTree;
import data.datahelperimpl.InitEnvironment;
import data.parse.Parse;

public class StockInfoCalculate {
	private String path;
	private InitEnvironment ie;
	public static void main(String[] args){
	}
	public StockInfoCalculate(){
		ie=InitEnvironment.getInstance();
		path=ie.getPath("stockinfo");
		IndexTree it=new IndexTree();
	}
	public void processAverage(){
		File rootpath=new File(path);
		String[] codelist=rootpath.list();
		int size=codelist.length;
		int i=0;
		for(String code:codelist){
			try {
				i++;
				System.out.println("当前处理第"+i+"个，剩余"+(size-i)+"个");
				calAverage(code,true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private	void calAverage(String code,boolean isAddition) throws IOException{
		String stockpath=path+"/"+code+"/";
//		BufferedReader br_data=new BufferedReader(new FileReader(stockpath+"data"));
		BufferedReader br_subadj=new BufferedReader(new FileReader(stockpath+"subscription"));
		BufferedReader br_aftadj=new BufferedReader(new FileReader(stockpath+"afterscription"));
		AverageStackFactory asf=new AverageStackFactory(stockpath,isAddition);
		asf.addstack(5);
//		BufferedReader br_average=new BufferedReader(new FileReader(stockpath+"average_5"));
//		while(br_subadj.ready()&&br_average.ready()){
//			Double subadj=Double.parseDouble(br_subadj.readLine().split(",")[0]);
//			Double aftadj=Double.parseDouble(br_aftadj.readLine().split(",")[0]);
//			asf.adddata(subadj, aftadj);
//			br_average.readLine();
//		}
//		br_average.close();
		while(br_subadj.ready()){
			Double subadj=Double.parseDouble(br_subadj.readLine().split(",")[0]);
			Double aftadj=Double.parseDouble(br_aftadj.readLine().split(",")[0]);
			asf.adddata(subadj, aftadj);
			asf.sumAndwrite();
		}
		asf.close();
//		br_data.close();
		br_subadj.close();
		br_aftadj.close();
	}
}
class List{
	ArrayList<Integer> list;
	ArrayList<HashMap<Integer,Integer>> map;
	public List(){
		list=new ArrayList<Integer>();
		map=new ArrayList<HashMap<Integer,Integer>>();
	}
	public void add(int cal,int code,int row){
		if(list.size()==0){
			HashMap<Integer,Integer> temp=new HashMap<Integer,Integer>();
			temp.put(code, row);
			list.add(cal);
			map.add(temp);
		}
		else if(list.get(list.size()-1)<cal){
			HashMap<Integer,Integer> temp=new HashMap<Integer,Integer>();
			temp.put(code, row);
			list.add(cal);
			map.add(temp);
		}
		else if(list.get(list.size()-1)==cal){
			map.get(list.size()-1).put(code, row);
		}
		else{
			for(int i=0;i<list.size()-1;i++){
				if(list.get(i)<=cal&&cal<list.get(i+1)){
					if(list.get(i)==cal){
						map.get(i).put(code, row);
					}
					else{
						HashMap<Integer,Integer> temp=new HashMap<Integer,Integer>();
						temp.put(code, row);
						list.add(i+1,cal);
						map.add(i+1,temp);
					}
				}
			}
		}
	}
	public int get(int i){
		return list.get(i);
	}
	public int size(){
		return list.size();
	}
}
