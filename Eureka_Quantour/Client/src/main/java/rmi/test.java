package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vo.EMAInfoVO;

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
		List<EMAInfoVO> listA = new ArrayList<EMAInfoVO>();
		try{
			listA = RemoteHelper.getInstance().getStockLogic().getEMAInfo("1", Calendar.getInstance(), Calendar.getInstance()).get(0);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		List<Double> listSave = new ArrayList<Double>();
		for(int i=0;i<listA.size();i++)
			listSave.add(listA.get(i).getEMA());
		for( int i=0; i<listSave.size(); i++ )
			System.out.println(listSave.get(i));
	}
}
