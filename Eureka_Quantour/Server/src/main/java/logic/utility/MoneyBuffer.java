package logic.utility;

public class MoneyBuffer {
	private int stock_number;
	private int init;
	private Node head;
	private Node tail;
	public MoneyBuffer(int stock_number, int init) {
		super();
		this.stock_number = stock_number;
		this.init = init;
		Node p=null;
		for(int i=0;i<stock_number;i++)
		{
			Node node=new Node(100.0,0.0,0.0,0.0,null);
			if(i==0)
			{
				head=node;
				p=node;
			}
			else
			{
				p.next=node;
				p=node;
			}
		}
		tail=p;
	}
	public void add(double before,double now,double buy_now,double after)
	{
			double temp_before=tail.before;
			double temp_now=tail.now;
			double rate=now/before*10000;
			double temprate=temp_now/temp_before*10000;
			if(rate>temprate)
			{
				changebuf(before,now,buy_now,after);
			}
	}
	private void changebuf(double before,double now,double buy_now,double after)
	{
		double rate=now/before*10000;
		Node p=head;
		boolean trans=false;
		double trans_before=0.0;
		double trans_now=0.0;
		double trans_buynow=0.0;
		double trans_after=0.0;
		for(int i=0;i<stock_number;i++)
		{

			if(trans)
			{
				double temp_now=p.now;
				double temp_before=p.before;
				double temp_buynow=p.buy_now;
				double temp_after=p.after;
				p.now=trans_now;
				p.before=trans_before;
				p.buy_now=trans_buynow;
				p.after=trans_after;
				if(temp_after==0.0)
				{
					break;
				}
				trans_before=temp_before;
				trans_now=temp_now;
				trans_after=temp_after;
				trans_buynow=temp_buynow;
				p=p.next;
			}
			else
			{
				double temp_rate=p.now/p.before*10000;
				if(rate>temp_rate)
				{
					trans=true;
					trans_before=p.before;
					trans_now=p.now;
					trans_after=p.after;
					trans_buynow=p.buy_now;
					p.now=now;
					p.before=before;
					p.after=after;
					p.buy_now=buy_now;
					p=p.next;
				}
				else
				{
					p=p.next;
				}
			}
		}
	}
	public void clear()
	{
		Node p=head;
			for(int i=0;i<stock_number;i++)
			{
				p.before=100.0;
				p.now=0.0;
				p.after=0.0;
				p=p.next;
			}
	}
	public int getStock_number() {
		return stock_number;
	}
	public void setStock_number(int stock_number) {
		this.stock_number = stock_number;
	}
	public double getInit() {
		return init;
	}
	public void setInit(int init) {
		this.init = init;
	}
	public double getRate()
	{
		Node p=head;
		double temp1=0.0;
		double temp2=0.0;
		//System.out.println("**********************************************");
		for(int i=0;i<stock_number;i++)
		{
			if(p.buy_now==0)
			{
				break;
			}
			else
			{
//				System.out.println("----------------------");
//				System.out.println(p.now);
//				System.out.println(p.before);
//				System.out.println((p.now-p.before)/p.before);
//				System.out.println("-----------------------");
				temp1=temp1+p.buy_now;
				temp2=temp2+p.after;
			}
			p=p.next;
		}
		//System.out.println("**********************************************");
		double rate=temp2/temp1;
		return rate;
	}
}
class Node
{
	double before;
	double now;
	double buy_now;
	double after;
	public Node(double before, double now,double buy_now, double after, Node next) {
		super();
		this.before = before;
		this.now = now;
		this.buy_now=buy_now;
		this.after = after;
		this.next = next;
	}
	Node next;
}