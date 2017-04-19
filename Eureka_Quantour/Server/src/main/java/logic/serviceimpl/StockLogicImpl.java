package logic.serviceimpl;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.*;
import logic.service.StockLogicInterface;
import logic.utility.Return;
import logic.utility.Utility;
import po.SingleStockInfoPO;
import vo.*;

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月8日
 */
public class StockLogicImpl implements StockLogicInterface{
  
	private IDataInterface idi = new DataInterfaceImpl();
	private Utility utility=Utility.getInstance();
	private Return stragety;

	@Override
	public List<SingleStockInfoVO> getSingleStockInfoByTime(String stockCode, LocalDate begin, LocalDate end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {

		// 判断日期是否有效
		utility.ifDateValid(begin, end,stockCode);
		LocalDate x=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());

		List<SingleStockInfoVO> lssi = new ArrayList<>();
		for (;x.compareTo(end)<=0;x=x.plusDays(1)){
			SingleStockInfoPO po= null;

			try {
				po = idi.getSingleStockInfo(stockCode,x);
			}catch (NullDateException e) {
				continue;
			}
			SingleStockInfoVO vo=new SingleStockInfoVO(po);
			lssi.add(vo);
		}
		return lssi;
	}

	@Override
	public List<EMAInfoVO> getEMAInfo( String stockCode, LocalDate begin, LocalDate end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {

		// methods用于存储日均线计算的方式
		int methods[] = { 5, 10, 20, 30, 60 };
			
		List<EMAInfoVO> llemai = new ArrayList<>();

		for (int i=0;i<methods.length;i++){
			EMAInfoVO vo=getEMAInfo(stockCode, begin, end,methods[i]);
			llemai.add(vo);
		}
		return  llemai;

	}

	public EMAInfoVO getEMAInfo( String stockCode, LocalDate begin, LocalDate end ,int days)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {

		utility.ifDateValid(begin.plusDays(-days),end,stockCode);

		LocalDate start=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());


		ArrayList<LocalDate> timelist=new ArrayList<>();
		ArrayList<Double> shujulist=new ArrayList<>();

		try {
			for (;start.compareTo(end)<=0;start=idi.addDays(start,1)){
                shujulist.add(utility.getEMA(stockCode,start,days));
                timelist.add(LocalDate.of(start.getYear(),start.getMonth(),start.getDayOfMonth()));

            }
		} catch (DateOverException e) {

		}
		EMAInfoVO vo = new EMAInfoVO(timelist,shujulist,days);
		return vo;
	}



	@Override
	public ComparedInfoVO getComparedInfo(String stockCodeA, LocalDate begin, LocalDate end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		try {
			utility.ifDateValid(begin, end,stockCodeA);
		} catch (NullStockIDException e) {
			e.printStackTrace();
		}

		String name1=idi.codeToname(stockCodeA);
		LocalDate itr=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());
		double low1=10000000;
		double high1=0;
		double diyitian=0;
		double zuihouyitian=0;
		List<Double> shoupanlist=new ArrayList<>();
		List<Double> duishushouyiilv=new ArrayList<>();
		List<LocalDate> timelist=new ArrayList<>();
		try {
			for (;itr.compareTo(end)<=0;
                    itr=idi.addDays(itr,1)){
				try {
					SingleStockInfoPO po1=idi.getSingleStockInfo(stockCodeA,itr);
					if (diyitian==0)
						diyitian=po1.getAftClose();
					zuihouyitian=po1.getAftClose();
					low1=Math.min(low1,po1.getLow());
					high1=Math.max(high1,po1.getHigh());


					shoupanlist.add(po1.getClose());
					duishushouyiilv.add(Math.log(po1.getAftClose()));
					timelist.add(itr);

				} catch (NullStockIDException e) {
					e.printStackTrace();
				} catch (NullDateException e) {
					continue;
				}

			}
		} catch (DateOverException e) {
			e.printStackTrace();
		}
		if (diyitian==0)
			throw  new DateInvalidException();
		double fangcha=utility.getCorvariance(duishushouyiilv,duishushouyiilv);

		return  new ComparedInfoVO(name1,stockCodeA,low1,high1,zuihouyitian/diyitian,shoupanlist,duishushouyiilv,fangcha,timelist);

	}

	@Override
	public MarketInfoVO getMarketInfo(LocalDate date,String marketname) throws RemoteException{
		List<String> namelist=idi.getStockSetInfo(marketname,null);
		long volume=0;
		int wushuju=0;
		int chaoguo10=0;
		int dieguo10=0;
		int zheng5dao10=0;
		int zheng0dao5=0;
		int fu5dao10=0;
		int fu0dao5=0;
		int fuduwei=0;

		for (String name:namelist){
			SingleStockInfoPO po=null;
			try {
				po=idi.getSingleStockInfo(name,date);
			} catch (NullStockIDException e) {
				e.printStackTrace();
			} catch (NullDateException e) {
				wushuju++;
				continue;
			}
			volume+=po.getVolume();
			if (po.getAftrate()>=10){
				chaoguo10++;
			}
			if (po.getAftrate()<=-10){
				dieguo10++;
			}
			if (po.getAftrate()<10 && po.getAftrate()>=5){
				zheng5dao10++;
			}
			if (po.getAftrate()>-10 && po.getAftrate()<=-5){
				fu5dao10++;
			}

			if (po.getAftrate()<5 && po.getAftrate()>0){
				zheng5dao10++;
			}
			if (po.getAftrate()>-10 && po.getAftrate()<-5){
				fu5dao10++;
			}







		}


		return null;
	}

	@Override
	public List<String> getStockSet(String username) {
		return idi.getStockSet(username);
	}

	@Override
	public List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now,String username) throws NullMarketException {
		List<String> namelist=idi.getStockSetInfo(stockSetName,username);
		if (namelist==null )
			return null;
		List<SingleStockInfoVO> res=new ArrayList<>();
		int p=0;
		for (String s:namelist){
			try {
				res.add(new SingleStockInfoVO(idi.getSingleStockInfo(s,now)));
			} catch (NullStockIDException e) {
				continue;
			} catch (NullDateException e) {
				String name =idi.codeToname(s);
				res.add(new SingleStockInfoVO(s,name));
				p++;
				continue;
			}
		}

		Collections.sort(res);
		Collections.reverse(res);
		if (p==namelist.size())
			throw  new NullMarketException();
		return res;
	}

	@Override
	public SingleStockInfoVO getStockBasicInfo(String code, LocalDate now) throws NullStockIDException, NullDateException {
		return new SingleStockInfoVO(idi.getSingleStockInfo(code,now));
	}

	@Override
	public void setStrategy(StrategyConditionVO strategyConditionVO, SaleVO s, LocalDate begin, LocalDate now, String stockSetName,String username) {
		//TODO

		if (stockSetName.equals("SHA") ||stockSetName.equals("SZA") ||stockSetName.equals("SHB")|| stockSetName.equals("SZB")||stockSetName.equals("CYB")||stockSetName.equals("HS300")
				||stockSetName.equals("ZXB") )
			username=null;
		List<String> stocklistname=idi.getStockSetInfo(stockSetName,username);

		stragety=new Return(stocklistname,begin,now,s,strategyConditionVO);
	}

	@Override
	public YieldChartDataVO getYieldChartData() {

		try {
			YieldChartDataVO p= new YieldChartDataVO(stragety.getTimelist(),stragety.getBasicReturn(),stragety.getStragetyReturn(),stragety.getAlpha(),stragety.getBeta(),stragety.getSharpe(),stragety.gerBasicYearReturn());
			return  p;
		} catch (PriceTypeException e) {
			e.printStackTrace();
		} catch (NullStockIDException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public YieldDistributionHistogramDataVO getYieldDistributionHistogramData() {

		Map<Double,List<Integer>> zuhe=new HashMap<>();
		try {
			List<Double> jizhunlist=stragety.getBasicReturn();
			List<Double> celuelist=stragety.getStragetyReturn();
			for (int i=0;i<jizhunlist.size();i++){
				double hengzhou=Math.rint(jizhunlist.get(i)*20)*5;
				if (zuhe.get(hengzhou)==null) {
					zuhe.put(hengzhou, Arrays.asList(0, 0));

				}
				if(celuelist.get(i)>=hengzhou){
					int shuzi=zuhe.get(hengzhou).get(0);
					shuzi++;
					zuhe.get(hengzhou).set(0,shuzi);
				}
				else{
					int shuzi=zuhe.get(hengzhou).get(1);
					shuzi++;

					zuhe.get(hengzhou).set(1,shuzi);
				}


			}
			return new YieldDistributionHistogramDataVO(zuhe);


		} catch (PriceTypeException e) {
			e.printStackTrace();
		} catch (NullStockIDException e) {
			e.printStackTrace();
		}



		return null;
	}

	@Override
	public void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException {
		idi.addStockSet(stockSetName, username);
	}

	@Override
	public void deleteStockSet(String stockSetName, String username) {
		idi.deleteStockSet(stockSetName, username);
	}

	@Override
	public void addStockToStockSet(String stockName, String stockSetName, String username) throws StockNameRepeatException {
		idi.addStockToStockSet(stockName, stockSetName, username);
	}

	@Override
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username) {
		idi.deleteStockFromStockSet(stockName, stockSetName, username);
	}

	@Override
	public String nameToCode(String name) throws RemoteException {
		return idi.nameTocode(name);
	}


}
