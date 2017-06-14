package logic.strategyfactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.PriceTypeException;
import logic.utility.MoneyBuffer;
import po.SingleStockInfoPO;
public class DongliangCeLue extends CeLue {


	public DongliangCeLue(int formate_day, HashMap<String, String> now_map, HashMap<String, String> next_map,
			LocalDate iter, MoneyBuffer mb, HashMap<String, String> code, List<String> code_list, String type) {
		super(formate_day, now_map, next_map, iter, mb, code, code_list, type);
	}

	@Override
	public void calculate() throws NullStockIDException {
		
		HashMap<String,String> before_map=null;
		try {
			before_map=stock.getOneDay_Date(stock.addDays(iter, -formate_day), code);
		} catch (NullDateException | DateOverException e) {
			flag=false;
			return;
		}
		this_time=0.0;
		next_time=0.0;
		try{
			for(String name:code_list)
			{
				String next_info=next_map.getOrDefault(name, "error");
				String now_info=now_map.getOrDefault(name, "error");
				if(now_info.equals("error")||next_info.equals("error"))
				{
					continue;
				}
				SingleStockInfoPO po1=new SingleStockInfoPO(next_info,name,name,iter);
				SingleStockInfoPO po2=new SingleStockInfoPO(now_info,name,name,iter);
				this_time=this_time+getjiage(po2);
				next_time=next_time+getjiage(po1);
				if(long_buy(po2, name))
				{
					continue;
				}
				String before_info=before_map.getOrDefault(name, "error");
				if(before_info.equals("error"))
				{
					continue;
				}
				SingleStockInfoPO po3=new SingleStockInfoPO(before_info,name,name,iter);
				if(po1.getHigh()<0.01||po2.getHigh()<0.01)
				{
					continue;
				}
				mb.add(po3.getAftClose(), po2.getAftClose(), getjiage(po2), getjiage(po1));
			}
		}catch(PriceTypeException e)
		{
		}
	}
}
