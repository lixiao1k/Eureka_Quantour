package exception;

/**
 * Created by huihantao on 2017/3/28.
 */
public class NameInvalidException extends Exception {


    private static final long serialVersionUID = -270379040636547209L;

    public NameInvalidException(){
        super();
    }
    public String toString(){
        return "用户名已存在";
    }
}
