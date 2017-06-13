package logic.serviceimpl;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import data.service.ICompanyDataInterface;
import data.service.IDataInterface;
import data.service.IStockDataInterface;
import data.service.IStockSetInterface;
import data.service.IStrategyDataInterface;
import data.serviceimpl.CompanyDataController;
import data.serviceimpl.DataInterfaceImpl;
import data.serviceimpl.StockDataController_2;
import data.serviceimpl.StockSetDataController;
import data.serviceimpl.StrategyDataController;
import exception.*;
import logic.service.StockLogicInterface;
import logic.utility.Return;
import logic.utility.SaveThread;
import logic.utility.StrategyCalculate;
import logic.utility.Utility;
import po.CommentPO;
import po.CompanyInfoPO;
import po.SingleStockInfoPO;
import po.StrategyInfoPO;
import po.StrategyShowPO;
import po.TimeSharingPO;
import vo.*;

/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月8日
 */
public class StockLogicImpl implements StockLogicInterface{
  
	private IStockDataInterface stock = StockDataController_2.getInstance();
	private ICompanyDataInterface company = CompanyDataController.getInstance();
	private IStockSetInterface set = StockSetDataController.getInstance();
	private IStrategyDataInterface strategyService = StrategyDataController.getInstance();
	private Utility utility=Utility.getInstance();
	//private Return stragety;
	private StrategyCalculate stragety;

	/**
	 * 添加一系列股票到股票池
	 * @param stockSetName 股票池名字
	 * @param userName 用户名
	 * @param codelist 股票编号的列表
	 */
	public void addStockList_to_StockSet(String userName,String stockSetName,List<String> codelist) throws RemoteException
	{
		set.addStockList_to_StockSet(userName, stockSetName, codelist);
	}
	public CompanyInfoVO getLatestCommpanyInfo(LocalDate time,String code) throws RemoteException,NullStockIDException, NullDateException
	{
		CompanyInfoPO po1=company.getLatestCommpanyInfo(time, code);
		SingleStockInfoPO po2=stock.getSingleStockInfo(code, time);
		double shijing=po2.getClose()/po1.getNetAsset();
		double shiying=po2.getClose()/(po1.getBasicIncome()*4);
		if(po1.getBasicIncome()==0.0)
		{
			shiying=0.0;
		}
		double huanshou=(double)po2.getVolume()/po1.getFluCapitalization()*100;
		CompanyInfoVO vo=new CompanyInfoVO(po1.getCode(),po1.getDate(),po1.getBasicIncome(),po1.getNetAsset(),
				po1.getTotalCapitalization(),po1.getFluCapitalization()
				,shiying,shijing,huanshou);
		return vo;
	}
	/**
	 * 获取市场指数的两个图
	 * @param name 市场名字
	 * @param begin 开始日期
	 * @param end 结束日期
	 * @return
	 * @throws RemoteException
	 * @throws DateInvalidException
	 * @throws BeginInvalidException
	 * @throws EndInvalidException
	 * @throws NullStockIDException
	 */
	public ExponentChartVO getExponentChart(String name,LocalDate begin,LocalDate end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException{

		utility.ifExpDateValid(begin.minusDays(90), end,name);
		List<SingleStockInfoVO> lssi = new ArrayList<>();
		List<SingleStockInfoPO> ls;
		try {
			ls = stock.getPeriodExponent(name, stock.addDays(begin, -60), end);
		} catch (DateOverException e) {
			throw new BeginInvalidException();
		}
		for (SingleStockInfoPO temp:ls){
			if(temp.getDate().isBefore(begin))
			{
				continue;
			}
			SingleStockInfoVO vo=new SingleStockInfoVO(temp);
			lssi.add(vo);
		}
		
		int methods[] = { 5, 10, 20, 30, 60 };
		
		List<EMAInfoVO> llemai = new ArrayList<>();

		for (int i=0;i<methods.length;i++){
			EMAInfoVO vo=getExponentEMAInfo(name, begin, end,methods[i],ls);
			llemai.add(vo);
		}
		
		return new ExponentChartVO(llemai,lssi);
	}
	
	public List<EMAInfoVO> getExponentEMAInfo( String stockCode, LocalDate begin, LocalDate end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {

		// methods用于存储日均线计算的方式
		int methods[] = { 5, 10, 20, 30, 60 };
			
		List<EMAInfoVO> llemai = new ArrayList<>();

		for (int i=0;i<methods.length;i++){
			EMAInfoVO vo=getExponentEMAInfo(stockCode, begin, end,methods[i]);
			llemai.add(vo);
		}
		return  llemai;

	}
	
	private EMAInfoVO getExponentEMAInfo( String name, LocalDate begin, LocalDate end ,int days,List<SingleStockInfoPO> l)
			throws DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {

		
		ArrayList<LocalDate> timelist=new ArrayList<>();
		ArrayList<Double> shujulist=new ArrayList<>();

			for (int i=0;i<l.size();i++){
				if(l.get(i).getDate().isBefore(begin))
				{
					continue;
				}
				double total=0;
				for(int j=0;j<days;j++)
				{
					if(i-j<0)
					{
						total=0;
						break;
					}
					total+=l.get(i-j).getClose();
				}
				total=total/days;
                shujulist.add(total);
                timelist.add(l.get(i).getDate());

            }
		EMAInfoVO vo = new EMAInfoVO(timelist,shujulist,days);
		return vo;
	}
	
	private EMAInfoVO getExponentEMAInfo( String name, LocalDate begin, LocalDate end ,int days)
			throws DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {

		utility.ifExpDateValid(begin.plusDays(-days),end,name);

		LocalDate start=begin.plusDays(-days);
		
		List<SingleStockInfoPO> list=stock.getPeriodExponent(name, start, end);

		ArrayList<LocalDate> timelist=new ArrayList<>();
		ArrayList<Double> shujulist=new ArrayList<>();

			for (int i=0;i<list.size();i++){
				if(i<days)
				{
					continue;
				}
				double total=0;
				for(int j=0;j<days;j++)
				{
					total+=list.get(i-j).getClose();
				}
				total=total/days;
                shujulist.add(total);
                timelist.add(list.get(i).getDate());

            }
		EMAInfoVO vo = new EMAInfoVO(timelist,shujulist,days);
		return vo;
	}
	
	public List<SingleStockInfoVO> getExponentInfoByTime (String name, LocalDate begin, LocalDate end )
			 throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException{
		utility.ifExpDateValid(begin, end,name);
		
		List<SingleStockInfoVO> lssi = new ArrayList<>();
		List<SingleStockInfoPO> ls = stock.getPeriodExponent(name, begin, end);
		for (SingleStockInfoPO temp:ls){
			SingleStockInfoVO vo=new SingleStockInfoVO(temp);
			lssi.add(vo);
		}
		return lssi;
	}
	
	
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
				po = stock.getSingleStockInfo(stockCode,x);
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
			for (;start.compareTo(end)<=0;start=stock.addDays(start,1)){
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
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException {
			utility.ifDateValid(begin, end,stockCodeA);

		LocalDate itr=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());

		List<Double> duishushouyiilv=new ArrayList<>();
		List<Integer> diantu=new ArrayList<>();
		for (int i=0;i<21;i++){
			diantu.add(0);
		}
		List<LocalDate> timelist=new ArrayList<>();

		try {
			for (;itr.compareTo(end)<=0;
                    itr=stock.addDays(itr,1)){
				try {
					SingleStockInfoPO po1=stock.getSingleStockInfo(stockCodeA,itr);


					duishushouyiilv.add(Math.log(po1.getAftClose()));

					timelist.add(itr);
					if(Math.abs(po1.getAftrate())>10) continue;
					Integer s=(int ) (Math.rint(po1.getAftrate()));

					int p=diantu.get(s+10);
					p++;
					diantu.set(s+10,p);


				}  catch (NullDateException e) {
					continue;
				}

			}
		} catch (DateOverException e) {
		}


		return new ComparedInfoVO(duishushouyiilv,timelist,diantu);
	}

	@Override
	public MarketInfoVO getMarketInfo(LocalDate date,String marketname) throws RemoteException, NullMarketException {
		List<String> namelist=set.getStockSetInfo(marketname,null);
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
				po=stock.getSingleStockInfo(name,date);
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

			//select the specific value

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
		List<Integer> shanxingtu=new ArrayList<>();
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
		return set.getStockSet(username);
	}

	@Override
	public List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now,String username) throws NullMarketException {
		List<String> namelist=set.getStockSetInfo(stockSetName,username);
		if (namelist==null )
			return null;
		List<SingleStockInfoVO> res=new ArrayList<>();
		int p=0;
		for (String s:namelist){
			try {
				res.add(new SingleStockInfoVO(stock.getSingleStockInfo(s,now)));
			} catch (NullStockIDException e) {
				continue;
			} catch (NullDateException e) {
				String name =stock.codeToname(s);
				res.add(new SingleStockInfoVO(s,name));
				p++;
				continue;
			}
		}

		//sort the objects

		Collections.sort(res);
		Collections.reverse(res);
		if (p==namelist.size())
			throw  new NullMarketException();
		return res;
	}

	@Override
	public SingleStockInfoVO getStockBasicInfo(String code, LocalDate now) throws NullStockIDException, NullDateException {
		stock.addBrowseTimes(code);
		return new SingleStockInfoVO(stock.getSingleStockInfo(code,now));
	}

	@Override
	public void setStrategy(StrategyConditionVO strategyConditionVO, SaleVO s, LocalDate begin, LocalDate now, String stockSetName,String username) {
		//TODO
		if (stockSetName.equals("SHA") ||stockSetName.equals("SZA") ||stockSetName.equals("SHB")|| stockSetName.equals("SZB")||stockSetName.equals("CYB")||stockSetName.equals("HS300")
				||stockSetName.equals("ZXB") )
			username=null;
		List<String> stocklistname=set.getStockSetInfo(stockSetName,username);
		System.out.println(strategyConditionVO.getExtra());

		//stragety=new Return(stocklistname,begin,now,s,strategyConditionVO);
		stragety=new StrategyCalculate(stocklistname,begin,now,s,strategyConditionVO);
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
				if (zuhe.get(hengzhou)==null) {
					zuhe.put(hengzhou, Arrays.asList(0, 0));
				}
				boolean judge;
				// fen bie tong  ji bu tong shou lv de pin lv
				if(i==0){
					judge=celuelist.get(i)>=jizhunlist.get(i);
				}
				else{
					judge=celuelist.get(i)/celuelist.get(i-1)>=jizhunlist.get(i)/jizhunlist.get(i-1);
				}
				if(judge){
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
		set.addStockSet(stockSetName, username);
	}

	@Override
	public void deleteStockSet(String stockSetName, String username) {
		set.deleteStockSet(stockSetName, username);
	}

	@Override
	public void addStockToStockSet(String stockName, String stockSetName, String username) throws StockNameRepeatException {
		set.addStockToStockSet(stockName, stockSetName, username);
	}

	@Override
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username) {
		set.deleteStockFromStockSet(stockName, stockSetName, username);
	}

	@Override
	public String nameToCode(String name) throws RemoteException {
		return stock.nameTocode(name);
	}

	@Override
	public void saveStragety(StrategyVO strategyVO, String username) throws RemoteException, StrategyRepeatException {
		StrategyConditionVO strategyConditionVO=strategyVO.getStrategyConditionVO();
		SaleVO saleVO=strategyVO.getSaleVO();
		String strategytypename=strategyConditionVO.getName();
		Boolean publicorprivate=strategyVO.isPublicorprivate();
		String name=strategyVO.getName();
		List<Integer> parameter=strategyConditionVO.getExtra();
		int purchasenum=strategyConditionVO.getNums();
		int tiaocangqi=saleVO.getTiaocangqi();
		String tiaocangjiage=saleVO.getTiaocangjiage();
		StrategyInfoPO po=new StrategyInfoPO(strategytypename,publicorprivate,parameter,purchasenum,tiaocangqi,tiaocangjiage);
		strategyService.saveStrategy(po,name,username);
		new SaveThread(strategyConditionVO,saleVO,username,name).start();
	}

	@Override
	public void deleteStrategy(String createName, String strategyName) throws RemoteException {
		strategyService.deleteStrategy(createName, strategyName);
	}

	@Override
	public void comment(String Username, String strategyName, String commenterName, LocalDateTime time, String comment) throws RemoteException {
		strategyService.comment(Username, strategyName, commenterName, time, comment);
	}

	@Override
	public StrategyShowVO getStrategy(String createrName, String StrategyName) throws RemoteException {

		StrategyInfoPO infoPO=strategyService.applyStrategy(createrName, StrategyName);
		StrategyShowPO showPO=strategyService.getStrategy(createrName, StrategyName);
		List<CommentPO> commentPOS=strategyService.getStrategyComments(createrName, StrategyName);

		List<CommentVO> commentVOS=new ArrayList<>();
		for (CommentPO po:commentPOS){
			commentVOS.add(new CommentVO(po.getComments(),po.getTime(),po.getCreaterName()));
		}
		StrategyShowVO res=new StrategyShowVO();
		res.setTimeList(showPO.getTimeList());
		res.setBasicReturn(showPO.getBasicReturn());
		res.setStrategyReturn(showPO.getStrategyReturn());
		res.setAlpha(showPO.getAlpha());
		res.setBeta(showPO.getBeta());
		res.setSharp(showPO.getSharp());
		res.setZuidahuiche(showPO.getZuidahuiche());
		res.setStrategyYearReturn(showPO.getStrategyYearReturn());
		res.setComments(commentVOS);
		res.setStrategyname(showPO.getStrategyName());
		res.setPublicorPrivate(infoPO.isPublicorprivate());
		res.setStrategyConditionVO(new StrategyConditionVO(infoPO.getStrategTypeNname(),infoPO.getParameter(),infoPO.getPurchasenum()));
		res.setSaleVO(new SaleVO(infoPO.getTiaocangqi(),infoPO.getTiaocangjiage(),infoPO.getPurchasenum()));
		res.setJizhunYearReturn(showPO.getJizhunYearReturn());
		return res;
	}

	@Override
	public List<StrategyListVO> getStrategyList(String createrName) {
		List<StrategyShowPO> list=strategyService.getStrategyList(createrName);
		List<StrategyListVO> reslist=new ArrayList<>();
		for(StrategyShowPO po:list){
			System.out.println(po.getStrategyYearReturn());
			reslist.add(new StrategyListVO(po.getCreaterName(),po.getStrategyName(),po.getStrategyYearReturn()));
		}
		Collections.sort(reslist);

		return reslist;
	}

	@Override
	public List<StrategyListVO> getStrategyList() throws RemoteException {
		List<StrategyShowPO> list=strategyService.getStrategyList();
		List<StrategyListVO> reslist=new ArrayList<>();
		for(StrategyShowPO po:list){
			reslist.add(new StrategyListVO(po.getCreaterName(),po.getStrategyName(),po.getStrategyYearReturn()));
		}
		Collections.sort(reslist);

		return reslist;
	}

	@Override
	public void setPublic(String creatroName, String straetgyName, boolean property) throws RemoteException {
		strategyService.setPublic(creatroName, straetgyName, property);
	}

	@Override
	public List<String> getAreaList() throws RemoteException {
		return set.getAreaList();
	}

	@Override
	public List<String> getConceptList() throws RemoteException {
		return set.getConceptList();
	}

	@Override
	public List<String> getIndustryList() throws RemoteException {
		return set.getIndustryList();
	}

	/**
	 * 获取股票分时线
	 * @param code
	 * @param date
	 * @return 从9点到11点半，再从13点到15点的以1秒为单位的double数组
	 * @throws TimeShraingLackException
	 * @throws NullStockIDException
	 */
	public TimeSharingVO getTimeSharingData(String code,LocalDate date)throws TimeShraingLackException,NullStockIDException,RemoteException{
		TimeSharingPO po=stock.getTimeSharingData(code, date);
		return new TimeSharingVO(po.getMinute_data(),po.getLast_close());
	}

	@Override
	public List<String> fuzzySearch(String input) throws RemoteException {
		List<String> list=stock.fuzzySearch(input);
		if (list.size()<10)
			return list;
		List<String> sublist=new ArrayList<>();
		for (int i=0;i<10;i++){
			sublist.add(list.get(i));
		}
		return sublist;
	}
}
