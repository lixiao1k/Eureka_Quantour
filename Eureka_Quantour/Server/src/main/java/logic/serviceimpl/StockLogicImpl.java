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
		for (;x.compareTo(end)<0;x=x.plusDays(1)){
			SingleStockInfoPO po= null;
			System.out.println(x);
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
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, NullStockIDException, DateOverException {

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
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException, DateOverException, NullStockIDException {

		utility.ifDateValid(begin.plusDays(-days),end,stockCode);

		LocalDate start=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());


		ArrayList<LocalDate> timelist=new ArrayList<>();
		ArrayList<Double> shujulist=new ArrayList<>();

		for (;start.compareTo(end)<0;start=idi.addDays(start,1)){
			shujulist.add(utility.getEMA(stockCode,start,days));
			timelist.add(LocalDate.of(start.getYear(),start.getMonth(),start.getDayOfMonth()));

		}
		EMAInfoVO vo = new EMAInfoVO(timelist,shujulist,days);
		return vo;
	}



//	@Override
//	public ComparedInfoVO getComparedInfo(String stockCodeA, String stockCodeB, LocalDate begin, LocalDate end)
//			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
//
//		//TODO
//		utility.ifDateValid(begin, end,stockCodeA);
//		utility.ifDateValid(begin, end,stockCodeB);
//
//
//		// 日期格式化
//		Calendar beginTempA = (Calendar)begin.clone();
//		Calendar endTempA =  (Calendar)end.clone();
//		Calendar beginTempB = (Calendar)begin.clone();
//		Calendar endTempB =  (Calendar)end.clone();
//
//
//		List<SingleStockInfoVO> lstiA = new ArrayList<>();
//		List<SingleStockInfoPO> listA=idi.getSingleStockInfo_byEnd(stockCodeA, beginTempA, endTempA);
//		List<SingleStockInfoPO> listB=idi.getSingleStockInfo_byEnd(stockCodeB, beginTempB, endTempB);
//
//		if( listA==null || listB==null)
//			throw new DateInvalidException();
//
//		for(SingleStockInfoPO po:listA){
//			SingleStockInfoVO vo=new SingleStockInfoVO(po);
//			lstiA.add(vo);
//		}
//		List<SingleStockInfoVO> lstiB = new ArrayList<>();
//		for(SingleStockInfoPO po:listB){
//			SingleStockInfoVO vo=new SingleStockInfoVO(po);
//			lstiB.add(vo);
//		}
//
//
//
//		boolean ifANull = false;
//		boolean ifBNull = false;
//
//
//
//		// 获取前一天的数据
//		SingleStockInfoVO ssiA = new SingleStockInfoVO();
//		SingleStockInfoVO ssiB = new SingleStockInfoVO();
//		Calendar tempCal = begin;
//		int getCount = 5; // 只取5次
//		for( int i=0; i<getCount; i++ ){
//			if( ifANull && ifBNull)
//				break;
//			utility.calendarAdvance(tempCal);
//			if( ssiA.getCode().equals("") && !ifANull )
//				ssiA = getSingleStockInfoByTime(stockCodeA, tempCal, tempCal).get(0);
//			if( ssiB.getCode().equals("") && !ifBNull )
//				ssiB = getSingleStockInfoByTime(stockCodeB, tempCal, tempCal).get(0);
//			if( !ssiA.getCode().equals("") && !ssiB.getCode().equals(""))
//				break;
//		}
//		// 用于暂存前一天的复权收盘价
//		double lastAdjcloseA = 0.0, lastAdjcloseB = 0.0;
//		lastAdjcloseA = ssiA.getAdjclose();
//		lastAdjcloseB = ssiB.getAdjclose();
//
//		// 开始存储返回的数据
//		int tempInt = lstiA.size(); // 用处初始化VO内部数组大小
//		ComparedInfoVO ci = new ComparedInfoVO(tempInt);
//		ci.setNameA( lstiA.get(0).getName() );
//		ci.setNameB( lstiB.get(0).getName() );
//		ci.setCodeA( stockCodeA );
//		ci.setCodeB( stockCodeB );
//		// 涨幅为正，跌幅为负
//		int iLastIndex = 0, iNewIndex = tempInt-1;
//		double dLastAdjclose =0.0, dNewAdjclose = 0.0;
//		// 如果A不为空，一直获取到A的最初一天复权收盘价 >0
//		if( !ifANull ){
//			if( lastAdjcloseA==0 ){
//				while( lstiA.get(iLastIndex).getAdjclose()==0 && iLastIndex<iNewIndex )
//					iLastIndex++;
//				dLastAdjclose = lstiA.get(iLastIndex).getAdjclose();
//				lastAdjcloseA = dLastAdjclose;
//			}
//			else
//				dLastAdjclose = lastAdjcloseA;
//			if( lastAdjcloseA!=0 ){
//				// 一直获取到A的最近的复权收盘价 >0
//				while( lstiA.get(iNewIndex).getAdjclose()==0 && iNewIndex>iLastIndex )
//					iNewIndex--;
//				dNewAdjclose = lstiA.get(iNewIndex).getAdjclose();
//				double result = ( dNewAdjclose-dLastAdjclose ) / dLastAdjclose;
//				ci.setRODA( utility.formatDoubleSaveFive( result ) );
//			}
//		}
//
//		iLastIndex = 0; iNewIndex = tempInt-1;
//		dLastAdjclose =0.0; dNewAdjclose = 0.0;
//		// 如果B不为空，一直获取到B的最初一天复权收盘价 >0
//		if( !ifBNull ){
//			if( lastAdjcloseB==0 ){
//				while( lstiB.get(iLastIndex).getAdjclose()==0 && iLastIndex<iNewIndex )
//					iLastIndex++;
//				dLastAdjclose = lstiB.get(iLastIndex).getAdjclose();
//				lastAdjcloseB = dLastAdjclose;
//			}
//			else
//				dLastAdjclose = lastAdjcloseB;
//			if( lastAdjcloseB!=0 ){
//				// 一直获取到B的最近的复权收盘价 >0
//				while( lstiB.get(iNewIndex).getAdjclose()==0 && iNewIndex>iLastIndex )
//					iNewIndex--;
//				dNewAdjclose = lstiB.get(iNewIndex).getAdjclose();
//				double result = ( dNewAdjclose-dLastAdjclose ) / dLastAdjclose;
//				ci.setRODB( utility.formatDoubleSaveFive( result ) );
//			}
//		}
//
//		// 计算最高值、最低值、储存收盘价、计算对数收益率和对数收益率方差
//		double maxA = 0, minA = 1000000.0, maxB = 0, minB = 1000000.0;
//		double newAdjloseA = 0.0, newAdjcloseB = 0.0;
//		double[] logYieldA = new double[tempInt];
//		double[] logYieldB = new double[tempInt];
//		double[] closeA = new double[tempInt];
//		double[] closeB = new double[tempInt];
//		Calendar[] date = new Calendar[tempInt];
//		ssiA = new SingleStockInfoVO();
//		ssiB = new SingleStockInfoVO();
//		for( int i=0 ; i<tempInt ; i++ ){
//			ssiA = lstiA.get(i);
//			ssiB = lstiB.get(i);
//			closeA[i] = ssiA.getClose();
//			closeB[i] = ssiB.getClose();
//			if( !ifANull)
//				date[i] = ssiA.getDate();
//			else
//				date[i] = ssiB.getDate();
//
//			// 获取最高（低）值
//			if( lstiA.get(i).getHigh()>maxA )
//				maxA = ssiA.getHigh();
//			if( lstiB.get(i).getHigh()>maxB )
//				maxB = ssiB.getHigh();
//			if( lstiA.get(i).getLow()<minA )
//				minA = ssiA.getLow();
//			if( lstiB.get(i).getLow()<minB )
//				minB = ssiB.getLow();
//
//			// 计算对数收益率
//			newAdjloseA = ssiA.getAdjclose();
//			if( lastAdjcloseA>0 )
//				logYieldA[i] = utility.formatDoubleSaveFive( Math.log(newAdjloseA/lastAdjcloseA) );
//			else
//				logYieldA[i] = Integer.MIN_VALUE;
//			lastAdjcloseA = newAdjloseA;
//
//			newAdjcloseB = ssiB.getAdjclose();
//			if( lastAdjcloseB>0 )
//				logYieldB[i] = utility.formatDoubleSaveFive( Math.log(newAdjcloseB/lastAdjcloseB) );
//			else
//				logYieldB[i] = Integer.MIN_VALUE;
//			lastAdjcloseB = newAdjcloseB;
//		}
//
//		ci.setLowA(minA); ci.setLowB(minB);
//		ci.setHighA(maxA); ci.setHighB(maxB);
//		ci.setCloseA(closeA); ci.setCloseB(closeB);
//		ci.setLogYieldA(logYieldA);
//		ci.setLogYieldB(logYieldB);
//		ci.setLogYieldVarianceA( utility.calVariance(logYieldA) );
//		ci.setLogYieldVarianceB( utility.calVariance(logYieldB) );
//		ci.setDate(date);
//		return ci;
//	}

//	@Override
//	public MarketInfoVO getMarketInfo(Calendar date)
//			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
//		// TODO Auto
//		MarketInfoVO mi = new MarketInfoVO();
//
////		utility.ifDateValid(date, date);
//
//
//		// 日期格式化
//		Calendar dateTemp = (Calendar) date.clone();
//
//
//		List<SingleStockInfoPO> lsti = idi.getMarketByDate( dateTemp );
//		// 如果没有数据，抛出日期无效异常
//		if( lsti==null )
//			throw new DateInvalidException();
//		else{
//			// 用来暂存市场中单支股票的数据
//			SingleStockInfoPO ssi = new SingleStockInfoPO();
//			long volume = 0;
//			int riseStop = 0, dropStop = 0, riseEFP = 0, stopEFP = 0;
//			int OMCEFP = 0, OMCLTFP = 0;
//			for( int i=0; i<lsti.size(); i++ ){
//				ssi = lsti.get(i);
//				double open = ssi.getOpen();
//				double close = ssi.getClose();
//				double lastClose = ssi.getLast_close();
//				double adjclose = ssi.getAdjclose();
//				double lastAdjclose = ssi.getLast_adjclose();// 用于保存前一天的复权收盘价
//				// 计算总的交易量
//				if( ssi.getVolume() > 0 )
//					volume += ssi.getVolume();
//
//				if( lastAdjclose>0 && adjclose>0 ){
//					// 计算涨停的股票数
//					if( utility.ifDoubleEqual((ssi.getHigh()-lastAdjclose)/lastAdjclose,  0.10) )
//						riseStop++;
//					// 计算跌停的股票数
//					if( utility.ifDoubleEqual((lastAdjclose-ssi.getLow())/lastAdjclose,  0.10) )
//						dropStop++;
//					// 计算涨幅超过5%
//					if( (adjclose-lastAdjclose)/lastAdjclose > 0.05
//							&& (adjclose-lastAdjclose)/lastAdjclose <= 0.1)
//						riseEFP++;
//					// 计算跌幅超过5%
//					if( (lastAdjclose-adjclose)/lastAdjclose > 0.05
//							&& (lastAdjclose-adjclose)/lastAdjclose <= 0.1)
//						stopEFP++;
//				}
//				if( open>0 && close>0 && lastClose>0 ){
//					// 计算某某数据
//					if( (open-close) > (0.05*lastClose) )
//						OMCEFP++;
//					else if( (open-close) < (-0.05*lastClose) )
//						OMCLTFP++;
//				}
//			}
//
//			mi.setVolume(volume);
//			mi.setNumOfRiseStop(riseStop);
//			mi.setNumOfDropStop(dropStop);
//			mi.setNumOfRiseEFP(riseEFP);
//			mi.setNumOfDropEFP(stopEFP);
//			mi.setNumOfOMCEFP(OMCEFP);
//			mi.setNumOfOMCLTFP(OMCLTFP);
//
//			return mi;
//		}
//	}

	@Override
	public List<String> getStockSet(String username) {
		return idi.getStockSet(username);
	}

	@Override
	public List<SingleStockInfoVO> getStockSetSortedInfo(String stockSetName, LocalDate now,String username) {
		List<String> namelist=idi.getStockSetInfo(stockSetName,username);
		if (namelist==null )
			return null;
		List<SingleStockInfoVO> res=new ArrayList<>();
		for (String s:namelist){
			try {
				res.add(new SingleStockInfoVO(idi.getSingleStockInfo(s,now)));
			} catch (NullStockIDException e) {
				continue;
			} catch (NullDateException e) {
				res.add(new SingleStockInfoVO(s));
				continue;
			}
		}
		Collections.sort(res);
		Collections.reverse(res);
		return res;
	}

	@Override
	public SingleStockInfoVO getStockBasicInfo(String code, LocalDate now) throws NullStockIDException, NullDateException {
		return new SingleStockInfoVO(idi.getSingleStockInfo(code,now));
	}

	@Override
	public void setStrategy(StrategyConditionVO strategyConditionVO, SaleVO s, LocalDate begin, LocalDate now, String stockSetName,String username) {
		//TODO
		List<String> stocklistname=idi.getStockSetInfo(stockSetName,username);

		stragety=new Return(stocklistname,begin,now,s,strategyConditionVO);
	}

	@Override
	public YieldChartDataVO getYieldChartData() {

		try {
			YieldChartDataVO p= new YieldChartDataVO(stragety.getTimelist(),stragety.getBasicReturn(),stragety.getStragetyReturn());
			System.out.println("asgr");
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
		List<LocalDate> timelist=stragety.getTimelist();
		try {
			List<Double> jizhunlist=stragety.getBasicReturn();
			List<Double> celuelist=stragety.getStragetyReturn();
			for (int i=0;i<timelist.size();i++){
				double hengzhou=Math.rint(jizhunlist.get(i));

				if (zuhe.get(hengzhou)==null)
					zuhe.put(hengzhou,Arrays.asList(0,0));
				if(celuelist.get(i)>=hengzhou){
					int shuzi=zuhe.get(hengzhou).get(0);
					zuhe.get(hengzhou).set(0,shuzi++);
				}
				else{
					int shuzi=zuhe.get(hengzhou).get(1);
					zuhe.get(hengzhou).set(1,shuzi++);
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





}
