package data.datahelperimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.datahelperservice.IStockSetDataHelper;
import exception.NullSetException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;

public class StockSetDataHelperImpl implements IStockSetDataHelper {
	private static IStockSetDataHelper datahelper;
	private String userPath;
	private InitEnvironment ie;
	private StockSetDataHelperImpl(){
		ie=InitEnvironment.getInstance();
		userPath=ie.getPath("userinfo");
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
	/**
	 * 获取某个用户自定义股票池的股票编号
	 * @param stockSetName 股票池名字
	 * @param userName,用户名字
	 * @return 一个股票编号的列表
	 * @throws NullSetException 
	 */
	public List<String>  getStockSetInfo(String stockSetName,String userName) throws NullSetException{
		String path=userPath+"/"+userName+"/"+stockSetName;
		File set=new File(path);
		if(!set.exists()){
			throw new NullSetException(stockSetName,userName);
		}
		String[] nameList=set.list();
		List<String> list=new ArrayList<String>();
		for(String name:nameList){
			list.add(name);
		}
		return list;
	}
	/**
	 * 获取某个股票池的股票编号
	 * @param stockSetName 股票池名字
	 * @return 一个股票编号的列表
	 * @throws NullSetException 
	 */
	public List<String>  getStockSetInfo(String stockSetName) throws NullSetException{
		String path="config/stock/stockset/";
		File set=new File(path+stockSetName);
		if(!set.exists()){
			throw new NullSetException(stockSetName,"System");
		}
		String[] nameList=set.list();
		List<String> list=new ArrayList<String>();
		for(String name:nameList){
			list.add(name);
		}
		return list;
	}
	/**
	 * 获取行业板块中的各种分类
	 * @return 行业板块中的各种分类
	 */
	public List<String> getIndustryList(){
		String path="config/stock/stockset/Industry";
		File set=new File(path);
		List<String> list=new ArrayList<String>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(set));
			while(br.ready()){
				String str=br.readLine();
				if(str.length()>0){
					list.add(str);
				}
			}
			br.close();
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}
