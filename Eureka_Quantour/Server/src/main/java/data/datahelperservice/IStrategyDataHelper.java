package data.datahelperservice;

import java.time.LocalDateTime;
import java.util.List;

import exception.StrategyRepeatException;
import po.CommentPO;
import po.StrategyInfoPO;
import po.StrategyShowPO;
import vo.StrategyShowVO;

public interface IStrategyDataHelper {
	/**
	 * 存储策略
	 * @param po 一个策略的po
	 * @throws StrategyRepeatException 
	 */
	public void saveStrategy(StrategyInfoPO po,String strategyName,String username) throws StrategyRepeatException;
	/**
	 * 删除策略
	 * @param createName 创建者名字
	 * @param strategyName 策略名字
	 */
	public void deleteStrategy ( String createName, String strategyName);

	/**
	 * 评价策略
	 * @param Username 创建者名字
	 * @param strategyName 策略名字
	 * @param commenterName 评价者名字
	 * @param time 评价时间
	 * @param comment 评价内容
	 */
	public void comment(String Username, String strategyName, String commenterName, LocalDateTime time, String comment);

	/**
	 * 获取策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public StrategyShowPO getStrategy ( String createrName, String StrategyName );
	
	/**
	 * 获取策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void addStrategyShow ( String createrName, String StrategyName ,StrategyShowPO vo);

	/**
	 * 获取某个用户的所有策略
	 * @param createrName 用户名
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( String createrName);

	/**
	 * 获取所有公开的策略
	 * @return 策略列表
	 */
	public List<StrategyShowPO> getStrategyList ( );
	/**
	 * 更新策略详细图标
	 * @param createrName 创建者名字
	 * @param StrategyName 策略名字
	 * @return 策略显示的po
	 */
	public void clearStrategyShow ();
	public List<CommentPO> getStrategyComments(String createrName,String strategyName);
	public StrategyInfoPO applyStrategy(String createrName,String strategyName);

	/**
	 * 修改策略是否公开
	 * @param creatroName 创建者名字
	 * @param straetgyName 策略名字
	 * @param property 是否公开
	 */
	public void setPublic(String creatroName, String straetgyName,boolean property);
}
