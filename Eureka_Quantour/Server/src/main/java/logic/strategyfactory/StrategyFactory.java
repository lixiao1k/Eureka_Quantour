package logic.strategyfactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import exception.DateOverException;
import exception.NullStockIDException;
import logic.utility.MoneyBuffer;

public class StrategyFactory {
	private static StrategyFactory sf=new StrategyFactory();
	private StrategyService celue;
	private List<Integer> parameter;
	private String name;
	private HashMap<String,String> code;
	private List<String> code_list;
	private String type;
	private StrategyFactory()
	{
	}
	public static StrategyFactory getInstance()
	{
		return sf;
	}
	public void setStrategy(String name,List<Integer> formate_day,HashMap<String,String> code,List<String> code_list,String type)
	{
		this.name=name;
		this.parameter=formate_day;
		this.code=code;
		this.code_list=code_list;
		this.type=type;
	}
	public void calculate(HashMap<String,String> now_map,HashMap<String,String> next_map,LocalDate iter,MoneyBuffer mb) throws NullStockIDException
	{
		switch(name)
		{
		case "动量策略":
			celue=new DongliangCeLue(parameter.get(0),now_map,next_map,iter,mb,code,code_list,type);
			break;
		case "均值策略":
			celue=new JunZhiCeLue(parameter.get(0),now_map,next_map,iter,mb,code,code_list,type);
			break;
		case "平均收盘价":
			celue=new PingJunShouPanCeLue(parameter.get(0),now_map,next_map,iter,mb,code,code_list,type);
			break;
		case "KNN":
			celue=new KNNCeLue(parameter.get(0),now_map,next_map,iter,mb,code,code_list,type,parameter.get(1));
			break;
		}
		celue.calculate();
	}
	public double getzheci()
	{
		return celue.getZheCi();
	}
	public double getxiaci()
	{
		return celue.getXiaCi();
	}
	public boolean isContinue()
	{
		return celue.isContinue();
	}
}
