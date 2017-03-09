package data.datahelperimpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
	private File calendarlog;
	private Properties prop_cal;
	private OutputStream out_cal;
	private File stockranklog;
	private Properties prop_rank;
	private OutputStream out_rank;
	private File filepath;
	private Boolean need_init;
	private StockDataHelperImp(){
		stockdata=new File("F://date.csv");
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
			calendarlog=new File("data/stock/calendarlog.properties");
			prop_cal=new Properties();
			
			if(!calendarlog.exists()){
				calendarlog.createNewFile();
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
			out_file = new FileOutputStream("data/stock/filelog.properties");
			out_cal = new FileOutputStream("data/stock/calendarlog.properties");
			out_rank = new FileOutputStream("data/stock/stockranklog.properties");
			FileReader fr=new FileReader(stockdata);
			BufferedReader br=new BufferedReader(fr);
			HashMap<String,HashMap<String,String>> result=
					new HashMap<String,HashMap<String,String>>();
			br.readLine();
			String printnumber="1";
			int i=0;
			int j=0;
			int now=1;
			while(br.ready()){
				String out=br.readLine();
				String[] output=out.split("\\s+");
				String cal=output[1];
				if(printnumber.equals(output[8])){
					j++;
				}
				else{
					prop_file.setProperty(printnumber,i+","+j);
					prop_rank.setProperty(String.valueOf(now), printnumber);
					printnumber=output[8];
					j++;
					i=j;
					now++;
				}
				prop_cal.setProperty(String.valueOf(j), output[1]);
				if(result.containsKey(cal)){
					result.get(cal).put(output[8], out);
				}
				else{
					HashMap<String,String> map=new HashMap<String,String>();
					map.put(output[8], out);
					result.put(cal, map);
				}
			}
			j++;
			prop_file.setProperty(printnumber,i+","+j);
			prop_file.store(out_file, "date汇总");
			prop_rank.setProperty(String.valueOf(now), printnumber);
			prop_rank.store(out_rank, "abcd");
			prop_cal.store(out_cal, "cal汇总");
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
		int now_row=1;
		try{
			File stockdata=new File("F://date.csv");
			FileReader fr=new FileReader(stockdata);
			BufferedReader br=new BufferedReader(fr);
			Properties prop=new Properties();
			BufferedInputStream inputStream = new BufferedInputStream(
					new FileInputStream("data/stock/calendarlog.properties"));
			prop.load(inputStream);
			inputStream.close();
			Properties prop1=new Properties();
			InputStream inputStream1 = new BufferedInputStream( 
					new FileInputStream("data/stock/stockranklog.properties"));
			prop1.load(inputStream1);
			inputStream1.close();
			br.readLine();
			HashMap<String,HashMap<String,String>> result=
					new HashMap<String,HashMap<String,String>>();
			int old_code=-1;
			int rownumber=1;
			String row_code="1";
			while(br.ready()){
				String cal=prop.getProperty(String.valueOf(now_row));
				String str=br.readLine();
				if(now_code(str)==(old_code+1)){
					old_code++;
				}
				else{
					old_code=0;
					rownumber=rownumber+1;
					row_code=prop1.getProperty(String.valueOf(rownumber));
				}
				if(result.containsKey(cal)){
					result.get(cal).put(row_code, str);
				}
				else{
					HashMap<String,String> map=new HashMap<String,String>();
					map.put(row_code, str);
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
	private int now_code(String str){
		int i=0;
		String code="";
		char now;
		while(true){
			now=str.charAt(i);
			i++;
			if(now>='0'&&now<='9'){
				code=code+now;
			}
			else{
				break;
			}
		}
		return Integer.parseInt(code);
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
	public ArrayList<String> find(HashMap<String,HashMap<String,String>> map,String code,Calendar start,Calendar end){
		Calendar i=start;
		ArrayList<String> list=new ArrayList<String>();
		while(i.compareTo(end)<=0){
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
			if(map.containsKey(tostring(sdf.format(i.getTime())))){
				list.add(map.get(tostring(sdf.format(i.getTime()))).get(code));
			}
			else{
				i.set(Calendar.DATE, i.get(Calendar.DATE)+1);
			}
			i.set(Calendar.DATE, i.get(Calendar.DATE)+1);
		}
		return list;
	}
	private String tostring(String str){
		String[] out=str.split("/");
		if(out[0].length()==2&&out[0].charAt(0)=='0'){
			out[0]=out[0].substring(1);
		}
		if(out[1].length()==2&&out[1].charAt(0)=='0'){
			out[1]=out[1].substring(1);
		}
		return out[0]+"/"+out[1]+"/"+out[2];
	}
}
