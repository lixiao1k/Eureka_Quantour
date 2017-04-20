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
		LocalDate itr=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());

		List<Double> duishushouyiilv=new ArrayList<>();
		List<Integer> diantu=new ArrayList<>();
		for (int i=0;i<21;i++){
			diantu.add(0);
		}
		List<LocalDate> timelist=new ArrayList<>();

		try {
			for (;itr.compareTo(end)<=0;
                    itr=idi.addDays(itr,1)){
				try {
					SingleStockInfoPO po1=idi.getSingleStockInfo(stockCodeA,itr);


					duishushouyiilv.add(Math.log(po1.getAftClose()));

					timelist.add(itr);
					if(Math.abs(po1.getAftrate())>10) continue;
					Integer s=(int ) (Math.rint(po1.getAftrate()));

					int p=diantu.get(s+10);
					p++;
					diantu.set(s+10,p);


				} catch (NullStockIDException e) {
					e.printStackTrace();
				} catch (NullDateException e) {
					continue;
				}

			}
		} catch (DateOverException e) {
		}


		return new ComparedInfoVO(duishushouyiilv,timelist,diantu);
	}

	@Override
	public MarketInfoVO getMarketInfo(LocalDate date,String marketname) throws RemoteException, NullMarketException {
		List<String> namelist=idi.getStockSetInfo(marketname,null);
		long volume=0;
		int wushuju=0;
		int chaoguo10=0;
		int dieguo10=0;
		int zheng5dao10=0;
		int zheng0dao5=0;
		int fu5dao10=0;
		int fu0dao5=0;
		List<Double> rate=new ArrayList<>();
		List<Integer> diantu=new ArrayList<>();
		for (int i=0;i<21;i++){
			diantu.add(0);
		}
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
			if (wushuju==namelist.size())
				throw new NullMarketException();
			volume+=po.getVolume();
			rate.add(po.getRate());

			if (po.getRate()<10 && po.getRate()>=5){
				zheng5dao10++;
			}
			if (po.getRate()>-10 && po.getRate()<=-5){
				fu5dao10++;
			}

			if (po.getRate()<5 && po.getRate()>=0){
				zheng0dao5++;
			}
			if (po.getRate()>-5 && po.getRate()<0){
				fu0dao5++;
			}

			if (po.getRate()>=10){
				chaoguo10++;
				continue;
			}
			if (po.getRate()<=-10){
				dieguo10++;
				continue;
			}
			Integer s=(int ) (Math.rint(po.getRate()));

			int p=diantu.get(s+10);
			p++;
			diantu.set(s+10,p);
		}
		List<Integer> shanxingtu=new ArrayList();
		shanxingtu.add(fu5dao10);
		shanxingtu.add(fu0dao5);
		shanxingtu.add(zheng0dao5);
		shanxingtu.add(zheng5dao10);
//		System.out.println("afesgrht   "+chaoguo10);
		MarketInfoVO marketInfoVO=new MarketInfoVO(volume,chaoguo10,dieguo10,wushuju,shanxingtu,diantu);
		marketInfoVO.setJunzhi(utility.getAverage(rate));
		marketInfoVO.setFangcha(utility.getCorvariance(rate,rate));


		return marketInfoVO;
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
			YieldChartDataVO p= new YieldChartDataVO(stragety.getTimelist(),stragety.getBasicReturn(),stragety.getStragetyReturn(),stragety.getAlpha(),stragety.getBeta(),stragety.getSharpe(),stragety.gerYearReturn(),stragety.gerBasicYearReturn());
			p.setZuidahuiche(stragety.getzuidaguiceh());
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
				System.out.println(hengzhou);
				if (zuhe.get(hengzhou)==null) {
					zuhe.put(hengzhou, Arrays.asList(0, 0));
				}


				boolean judge=false;
				if(i==0){
					judge=celuelist.get(i)>=jizhunlist.get(i);
				}
				else{
					judge=celuelist.get(i)/celuelist.get(i-1)>=jizhunlist.get(i)/jizhunlist.get(i-1);
				}
				if(judge){
					System.out.println("haha");
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
			System.out.println("--------"+zuhe);
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

	@Override
	public List<List<Double>> getDantengchart(String name, LocalDate begin, LocalDate end, String hengzhou, String usernam, String stocksetname,int geiding) {
		if (stocksetname.equals("SHA") ||stocksetname.equals("SZA") ||stocksetname.equals("SHB")|| stocksetname.equals("SZB")||stocksetname.equals("CYB")||stocksetname.equals("HS300")
				||stocksetname.equals("ZXB") )
			usernam=null;
		List<String> stocklistname=idi.getStockSetInfo(stocksetname,usernam);
		List<Double> heng=new ArrayList<>();
		List<Double> zong1=new ArrayList<>();
		List<Double> zong2=new ArrayList<>();
				for (int i=5;i<30;i+=5){
					heng.add(i*1.0);
					SaleVO saleVO=new SaleVO(i,"收盘价");
					List<Object> list=new ArrayList<>();
					list.add(new Integer(geiding));
					StrategyConditionVO strategyConditionVO=new StrategyConditionVO(name,list,10);
					Return stra=new Return(stocklistname,begin,end,saleVO,strategyConditionVO);
					try {
						List<Double> celueshouyi=stra.getStragetyReturn();
						List<Double> jizhunshouyi=stra.getBasicReturn();
						double a1=(celueshouyi.get(celueshouyi.size()-1)-jizhunshouyi.get(jizhunshouyi.size()))/jizhunshouyi.get(jizhunshouyi.size());
						zong1.add(a1);
						int index=0;
						for (int j=0;j<celueshouyi.size();j++){
							if (celueshouyi.get(i)>jizhunshouyi.get(i))
								index++;
						}
						zong2.add(1.0*index/jizhunshouyi.size());
					} catch (NullStockIDException e) {
						e.printStackTrace();
					} catch (PriceTypeException e) {
						e.printStackTrace();
					}




				}





		List<List<Double>> res=new ArrayList<>();
			res.add(heng);
			res.add(zong1);
			res.add(zong2);
		return res;
	}

	@Override
	public List<String> getIndustryList() throws RemoteException {
		return idi.getIndustryList();
	}


}
