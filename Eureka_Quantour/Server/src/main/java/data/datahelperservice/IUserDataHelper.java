package data.datahelperservice;

import java.util.HashMap;

/**
 * 用户模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public interface IUserDataHelper {
	/**
	 * 添加一位用户
	 * @param username String，用户名
	 * @param password String, 密码
	 * @return
	 */
	public HashMap<String,String> getAllUser();
	/**
	 * 获取所有用户的信息
	 * @return HashMap<String,String>,一个所有用户信息的表单
	 */
	public void insertUser(String username,String password);
	/**
	 * 初始化所有用户登录状态
	 * @return HashMap<String,Boolean>,所有用户状态的表单
	 */
	public HashMap<String,Boolean> getAllUserStatus();
}
