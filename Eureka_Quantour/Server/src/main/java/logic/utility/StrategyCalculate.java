package logic.utility;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.service.IStockDataInterface;
import data.serviceimpl.StockDataController_2;
import exception.DateOverException;
import exception.NullDateException;
import exception.NullStockIDException;
import exception.PriceTypeException;
import logic.strategyfactory.StrategyFactory;
import po.SingleStockInfoPO;
import vo.SaleVO;
import vo.StrategyConditionVO;

public class StrategyCalculate {
	private Utility utility=Utility.getInstance();
	private IStockDataInterface stock = StockDataController_2.getInstance();
	private LocalDate begin;
	private LocalDate end;
	private List<Double> basic_value;
	private List<Double> strategy_value;
	private List<Double> basic_rate=new ArrayList<Double>();
	private List<Double> strategy_rate=new ArrayList<Double>();
	private List<String> code_list;
	private List<LocalDate> time_list=new ArrayList<LocalDate>();
	private SaleVO salevo;
	private StrategyConditionVO strategyConditionVO;
	private double basic_yearreturn=0.0;
	private double strategy_yearreturn=0.0;
	private double beta=0.0;
	private double alpha=0.0;
	private double sharp=0.0;
	
	private int date_length=0;
	
	private int last_day;
	private int stock_number;
	
	public StrategyCalculate(List<String> stockcode, LocalDate begin, LocalDate end, SaleVO salevo, StrategyConditionVO strategyConditionVO) {
		super();
		this.begin = begin;
		this.end = end;
		this.strategyConditionVO = strategyConditionVO;
		this.last_day = salevo.getTiaocangqi();
		this.stock_number = strategyConditionVO.getNums();
		this.code_list=stockcode;
		this.salevo=salevo;
	}
    public Double getAlpha(){
    	if(alpha==0.0)
    	{
    		double rp=gerYearReturn();
            double rm=gerBasicYearReturn();
            double rf=0.04;
            alpha = rp-(rf+(rm-rf)*getBeta());
    	}
        return alpha;
    }
    //get beta

    public Double getBeta(){
    	if(beta==0.0)
    	{
    		beta=utility.getCorvariance(basic_rate,strategy_rate)/utility.getCorvariance(basic_rate,basic_rate);
    	}
        return beta;
    }

    // get strategy return when it uses for a year
    public Double gerYearReturn(){
    	if(strategy_yearreturn==0.0)
    	{
    		strategy_yearreturn=getYear(strategy_value);
    	}
        return strategy_yearreturn;
    }

    // get the return without a strategy

    public Double gerBasicYearReturn(){
    	if(basic_yearreturn==0.0)
    	{
    		basic_yearreturn=getYear(basic_value);
    	}
        return basic_yearreturn;
    }

    //abstract method for calculation
    private Double getYear(List<Double> list){
        double shuzi=list.get(list.size()-1);
        int i=0;
        if(date_length==0)
        {
        	date_length=(int) begin.until(end, ChronoUnit.DAYS);
        }
        i=date_length;
        double a=1+shuzi;
        double b=365.0/i;
        return Math.pow(a,b)-1;
    }
    //get sharp
    public Double getSharpe(){
    	if(sharp==0.0)
    	{
    		 double ri=gerYearReturn();
    	     double rf=0.04;
    	     double seta=utility.getCorvariance(strategy_value,strategy_value);
    	     sharp = (ri-rf)/seta;
    	}
       return sharp;
    }

    //get zuidahuiche

    public Double getzuidaguiceh(){
        double max=0;
        for (int i=1;i<strategy_value.size();i++){
            max=Math.max(max,(strategy_value.get(i-1)-strategy_value.get(i)/strategy_value.get(i-1)));
        }
        return max;
    }

    // get the time list of strategy

    public List<LocalDate> getTimelist() {
        return time_list;
    }
    public List<Double> getBasicReturn () throws PriceTypeException, NullStockIDException
    {
    	if(basic_value==null)
    	{
    		calculate_strategy1();
    	}
    	return basic_value;
    }
    public List<Double> getStragetyReturn() throws PriceTypeException, NullStockIDException
    {
    	if(strategy_value==null)
    	{
    		calculate_strategy1();
    	}
    	return strategy_value;
    }
	public void calculate_strategy1() throws PriceTypeException, NullStockIDException
	{
		MoneyBuffer mb=null;
		HashMap<String,String> code=new HashMap<String,String>();
		List<Integer> para = strategyConditionVO.getExtra();
		mb=new MoneyBuffer(stock_number,1);
		for(String name:code_list)
		{
			code.put(name, null);
		}
		StrategyFactory sf=StrategyFactory.getInstance();
		sf.setStrategy(strategyConditionVO.getName(), para, code, code_list, salevo.getTiaocangjiage());
		
		List<Double> basic_list=new ArrayList<>();
		List<Double> strategy_list=new ArrayList<>();
	    LocalDate iter=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());
	    double init=10000;
	    double strategy_init=10000;
	    HashMap<String,String> next_map=null;
	    HashMap<String, String> now_map=null;
		try {
			for(;!iter.isAfter(end);iter=stock.addDays(iter, last_day))
			{
				mb.clear();
				double this_time=0;
				double next_time=0;
				try {
					next_map=stock.getOneDay_Date(stock.addDays(iter, last_day), code);
					if(now_map==null)
					{
						now_map=stock.getOneDay_Date(iter, code);
					}
				} catch (NullDateException e) {
					continue;
				}
				sf.calculate(now_map, next_map, iter, mb);
				if(!sf.isContinue())
				{
					continue;
				}
				now_map=next_map;
				this_time=sf.getzheci();
				next_time=sf.getxiaci();
				if(this_time==0.0)
				{		
					continue;
				}
				time_list.add(iter);
				basic_rate.add(next_time/this_time);
				double s_rate=mb.getRate();
//				System.out.println("--------------------------------------");
//				System.out.println(s_rate);
				strategy_rate.add(s_rate);
				init=init*next_time/this_time;
				strategy_init=strategy_init*s_rate;
				double rate=(init-10000)/10000;
//				if(s_rate-0.0<0.0001)
//				{
//					System.out.println("error");
//				}
				if(strategy_init<100)
				{
					//System.out.println("error");
					strategy_init=100;
				}
				double strategy_celue=(strategy_init-10000)/10000;
//				System.out.println(rate);
//				System.out.println(strategy_celue);
//				System.out.println("--------------------------------------");
				basic_list.add(rate);
				strategy_list.add(strategy_celue);
			}
		} catch (DateOverException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		setBasic_value(basic_list);
		setStrategy_value(strategy_list);
	}
	public LocalDate getBegin() {
		return begin;
	}
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}
	public LocalDate getEnd() {
		return end;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	public List<Double> getBasic_value() {
		return basic_value;
	}
	public void setBasic_value(List<Double> basic_value) {
		this.basic_value = basic_value;
	}
	public List<Double> getStrategy_value() {
		return strategy_value;
	}
	public void setStrategy_value(List<Double> strategy_value) {
		this.strategy_value = strategy_value;
	}
	public List<Double> getBasic_rate() {
		return basic_rate;
	}
	public void setBasic_rate(List<Double> basic_rate) {
		this.basic_rate = basic_rate;
	}
	public List<Double> getStrategy_rate() {
		return strategy_rate;
	}
	public void setStrategy_rate(List<Double> strategy_rate) {
		this.strategy_rate = strategy_rate;
	}
	public List<String> getCode_list() {
		return code_list;
	}
	public void setCode_list(List<String> code_list) {
		this.code_list = code_list;
	}
	public List<LocalDate> getTime_list() {
		return time_list;
	}
	public void setTime_list(List<LocalDate> time_list) {
		this.time_list = time_list;
	}
	public SaleVO getSalevo() {
		return salevo;
	}
	public void setSalevo(SaleVO salevo) {
		this.salevo = salevo;
	}
	public StrategyConditionVO getStrategyConditionVO() {
		return strategyConditionVO;
	}
	public void setStrategyConditionVO(StrategyConditionVO strategyConditionVO) {
		this.strategyConditionVO = strategyConditionVO;
	}
	public int getLast_day() {
		return last_day;
	}
	public void setLast_day(int last_day) {
		this.last_day = last_day;
	}
	public int getStock_number() {
		return stock_number;
	}
	public void setStock_number(int stock_number) {
		this.stock_number = stock_number;
	}
}
