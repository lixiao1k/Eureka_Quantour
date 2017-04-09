package data.datahelperimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Properties;

import data.common.FileMethod;

public class InitEnvironment {
	Properties initpro;
	private static InitEnvironment i;
	private InitEnvironment(){
		initEnvironment();
	}
	public static InitEnvironment getInstance(){
		if(i==null) i=new InitEnvironment();
		return i;
	}
	/**
	 * 初始化环境
	 */
	private void initEnvironment(){
		String config="config";
		String backups="config/backups";
		String parse="config/parse";
		String resources="config/resources";
		String stock="config/stock";
		String user="config/user";
		String stockinfo="config/stock/info";
		String userinfo="config/user/info";
		String stockset="config/stock/stockset";
		String SHA="config/stock/stockset/SHA";
		String SHB="config/stock/stockset/SHB";
		String SZA="config/stock/stockset/SZA";
		String SZB="config/stock/stockset/SZB";
		String CYB="config/stock/stockset/CYB";
		String ZXB="config/stock/stockset/ZXB";
		String HS300="config/stock/stockset/HS300";
		String stocknamelisthtml="quote.stockstar.com/stock/stock_index.htm";
		FileMethod.getInstance().makepath(config);
		FileMethod.getInstance().makepath(backups);
		FileMethod.getInstance().makepath(parse);
		FileMethod.getInstance().makepath(resources);
		FileMethod.getInstance().makepath(stock);
		FileMethod.getInstance().makepath(user);
		FileMethod.getInstance().makepath(stockinfo);
		FileMethod.getInstance().makepath(userinfo);
		FileMethod.getInstance().makepath(stockset);
		FileMethod.getInstance().makepath(SHA);
		FileMethod.getInstance().makepath(SHB);
		FileMethod.getInstance().makepath(SZA);
		FileMethod.getInstance().makepath(SZB);
		FileMethod.getInstance().makepath(HS300);
		FileMethod.getInstance().makepath(CYB);
		FileMethod.getInstance().makepath(ZXB);
		File init=new File("config/init.properties");
		try{
			if(!init.exists()){
				init.createNewFile();
				initpro=new Properties();
				OutputStream out=new FileOutputStream("config/init.properties");
				initpro.setProperty("config", config);
				initpro.setProperty("backups", backups);
				initpro.setProperty("parse", parse);
				initpro.setProperty("resources", resources);
				initpro.setProperty("stock", stock);
				initpro.setProperty("user", user);
				initpro.setProperty("stockinfo", stockinfo);
				initpro.setProperty("userinfo", userinfo);
				initpro.setProperty("SHA", SHA);
				initpro.setProperty("SHB", SHB);
				initpro.setProperty("SZA", SZA);
				initpro.setProperty("SZB", SZB);
				initpro.setProperty("CYB", CYB);
				initpro.setProperty("ZXB", ZXB);
				initpro.setProperty("HS300", HS300);
				initpro.setProperty("StockNameListHtml", stocknamelisthtml);
				initpro.store(out, "init environment!");
				String path=init.getAbsolutePath();
					path=URLDecoder.decode(path, "UTF-8");
				String sets = "attrib +H \"" + path + "\"";    
	            Runtime.getRuntime().exec(sets);  
			}
			else{
				InputStream is=new FileInputStream("config/init.properties");
				initpro=new Properties();
				initpro.load(is);
				is.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public String getPath(String name){
		return initpro.getProperty(name,null);
	}
}
