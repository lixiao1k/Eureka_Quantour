package exception;

/**
 * Created by huihantao on 2017/3/28.
 */
public class LoginFailException extends Exception {

    private static final long serialVersionUID = -7297489649100512522L;
    public LoginFailException(){
        super();
    }
    public String toString(){
        return "用户名或密码错误";
    }
}
