package presentation.chart.scatterchart;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.RemoteHelper;

public class Maintest {
	private static RemoteHelper rmic;
	
	public static void main(String[] args) {

		try {
			rmic = RemoteHelper.getInstance();
			rmic.setRemote(Naming.lookup("rmi://localhost:8888/DateRemote"));
//			rmic.setRemote(Naming.lookup("rmi://114.212.43.109:8888/DateRemote"));
			System.out.println("连接服务器成功！");
		} catch (MalformedURLException e) {
			e.toString();
		} catch (RemoteException e) {
			e.toString();
		} catch (NotBoundException e) {
			e.toString();
		}
		
		new test().getROD();
	}
}
