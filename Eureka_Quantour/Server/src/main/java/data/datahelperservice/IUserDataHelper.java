package data.datahelperservice;

import exception.LogErrorException;
import exception.UserNameRepeatException;

/**
 * 用户模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public interface IUserDataHelper {
	/**
	 * 判断是否存在该用户
	 * @param String username,用户名字
	 * @return boolean,含有该用户则返回true，没有则返回false。
	 */
	public void containName(String username) throws UserNameRepeatException;
	/**
	 * 判断密码是否正确
	 * @param String username,用户名称
	 * @param String password,用户密码
	 * @return boolean,密码正确则返回true，不正确则返回false。
	 */
	public void login(String username, String password) throws LogErrorException;
	/**
	 * 添加一位用户
	 * @param username String，用户名
	 * @param password String, 密码
	 * @return
	 */
	public void insertUser(String username,String password);
	/**
	 * 当用户登出后，将用户状态设置为未登录
	 * @param username 用户名
	 */
	public void logout(String username);
}
