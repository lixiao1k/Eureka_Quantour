package data.datahelperimpl_ByDataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DateFormatter;

import com.mysql.cj.jdbc.PreparedStatement;

import data.database.ConnectionPoolManager;
import data.datahelperservice.IExponentDataHelper;
import po.SingleStockInfoPO;
import po.StrategyShowPO;

public class ExponentDataHelperImpl_DBO implements IExponentDataHelper{
	private static ExponentDataHelperImpl_DBO datahelper;
	private ExponentDataHelperImpl_DBO(){
		
	}
	public static ExponentDataHelperImpl_DBO getInstance(){
		if(datahelper==null) datahelper=new ExponentDataHelperImpl_DBO();
		return datahelper;
	}
	public LocalDate getExponentMinDay(String name)
	{
		String code=getcode(name);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select min(date) from quantour.exponent where code = '"+code+"'";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				LocalDate ld=LocalDate.parse(rs.getString(1),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				return ld;
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return null;
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			
		}
		return null;
	}
	
	public LocalDate getExponentMaxDay(String name)
	{
		String code=getcode(name);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select max(date) from quantour.exponent where code = '"+code+"'";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				LocalDate ld=LocalDate.parse(rs.getString(1),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				return ld;
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return null;
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			
		}
		return null;
	}
	
	public List<SingleStockInfoPO> getPeriodExponent(String name,LocalDate start,LocalDate end){
		String code=getcode(name);
		List<SingleStockInfoPO> result=new ArrayList<SingleStockInfoPO>();
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from quantour.exponent where code = '"+code+"' and date>='" 
		+start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +"' and date<='"+end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"'" ;
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				LocalDate ld=LocalDate.parse(rs.getString(2),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				double open=rs.getDouble(3);
				double close=rs.getDouble(4);
				double high=rs.getDouble(5);
				double low=rs.getDouble(6);
				double lclose=rs.getDouble(7);
				double rate=rs.getDouble(8);
				long volume=rs.getLong(9);
				SingleStockInfoPO po=new SingleStockInfoPO(name,code,ld,open,high,low,volume,
						close,0.0,0.0,lclose,0.0,0.0,rate,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
				result.add(po);
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			
		}
		return null;
	}
	private String getcode(String name){
		String sql="select code from stockinfo where binary name ='"+name+"'";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		String result="";
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				result=rs.getString(1);
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return null;
		}
	}
}
