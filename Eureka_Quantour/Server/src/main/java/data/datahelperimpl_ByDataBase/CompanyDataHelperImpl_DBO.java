package data.datahelperimpl_ByDataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.mysql.cj.jdbc.PreparedStatement;

import data.database.ConnectionPoolManager;
import data.datahelperservice.ICompanyDataHelper;
import po.CompanyInfoPO;
import po.SingleStockInfoPO;

public class CompanyDataHelperImpl_DBO implements ICompanyDataHelper{
	private static CompanyDataHelperImpl_DBO datahelper;
	private CompanyDataHelperImpl_DBO(){
		
	}
	public static CompanyDataHelperImpl_DBO getInstance(){
		if(datahelper==null) datahelper=new CompanyDataHelperImpl_DBO();
		return datahelper;
	}
	@Override
	public CompanyInfoPO getLatestCommpanyInfo(LocalDate time, String code) {
		double basicincome=0.0;
		double netasset=0.0;
		long totalcapital=0;
		long flucapital=0;
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="SELECT * FROM companyquota where code ='"+code+"'and date <= '"+time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"' order by date desc";
		PreparedStatement pstmt=null;
		try {
			System.out.println(sql);
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			int flag=1;
			while(rs.next()){
				basicincome=rs.getDouble(3);
				if(flag==1)
				{
					netasset=rs.getDouble(4);
					flag=0;
				}
				if(basicincome!=0.0)
				{
					break;
				}
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			sql="SELECT * FROM companycaptial where code ='"+code+"'and date <= '"+time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"' order by date desc limit 1";
			conn=ConnectionPoolManager.getInstance().getConnection("quantour");
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()){
				totalcapital=rs.getLong(3);
				flucapital=rs.getLong(4);
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return new CompanyInfoPO(code, time, basicincome, netasset, totalcapital, flucapital);
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return null;
		}
	}
	public List<CompanyInfoPO> getCompanyDetail(String code){
		return null;
	}
}
