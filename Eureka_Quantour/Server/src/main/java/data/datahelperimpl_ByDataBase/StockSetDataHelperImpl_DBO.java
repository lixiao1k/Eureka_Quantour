package data.datahelperimpl_ByDataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.PreparedStatement;

import data.common.FileMethod;
import data.database.ConnectionPoolManager;
import data.datahelperservice.IStockSetDataHelper;
import exception.NullSetException;
import exception.StockNameRepeatException;
import exception.StockSetNameRepeatException;

public class StockSetDataHelperImpl_DBO implements IStockSetDataHelper {
	private static IStockSetDataHelper datahelper;
	private StockSetDataHelperImpl_DBO(){
	}
	public static IStockSetDataHelper getInstance(){
		if(datahelper==null) datahelper=new StockSetDataHelperImpl_DBO();
		return datahelper;
	}
	@Override
	public List<String> getStockSet(String username) {
		String userid=getuserid(username);
		String sql="select setname from userset where userid='"+userid+"'";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		List<String> result=new ArrayList<String>();
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
	}
	@Override
	public void addStockSet(String stockSetName, String username) throws StockSetNameRepeatException {
		String userid=getuserid(username);
		if(isExist(userid, stockSetName)){
			throw new StockSetNameRepeatException();
		}
		String sql="insert into userset values(?,?,?)";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		ConnectionPoolManager.getInstance().close("quantour", conn);
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, stockSetName);
			pstmt.setString(3, FileMethod.getInstance().getUUID());
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			addStockSet(stockSetName,username);
		}
	}

	@Override
	public void deleteStockSet(String stockSetName, String username) {
		String userid=getuserid(username);
		String setid=getsetid(userid,stockSetName);
		String sql="delete from userset where userid='"+userid+"' and setname='"+stockSetName+"'";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		String sql1="delete from stockset where setid='"+setid+"'";
		conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt1=null;
		try {
			pstmt1 = (PreparedStatement)conn.prepareStatement(sql1);
			pstmt1.executeUpdate();
			pstmt1.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addStockToStockSet(String stockName, String stockSetName, String username)
			throws StockNameRepeatException {
		// TODO Auto-generated method stub
		String userid=getuserid(username);
		String setid=getsetid(userid,stockSetName);
		if(isStockExist(setid,stockName)){
			throw new StockNameRepeatException();
		}
		System.out.println(stockName+":"+stockSetName);
		String sql="insert into stockset values(?,?)";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setString(1, setid);
			pstmt.setString(2, stockName);
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void deleteStockFromStockSet(String stockName, String stockSetName, String username) {
		String userid=getuserid(username);
		String setid=getsetid(userid,stockSetName);
		String sql="delete from stockset where setid='"+setid+"' and code='"+stockName+"'";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getStockSetInfo(String stockSetName, String userName) throws NullSetException {
		String userid=getuserid(userName);
		String setid=getsetid(userid,stockSetName);
		String sql="select code from stockset where setid='"+setid+"'";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		List<String> result=new ArrayList<String>();
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getStockSetInfo(String stockSetName) throws NullSetException {
		String userid=getuserid("system");
		String setid=getsetid(userid,stockSetName);
		String sql="select code from stockset where setid='"+setid+"'";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		List<String> result=new ArrayList<String>();
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getBKList(String bk) {
		String sql="select setname from userset where setid in (select setid from stocksection where sectionname='"+bk+"')";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		List<String> result=new ArrayList<String>();
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	private String getuserid(String username){
		String sql="select userid from user where binary username ='"+username+"'";
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
	private String getsetid(String userid,String setname){
		String sql="select setid from userset where binary setname ='"+setname+"' and userid='"+userid+"'";
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
	private boolean isExist(String userid,String setname){
		String sql="select setid from userset where binary setname ='"+setname+"' and userid='"+userid+"'";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		boolean result=false;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				result= true;
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return false;
		}
	}
	private boolean isStockExist(String setid,String stockname){
		String sql="select setid from stockset where setid ='"+setid+"' and code='"+stockname+"'";
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		PreparedStatement pstmt=null;
		boolean result=false;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				result= true;
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return false;
		}
	}
}
