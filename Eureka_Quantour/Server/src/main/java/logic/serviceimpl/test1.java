package logic.serviceimpl;

import java.rmi.RemoteException;
import java.time.LocalDate;

import exception.NullDateException;
import exception.NullStockIDException;
import vo.CompanyInfoVO;

public class test1 {
	public static void main(String[] args)
	{
		new test1();
	}
	private test1()
	{
		StockLogicImpl s =new StockLogicImpl();
		try {
			CompanyInfoVO vo=s.getLatestCommpanyInfo(LocalDate.of(2016, 5, 30), "000001");
			System.out.println(vo.toString());
		} catch (RemoteException | NullStockIDException | NullDateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
