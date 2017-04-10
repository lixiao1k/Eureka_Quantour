package data.fetchdataimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StockAverageStack {
	private int average_day;
	
	private double[] substockdata;
	private int subnowptr;
	private int subnumber;
	
	private double[] aftstockdata;
	private int aftnowptr;
	private int aftnumber;
	
	private BufferedWriter bw;
	private BufferedReader br;
	
	private String path;
	public StockAverageStack(int averagenumber,String stockpath,boolean isAddition) throws IOException{
		average_day=averagenumber;
		subnowptr=0;
		subnumber=0;
		substockdata=new double[averagenumber];
		aftnowptr=0;
		aftnumber=0;
		aftstockdata=new double[averagenumber];
		createAverageFile(stockpath,averagenumber);
		path=stockpath+"average_"+averagenumber;
		bw=new BufferedWriter(new FileWriter(stockpath+"average_"+averagenumber,isAddition));
	}
	private void createAverageFile(String stockPath,int average_day) throws IOException{
		File average=new File(stockPath+"average_"+average_day);
		if(!average.exists()){
			average.createNewFile();
		}
	}
	public void close() throws IOException{
		bw.close();
	}
	public void write(String str) throws IOException{
		bw.write(str+"\n");
	}
	public void addsub(double data){
		substockdata[subnowptr]=data;
		subnowptr++;
		subnumber++;
		if(subnowptr==average_day){
			subnowptr=0;
		}
	}
	public double sumsub(){
		double sum=0;
		if(subnumber>=average_day){
			for(int i=0;i<average_day;i++){
				sum=sum+substockdata[i];
			}
		}
		sum=sum/average_day;
		return sum;
	}
	public void addaft(double data){
		aftstockdata[aftnowptr]=data;
		aftnowptr++;
		aftnumber++;
		if(aftnowptr==average_day){
			aftnowptr=0;
		}
	}
	public double sumaft(){
		double sum=0;
		if(aftnumber>=average_day){
			for(int i=0;i<average_day;i++){
				sum=sum+aftstockdata[i];
			}
		}
		sum=sum/average_day;
		return sum;
	}
	public void openReader() throws FileNotFoundException{
		br=new BufferedReader(new FileReader(path));
	}
	public String readline() throws IOException{
		return br.readLine();
	}
	public boolean ready() throws IOException{
		return br.ready();
	}
	public void closeReader() throws IOException{
		br.close();
	}
}
