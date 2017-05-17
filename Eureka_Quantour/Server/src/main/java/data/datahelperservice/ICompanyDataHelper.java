package data.datahelperservice;

import java.time.LocalDate;
import java.util.List;

import po.CompanyInfoPO;

public interface ICompanyDataHelper {
	public CompanyInfoPO getLatestCommpanyInfo(LocalDate time,String code);
	public List<CompanyInfoPO> getCompanyDetail(String code);
}
