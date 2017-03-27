package data.datahelperimpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import data.datahelperservice.IUserDataHelper;
import resultmessage.LogErrorException;
import resultmessage.UserNameRepeatException;
/**
 * 用户模块数据的数据处理实现
 * @author 刘宇翔
 *
 */
public class UserDataHelperImpl implements IUserDataHelper {
	private File userPath;
	private File userLog;
	private File userStatus;
	private static IUserDataHelper datahelper;
	private Properties prop_userinfo;
	private Properties prop_userstatus;
	private BufferedInputStream userinfo_in;
	private BufferedInputStream userstatus_in;
	private String path;
	private UserDataHelperImpl(){
		path="config/";
		try {
			path=URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file=new File(path+"");
		if(!file.exists()&&!file.isDirectory()){
			file.mkdir();
		}
		try {
			userPath=new File(path+"user");
			userLog=new File(path+"user/userLog.properties");
			userStatus=new File(path+"user/userStatus.properties");
			prop_userinfo=new Properties();
			prop_userstatus=new Properties();
			if(!userPath.exists()&&!userPath.isDirectory())
			{
				userPath.mkdirs();
			}
			if(!userLog.exists()){
				userLog.createNewFile();				
			}
			if(!userStatus.exists()){
				userStatus.createNewFile();
			}
//			init_status();
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
		try {
			FileOutputStream out_info = new FileOutputStream(path+"user/userLog.properties");
			FileOutputStream out_status = new FileOutputStream(path+"user/userStatus.properties");
			prop_userinfo.setProperty(username, password);
			prop_userstatus.setProperty(username, "0");
			prop_userinfo.store(out_info, "update new user:" + username);
			prop_userstatus.store(out_status, "update new user:" + username);
			File userDir=new File(userPath+"/info/"+username);
			if(!userDir.exists()&&!userDir.isDirectory())
			{
				userDir.mkdirs();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	/**
//	 * 获取所有用户的信息
//	 * @return HashMap<String,String>,一个所有用户信息的表单
//	 */
//	public HashMap<String,String> getAllUser(){
//		HashMap<String,String> usermap=new HashMap<String,String>();
//		try {
//			FileReader fr=new FileReader(userLog);
//			BufferedReader br=new BufferedReader(fr);
//			String output="";
//			while(br.ready()){
//				output=br.readLine();
//				String[] i=output.split(",");
//				usermap.put(i[0], i[1]);
//			}
//			br.close();
//			fr.close();
//			return usermap;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("配置文件不存在");
//			return null;
//		}
//	}
	/**
	 * 判断是否存在该用户
	 * @param String username,用户名字
	 * @throws UserNameRepeatException 如果有重复的名字则抛出该异常
	 */
	@Override
	public void containName(String username) throws UserNameRepeatException {
		try {
			userstatus_in = new BufferedInputStream(
					new FileInputStream(path+"user/userStatus.properties"));
			prop_userstatus.load(userstatus_in);
			userstatus_in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(prop_userstatus.containsKey(username)){	
			System.out.println(new UserNameRepeatException().toString());
			throw new UserNameRepeatException();
		}
		else{
			return ;
		}
	} 
	/**
	 * 判断密码是否正确
	 * @param String username,用户名称
	 * @param String password,用户密码
	 * @throws LogErrorException 如果账号或密码错误则抛出该异常
	 */
	@Override
	public void login(String username,String password) throws LogErrorException{
		try {
			userinfo_in = new BufferedInputStream(
					new FileInputStream(path+"user/userLog.properties"));
			prop_userinfo.load(userinfo_in);
			userinfo_in.close();
			userstatus_in = new BufferedInputStream(
					new FileInputStream(path+"user/userStatus.properties"));
			prop_userstatus.load(userstatus_in);
			userstatus_in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pw=prop_userinfo.getProperty(username,null);
		if(pw==null){
			System.out.println(new LogErrorException().toString());
			throw new LogErrorException();
		}
		if(password.equals(pw)){
//			if(judge_status(username)){
//				change_status(username);
				return;
//			}
//			else{
//				return false;
//			}
		}
		else{
			System.out.println(new LogErrorException().toString());
			throw new LogErrorException();
		}
	} 
	
	/**
	 * 当用户登录后，将用户状态设置为已登录
	 * @param username
	 */
//	private void change_status(String username){
//		try {
//			FileOutputStream out_status = new FileOutputStream(path+"user/userStatus.properties");
//			prop_userstatus.setProperty(username, "1");
//			prop_userstatus.store(out_status, "login user:" + username);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	/**
	 * 判断登录状态
	 * @param String username,用户名称
	 * @return boolean,密码正确则返回true，不正确则返回false。
	 */
//	private boolean judge_status(String username) {
//		String pw=prop_userstatus.getProperty(username, null);
//		if(pw.equals("0")){
//			return true;
//		}
//		else{
//			return false;
//		}
//	}
	
	/**
	 * 当用户登出后，将用户状态设置为未登录
	 * @param username 用户名
	 */
	public void logout(String username){
		try {
			FileOutputStream out_status = new FileOutputStream(path+"user/userStatus.properties");
			try {
				containName(username);
				System.out.println("用户名不存在");
			} catch (UserNameRepeatException e) {
				prop_userstatus.setProperty(username, "0");
				prop_userstatus.store(out_status, "username logout:"+username);
			}	
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 当启动服务器时，将所有用户状态更新为未登录
	 */
//	private void init_status() {
//		try {
//			userstatus_in = new BufferedInputStream(
//					new FileInputStream(path+"user/userStatus.properties"));
//			prop_userstatus.load(userstatus_in);
//			userstatus_in.close();
//			FileOutputStream out_status = new FileOutputStream(path+"user/userStatus.properties");
//			Iterator it=prop_userstatus.entrySet().iterator();
//			while(it.hasNext()){
//			    Map.Entry entry=(Map.Entry)it.next();
//			    prop_userstatus.setProperty((String) entry.getKey(), "0");
//			}
//			prop_userstatus.store(out_status, "init");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
