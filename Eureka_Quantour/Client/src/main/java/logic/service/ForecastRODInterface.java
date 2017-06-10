package logic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

import vo.KaFangVO;
import vo.PredictVO;

public interface ForecastRODInterface extends Remote {
	
	/**
	 * 
	 * @Description: 预测明天股票价格
	 * @author: hzp
	 * @date: May 14, 2017
	 * @param: stockcode : the code of stock
	 * @param: date : the newest time of stock
	 * @return: PredictVO
	 */
	public PredictVO predict( String stockcode, LocalDate date ) throws RemoteException;

	/**
	 * @Description: 判断默认股票池市场 date 日涨跌幅是否符合正态分布
	 * @author: hzp
	 * @date: 2017年6月10日
	 * @param stockSetName
	 * @param date
	 */
	public KaFangVO isNormalDistribution( String stockSetName, LocalDate date ) throws RemoteException;
	
	/**
	 * @Description: 判断用户自定义股票池市场 date 日涨跌幅是否符合正态分布
	 * @author: hzp
	 * @date: 2017年6月10日
	 * @param stockSetName
	 * @param date
	 */
	public KaFangVO isNormalDistribution( String stockSetName, String userName, LocalDate date ) throws RemoteException;
}

