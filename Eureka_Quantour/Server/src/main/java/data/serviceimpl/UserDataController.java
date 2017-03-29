package data.serviceimpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import data.datahelperimpl.UserDataHelperImpl;
import data.datahelperservice.IUserDataHelper;
import exception.LogErrorException;
import exception.UserNameRepeatException;

/**
 * 用户模块方法的实现
 * @author 刘宇翔
 *
 */
public class UserDataController 
{
	private IUserDataHelper userdatahelper;
	private static UserDataController userdata;
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
	 * @throws UserNameRepeatException 如果用户名重复则抛出异常
	 */
	public void signUpCheck(String username, String password) throws UserNameRepeatException{
		password=EncoderByMd5(password);
		userdatahelper.containName(username);
		System.out.println("---------------------注册成功-----------------------\n  username:" +username+"    password:"+password);
		userdatahelper.insertUser(username, password);
	}
	/**
	 * 判断用户是否登录成功，如果成功，登录用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @throws LogErrorException 如果用户名或密码错误则抛出异常
	 */
	public void signInCheck(String username,String password) throws LogErrorException {
		password=EncoderByMd5(password);
		userdatahelper.login(username, password);
		System.out.println("---------------------登录成功-----------------------\n  username:" +username+"    password:"+password);
	}
	/**
	 * 登出账号。
	 * @param username String,用户的登录名
	 * @return 一个boolean值，登录成功返回true，否则返回false
	 */
	public void logout(String username){
		userdatahelper.logout(username);
	}
	
	
	/**
	 * 将输入的字符串加密返回
	 * @param str 待加密的字符串
	 * @return String 完成加密的字符串
	 */
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
