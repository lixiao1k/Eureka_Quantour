package po;

import java.time.LocalDateTime;

public class CommentPO {
	private String createrName;
	private String strategyName;
	private LocalDateTime time;
	private String commentUserName;
	private String comments;
	public CommentPO(String createrName, String strategyName, LocalDateTime time, String commentUserName,
			String comments) {
		super();
		this.createrName = createrName;
		this.strategyName = strategyName;
		this.time = time;
		this.commentUserName = commentUserName;
		this.comments = comments;
	}
	/**
	 * @return the createrName
	 */
	public String getCreaterName() {
		return createrName;
	}
	/**
	 * @param createrName the createrName to set
	 */
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	/**
	 * @return the strategyName
	 */
	public String getStrategyName() {
		return strategyName;
	}
	/**
	 * @param strategyName the strategyName to set
	 */
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	/**
	 * @return the time
	 */
	public LocalDateTime getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	/**
	 * @return the commentUserName
	 */
	public String getCommentUserName() {
		return commentUserName;
	}
	/**
	 * @param commentUserName the commentUserName to set
	 */
	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
}
