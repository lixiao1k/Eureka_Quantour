package logic.strategyfactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.PriceTypeException;
import logic.supportimpl.PredictImpl;
import logic.supportservice.PredictInterface;
import logic.utility.MoneyBuffer;
import po.SingleStockInfoPO;

public class KNNCeLue extends CeLue{
	private int m;
	private PredictInterface predict;
	public KNNCeLue(int formate_day, HashMap<String, String> now_map, HashMap<String, String> next_map, LocalDate iter,
			MoneyBuffer mb, HashMap<String, String> code, List<String> code_list, String type,int m) {
		super(formate_day, now_map, next_map, iter, mb, code, code_list, type);
		this.m=m;
		predict=new PredictImpl();
	}
	@Override
	public void calculate() throws NullStockIDException {
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
				double value = predict.KNNPredictPriceForStrategy(name, iter, 115, m, formate_day);
				if(value>0)
				{
					mb.add(1, value-po2.getAftClose(), getjiage(po2), getjiage(po1));
				}
			}
		}catch(PriceTypeException e)
		{
		}
	}
}
