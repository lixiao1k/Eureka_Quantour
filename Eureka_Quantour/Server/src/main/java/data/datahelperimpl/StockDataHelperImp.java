package data.datahelperimpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

import data.datahelperservice.IStockDataHelper;
/**
 * 股票模块数据的数据处理实现
 * @author 刘宇翔
 *
 */
public class StockDataHelperImp implements IStockDataHelper {
	private static IStockDataHelper datahelper;
	private File stockdata;
	private File filelog;
	private Properties prop_file;
	private OutputStream out_file;
	private File stockranklog;
	private Properties prop_rank;
	private OutputStream out_rank;
	private File filepath;
	private Boolean need_init;
	private StockDataHelperImp(){
		stockdata=new File("date.csv");
		filepath=new File("data/stock");
		if(!filepath.exists()&&!filepath.isDirectory())
		{
			filepath.mkdirs();
		}
		init();
	}
	private void init(){
    	try{
    		filelog=new File("data/stock/filelog.properties");
			prop_file=new Properties();
			
			if(!filelog.exists()){
				need_init=true;
				filelog.createNewFile();
			}
			else{
				need_init=false;
			}
			stockranklog=new File("data/stock/stockranklog.properties");
			prop_rank=new Properties();		
			if(!stockranklog.exists()){
				stockranklog.createNewFile();
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	public static IStockDataHelper getInstance(){
		if(datahelper==null) datahelper=new StockDataHelperImp();
		return datahelper;
	}
	public HashMap<String,HashMap<String,String>> getAllStock(){
		if(need_init){
			return initData_Byrow();
		}
		else{
			return initData_Byproperties();
		}
	}
	private HashMap<String,HashMap<String,String>> initData_Byrow(){
		try
		{
			System.out.println("initData_Byproperties");
			out_file = new FileOutputStream("data/stock/filelog.properties");
			out_rank = new FileOutputStream("data/stock/stockranklog.properties");
			FileReader fr=new FileReader(stockdata);
			BufferedReader br=new BufferedReader(fr);
			HashMap<String,HashMap<String,String>> result=
					new HashMap<String,HashMap<String,String>>();
			br.readLine();
			String printnumber="1";
			int i=2;
			int j=2;
			int now=1;
			while(br.ready()){
				String out=br.readLine();
				String[] output=out.split("\t");
				String cal=output[1];
				if(printnumber.equals(output[8])){
					j++;
				}
				else{
					prop_file.setProperty(printnumber,i+","+(j-1));
					prop_rank.setProperty(String.valueOf(now), printnumber);
					printnumber=output[8];
					i=j;
					j++;
					now++;
				}
				if(result.containsKey(cal)){
					result.get(cal).put(output[8], out);
				}
				else{
					HashMap<String,String> map=new HashMap<String,String>();
					map.put(output[8], out);
					result.put(cal, map);
				}
			}
			prop_file.setProperty(printnumber,i+","+(j-1));
			prop_file.store(out_file, "date汇总");
			prop_rank.setProperty(String.valueOf(now), printnumber);
			prop_rank.store(out_rank, "abcd");
			br.close();
			fr.close();
			return result;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	private HashMap<String,HashMap<String,String>> initData_Byproperties(){
		try{
			HashMap<String,HashMap<String,String>> result=new HashMap<String,HashMap<String,String>>();
			FileReader fr=new FileReader(stockdata);
			BufferedReader br=new BufferedReader(fr);
			Properties prop=new Properties();
			BufferedInputStream inputStream = new BufferedInputStream(
					new FileInputStream("data/stock/filelog.properties"));
			prop.load(inputStream);
			inputStream.close();
			Properties prop1=new Properties();
			BufferedInputStream inputStream1 = new BufferedInputStream(
					new FileInputStream("data/stock/stockranklog.properties"));
			prop1.load(inputStream1);
			inputStream1.close();
			br.readLine();
			int now_row=2;
			String now_rank="1";
			Boolean flag=true;
			String[] range=new String[2];
			range[0]="2";
			range[1]="2";
			int rank=1;
			int code=0;
			int i=0;
			String str="";
			String cal="";
			int big_number=2;
			int small_number=2;
			while(br.ready()){
				str=br.readLine();
				if(now_row>big_number||now_row==2){
					flag=true;
					now_rank=prop1.getProperty(String.valueOf(rank));
					rank++;
				}
				if(flag){
					range=prop.getProperty(now_rank).split(",");
					small_number=Integer.parseInt(range[0]);
					big_number=Integer.parseInt(range[1]);
					flag=false;
				}
				code=now_row-small_number;
				i=String.valueOf(code).length()+1;
				cal=str.substring(i, str.indexOf("\t", i));
				if(result.containsKey(cal)){
					result.get(cal).put(now_rank, str);
				}
				else{
					HashMap<String,String> map=new HashMap<String,String>();
					map.put(now_rank,str);
					result.put(cal, map);
				}
				now_row++;
			}
			br.close();
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
	
//	public ArrayList<String> find(HashMap<String,HashMap<String,String>> map,String code,Calendar start,Calendar end){
//		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
//		String cal2=tostring(sdf.format(start.getTime()));
//		String cal1=tostring(sdf.format(end.getTime()));
//		String start_line=map.get(cal1).get(code).split("\\s+")[0];
//		String end_line=map.get(cal2).get(code).split("\\s+")[0];
//		Properties prop=new Properties();
//		InputStream in;
//		String lines="";
//		ArrayList<String> list=new ArrayList<String>();
//		try {
//			in = new BufferedInputStream(new FileInputStream("data/stock/calendarlog.properties"));
//			prop.load(in);
//			lines=prop.getProperty(code);
//			System.out.println(lines);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		File stockdata=new File("F://date.csv");
//		FileReader fr;
//		try {
//			fr = new FileReader(stockdata);
//			BufferedReader br=new BufferedReader(fr);
//			int i=0;
//			br.readLine();
//			while(br.ready()){
//				String read=br.readLine();
//				if(i>=Integer.parseInt(start_line)+Integer.parseInt(lines.split(",")[0])
//				&&i<=Integer.parseInt(end_line)+Integer.parseInt(lines.split(",")[0])){
//					list.add(read);
//				}
//				else if(i>=Integer.parseInt(end_line)+Integer.parseInt(lines.split(",")[0])){
//					break;
//				}
//				i++;
//			}
//			br.close();
//			return list;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("发生错误");
//			return null;
//		}
//	}
}
