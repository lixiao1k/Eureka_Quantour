package logic.service;

//import junit.framework.Test;
import junit.framework.TestCase;
import logic.service.ClientLogicInterface;
import logic.serviceimpl.ClientLogicImpl;

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
