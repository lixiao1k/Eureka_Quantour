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
			ifDateValid(begin, end);
		}catch( BeginInvalidException e ){
			throw new BeginInvalidException();
		}catch( EndInvalidException e ){
			throw new EndInvalidException();
		}
		
		// methods用于存储日均线计算的方式
		int methods[] = { 5, 10, 20, 30, 60 };
		
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
		List<SingleStockInfoPO> lssi = idi.getSingleStockInfo(stockCode, beginTemp, endTemp);
		
		// 如果都没有数据，抛出日期无效异常
		if( lssi==null )
			throw new DateInvalidException();
		else{
			// 用于存放List，并且作为返回变量
			List<List<EMAInfoVO>> llemai = new ArrayList<List<EMAInfoVO>>();
			// 用于暂存取到的每一个Stock数据
			SingleStockInfoPO ssi = new SingleStockInfoPO();
			
			for( int j=0; j<methods.length; j++){
				int method = methods[j];
				int methodTemp = method;
				double close = 0.0;
				List<EMAInfoVO> lemai = new ArrayList<EMAInfoVO>();
	
				double tempDouble = 0.0;
				for( int i=0; i<lssi.size(); i++){
					ssi = lssi.get(i);
					close = ssi.getClose();
					System.out.println(close);
					// 数据丢失
					if( close==0 ){
						methodTemp++;
						continue;
					}
					// 先处理不需要加权平均的数据
					if( i<(methodTemp-1) ){
						lemai.add( new EMAInfoVO( ssi.getDate(), close) );
						tempDouble += close;			
					}
					// 处理需要加权平均的数据
					else{
						tempDouble += close;
						lemai.add( new EMAInfoVO(ssi.getDate(), formatDoubleSaveTwo(tempDouble/method)) );
						tempDouble -= lemai.get(i-method+1).getEMA();
					}
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

		List<SingleStockInfoVO> lstiA = new SingleStockInfoPO().POToVO( idi.getSingleStockInfo(stockCodeA, beginTemp, endTemp) );
		List<SingleStockInfoVO> lstiB = new SingleStockInfoPO().POToVO( idi.getSingleStockInfo(stockCodeB, beginTemp, endTemp) );
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
			if( ssiA.getCode().equals("") && !ifANull)
				ssiA = getSingleStockInfoByTime(stockCodeA, tempCal, tempCal).get(0);
			if( ssiB.getCode().equals("") && !ifBNull)
				ssiB = getSingleStockInfoByTime(stockCodeB, tempCal, tempCal).get(0);
			if( !ssiA.getCode().equals("") && !ssiB.getCode().equals(""))
				break;
		}
		// 用于暂存前一天的收盘价
		double lastCloseA = 0.0, lastCloseB = 0.0;
		lastCloseA = ssiA.getClose();
		lastCloseB = ssiB.getClose();
		
		// 开始存储返回的数据
		int tempInt = lstiA.size(); // 用处初始化VO内部数组大小
		ComparedInfoVO ci = new ComparedInfoVO(tempInt);
		ci.setNameA(lstiA.get(0).getName());
		ci.setNameB(lstiB.get(0).getName());
		ci.setCodeA(stockCodeA);
		ci.setCodeB(stockCodeB);
		// 涨幅为正，跌幅为负
		int iLastClose = 0, iNewClose = tempInt-1;
		double dLastClose =0.0, dNewClose = 0.0;
		// 如果A不为空，一直获取到A的最初一天收盘价 >0
		if( !ifANull ){
			if( lastCloseA==0 ){
				while( lstiA.get(iLastClose).getClose()==0 && iLastClose<iNewClose )
					iLastClose++;
				dLastClose = lstiA.get(iLastClose).getClose();
				lastCloseA = dLastClose;
			}
			else
				dLastClose = lastCloseA;
			if( lastCloseA!=0 ){
				// 一直获取到A的最近的收盘价 >0
				while( lstiA.get(iNewClose).getClose()==0 && iNewClose>iLastClose )
					iNewClose--;
				dNewClose = lstiA.get(iNewClose).getClose();
				double result = ( dNewClose-dLastClose ) / dLastClose;
				ci.setRODA( formatDoubleSaveFive( result ) );
			}
		}
		
		iLastClose = 0; iNewClose = tempInt-1;
		dLastClose =0.0; dNewClose = 0.0;
		// 如果B不为空，一直获取到B的最初一天收盘价 >0
		if( !ifBNull ){
			if( lastCloseB==0 ){
				while( lstiB.get(iLastClose).getClose()==0 && iLastClose<iNewClose )
					iLastClose++;
				dLastClose = lstiB.get(iLastClose).getClose();
				lastCloseB = dLastClose;
			}
			else
				dLastClose = lastCloseB;
			if( lastCloseB!=0 ){
				// 一直获取到B的最近的收盘价 >0
				while( lstiB.get(iNewClose).getClose()==0 && iNewClose>iLastClose )
					iNewClose--;
				dNewClose = lstiB.get(iNewClose).getClose();
				double result = ( dNewClose-dLastClose ) / dLastClose;
				ci.setRODB( formatDoubleSaveFive( result ) );
			}
		}
		
		// 计算最高值、最低值、储存收盘价、计算对数收益率和对数收益率方差
		double maxA = 0, minA = 1000000.0, maxB = 0, minB = 1000000.0;
		double newCloseA = 0.0, newCloseB = 0.0;
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
			newCloseA = ssiA.getClose();
			if( lastCloseA!=0 )
				logYieldA[i] = formatDoubleSaveFive( Math.log(newCloseA/lastCloseA) );
			lastCloseA = newCloseA;
			
			newCloseB = ssiB.getClose();
			if( lastCloseB!=0 )
				logYieldB[i] = formatDoubleSaveFive( Math.log(newCloseB/lastCloseB) );
			lastCloseB = newCloseB;
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
				double close = ssi.getClose();
				double open = ssi.getOpen();
				double lastClose = ssi.getLast_close(); // 用于保存前一天的收盘价
				// 计算总的交易量
				if( ssi.getVolume() > 0 )
					volume += ssi.getVolume();
							
				if( lastClose>0 && close>0 ){
					// 计算涨停的股票数
					if( ifDoubleEqual((ssi.getHigh()-lastClose)/lastClose,  0.10) )
						riseStop++;
					// 计算跌停的股票数
					if( ifDoubleEqual((lastClose-ssi.getLow())/lastClose,  0.10) )
						dropStop++;
					// 计算涨幅超过5%
					if( (close-lastClose)/lastClose > 0.05 )
						riseEFP++;
					// 计算跌幅超过5%
					if( (lastClose-close)/lastClose > 0.05 )
						stopEFP++;
				}
				if( open>0 && close>0 ){
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
	public boolean ifDateValid(Calendar begin, Calendar end)throws BeginInvalidException, EndInvalidException{
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
