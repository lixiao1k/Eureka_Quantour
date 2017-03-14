package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vo.EMAInfoVO;
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
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		begin.set(14, 3, 1, 0, 0, 0);
		end.set(14, 3, 29, 0, 0, 0);
		try{
			listA = RemoteHelper.getInstance().getStockLogic().getSingleStockInfoByTime("1", begin, end);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		List<Double> listSave = new ArrayList<Double>();
		for(int i=0;i<listA.size();i++){
//			listSave.add(listA.get(i).getEMA());
			listSave.add(listA.get(i).getLow());
		}
		for( int i=0; i<listSave.size(); i++ )
			System.out.println(listSave.get(i));
	}
}
