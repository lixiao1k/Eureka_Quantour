package data.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import data.datahelperimpl_ByDataBase.CompanyDataHelperImpl_DBO;
import data.datahelperimpl_ByDataBase.StrategyDataHelperImpl_DBO;
import data.datahelperservice.ICompanyDataHelper;
import data.datahelperservice.IStrategyDataHelper;
import data.parse.Parse;
import data.parse.Translate;
import data.service.ICompanyDataInterface;
import exception.NullStockIDException;
import exception.StrategyRepeatException;
import po.CompanyInfoPO;
import po.StrategyInfoPO;

public class CompanyDataController implements ICompanyDataInterface{
	private static CompanyDataController strategydata;
	private ICompanyDataHelper datahelper;
	private CompanyDataController(){
		datahelper=CompanyDataHelperImpl_DBO.getInstance();
	}
	public static CompanyDataController getInstance(){
		if(strategydata==null) strategydata=new CompanyDataController();
		return strategydata;
	}
	public CompanyInfoPO getLatestCommpanyInfo(LocalDate time,String code) throws NullStockIDException{
		int c=transStockCode(code);
		return datahelper.getLatestCommpanyInfo(time, Parse.getInstance().supCode(c));
	}
	public List<CompanyInfoPO> getCompanyDetail(String code) throws NullStockIDException{
		int c=transStockCode(code);
		return datahelper.getCompanyDetail(Parse.getInstance().supCode(c));
	}
	private int transStockCode(String stockCode) throws NullStockIDException{
		int code;
		String strCode;
		try{
			code=Integer.parseInt(stockCode);
			if(!Translate.getInstance().containsCode(Parse.getInstance().supCode(stockCode))){
				throw new NullStockIDException(stockCode);
			}
			else{
				return code;
			}
		}catch(NumberFormatException e1){
			try{
				strCode=Translate.getInstance().trans_nameTocode(stockCode);
				code=Integer.parseInt(strCode);
				return code;
			}catch(NumberFormatException e2){
				throw new NullStockIDException(stockCode);
			}
		}
	}
}
