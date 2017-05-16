package data.datahelperimpl_ByDataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.PreparedStatement;

import data.common.FileMethod;
import data.database.ConnectionPoolManager;
import data.datahelperservice.IStrategyDataHelper;
import exception.StrategyRepeatException;
import po.CommentPO;
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
		String sql="insert into strategy values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, strategyName);
			pstmt.setString(3, po.getStrategTypeNname());
			pstmt.setBoolean(4, po.isPublicorprivate());
			String sum="";
			if(po.getParameter()==null||po.getParameter().size()==0){
				sum="blank";
			}
			else{
				sum=sum+po.getParameter().get(0);
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
			e.printStackTrace();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			throw new StrategyRepeatException();
		}
	}
	public StrategyInfoPO applyStrategy(String createrName,String strategyName){
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select strategyid from strategy where binary username ='"+createrName+"' and strategyname='"+strategyName+"'";
		PreparedStatement pstmt=null;
		StrategyInfoPO result=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				String type=rs.getString(3);
				boolean pub=rs.getBoolean(4);
				String sharp=rs.getString(5);
				List<Integer> temp=new ArrayList<Integer>();
				if(sharp.equals("blank")){
				}
				else{
					temp.add(Integer.valueOf(sharp));
				}
				int purchase=rs.getInt(6);
				int tiaocang=rs.getInt(7);
				String jiage=rs.getString(8);
				result=new StrategyInfoPO(type,pub, temp, purchase, tiaocang, jiage);
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
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
			e.printStackTrace();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			comment(Username, strategyName, commenterName, time, comment);
		}
	}
	public List<CommentPO> getStrategyComments(String createrName,String strategyName){
		String strategyid=getStrategyid(createrName,strategyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from comments where strategyid='"+strategyid+"' order by commenttime desc";
		PreparedStatement pstmt=null;
		List<CommentPO> result=new ArrayList<CommentPO>();
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				String comments=rs.getString(3);
				String user=rs.getString(4);
				LocalDateTime ldt=LocalDateTime.parse(rs.getString(5),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				result.add(new CommentPO(createrName,strategyName,ldt,user,comments));
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			ConnectionPoolManager.getInstance().close("quantour", conn);
			return null;
		}
	}
	/**
	 * 获取策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public StrategyShowPO getStrategy ( String createrName, String StrategyName ){
		String id=getStrategyid(createrName,StrategyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from strategyshow where strategyid='"+id+"'";
		PreparedStatement pstmt=null;
		StrategyShowPO result=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				double alpha=rs.getDouble(2);
				double beta=rs.getDouble(3);
				double sharp=rs.getDouble(4);
				double huiche=rs.getDouble(5);
				double yearreturn=rs.getDouble(6);
				int length=rs.getInt(7);
				result=new StrategyShowPO(createrName,StrategyName,alpha,beta,sharp,huiche,yearreturn,length);
			}
			rs.close();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		if(result!=null){
			conn=ConnectionPoolManager.getInstance().getConnection("quantour");
			sql="select * from strategychart where strategyid='"+id+"'";
			pstmt=null;
			try {
				pstmt = (PreparedStatement)conn.prepareStatement(sql);
				ResultSet rs=pstmt.executeQuery();
				while(rs.next()){
					int index=rs.getInt(2);
					LocalDate alpha=LocalDate.parse(rs.getString(3),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					double br=rs.getDouble(4);
					double sr=rs.getDouble(5);
					result.add(br, sr, alpha, index);
				}
				rs.close();
				pstmt.close();
				ConnectionPoolManager.getInstance().close("quantour", conn);
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 添加策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void addStrategyShow ( String createrName, String StrategyName ,StrategyShowPO vo){
		String id=getStrategyid(createrName,StrategyName);
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="insert into strategyshow values(?,?,?,?,?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setDouble(2, vo.getAlpha());
			pstmt.setDouble(3, vo.getBeta());
			pstmt.setDouble(4, vo.getSharp());
			pstmt.setDouble(5, vo.getStrategyYearReturn());
			pstmt.setDouble(6, vo.getZuidahuiche());
			pstmt.setDouble(7, vo.getBasicReturn().size());
			pstmt.executeUpdate();
			pstmt.close();
			ConnectionPoolManager.getInstance().close("quantour", conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		sql="insert into strategychart values(?,?,?,?,?)";
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<vo.getBasicReturn().size();i++){
			try {
				pstmt.setString(1, id);
				pstmt.setDouble(2, i);
				pstmt.setString(3, vo.getTimeList().get(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				pstmt.setDouble(4, vo.getBasicReturn().get(i));
				pstmt.setDouble(5, vo.getStrategyReturn().get(i));
				pstmt.addBatch();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			pstmt.executeBatch();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionPoolManager.getInstance().close("quantour", conn);
	}
	/**
	 * 更新策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void clearStrategyShow (){
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql=" TRUNCATE TABLE strategyshow";
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
		sql=" TRUNCATE TABLE strategychart";
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
	 * 获取某个用户的所有策略
	 * @param createrName 用户名
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( String createrName){
		List<StrategyShowPO> result=new ArrayList<StrategyShowPO>();
		Connection conn=ConnectionPoolManager.getInstance().getConnection("quantour");
		String sql="select * from quantour.strategyshow A ,quantour.strategy B where A.strategyid=B.strategyid and username = '"+createrName+"'";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				double alpha=rs.getDouble(2);
				double beta=rs.getDouble(3);
				double sharp=rs.getDouble(4);
				double huiche=rs.getDouble(5);
				double yearreturn=rs.getDouble(6);
				int length=rs.getInt(7);
				String name=rs.getString("username");
				String strategyname=rs.getString("strategyname");
				result.add(new StrategyShowPO(name,strategyname,alpha,beta,sharp,huiche,yearreturn,length));
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
		String sql="select * from quantour.strategyshow A ,quantour.strategy B where A.strategyid=B.strategyid and pubOrpri = '1'";
		PreparedStatement pstmt=null;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				double alpha=rs.getDouble(2);
				double beta=rs.getDouble(3);
				double sharp=rs.getDouble(4);
				double huiche=rs.getDouble(5);
				double yearreturn=rs.getDouble(6);
				int length=rs.getInt(7);
				String name=rs.getString("username");
				String strategyname=rs.getString("strategyname");
				result.add(new StrategyShowPO(name,strategyname,alpha,beta,sharp,huiche,yearreturn,length));
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
