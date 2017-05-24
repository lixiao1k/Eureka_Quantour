package data.fetchpool;

public class FetchConnection {
	private String url;
	private String type;
	private String targetpath;
	private int index;
	private boolean status;
	private String code;
	private String url2;
	public FetchConnection(String url, String type, String targetpath, int index, boolean status, String code,
			boolean isA,String url2) {
		super();
		this.url = url;
		this.type = type;
		this.targetpath = targetpath;
		this.index = index;
		this.status = status;
		this.code = code;
		this.isA = isA;
		this.url2=url2;
	}
	private boolean isA;
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
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the isA
	 */
	public boolean isA() {
		return isA;
	}
	/**
	 * @param isA the isA to set
	 */
	public void setA(boolean isA) {
		this.isA = isA;
	}
	/**
	 * @return the url2
	 */
	public String getUrl2() {
		return url2;
	}
	/**
	 * @param url2 the url2 to set
	 */
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
}
