package data.serviceimpl;

import java.util.Calendar;
import java.util.List;

import data.service.IDataInterface;
import po.SingleStockInfoPO;
/**
 * 数据层接口的实现
 * @author 刘宇翔
 *
 */
public class DataInterfaceImpl implements IDataInterface
{
	private UserDataController user;
	private StockDataController stock;
	public DataInterfaceImpl(){
		stock=StockDataController.getInstance();
		user=UserDataController.getInstance();
	}
	@Override
	public boolean signUpCheck(String username, String password) {
		return user.signUpCheck(username, password);
	}

	@Override
	public boolean signInCheck(String username, String password) {
		return user.signInCheck(username, password);
	}

	@Override
	public List<SingleStockInfoPO> getSingleStockInfo(String stockcode, Calendar begin, Calendar end) {
		// TODO Auto-generated method stub
		return stock.getSingleStockInfo(stockcode, begin, end);
	}

	@Override
	public List<SingleStockInfoPO> getMarketByDate(Calendar date) {
		// TODO Auto-generated method stub
		return stock.getMarketByDate(date);
	}
	@Override
	public void logout(String username) {
		user.logout(username);
	}

}
