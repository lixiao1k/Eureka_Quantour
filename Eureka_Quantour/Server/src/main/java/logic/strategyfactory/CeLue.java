package logic.strategyfactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import data.service.IStockDataInterface;
import data.serviceimpl.StockDataController_2;
import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.PriceTypeException;
import logic.utility.MoneyBuffer;
import po.SingleStockInfoPO;

public class CeLue implements StrategyService{
	protected HashMap<String,String> now_map;
	protected HashMap<String,String> next_map;
	protected LocalDate iter;
	protected MoneyBuffer mb;
	protected double rate=0.0;
	protected int formate_day;
	protected HashMap<String,String> code;
	protected boolean flag;
	protected List<String> code_list;
	protected String type;
	protected double this_time;
	protected double next_time;
	protected IStockDataInterface stock = StockDataController_2.getInstance();
	public CeLue(int formate_day,HashMap<String,String> now_map,HashMap<String,String> next_map,LocalDate iter,MoneyBuffer mb,HashMap<String,String> code,List<String> code_list,String type)
	{
		super();
		this.now_map=now_map;
		this.next_map=next_map;
		this.iter=iter;
		this.mb=mb;
		this.formate_day=formate_day;
		this.code=code;
		flag=true;
		this.code_list=code_list;
		this.type=type;
		this.this_time=0.0;
		this.next_time=0.0;
	}
	@Override
	public void calculate() throws NullStockIDException{
	}
	protected double getjiage(SingleStockInfoPO po) throws PriceTypeException {
        if (type.equals("收盘价")) return po.getAftClose();
        if (type.equals("开盘价")) return po.getOpen();
        throw new PriceTypeException();
    }
	protected boolean canBuy(SingleStockInfoPO po2)
	{
		boolean flag=true;
		if(po2.getRate()>9.9)
		{
			flag=false;
		}
		return flag;
	}
	protected boolean long_buy(SingleStockInfoPO po2,String name)
	{
		if(po2.getRate()+9.5<0)
		{
			return true;
		}
		if(!canBuy(po2))
		{
			boolean flag=true;
			for(int i=1;i<2;i++)
			{
				try {
					SingleStockInfoPO temp;
					try {
						temp = stock.getSingleStockInfo(name, stock.addDays(iter, -i));
					} catch (NullStockIDException e) {
						break;
					}
					if(canBuy(temp))
					{
						flag=false;
						break;
					}
				} catch (NullDateException | DateOverException e1) {
					break;
				}
			}
			if(flag)
			{
				return true;
			}
		}
		return false;
	}
	public double getZheCi()
	{
		return this_time;
	}
	public double getXiaCi()
	{
		return next_time;
	}
	public boolean isContinue()
	{
		return flag;
	}
}
