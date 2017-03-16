package logic.serviceimpl;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import logic.service.StockLogicInterface;
import po.SingleStockInfoPO;
import resultmessage.BeginInvalidException;
import resultmessage.DateInvalidException;
import resultmessage.EndInvalidException;
import vo.ComparedInfoVO;
import vo.EMAInfoVO;
import vo.MarketInfoVO;
import vo.SingleStockInfoVO;
/**
 * 
 * @Description: TODO
 * @author: hzp
 * @time: 2017年3月8日
 */
public class StockLogicImpl implements StockLogicInterface{
  
	private IDataInterface idi = new DataInterfaceImpl();
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
	
	@Override
	public List<SingleStockInfoVO> getSingleStockInfoByTime( String stockCode, Calendar begin, Calendar end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		// TODO Auto-generated method stub
		
		// 判断日期是否有效
		try{
			ifDateValid(begin, end);
		}catch( BeginInvalidException e ){
			throw new BeginInvalidException();
		}catch( EndInvalidException e ){
			throw new EndInvalidException();
		}
		
		// 日期格式化
		Calendar beginTemp = Calendar.getInstance();
		Calendar endTemp = Calendar.getInstance();
		try {
			beginTemp.setTime(sdf.parse( formateCalendar(begin) ));
			endTemp.setTime(sdf.parse( formateCalendar(end) ));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("日期转换出错");
			e.printStackTrace();
		}
		List<SingleStockInfoVO> lssi = new SingleStockInfoPO().POToVO( idi.getSingleStockInfo(stockCode, beginTemp, endTemp) );
		
		// 如果没有数据，抛出日期无效异常
		if( lssi==null )
			throw new DateInvalidException();
		else
			return lssi;
	}

	@Override
	public List<List<EMAInfoVO>> getEMAInfo( String stockCode, Calendar begin, Calendar end )
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		// TODO Auto-generated method stub
		
		// 判断日期是否有效
		try{
			ifDateValid((Calendar)begin.clone(), (Calendar)end.clone());
		}catch( BeginInvalidException e ){
			throw new BeginInvalidException();
		}catch( EndInvalidException e ){
			throw new EndInvalidException();
		}
		
		// 日期格式化
		Calendar beginTemp = Calendar.getInstance();
		Calendar endTemp = Calendar.getInstance();
		try {
			beginTemp.setTime(sdf.parse( formateCalendar((Calendar)begin.clone()) ));
			endTemp.setTime(sdf.parse( formateCalendar((Calendar)end.clone()) ));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("日期转换出错");
			e.printStackTrace();
		}
		List<SingleStockInfoPO> lssi = idi.getSingleStockInfo(stockCode, beginTemp, endTemp);
		
		// 如果都没有数据，抛出日期无效异常
		if( lssi==null )
			throw new DateInvalidException();
		else{
			// methods用于存储日均线计算的方式
			int methods[] = { 5, 10, 20, 30, 60 };
			
			// 用于暂存前method天的股票数据
			Calendar lastBegin = (Calendar)begin.clone();
			Calendar lastEnd = calendarAdvance( (Calendar)begin.clone() );
			int circleCount = methods[methods.length-1]+5; //用于表示往前取多少天
			for( int i=0; i<circleCount; i++)
				lastBegin = calendarAdvance( (Calendar)lastBegin.clone() );
			try {
				lastBegin.setTime(sdf.parse( formateCalendar((Calendar)lastBegin.clone()) ));
				lastEnd.setTime(sdf.parse( formateCalendar((Calendar)lastEnd.clone()) ));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("日期转换出错");
				e.printStackTrace();
			}
			List<SingleStockInfoPO> lssiTemp = idi.getSingleStockInfo(stockCode, lastBegin, lastEnd);
			// 用于存放List，并且作为返回变量
			List<List<EMAInfoVO>> llemai = new ArrayList<List<EMAInfoVO>>();
			// 用于暂存取到的每一个Stock数据
			SingleStockInfoPO ssi = new SingleStockInfoPO();
			
			for( int j=0; j<methods.length; j++){
				int method = methods[j];
				double close = 0.0;
				List<EMAInfoVO> lemai = new ArrayList<EMAInfoVO>();
	
				double tempDouble = 0.0;
				List<Double> closes = new ArrayList<Double>();
				// 先叠加前（method-1）天的数据
				if( lssiTemp.size()>0 ){
					int maxIndex= lssiTemp.size()-1;
					circleCount = Math.min( method-1, maxIndex+1 );
					for( int k=0; k<circleCount; k++){
						if( lssiTemp.get(maxIndex-k).getClose()>0 ){
							tempDouble += lssiTemp.get(maxIndex-k).getClose();
							closes.add( 0, lssiTemp.get(maxIndex-k).getClose() );
						}
						else{
							if( circleCount<(maxIndex+1) )
								circleCount++;
						}
					}
				}
				// 平均的基数
				method = closes.size()+1;
				for( int i=0; i<lssi.size(); i++){
					ssi = lssi.get(i);
					close = ssi.getClose();
					tempDouble += close;
					closes.add(close);
					lemai.add( new EMAInfoVO(ssi.getDate(), formatDoubleSaveTwo(tempDouble/method)) );
					tempDouble -= closes.remove(0);
				}
				llemai.add(lemai);
			}
			return llemai;
		}
	}

	@Override
	public ComparedInfoVO getComparedInfo(String stockCodeA, String stockCodeB, Calendar begin, Calendar end)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		// TODO Auto-generated method stub
		
		// 判断日期是否有效
		try{
			ifDateValid(begin, end);
		}catch( BeginInvalidException e ){
			throw new BeginInvalidException();
		}catch( EndInvalidException e ){
			throw new EndInvalidException();
		}
				
		// 日期格式化
		Calendar beginTempA = Calendar.getInstance();
		Calendar endTempA = Calendar.getInstance();
		Calendar beginTempB = Calendar.getInstance();
		Calendar endTempB = Calendar.getInstance();
		try {
			beginTempA.setTime(sdf.parse( formateCalendar( (Calendar)begin.clone() ) ));
			endTempA.setTime(sdf.parse( formateCalendar( (Calendar)end.clone() ) ));
			beginTempB.setTime(sdf.parse( formateCalendar( (Calendar)begin.clone() ) ));
			endTempB.setTime(sdf.parse( formateCalendar( (Calendar)end.clone() ) ));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("日期转换出错");
			e.printStackTrace();
		}

		List<SingleStockInfoVO> lstiA = new SingleStockInfoPO().POToVO( idi.getSingleStockInfo(stockCodeA, beginTempA, endTempA) );
		List<SingleStockInfoVO> lstiB = new SingleStockInfoPO().POToVO( idi.getSingleStockInfo(stockCodeB, beginTempB, endTempB) );
		boolean ifANull = false;
		boolean ifBNull = false;
		
		// 如果都没有数据，抛出日期无效异常
		if( lstiA==null && lstiB==null)
			throw new DateInvalidException();
		// 如果一个没数据，就添加new的对象
		else if( lstiA==null && lstiB!=null ){
			ifANull = true;
			lstiA = new ArrayList<SingleStockInfoVO>();
			for( int i=0; i<lstiB.size(); i++)
				lstiA.add(new SingleStockInfoVO());
		}
		else if( lstiA!=null && lstiB==null ){
			ifBNull = true;
			lstiB = new ArrayList<SingleStockInfoVO>();
			for( int i=0; i<lstiA.size(); i++)
				lstiB.add(new SingleStockInfoVO());
		}
		
		// 获取前一天的数据
		SingleStockInfoVO ssiA = new SingleStockInfoVO();
		SingleStockInfoVO ssiB = new SingleStockInfoVO();
		Calendar tempCal = begin;
		int getCount = 5; // 只取5次
		for( int i=0; i<getCount; i++ ){
			if( ifANull && ifBNull)
				break;
			tempCal = calendarAdvance(tempCal);
			if( ssiA.getCode().equals("") && !ifANull )
				ssiA = getSingleStockInfoByTime(stockCodeA, tempCal, tempCal).get(0);
			if( ssiB.getCode().equals("") && !ifBNull )
				ssiB = getSingleStockInfoByTime(stockCodeB, tempCal, tempCal).get(0);
			if( !ssiA.getCode().equals("") && !ssiB.getCode().equals(""))
				break;
		}
		// 用于暂存前一天的复权收盘价
		double lastAdjcloseA = 0.0, lastAdjcloseB = 0.0;
		lastAdjcloseA = ssiA.getAdjclose();
		lastAdjcloseB = ssiB.getAdjclose();
		
		// 开始存储返回的数据
		int tempInt = lstiA.size(); // 用处初始化VO内部数组大小
		ComparedInfoVO ci = new ComparedInfoVO(tempInt);
		ci.setNameA( lstiA.get(0).getName() );
		ci.setNameB( lstiB.get(0).getName() );
		ci.setCodeA( stockCodeA );
		ci.setCodeB( stockCodeB );
		// 涨幅为正，跌幅为负
		int iLastIndex = 0, iNewIndex = tempInt-1;
		double dLastAdjclose =0.0, dNewAdjclose = 0.0;
		// 如果A不为空，一直获取到A的最初一天复权收盘价 >0
		if( !ifANull ){
			if( lastAdjcloseA==0 ){
				while( lstiA.get(iLastIndex).getAdjclose()==0 && iLastIndex<iNewIndex )
					iLastIndex++;
				dLastAdjclose = lstiA.get(iLastIndex).getAdjclose();
				lastAdjcloseA = dLastAdjclose;
			}
			else
				dLastAdjclose = lastAdjcloseA;
			if( lastAdjcloseA!=0 ){
				// 一直获取到A的最近的复权收盘价 >0
				while( lstiA.get(iNewIndex).getAdjclose()==0 && iNewIndex>iLastIndex )
					iNewIndex--;
				dNewAdjclose = lstiA.get(iNewIndex).getAdjclose();
				double result = ( dNewAdjclose-dLastAdjclose ) / dLastAdjclose;
				ci.setRODA( formatDoubleSaveFive( result ) );
			}
		}
		
		iLastIndex = 0; iNewIndex = tempInt-1;
		dLastAdjclose =0.0; dNewAdjclose = 0.0;
		// 如果B不为空，一直获取到B的最初一天复权收盘价 >0
		if( !ifBNull ){
			if( lastAdjcloseB==0 ){
				while( lstiB.get(iLastIndex).getAdjclose()==0 && iLastIndex<iNewIndex )
					iLastIndex++;
				dLastAdjclose = lstiB.get(iLastIndex).getAdjclose();
				lastAdjcloseB = dLastAdjclose;
			}
			else
				dLastAdjclose = lastAdjcloseB;
			if( lastAdjcloseB!=0 ){
				// 一直获取到B的最近的复权收盘价 >0
				while( lstiB.get(iNewIndex).getAdjclose()==0 && iNewIndex>iLastIndex )
					iNewIndex--;
				dNewAdjclose = lstiB.get(iNewIndex).getAdjclose();
				double result = ( dNewAdjclose-dLastAdjclose ) / dLastAdjclose;
				ci.setRODB( formatDoubleSaveFive( result ) );
			}
		}
		
		// 计算最高值、最低值、储存收盘价、计算对数收益率和对数收益率方差
		double maxA = 0, minA = 1000000.0, maxB = 0, minB = 1000000.0;
		double newAdjloseA = 0.0, newAdjcloseB = 0.0;
		double[] logYieldA = new double[tempInt];
		double[] logYieldB = new double[tempInt];
		double[] closeA = new double[tempInt];
		double[] closeB = new double[tempInt];
		Calendar[] date = new Calendar[tempInt];
		ssiA = new SingleStockInfoVO();
		ssiB = new SingleStockInfoVO();	
		for( int i=0 ; i<tempInt ; i++ ){
			ssiA = lstiA.get(i);
			ssiB = lstiB.get(i);
			System.out.println(ssiA.getAdjclose()+" "+ssiB.getAdjclose());
			closeA[i] = ssiA.getClose();
			closeB[i] = ssiB.getClose();
			if( !ifANull)
				date[i] = ssiA.getDate();
			else
				date[i] = ssiB.getDate();
			
			// 获取最高（低）值
			if( lstiA.get(i).getHigh()>maxA )
				maxA = ssiA.getHigh();
			if( lstiB.get(i).getHigh()>maxB )
				maxB = ssiB.getHigh();
			if( lstiA.get(i).getLow()<minA )
				minA = ssiA.getLow();
			if( lstiB.get(i).getLow()<minB )
				minB = ssiB.getLow();
			
			// 计算对数收益率
			newAdjloseA = ssiA.getAdjclose();
			if( lastAdjcloseA>0 )
				logYieldA[i] = formatDoubleSaveFive( Math.log(newAdjloseA/lastAdjcloseA) );
			else
				logYieldA[i] = Integer.MIN_VALUE;
			lastAdjcloseA = newAdjloseA;
			
			newAdjcloseB = ssiB.getAdjclose();
			if( lastAdjcloseB>0 )
				logYieldB[i] = formatDoubleSaveFive( Math.log(newAdjcloseB/lastAdjcloseB) );
			else
				logYieldB[i] = Integer.MIN_VALUE;
			lastAdjcloseB = newAdjcloseB;
		}
		
		ci.setLowA(minA); ci.setLowB(minB);
		ci.setHighA(maxA); ci.setHighB(maxB);
		ci.setCloseA(closeA); ci.setCloseB(closeB);
		ci.setLogYieldA(logYieldA);
		ci.setLogYieldB(logYieldB);
		ci.setLogYieldVarianceA( calVariance(logYieldA) );
		ci.setLogYieldVarianceB( calVariance(logYieldB) );
		ci.setDate(date);
		return ci;
	}

	@Override
	public MarketInfoVO getMarketInfo(Calendar date)
			throws RemoteException, DateInvalidException, BeginInvalidException, EndInvalidException {
		// TODO Auto-generated method stub
		MarketInfoVO mi = new MarketInfoVO();
		
		// 判断日期是否有效
		try{
			ifDateValid(date, date);
		}catch( BeginInvalidException e ){
			throw new BeginInvalidException();
		}catch( EndInvalidException e ){
			throw new EndInvalidException();
		}
		
		// 日期格式化
		Calendar dateTemp = Calendar.getInstance();
		try {
			dateTemp.setTime(sdf.parse( formateCalendar(date) ));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("日期转换出错");
			e.printStackTrace();
		}
					
		List<SingleStockInfoPO> lsti = idi.getMarketByDate( dateTemp );
		// 如果没有数据，抛出日期无效异常
		if( lsti==null )
			throw new DateInvalidException();
		else{
			// 用来暂存市场中单支股票的数据
			SingleStockInfoPO ssi = new SingleStockInfoPO();
			long volume = 0;
			int riseStop = 0, dropStop = 0, riseEFP = 0, stopEFP = 0;
			int OMCEFP = 0, OMCLTFP = 0;
			for( int i=0; i<lsti.size(); i++ ){
				ssi = lsti.get(i);
				double open = ssi.getOpen();
				double close = ssi.getClose();
				double lastClose = ssi.getLast_close();
				double adjclose = ssi.getAdjclose();
				double lastAdjclose = ssi.getLast_adjclose();// 用于保存前一天的复权收盘价
				// 计算总的交易量
				if( ssi.getVolume() > 0 )
					volume += ssi.getVolume();
							
				if( lastAdjclose>0 && adjclose>0 ){
					// 计算涨停的股票数
					if( ifDoubleEqual((ssi.getHigh()-lastAdjclose)/lastAdjclose,  0.10) )
						riseStop++;
					// 计算跌停的股票数
					if( ifDoubleEqual((lastAdjclose-ssi.getLow())/lastAdjclose,  0.10) )
						dropStop++;
					// 计算涨幅超过5%
					if( (adjclose-lastAdjclose)/lastAdjclose > 0.05 
							&& (adjclose-lastAdjclose)/lastAdjclose <= 0.1)
						riseEFP++;
					// 计算跌幅超过5%
					if( (lastAdjclose-adjclose)/lastAdjclose > 0.05 
							&& (lastAdjclose-adjclose)/lastAdjclose <= 0.1)
						stopEFP++;
				}
				if( open>0 && close>0 && lastClose>0 ){
					// 计算某某数据
					if( (open-close) > (0.05*lastClose) )
						OMCEFP++;
					else if( (open-close) < (-0.05*lastClose) )
						OMCLTFP++;
				}
			}
					
			mi.setVolume(volume);
			mi.setNumOfRiseStop(riseStop);
			mi.setNumOfDropStop(dropStop);
			mi.setNumOfRiseEFP(riseEFP);
			mi.setNumOfDropEFP(stopEFP);
			mi.setNumOfOMCEFP(OMCEFP);
			mi.setNumOfOMCLTFP(OMCLTFP);
			
			return mi;
		}
	}

	/**
	 * 
	 * @Description: 格式化数据，小数点后两位
	 * @author: hzp
	 * @time: 2017年3月10日
	 * @return: double
	 */
	private double formatDoubleSaveTwo(double d){
		DecimalFormat df = new DecimalFormat("#0.00");
		return Double.parseDouble( df.format(d) );
	}
	
	/**
	 * 
	 * @Description: 格式化数据，小数点后五位
	 * @author: hzp
	 * @time: 2017年3月13日
	 * @return: double
	 */
	private double formatDoubleSaveFive(double d){
		DecimalFormat df = new DecimalFormat("#0.00000");
		return Double.parseDouble( df.format(d) );
	}
	
	/**
	 * 
	 * @Description: 用来计算一组数据的方差
	 * @author: hzp
	 * @time: 2017年3月10日
	 * @return: double
	 */
	private double calVariance(double[] num){
		int length = num.length;
		if( length==0 )
			return 0.0;
		double result = 0.0;
		double tempD1 = 0.0;
		double tempD2 = 0.0;
		for(int i=0;i<length;i++){
			tempD1 += Math.pow(num[i], 2);
			tempD2 += num[i];
		}
		tempD2 = Math.pow(tempD2, 2);
		result = (tempD1 - tempD2/length) / length;
//		System.out.println(result);
//		double result2 = 0.0;
//		double avg = 0.0;
//		for( int i=0;i<length;i++){
//			avg += num[i];
//		}
//		avg = avg/num.length;
//		for( int i=0;i<length;i++){
//			result2 += Math.pow(num[i]-avg, 2);
//		}
//		result2 /= num.length;
//		System.out.println(result2);
		return formatDoubleSaveFive( result );
	}
	
	/**
	 * 
	 * @Description: 根据传入Calendar，获取其“年月日”，并组装成String返回
	 * @author: hzp
	 * @time: 2017年3月15日
	 * @return: String
	 */
	private String formateCalendar(Calendar cal){
		String month = String.valueOf( cal.get(Calendar.MONTH)+1 );
		String day = String.valueOf( cal.get(Calendar.DAY_OF_MONTH) );
		String year = String.valueOf( cal.get(Calendar.YEAR) );
		return month+"/"+day+"/"+year;
	}
	
	/**
	 * 
	 * @Description: 判断两个double是否相等
	 * @author: hzp
	 * @time: 2017年3月10日
	 * @return: boolean
	 */
	private boolean ifDoubleEqual(double d1, double d2){
		String s1 = String.valueOf(formatDoubleSaveTwo(d1));
		String s2 = String.valueOf(formatDoubleSaveTwo(d2));
		return s1.equals(s2);
	}
	
	/**
	 * 
	 * @Description: 将有效日期向前推到最近的有效日期。“有效日期”即当天不是星期六和星期天。
	 * @author: hzp
	 * @time: 2017年3月10日
	 * @return: Calendar
	 */
	public Calendar calendarAdvance(Calendar cal){
		if( cal.get(Calendar.DAY_OF_WEEK) == 2 )
			cal.add(Calendar.DAY_OF_MONTH, -3); // 星期一
		else if( cal.get(Calendar.DAY_OF_WEEK) == 1 )
			cal.add(Calendar.DAY_OF_MONTH, -2); //星期天
		else
			cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal;
	}
	
	/**
	 * 
	 * @Description: 判断日期是否有效
	 * @author: hzp
	 * @time: 2017年3月15日
	 * @return: boolean
	 * @exception: BeginInvalidException: 起始日期大于最大有效日期
	 * @exception: EndInvalidException: 结束日期小于最小有效日期
	 */
	private boolean ifDateValid(Calendar begin, Calendar end)throws BeginInvalidException, EndInvalidException{
		Calendar head=Calendar.getInstance();
		Calendar tail=Calendar.getInstance();
		try {
			head.setTime(sdf.parse("2/1/05"));
			tail.setTime(sdf.parse("4/29/14"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 根据日期，抛出异常或返回true
		if( begin.compareTo(tail)>0 )
			throw new BeginInvalidException();
		else if( end.compareTo(head)<0 )
			throw new EndInvalidException();
		else
			return true;
	}
}
