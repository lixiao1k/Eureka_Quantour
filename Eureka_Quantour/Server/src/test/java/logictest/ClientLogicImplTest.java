package logictest;

//import junit.framework.Test;
import junit.framework.TestCase;
import logicservice.ClientLogicInterface;
import logicserviceimpl.ClientLogicImpl;

public class ClientLogicImplTest extends TestCase {
	private ClientLogicInterface CLI;
	
	protected void setUp() throws Exception {
		super.setUp();
		CLI = new ClientLogicImpl();
	}
    
    public void testsignUp(){
//        assertTrue();
    }
}
