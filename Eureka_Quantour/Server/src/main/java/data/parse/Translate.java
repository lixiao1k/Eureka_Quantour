package data.parse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import data.fetchdataimpl.StockDataFetchImpl;
import data.fetchdataservice.IStockDataFetch;
import exception.InternetdisconnectException;

/**
 * 将股票编号翻译成股票名字或把股票名字翻译成股票编号
 * @author 刘宇翔
 *
 */
public class Translate {
	private static Translate translate;
	private Properties nameTocode_pro;
	private Properties codeToname_pro;
	private File nameTocode;
	private File codeToname;
	private IStockDataFetch datafetch;
	private Translate(){	
		try{
			nameTocode_pro=new Properties();
			codeToname_pro=new Properties();
			nameTocode=new File("config/parse/nameTocode.properties");
			codeToname=new File("config/parse/codeToname.properties");
			if(!nameTocode.exists()||!codeToname.exists()){
				try {
					datafetch=StockDataFetchImpl.getInstance();
					datafetch.fetchAllStockSet();
					datafetch=null;
				} catch (InternetdisconnectException e) {
					System.out.println(e.toString());
				}
			}
			BufferedInputStream nameTocode_in = new BufferedInputStream(
					new FileInputStream("config/parse/nameTocode.properties"));
			nameTocode_pro.load(nameTocode_in);
			nameTocode_in.close();
			BufferedInputStream codeToname_in = new BufferedInputStream(
					new FileInputStream("config/parse/codeToname.properties"));
			codeToname_pro.load(codeToname_in);
			codeToname_in.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static Translate getInstance(){
		if(translate==null) translate=new Translate();
		return translate;
	}
	public String trans_nameTocode(String name){
		return nameTocode_pro.getProperty(Parse.getInstance().chkHalf(name),name);
	}
	public String trans_codeToname(String code){
		return codeToname_pro.getProperty(Parse.getInstance().supCode(code),code);
	}
	public List<String> getAllCode(){
		List<String> result=new ArrayList<String>();
		Iterator<Entry<Object, Object>> it=codeToname_pro.entrySet().iterator();
		while(it.hasNext()){
			result.add((String) it.next().getKey());
		}
		return result;
	}
	public boolean containsCode(String code){
		return codeToname_pro.containsKey(code);
	}
	public void reload(){
		try{
			nameTocode_pro=new Properties();
			codeToname_pro=new Properties();
			try {
				datafetch=StockDataFetchImpl.getInstance();
				datafetch.fetchAllStockSet();
				datafetch=null;
			} catch (InternetdisconnectException e) {
				System.out.println(e.toString());
			}
			BufferedInputStream nameTocode_in = new BufferedInputStream(
					new FileInputStream("config/parse/nameTocode.properties"));
			nameTocode_pro.load(nameTocode_in);
			nameTocode_in.close();
			BufferedInputStream codeToname_in = new BufferedInputStream(
					new FileInputStream("config/parse/codeToname.properties"));
			codeToname_pro.load(codeToname_in);
			codeToname_in.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
