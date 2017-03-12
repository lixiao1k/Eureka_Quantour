package data.datahelperservice;

/**
 * 用户模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public interface IUserDataHelper {
	public boolean containName(String username);
	public boolean login(String username, String password);
	/**
	 * 添加一位用户
	 * @param username String，用户名
	 * @param password String, 密码
	 * @return
	 */
	public void insertUser(String username,String password);
	public void logout(String username);
}
