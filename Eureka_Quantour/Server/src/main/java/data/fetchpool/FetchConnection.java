package data.fetchpool;

public class FetchConnection {
	private String url;
	private String type;
	private String targetpath;
	private int index;
	private boolean status;
	public FetchConnection(String url,String type,String targetpath,int index,boolean status){
		setUrl(url);
		setType(type);
		setTargetpath(targetpath);
		setIndex(index);
		setStatus(status);
	}
	public FetchConnection(FetchConnection fc){
		setUrl(fc.getUrl());
		setType(fc.getType());
		setTargetpath(fc.getTargetpath());
		setIndex(fc.getIndex());
		setStatus(fc.isStatus());
	}
	public void setFetchConnection(FetchConnection fc){
		setUrl(fc.getUrl());
		setType(fc.getType());
		setTargetpath(fc.getTargetpath());
		setIndex(fc.getIndex());
		setStatus(fc.isStatus());
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the targetpath
	 */
	public String getTargetpath() {
		return targetpath;
	}
	/**
	 * @param targetpath the targetpath to set
	 */
	public void setTargetpath(String targetpath) {
		this.targetpath = targetpath;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
}
