package data.fetchdataimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import data.common.IndexTree;
import data.datahelperimpl.InitEnvironment;
import data.parse.Parse;

public class StockInfoCalculate {
	private String path;
	private InitEnvironment ie;
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
		asf.addstack(10);
		asf.addstack(20);
		asf.addstack(30);
		asf.addstack(60);
		BufferedReader br_average=new BufferedReader(new FileReader(stockpath+"average_5"));
		while(br_subadj.ready()&&br_average.ready()){
			Double subadj=Double.parseDouble(br_subadj.readLine().split(",")[0]);
			Double aftadj=Double.parseDouble(br_aftadj.readLine().split(",")[0]);
			asf.adddata(subadj, aftadj);
			br_average.readLine();
		}
		br_average.close();
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
	public void processCalendarIndex() throws IOException{
		BufferedReader br=new BufferedReader(new FileReader("config/resources/mainIndex"));
		int count=0;
		IndexTree it=new IndexTree();
		while(br.ready()){
			String out=br.readLine();
			int cal=Parse.getInstance().getIntDate(out.substring(0, 10));
			int code=Integer.parseInt(out.substring(11));
			int year= cal / 10000;
			int month= (cal -year * 10000 ) / 100;
			int day=cal - year * 10000 - month * 100;
			it.add(year, month, day, code, count);
			count++;
		}
		it.end();
		br.close();
		
		File file1=new File("config/resources/mainIndex1");
		file1.createNewFile();
		BufferedWriter bw=new BufferedWriter(new FileWriter("config/resources/mainIndex1"));
		HashMap<Integer,Integer> map=it.stockindex;
		List<HashMap<Integer,Integer>> list=it.i;
		BufferedReader br1=new BufferedReader(new FileReader("config/resources/mainIndex"));
		int count1=0;
		while(br1.ready()){
			String out=br1.readLine();
			int cal=Parse.getInstance().getIntDate(out.substring(0, 10));
			int code=Integer.parseInt(out.substring(11));
			int year= cal / 10000;
			int month= (cal -year * 10000 ) / 100;
			int day=cal - year * 10000 - month * 100;
			list.get(map.get(code)).get(cal);
			count1++;
		}
		it.end();
		br1.close();
	}
}
