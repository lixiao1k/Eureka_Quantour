package data.datahelperimpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;
import java.util.Stack;

import data.datahelperservice.IStockDataHelper;
/**
 * 股票模块数据的数据处理实现
 * @author 刘宇翔
 *
 */
public class StockDataHelperImp implements IStockDataHelper {
	private static IStockDataHelper datahelper;//datahelper层的单例对象
	private File stockdata;//数据源文件
	private File filelog;//数据中每支股票的配置文件
	private Properties prop_file;//股票配置文件的properties对象
	private OutputStream out_file;//股票配置文件的outputstream对象
	private File stockranklog;//数据中股票顺序文件的配置文件
	private Properties prop_rank;//股票顺序配置文件的properties对象
	private OutputStream out_rank;//股票顺序配置文件的outputstream对象
	private File nameTocodelog;//股票名字与编号转换的配置文件
	private Properties prop_nameTocode;//股票名字与编号转换的配置文件的properties对象
	private OutputStream out_nameTocode;//股票名字与编号转换的配置文件的outputstream对象
	private File filepath;//所有stockdata相关数据的存储路径
	private Boolean need_init;//判断程序的配置文件是否生成的boolean变量
	private String path;
	/**
	 * stockdatahelper的初始化
	 */
	private StockDataHelperImp(){
		path=this.getClass().getResource("/").getPath();
		try {
			path=URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file=new File(path+"resources");
		if(!file.exists()&&!file.isDirectory()){
			file.mkdir();
		}
		stockdata=new File(path+"date.csv");
		filepath=new File(path+"resources/stock");
		if(!filepath.exists()&&!filepath.isDirectory())
		{
			filepath.mkdirs();
		}
		init();
	}
	/**
	 * stockdatahelper的初始化
	 */
	private void init(){
    	try{
    		filelog=new File(path+"resources/stock/filelog.properties");
			prop_file=new Properties();
			
			if(!filelog.exists()){
				need_init=true;
				filelog.createNewFile();
			}
			else{
				need_init=false;
			}
			stockranklog=new File(path+"resources/stock/stockranklog.properties");
			prop_rank=new Properties();		
			if(!stockranklog.exists()){
				need_init=true;
				stockranklog.createNewFile();
			}
			else{
				need_init=false;
			}
			nameTocodelog=new File(path+"resources/stock/nameTocodelog.properties");
			prop_nameTocode=new Properties();		
			if(!nameTocodelog.exists()){
				need_init=true;
				nameTocodelog.createNewFile();
			}
			else{
				need_init=false;
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	public static IStockDataHelper getInstance(){
		if(datahelper==null) datahelper=new StockDataHelperImp();
		return datahelper;
	}
	
	/**
	 * 获得所有股票信息
	 * @return HashMap<String,HashMap<String,String>> 股票信息的哈希表
	 */
	public HashMap<String,HashMap<String,String>> getAllStock(){
		if(need_init){
			return initData_Byrow();
		}
		else{
			return initData_Byproperties();
		}
	}
	
	/**
	 * 软件初次启动时，读取数据的同时生成配置文件
	 * @return HashMap<String,HashMap<String,String>> 股票信息的哈希表
	 */
	private HashMap<String,HashMap<String,String>> initData_Byrow(){
		try
		{
			System.out.println("初始化可能会花费稍微多一点的时间，当出现success时即可运行。");
			
			//创建变量
			out_file = new FileOutputStream(path+"resources/stock/filelog.properties");
			out_rank = new FileOutputStream(path+"resources/stock/stockranklog.properties");
			out_nameTocode = new FileOutputStream(path+"resources/stock/nameTocodelog.properties");
			FileReader fr=new FileReader(stockdata);
			BufferedReader br=new BufferedReader(fr);
			File file1=new File(path+"date1.csv");
			FileWriter fw=new FileWriter(file1);
        	BufferedWriter bw=new BufferedWriter(fw);
			HashMap<String,HashMap<String,String>> result=
					new HashMap<String,HashMap<String,String>>();
			bw.write(br.readLine()+"\n");
			String printnumber="1";
			int i=2;
			int j=2;
			int now=1;
			int code_series=-1;
			String ne="";
			String printCodeName="深发展A";
			//开始读取数据以及生成配置文件
			while(br.ready()){
				String out=br.readLine();
				String[] output=out.split("\t");
				String cal=output[1];
				if(printnumber.equals(output[8])){
					j++;
					code_series++;
				}
				else{
					prop_file.setProperty(printnumber,i+","+(j-1));
					prop_nameTocode.setProperty(printCodeName, printnumber);
					prop_rank.setProperty(String.valueOf(now), printnumber);
					printnumber=output[8];
					printCodeName=chkHalf(output[9].replace(" ", ""));
					i=j;
					j++;
					code_series=0;
					now++;
				}
				ne=String.valueOf(code_series)+"\t"+output[1]+"\t"+output[2]+"\t"+output[3]+"\t"+output[4]+"\t"+output[5]+
            			"\t"+output[6]+"\t"+output[7]+"\t"+output[8]+"\t"+chkHalf(output[9].replace(" ", ""))+"\t"+output[10];
				if(output[6].equals("0")){
					code_series--;
					j--;
				}
			//处理交易量为0的数据。
//					stack.push(output);
//					wrongdate=true;
//				}
//				else if(wrongdate){
//					wrongdate=false;
//					String input=ne;
//					last_close=output[5];
//					last_adjclose=output[7];
//					while(!stack.isEmpty()){
//						String[] m=stack.pop();
//						String str_pop=m[0]+"\t"+m[1]+"\t"+last_close+"\t"+last_close+"\t"+last_close
//								+"\t"+last_close+"\t"+m[6]+"\t"+last_adjclose
//								+"\t"+m[8]+"\t"+chkHalf(m[9].replace(" ", ""))+"\t"+m[10];
//						input=str_pop+"\n"+input;
//						if(result.containsKey(m[1])){
//							result.get(m[1]).put(output[8], str_pop);
//						}
//						else{
//							HashMap<String,String> map=new HashMap<String,String>();
//							map.put(m[8], str_pop);
//							result.put(m[1], map);
//						}
//					}
//					bw.write(input+"\n");
//				}
				else{
					bw.write(ne+"\n");
					if(result.containsKey(cal)){
						result.get(cal).put(output[8], ne);
					}
					else{
						HashMap<String,String> map=new HashMap<String,String>();
						map.put(output[8], ne);
						result.put(cal, map);
					}
				}			
//				if(output[6].equals("0")&&!output[5].equals(output[2])
//            			&&!output[3].equals(output[4])){
//            		prop_ignore.setProperty(String.valueOf(j-1), "1");
//            		continue;
//            	}
				
			}
			
			//导出配置文件
			prop_file.setProperty(printnumber,i+","+(j-1));
			prop_file.store(out_file, "date汇总");
			prop_rank.setProperty(String.valueOf(now), printnumber);
			prop_rank.store(out_rank, "abcd");
			prop_nameTocode.setProperty(printCodeName.replace(" ", ""),printnumber);
			prop_nameTocode.store(out_nameTocode, "股票编号与名字转换");
			br.close();
			fr.close();
			bw.close();
			fw.close();
			stockdata.delete();
            file1.renameTo(stockdata);
//          FileReader fr_again=new FileReader(stockdata);
//			BufferedReader br_again=new BufferedReader(fr_again);
//			br_again.readLine();
//            while(br_again.ready()){
//				String out=br_again.readLine();
//				String[] output=out.split("\t");
//				String cal=output[1];
//				if(result.containsKey(cal)){
//					result.get(cal).put(output[8], out);
//				}
//				else{
//					HashMap<String,String> map=new HashMap<String,String>();
//					map.put(output[8], out);
//					result.put(cal, map);
//				}
//            }
//            br_again.close();
            BufferedInputStream nameTocode_in = new BufferedInputStream(
					new FileInputStream(path+"resources/stock/nameTocodeLog.properties"));
			prop_nameTocode.load(nameTocode_in);
			nameTocode_in.close();
			return result;
		}catch(Exception e)
		{
			e.printStackTrace();
			return this.initData_Byrow();
		}
	}
	
	/**
	 * 软件不是初次启动时，根据配置文件读取数据
	 * @return HashMap<String,HashMap<String,String>> 股票信息的哈希表
	 */
	private HashMap<String,HashMap<String,String>> initData_Byproperties(){
		try{
			System.out.println("当出现success时即可运行。");
			//创建变量
			HashMap<String,HashMap<String,String>> result=new HashMap<String,HashMap<String,String>>();
			FileReader fr=new FileReader(stockdata);
			BufferedReader br=new BufferedReader(fr);
			
			//导入配置文件
			Properties prop=new Properties();
			BufferedInputStream inputStream = new BufferedInputStream(
					new FileInputStream(path+"resources/stock/filelog.properties"));
			prop.load(inputStream);
			inputStream.close();
			Properties prop1=new Properties();
			BufferedInputStream inputStream1 = new BufferedInputStream(
					new FileInputStream(path+"resources/stock/stockranklog.properties"));
			prop1.load(inputStream1);
			inputStream1.close();
			
			//读取数据的准备工作
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
			
			//开始根据配置文件读取数据
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
				if(cal.equals("\\s+")){
					System.out.println(str);
					break;
				}
				if(cal.equals("")){
					System.out.println(i);
					System.out.println(str);
					break;
				}
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
			BufferedInputStream nameTocode_in = new BufferedInputStream(
					new FileInputStream(path+"resources/stock/nameTocodeLog.properties"));
			prop_nameTocode.load(nameTocode_in);
			nameTocode_in.close();
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
	public String getNameToCode(String name){
		return prop_nameTocode.getProperty(name,null);
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
	 private String chkHalf(String str){
	    	String sum="";
	        for(int i=0;i<str.length();i++)
	            {   
	               char strCode=str.charAt(i);   
	               if((strCode>65248)||(strCode==12288)){  
	            	   strCode=(char) (strCode-65248);
	                } 
	               sum=sum+strCode;
	        }
	        return sum;
	  }
}
