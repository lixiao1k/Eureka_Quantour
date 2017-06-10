package data.service;

import exception.LogErrorException;
import exception.SqlNotConnectedException;
import exception.UserNameRepeatException;

public interface IUserDataInterface {
	/**
	 * 判断用户是否注册成功，如果成功，登记用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @throws UserNameRepeatException 如果用户名已存在则抛出该异常
	 */
	public void signUpCheck(String username,String password) throws UserNameRepeatException;
	/**
	 * 登出账号。
	 * @param username String,用户的登录名
	 * @return 一个boolean值，登录成功返回true，否则返回false
	 */
	public void logout(String username);
	/**
	 * 判断用户是否登录成功，如果成功，登录用户的账号；如果失败，返回错误信息。
	 * @param username String,用户的登录名
	 * @param password String,用户的登录密码
	 * @throws LogErrorException 如果用户名或密码错误则抛出该异常。
	 * @throws SqlNotConnectedException 
	 */
	public void signInCheck(String username,String password) throws LogErrorException, SqlNotConnectedException;
}
