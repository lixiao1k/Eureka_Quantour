package data.datahelperimpl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import data.datahelperservice.IStockSetDataHelper;
import resultmessage.StockNameRepeatException;
import resultmessage.StockSetNameRepeatException;

public class StockSetDataHelperImpl implements IStockSetDataHelper {
	private static IStockSetDataHelper datahelper;
	private String userPath;
	private StockSetDataHelperImpl(){
		userPath="config/user/info";
		try {
			userPath=URLDecoder.decode(userPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file=new File(userPath);
		if(!file.exists()&&!file.isDirectory()){
			file.mkdir();
		}
	}
	public static IStockSetDataHelper getInstance(){
		if(datahelper==null) datahelper=new StockSetDataHelperImpl();
		return datahelper;
	}
	/**
	 * 获取某个用户的自定义股票池
	 * @param username 用户名
	 * @return 所有股票池的自定义名字
	 */
	public List<String> getStockSet(String username){
		File setpath=new File(userPath+"/"+username);
		String[] setlist=setpath.list();
		if(setlist.length==0){
			System.out.println("该用户没有股票池");
			return null;
		}
		else{
			List<String> result=new ArrayList<String>();
			for(String str:setlist){
				result.add(str);
				System.out.println(str);
			}
			return result;
		}
	}
	/**
	 * 添加一个新的股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 * @throws StockSetNameRepeatException 股票池名字重复时抛出该异常
	 */
	public void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException{
		File path=new File(userPath+"/"+username+"/"+stockSetName);
		if(!path.exists()&&!path.isDirectory()){
			path.mkdirs();
		}
		else{
			throw new StockSetNameRepeatException();
		}
	}
	/**
	 * 删除一个股票池
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	public void deleteStockSet(String stockSetName, String username){
		File path=new File(userPath+"/"+username+"/"+stockSetName);
		File[] filelist=path.listFiles();
		for(File file:filelist){
			file.delete();
		}
		path.delete();
	}
	/**
	 * 添加一个新的股票到股票池中
	 * @param stockName 股票的名字
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 * @throws StockNameRepeatException 股票池中已存在该股票时抛出异常
	 */
	public void addStockToStockSet(String stockName, String stockSetName, String username) throws
	StockNameRepeatException{
		File file=new File(userPath+"/"+username+"/"+stockSetName+"/"+stockName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			throw new StockNameRepeatException();
		}
	}
	/**
	 * 从股票池中删除一个股票
	 * @param stockName 股票的名字
	 * @param stockSetName 股票池的名字
	 * @param username	用户名
	 */
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username){
		File file=new File(userPath+"/"+username+"/"+stockSetName+"/"+stockName);
		file.delete();
	}
}
