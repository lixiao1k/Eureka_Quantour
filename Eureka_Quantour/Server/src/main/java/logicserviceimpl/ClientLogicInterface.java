package logicserviceimpl;

/**
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月6日
 */
public interface ClientLogicInterface {
	/**
	 * @Description: to sign up, invoke signUp() in data
	 * @return: boolean
	 */
	public boolean signUp ( String username, String password );
	/**
	 * @Description: to sign in, invoke signIn() indata
	 * @return: boolean
	 */
	public boolean signIn ( String username, String password );

}
