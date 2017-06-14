package data.serviceimpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import data.datahelperimpl_ByDataBase.UserDataHelperImpl_DBO;
import exception.DisConnectedException;

public class UserPool implements Runnable{
	private HashMap<String,Integer> user_map;
	private static int check_time=1000*5;
	@Override
	public void run() {
		init();
	}
	public void init()
	{
		user_map=new HashMap<String,Integer>();
		HashMap<String,Integer> previous=new HashMap<String,Integer>();
		while(true)
		{
			try {
				Thread.sleep(check_time);
			} catch (InterruptedException e) {
			}
			Iterator<Entry<String, Integer>> it=user_map.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String, Integer> entry=it.next();
				int temp=previous.getOrDefault(entry.getKey(), 0);
				System.out.println(temp+":"+entry.getValue());
				if(temp==entry.getValue())
				{
					logout(entry.getKey());
				}
			}
			previous=user_map;
		}
	}
	public synchronized void register(String userName)
	{
		user_map.put(userName, 0);
	}
	public synchronized void getConn(String userName) throws DisConnectedException
	{
		int i = user_map.getOrDefault(userName, -1);
		if(i==-1)
		{
			throw new DisConnectedException("之前网络连接断开，请重新连接");
		}
		user_map.put(userName, i+1);
	}
	public synchronized void logout(String userName)
	{
		user_map.remove(userName);
		UserDataHelperImpl_DBO.getInstance().logout(userName);
	}
}
