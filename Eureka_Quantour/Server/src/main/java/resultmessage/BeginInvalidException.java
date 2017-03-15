package resultmessage;

public class BeginInvalidException extends Exception{

	private static final long serialVersionUID = -9005361763463590019L;

	public BeginInvalidException(){
		super();
	}
	
	public String toString(){
		return "开始日期过大";
	}
}
