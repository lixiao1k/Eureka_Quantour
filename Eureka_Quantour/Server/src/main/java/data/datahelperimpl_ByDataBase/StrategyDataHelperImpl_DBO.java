package data.datahelperimpl_ByDataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.PreparedStatement;

import data.common.FileMethod;
import data.database.ConnectionPoolManager;
import data.datahelperservice.IStrategyDataHelper;
import exception.StrategyRepeatException;
import po.StrategyInfoPO;
import po.StrategyShowPO;
import vo.StrategyShowVO;

public class StrategyDataHelperImpl_DBO implements IStrategyDataHelper{
	private static IStrategyDataHelper datahelper;
	private StrategyDataHelperImpl_DBO(){
	}
	public static IStrategyDataHelper getInstance(){
		if(datahelper==null) datahelper=new StrategyDataHelperImpl_DBO();
		return datahelper;
	}
	@Override
	public void saveStrategy(StrategyInfoPO po,String strategyName,String username) throws StrategyRepeatException{
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="insert into strategy values(?,?,?,?,?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, strategyName);
			pstmt.setString(3, po.getStrategTypeNname());
			pstmt.setBoolean(4, po.isPublicorprivate());
			String sum="";
			for(int i=0;i<po.getParameter().size();i++){
				sum=sum+po.getParameter().get(i);
			}
			pstmt.setString(5, sum);
			pstmt.setInt(6, po.getPurchasenum());
			pstmt.setInt(7, po.getTiaocangqi());
			pstmt.setString(8, po.getTiaocangjiage());
			pstmt.setString(9, FileMethod.getInstance().getUUID());
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			throw new StrategyRepeatException();
		}
	}
	/**
	 * 删除策略
	 * @param createName 创建者名字
	 * @param strategyName 策略名字
	 */
	public void deleteStrategy ( String createName, String strategyName){
		String strategyid=getStrategyid(createName,strategyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="delete from strategy where binary username='"+createName+"' and binary strategyName='"+strategyName+"'";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		sql="delete from comments where strategyid='"+strategyid+"'";
		pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		sql="delete from comments where strategyid='"+strategyid+"'";
		pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		sql="delete from strategyshow where strategyid='"+strategyid+"'";
		pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 评价策略
	 * @param Username 创建者名字
	 * @param strategyName 策略名字
	 * @param commenterName 评价者名字
	 * @param time 评价时间
	 * @param comment 评价内容
	 */
	public void comment(String Username, String strategyName, String commenterName, LocalDateTime time, String comment){
		String strategyid=getStrategyid(Username,strategyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="insert into comments values(?,?,?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setString(1, strategyid);
			pstmt.setString(2, FileMethod.getInstance().getUUID());
			pstmt.setString(3, comment);
			pstmt.setString(4, commenterName);
			pstmt.setString(5, time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			ConnectionPoolManager.getInstance().close("quantour", conn);
			comment(Username, strategyName, commenterName, time, comment);
		}
	}

	/**
	 * 获取策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public StrategyShowPO getStrategy ( String createrName, String StrategyName ){
		StrategyShowPO result=new StrategyShowPO();
		String id=getStrategyid(createrName,StrategyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from strategyshow where strategyid='"+id+"'";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				rs.getString(2);
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
	
	/**
	 * 添加策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void addStrategyShow ( String createrName, String StrategyName ,StrategyShowPO vo){
		StrategyShowVO result=new StrategyShowVO();
		String id=getStrategyid(createrName,StrategyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="insert into strategyshow values(?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, "");
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 更新策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void updateStrategyShow ( String createrName, String StrategyName ,StrategyShowPO vo){
		StrategyShowVO result=new StrategyShowVO();
		String id=getStrategyid(createrName,StrategyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="update strategyshow set info ='' where strategyid ='"+id+"'";
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

	/**
	 * 获取某个用户的所有策略
	 * @param createrName 用户名
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( String createrName){
		List<StrategyShowPO> result=new ArrayList<StrategyShowPO>();
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from strategyshow where strategyid in (select strategyid from strategy where username = '"+createrName+"')";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				rs.getString(2);
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

	/**
	 * 获取所有公开的策略
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( ){
		List<StrategyShowPO> result=new ArrayList<StrategyShowPO>();
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from strategyshow where strategyid in (select strategyid from strategy where pubOrpri = 1)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				rs.getString(2);
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

	/**
	 * 修改策略是否公开
	 * @param creatroName 创建者名字
	 * @param straetgyName 策略名字
	 * @param property 是否公开
	 */
	public void setPublic(String creatroName, String straetgyName,boolean property){
		String strategyid=getStrategyid(creatroName,straetgyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="update strategy set pubOrpri='"+property+"' where strategyid ='"+strategyid+"'";
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
	private String getStrategyid(String username,String strategyname){
		String sql="select strategyid from strategy where binary username ='"+username+"' and strategyname='"+strategyname+"'";
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
