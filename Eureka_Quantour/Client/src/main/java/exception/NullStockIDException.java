package exception;
/**
 * 不存在该股票时抛出该异常
 * @author 刘宇翔
 *
 */
public class NullStockIDException extends Exception{
	private String stock;//股票信息，编号或是名字，如1
	/**
	 * 
	 */
	private static final long serialVersionUID = 1524994497291118046L;
	
	public NullStockIDException(String _stock){
		super();
		this.stock=_stock;
	}
	
	public String toString(){
		return "搜索不到股票"+stock+"的信息";
	}
}
