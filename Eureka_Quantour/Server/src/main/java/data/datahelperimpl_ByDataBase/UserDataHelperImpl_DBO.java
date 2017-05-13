package data.datahelperimpl_ByDataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.cj.jdbc.PreparedStatement;

import data.common.FileMethod;
import data.database.ConnectionPoolManager;
import data.database.DataBaseOperation;
import data.datahelperservice.IUserDataHelper;
import exception.LogErrorException;
import exception.UserNameRepeatException;

public class UserDataHelperImpl_DBO implements IUserDataHelper{
	private static IUserDataHelper datahelper;
	private UserDataHelperImpl_DBO(){
	}
	public static IUserDataHelper getInstance(){
		if(datahelper==null) datahelper=new UserDataHelperImpl_DBO();
		return datahelper;
	}
	@Override
	public void containName(String username) throws UserNameRepeatException {
		if(username.equals("")){
			throw new UserNameRepeatException("用户名错误");
		}
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select username from user where binary username='"+username+"'";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				throw new UserNameRepeatException("用户名重复");
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void login(String username, String password) throws LogErrorException {
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select password from user where binary username='"+username+"'";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(!rs.next()){
				throw new LogErrorException();
			}
			String pw=rs.getString(1);
			if(!pw.equals(password)){
				throw new LogErrorException();
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void insertUser(String username, String password) {
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="insert into user values(?,?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setBoolean(3, false);
			pstmt.setString(4, FileMethod.getInstance().getUUID());
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			insertUser(username,password);
		}
	}
	@Override
	public void logout(String username) {
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="update user set status=0 WHERE binary username='"+username+"'";
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
}
