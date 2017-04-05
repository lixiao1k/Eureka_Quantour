package logic.service;

import java.util.ArrayList;
import java.util.List;

public class Stub {
	private List<String> list = new ArrayList<String>();
	public List<String> getStockSet(String username){
		list.add("股池1");
		list.add("股池2");
		return list;
	}
}
