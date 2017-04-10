package data.fetchdataimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import data.datahelperimpl.InitEnvironment;

public class StockInfoCalculate {
	private String path;
	private InitEnvironment ie;
	public StockInfoCalculate(){
		ie=InitEnvironment.getInstance();
		path=ie.getPath("stockinfo");
		
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
}
