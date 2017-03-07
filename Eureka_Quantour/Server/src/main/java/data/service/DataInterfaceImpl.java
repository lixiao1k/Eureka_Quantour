package data.service;

import java.util.Calendar;
import java.util.List;

import po.SingleStockInfoPO;

public class DataInterfaceImpl implements IDataInterface
{

	@Override
	public boolean signUpCheck(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean signInCheck(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode, Calendar begin, Calendar end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SingleStockInfoPO> getMarketByDate(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

}
