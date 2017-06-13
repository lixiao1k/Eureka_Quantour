package logic.strategyfactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.PriceTypeException;
import logic.utility.MoneyBuffer;
import po.SingleStockInfoPO;

public class PingJunShouPanCeLue extends CeLue{

	public PingJunShouPanCeLue(int formate_day, HashMap<String, String> now_map, HashMap<String, String> next_map,
			LocalDate iter, MoneyBuffer mb, HashMap<String, String> code, List<String> code_list, String type) {
		super(formate_day, now_map, next_map, iter, mb, code, code_list, type);
	}
	@Override
	public void calculate() throws NullStockIDException{
		ArrayList<HashMap<String,String>> ema_list=new ArrayList<HashMap<String,String>>();
		try {
			ema_list=new ArrayList<HashMap<String,String>>();
			for(int i=1;i<formate_day;i++)
			{				
				ema_list.add(stock.getOneDay_Date(stock.addDays(iter, -i), code));
			}
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
				double total=po2.getAftClose();
				int cc=1;
				for(int i=0;i<formate_day-1;i++)
				{
					String t_info=ema_list.get(i).getOrDefault(name, "error");
					double num=0.0;
					if(t_info.equals("error"))
					{
					}
					else
					{
						SingleStockInfoPO t_po=new SingleStockInfoPO(t_info,name,name,iter);
						num=t_po.getAftClose();
						cc++;
					}
					total=total+num;
				}
				if(cc==1)
				{
					continue;
				}
				else
				{
					total=total/cc;
					if(po1.getHigh()<0.01||po2.getHigh()<0.01)
					{
						continue;
					}
					mb.add(1.0, total-po2.getAftClose(), getjiage(po2), getjiage(po1));
				}
			}
		}catch(PriceTypeException e)
		{
		}
	}
}
