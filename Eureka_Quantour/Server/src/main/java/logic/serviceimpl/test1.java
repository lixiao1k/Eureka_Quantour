package logic.serviceimpl;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import data.service.IStockDataInterface;
import data.service.IStockSetInterface;
import data.serviceimpl.StockDataController_2;
import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import logic.supportimpl.PredictImpl;
import po.SingleStockInfoPO;
import vo.CompanyInfoVO;
import vo.EMAInfoVO;
import vo.ExponentChartVO;
import vo.SaleVO;
import vo.SingleStockInfoVO;
import vo.StrategyConditionVO;
import vo.YieldChartDataVO;

public class test1 {
	public static void main(String[] args)
	{
		new test1();
	}
//	private test1()
//	{
//		IStockDataInterface stock=StockDataController_2.getInstance();
//		PredictImpl s=new PredictImpl();
//		LocalDate i=LocalDate.of(2017, 2, 10);
//			long t1=System.currentTimeMillis();
//			try {
//				SingleStockInfoPO po1=stock.getSingleStockInfo("000001", i);
//				System.out.println(po1.toString());
//				
//			} catch (NullStockIDException | NullDateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				SingleStockInfoPO po2;
//				try {
//					po2 = stock.getSingleStockInfo("000001", stock.addDays(i, 1));
//					System.out.println(po2.toString());
//				} catch (NullStockIDException | NullDateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			} catch (DateOverException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println(s.KNNPredictPriceForStrategy("000001", i, 115,15, 15));
//			long t2=System.currentTimeMillis();
//			System.out.println(t2-t1);
//	}
	private test1()
	{
		StockLogicImpl s =new StockLogicImpl();
		
		try {
			List<Integer> list=new ArrayList<Integer>();
			list.add(15);
			list.add(15);
			StrategyConditionVO scv=new StrategyConditionVO("KNN",list,20);
			SaleVO sale=new SaleVO();
			sale.setNum(100);
			sale.setTiaocangqi(1);
			s.setStrategy(scv, sale, LocalDate.of(2016, 9, 2),  LocalDate.of(2017, 6, 13), "CYB", "Lyx123");
			long t1=System.currentTimeMillis();
//			YieldChartDataVO temp=s.getYieldChartData();
			s.getYieldDistributionHistogramData();
			long t2=System.currentTimeMillis();
			System.out.println(t2-t1);
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
