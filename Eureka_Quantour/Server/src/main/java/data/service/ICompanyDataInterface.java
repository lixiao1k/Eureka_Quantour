package data.service;

import java.time.LocalDate;
import java.util.List;

import exception.NullStockIDException;
import po.CompanyInfoPO;

public interface ICompanyDataInterface {
	/**
	 * 获取某只股票最近的股本结构和基本收益变化
	 * @param time
	 * @param code
	 * @return
	 * @throws NullStockIDException 
	 */
	public CompanyInfoPO getLatestCommpanyInfo(LocalDate time,String code) throws NullStockIDException;
	/**
	 * 获取某只股票所有的股本结构和基本收益变化情况
	 * @param code
	 * @return
	 * @throws NullStockIDException 
	 */
	public List<CompanyInfoPO> getCompanyDetail(String code) throws NullStockIDException;
}
