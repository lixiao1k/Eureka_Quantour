package data.common;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Scanner;

public class test {
	public static void main(String[] args){
		new test();
	}
	public test(){
		BPlusTree t=new BPlusTree(6);
		Scanner sc=new Scanner(System.in);
		String str=sc.nextLine();
		while(!str.equals("esc")){
			if(str.equals("1")){
				int i=sc.nextInt();
				t.get(i);
			}
			else if(str.equals("2")){
				int i=sc.nextInt();
				String code=sc.nextLine();
				int k=sc.nextInt();
				t.insertOrUpdate(i,new AbstractMap.SimpleEntry<String,Integer>(code,k));
			}
			else if(str.equals("3")){
				t.toString();
			}
		}
	}
}
