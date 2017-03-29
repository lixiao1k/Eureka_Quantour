package data.fetchdataimpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import data.fetchdataservice.IStockDataFetch;
import resultmessage.InternetdisconnectException;

public class StockDataFetchImpl implements IStockDataFetch{
	public static void main(String[] args){
		new StockDataFetchImpl();
	}
	public StockDataFetchImpl(){
		try {
			testInternet();
		} catch (InternetdisconnectException e) {
			System.out.println(e.toString());
		}
	}
	/**
	 * 从网上获取所有股票的名字与编号
	 * @return 名字与编号组成的String的列表（格式:"编号名字"）
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	@Override
	public List<String> getAllStockName() throws InternetdisconnectException{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 判断网络是否可连接
	 * @throws InternetdisconnectException
	 */
	private void testInternet() throws InternetdisconnectException{
		String address ="www.baidu.com";
		BufferedReader br=null;
		try {
			Process process = Runtime.getRuntime().exec("ping " + address+" -n 5");
			br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
			String connectionStr =null;
			String total="";
			while ((connectionStr = br.readLine()) != null) {
				System.out.println(connectionStr);
				total=total+connectionStr;
			}
			br.close();
			if(total.indexOf("TTL")<0){
				throw new InternetdisconnectException();
			}
		} catch (IOException e) {
			System.out.println("链接失败");
			e.printStackTrace();
		}
	}
}
