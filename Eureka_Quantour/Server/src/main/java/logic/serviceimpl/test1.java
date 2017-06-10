package logic.serviceimpl;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import exception.NullDateException;
import exception.NullStockIDException;
import vo.CompanyInfoVO;
import vo.EMAInfoVO;
import vo.ExponentChartVO;
import vo.SingleStockInfoVO;

public class test1 {
	public static void main(String[] args)
	{
		new test1();
	}
	private test1()
	{
		StockLogicImpl s =new StockLogicImpl();
		
		try {
			s.getLatestCommpanyInfo(LocalDate.of(2017, 4, 18), "000001");
			System.out.println("start");
			long t1=System.currentTimeMillis();
			List<SingleStockInfoVO> list1=s.getExponentInfoByTime("SHA", LocalDate.of(2007, 1, 1), LocalDate.of(2010, 1, 1));
			List<EMAInfoVO> list2=s.getExponentEMAInfo("SHA", LocalDate.of(2007, 1, 1), LocalDate.of(2010, 1, 1));
			long t2=System.currentTimeMillis();
			System.out.println(t2-t1);
			t1=System.currentTimeMillis();
			
			ExponentChartVO vo=s.getExponentChart("SHA", LocalDate.of(2007, 1, 1), LocalDate.of(2010, 1, 1));
			
			t2=System.currentTimeMillis();
			System.out.println(t2-t1);
			System.out.println(list2.get(3).getEMA().size());
			System.out.println(vo.getEma().get(3).getEMA().size());
//			for(int i=0;i<5;i++)
//			{
//				List<Double> l1=vo.getEma().get(i).getEMA();
//				List<Double> l2=list2.get(i).getEMA();
//				for(int j=0;j<l1.size();j++)
//				{
//					if(l1.get(j)-l2.get(j)>0.0001)
//					{
//						System.out.println(vo.getEma().get(i).getEMA().get(j));
//						System.out.println(list2.get(i).getEMA().get(j));
//						System.out.println("wrong");
//						System.exit(0);
//					}
//				}
//			}
//			for(int i=0;i<list1.size();i++)
//			{
//				if(!equals(list1.get(i),vo.getList().get(i)))
//				{
//					System.out.println("wrong2");
//					System.exit(0);
//				}
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean equals(SingleStockInfoVO vo1,SingleStockInfoVO vo2)
	{
		boolean flag=true;
		if(vo1.getClose()!=vo2.getClose()
				||vo1.getOpen()!=vo2.getOpen()
				||vo1.getHigh()!=vo2.getHigh()
				||vo1.getLow()!=vo2.getLow())
			flag=false;
		return flag;
	}
}
