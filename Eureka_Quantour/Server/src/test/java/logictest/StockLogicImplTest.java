package logictest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;
import logicserver.StockLogicImpl;
import logicserver.StockLogicInterface;

public class StockLogicImplTest extends TestCase {
	private StockLogicInterface SLI;

	protected void setUp() throws Exception {
		super.setUp();
		SLI = new StockLogicImpl();
	}
	
	public void testGetEMAInfo(){
		List<Double> list1 = SLI.getEMAInfo("1", Calendar.getInstance(), Calendar.getInstance(), 5); 
		List<Double> list2 = new ArrayList<Double>();
		this.assertTrue(ifListEqual(list1, list2));
	}
	
	private boolean ifListEqual(List<Double> list1, List<Double> list2){
		if(list1.size()!=list2.size())
			return false;
		else{
			for(int i=0;i<list1.size();i++){
				if(list1.get(i)!=list2.get(i))
					return false;
			}
		}
		return true;
	}
}
