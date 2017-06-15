package data.serviceimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import data.database.DataBaseOperation;
import data.datahelperimpl_ByDataBase.UserDataHelperImpl_DBO;
import exception.DisConnectedException;
import exception.SqlNotConnectedException;

public class UserPool implements Runnable{
	private HashMap<String,Integer> user_map;
	private HashMap<String,Integer> previous;
	private static int check_time=1000*5;
	@Override
	public void run() {
		before_init();
		init();
	}
	public synchronized void before_init()
	{
		try
		{
			File file=new File("config/SQL_LOG");
			if(file.exists())
			{
				BufferedReader bw=new BufferedReader(new FileReader(file));
				while(bw.ready())
				{
					String userName = bw.readLine();
					UserDataHelperImpl_DBO.getInstance().logout(userName);
				}
				bw.close();
				file.delete();
			}
		}catch(IOException e)
		{
			
		}
	}
	public void init()
	{
		user_map=new HashMap<String,Integer>();
		previous=new HashMap<String,Integer>();
		while(true)
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			check();
		}
	}
	public synchronized void check()
	{
		Connection conn=null;
		conn=DataBaseOperation.getInstance().getConn(conn);
		boolean flag=true;
		if(conn==null)
		{
			flag=false;
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		Iterator<Entry<String, Integer>> it=user_map.entrySet().iterator();
		if(!flag)
		{
			try
			{
				File file=new File("config/SQL_LOG");
				if(!file.exists())
				{
					file.createNewFile();
				}
				BufferedWriter bw=new BufferedWriter(new FileWriter(file));
				while(it.hasNext())
				{
					bw.write(it.next().getKey());
					bw.write("\n");
				}
				bw.close();
				System.out.println("连接不上数据库，请检查网络后再试");
				System.exit(0);
			}catch(IOException e)
			{
				
			}
		}
		else
		{
			while(it.hasNext())
			{
				Entry<String, Integer> entry=it.next();
				int temp=previous.getOrDefault(entry.getKey(), -1);
				if(temp==entry.getValue())
				{
					logout(entry.getKey());
				}
				else
				{
					previous.put(entry.getKey(), entry.getValue());
				}
			}
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
		previous.remove(userName);
		UserDataHelperImpl_DBO.getInstance().logout(userName);
	}
}
