package data.service;

import data.serviceimpl.DataInterfaceImpl;

public class DataThread implements Runnable{

	@Override
	public void run() {
		IDataInterface data=new DataInterfaceImpl();
	}

}
