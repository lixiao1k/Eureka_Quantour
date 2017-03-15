package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vo.EMAInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;
/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月13日
 */
public class test {
	private static RemoteHelper rmic;
	
	public static void main(String args[]){
		try{
			rmic = RemoteHelper.getInstance();
			rmic.setRemote(Naming.lookup("rmi://localhost:8888/DateRemote"));
			System.out.println("连接服务器");
		}catch(Exception e){
			e.printStackTrace();
		}
//		List<EMAInfoVO> listA = new ArrayList<EMAInfoVO>();
		List<SingleStockInfoVO> listA = new ArrayList<SingleStockInfoVO>();
		MarketInfoVO listB = new MarketInfoVO();
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
//		Calendar begin1 = Calendar.getInstance();
//		Calendar end1 = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		try {
//			begin1.setTime(sdf.parse("4/1/14"));
//			end1.setTime(sdf.parse("4/29/14"));
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
//		begin.set(2014, 3, 1, 0, 0, 0);
//		end.set(2014, 3, 29, 0, 0, 0);
//		
//		try{
//			listA = RemoteHelper.getInstance().getStockLogic().getSingleStockInfoByTime("1", begin, end);
//			listB = RemoteHelper.getInstance().getStockLogic().getMarketInfo(begin);
//		}catch(RemoteException e){
//			e.printStackTrace();
//		}
//		System.out.println(listB.getVolume()+" "+listB.getNumOfDropEFP()+" "+
//				listB.getNumOfDropStop()+" "+listB.getNumOfOMCEFP());
//		if(	listA!=null	)
//			for(int i=0;i<listA.size();i++){
//				System.out.println(sdf.format(listA.get(i).getDate().getTime())+" "+listA.get(i).getHigh()+" "+listA.get(i).getVolume());
//			}
//		else{
//			System.out.println("listA is null");
//		}
		
//		System.out.println(sdf1.format(begin.getTime())+" "+sdf1.format(end.getTime()));
//		System.out.println(begin);
//		System.out.println(begin1);
//		System.out.println(sdf1.format(begin1.getTime())+" "+sdf1.format(end1.getTime()));
//		System.out.println(begin.compareTo(begin1));
	}
}
