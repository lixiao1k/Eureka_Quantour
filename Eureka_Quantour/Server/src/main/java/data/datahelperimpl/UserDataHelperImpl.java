package data.datahelperimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import data.datahelperservice.IUserDataHelper;
/**
 * 用户模块数据的数据处理实现
 * @author 刘宇翔
 *
 */
public class UserDataHelperImpl implements IUserDataHelper {
	private File userPath;
	private File userLog;
	private File userName;
	private static IUserDataHelper datahelper;
	private UserDataHelperImpl(){
		try {
			userPath=new File("data/user");
			userLog=new File("data/user/userLog.csv");
			userName=new File("data/user/userName.csv");
			if(!userPath.exists()&&!userPath.isDirectory())
			{
				userPath.mkdirs();
			}
			if(!userLog.exists()){
				userLog.createNewFile();				
			}
			if(!userName.exists()){
				userName.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static IUserDataHelper getInstance(){
		if(datahelper==null) datahelper=new UserDataHelperImpl();
		return datahelper;
	}
	/**
	 * 添加一位用户
	 * @param username String，用户名
	 * @param password String, 密码
	 * @return
	 */
	public void insertUser(String username,String password){
			String input_userLog=username+","+password+"\n";
			String input_userName=username+"\n";
			insert(input_userLog,userLog);
			insert(input_userName,userName);
	}
	private void insert(String input,File file){
		try {
			FileWriter fw=new FileWriter(file,true);
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(input);
			fw.close();
			bw.close();
		} catch (Exception e) {
			System.out.println("添加失败");
		}
	}
	/**
	 * 获取所有用户的信息
	 * @return HashMap<String,String>,一个所有用户信息的表单
	 */
	public HashMap<String,String> getAllUser(){
		HashMap<String,String> usermap=new HashMap<String,String>();
		try {
			FileReader fr=new FileReader(userLog);
			BufferedReader br=new BufferedReader(fr);
			String output="";
			while(br.ready()){
				output=br.readLine();
				String[] i=output.split(",");
				usermap.put(i[0], i[1]);
			}
			br.close();
			fr.close();
			return usermap;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("配置文件不存在");
			return null;
		}
	}
	/**
	 * 初始化所有用户登录状态
	 * @return HashMap<String,Boolean>,所有用户状态的表单
	 */
	public HashMap<String,Boolean> getAllUserStatus() {
		try {
			HashMap<String,Boolean> result=new HashMap<String,Boolean>();
			FileReader fr = new FileReader(userName);
			BufferedReader br = new BufferedReader(fr);
			while(br.ready()) {
				result.put(br.readLine(), false);
			}
			br.close();
			fr.close();
			return result;
		} catch (Exception e) {
			System.out.println("有异常发生");
			return null;
		}
    } 
}
