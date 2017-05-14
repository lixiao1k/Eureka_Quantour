package data.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import data.datahelperimpl_ByDataBase.StrategyDataHelperImpl_DBO;
import data.datahelperservice.IStrategyDataHelper;
import exception.StrategyRepeatException;
import po.StrategyInfoPO;
import po.StrategyShowPO;
import vo.StrategyShowVO;

public class StrategyDataController {
	private static StrategyDataController strategydata;
	private IStrategyDataHelper strategydatahelper;
	private StrategyDataController(){
		strategydatahelper=StrategyDataHelperImpl_DBO.getInstance();
	}
	public static StrategyDataController getInstance(){
		if(strategydata==null) strategydata=new StrategyDataController();
		return strategydata;
	}
	/**
	 * 存储策略
	 * @param po 一个策略的po
	 * @throws StrategyRepeatException 
	 */
	public void saveStrategy(StrategyInfoPO po,String strategyName,String username) throws StrategyRepeatException{
		strategydatahelper.saveStrategy(po,strategyName,username);
	}
	/**
	 * 删除策略
	 * @param createName 创建者名字
	 * @param strategyName 策略名字
	 */
	public void deleteStrategy ( String createName, String strategyName){
		return ;
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
		strategydatahelper.comment(Username, strategyName, commenterName, time, comment);
	}

	/**
	 * 获取策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public StrategyShowPO getStrategy ( String createrName, String StrategyName ){
		return null;
	}
	
	/**
	 * 添加策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void addStrategyShow ( String createrName, String StrategyName ,StrategyShowPO vo){
		return ;
	}
	/**
	 * 更新策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void updateStrategyShow ( String createrName, String StrategyName ,StrategyShowPO vo){
		return ;
	}

	/**
	 * 获取某个用户的所有策略
	 * @param createrName 用户名
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( String createrName){
		return null;
	}

	/**
	 * 获取所有公开的策略
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( ){
		return null;
	}

	/**
	 * 修改策略是否公开
	 * @param creatroName 创建者名字
	 * @param straetgyName 策略名字
	 * @param property 是否公开
	 */
	public void setPublic(String creatroName, String straetgyName,boolean property){
		
	}
}
