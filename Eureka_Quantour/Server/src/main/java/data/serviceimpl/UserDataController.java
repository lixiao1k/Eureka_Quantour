package data.serviceimpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import data.datahelperimpl.UserDataHelperImpl;
import data.datahelperservice.IUserDataHelper;

/**
 * 用户模块方法的实现
 * @author 刘宇翔
 *
 */
public class UserDataController 
{
	private IUserDataHelper userdatahelper;
	public static UserDataController userdata;
	private UserDataController(){
		userdatahelper=UserDataHelperImpl.getInstance();
	}
	public static UserDataController getInstance(){
		if(userdata==null) userdata=new UserDataController();
		return userdata;
	}
	/**
	 * 判断用户是否注册成功，如果成功，登记用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @return 一个boolean值，注册成功返回true，否则返回false
	 */
	public boolean signUpCheck(String username, String password) {
		password=EncoderByMd5(password);
		if(userdatahelper.containName(username)){
			System.out.println("---------------------注册失败-----------------------");
			return false;
		}
		else{
			System.out.println("---------------------注册成功-----------------------\n  username:" +username+"    password:"+password);
			userdatahelper.insertUser(username, password);
			return true;
		}
	}
	/**
	 * 判断用户是否登录成功，如果成功，登录用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @return 一个boolean值，登录成功返回true，否则返回false
	 */
	public boolean signInCheck(String username,String password){
		password=EncoderByMd5(password);
		if(userdatahelper.login(username, password)){
			System.out.println("---------------------登录成功-----------------------\n  username:" +username+"    password:"+password);
			return true;
		}
		else{
			System.out.println("---------------------登录失败-----------------------");
			return false;
		}
	}
	/**
	 * 登出账号。
	 * @param username String,用户的登录名
	 * @return 一个boolean值，登录成功返回true，否则返回false
	 */
	public void logout(String username){
		userdatahelper.logout(username);
	}
	public String EncoderByMd5(String str){
		 MessageDigest md;
	     StringBuffer sb = new StringBuffer();
	     try {
	          md = MessageDigest.getInstance("MD5");
	          md.update(str.getBytes());
	          byte[] data = md.digest();
	          int index;
	          for(byte b : data) {
	               index = b;
	               if(index < 0) index += 256;
	               if(index < 16) sb.append("0");
	               sb.append(Integer.toHexString(index));
	          }
	     } catch (NoSuchAlgorithmException e) {
	      e.printStackTrace();
	     }
	     return sb.toString();
	}	
}
