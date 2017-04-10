package data.fetchdataimpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AverageStackFactory {
	private List<StockAverageStack> list;
	private String path;
	private boolean isAddition;
	private DecimalFormat df;
	public AverageStackFactory(String stockpath,boolean isAddition){
		list=new ArrayList<StockAverageStack>();
		df = new DecimalFormat("0.0000");
		this.path=stockpath;
		this.isAddition=isAddition;
	}
	public void addstack(int number) throws IOException{
		StockAverageStack temp=new StockAverageStack(number,path,isAddition);
		list.add(temp);
	}
	public void adddata(double subadj,double aftadj){
		for(StockAverageStack stack:list){
			stack.addsub(subadj);
			stack.addaft(aftadj);
		}
	}
	public void sumAndwrite() throws IOException{
		for(StockAverageStack stack:list){
			double a=stack.sumsub();
			double b=stack.sumaft();
			String str=df.format(a)+","+df.format(b);
			stack.write(str);
		}
	}
	public void close() throws IOException{
		for(StockAverageStack stack:list){
			stack.close();
		}
	}
	public void openReader() throws FileNotFoundException{
		for(StockAverageStack stack:list){
			stack.openReader();
		}
	}
	public String readline() throws IOException{
		String result="";
		for(int i=0;i<list.size();i++){
			result=result+","+list.get(i).readline();
		}
		return result;
	}
	public boolean ready() throws IOException{
		return list.get(0).ready();
	}
	public void closeReader() throws IOException{
		for(StockAverageStack stack:list){
			stack.close();
		}
	}
}
