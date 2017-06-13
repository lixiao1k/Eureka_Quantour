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
		Node q=null;
		for(int i=0;i<stock_number;i++)
		{
			Node node=new Node(100.0,0.0,0.0,0.0,null,null);
			if(i==0)
			{
				head=node;
				p=node;
			}
			else
			{
				p.next=node;
				node.previous=p;
				p=node;
			}
		}
		tail=p;
	}
	public void add(double before,double now,double buy_now,double after)
	{
			double rate=now/before*10000;
			double temprate=tail.rate_compare;
			if(rate>temprate)
			{
				changebuf(before,now,buy_now,after);
			}
	}
	private void changebuf(double before,double now,double buy_now,double after)
	{
		Node temp=new Node(before,now,buy_now,after,null,null);
		double rate=temp.rate_compare;
		Node p=head;
		Node q=head;
		for(int i=0;i<stock_number;i++)
		{
			if(p.rate_compare<rate)
			{
				if(i==0)
				{
					temp.next=head;
					head.previous=temp;
					head=temp;
				}
				else
				{
					q.next=temp;
					temp.next=p;
					p.previous=temp;
					temp.previous=q;
				}
				break;
			}
			else
			{
				q=p;
				p=p.next;
			}
		}
		tail=tail.previous;
		if(tail==null)
		{
			System.out.println("wrong");
			System.exit(0);
		}
		tail.next=null;
	}
	public void clear()
	{
		Node p=head;
			for(int i=0;i<stock_number;i++)
			{
				p.before=100.0;
				p.now=0.0;
				p.after=0.0;
				p.rate_buy=0.0;
				p.rate_compare=0.0;
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
//		double temp=0.0;
//		int count=0;
//		System.out.println("**********************************************");
		for(int i=0;i<stock_number;i++)
		{
			if(p.buy_now==0||p.after==0)
			{
				break;
			}
			else
			{
//				System.out.println("----------------------");
//				System.out.println(p.now);
//				System.out.println(p.after);
//				System.out.println((p.after-p.now)/p.now);
//				System.out.println("-----------------------");
				temp1=temp1+p.buy_now;
				temp2=temp2+p.after;
//				temp=temp+p.rate_buy;
//				count++;
			}
			p=p.next;
		}
//		System.out.println(temp2/temp1);
//		System.out.println("**********************************************");
		double rate=temp2/temp1;
		if(temp1==0)
		{
			return 1;
		}
		else
		{
			return rate;
		}
//		double rate=temp/count/10000;
	}
}
class Node
{
	double rate_compare;
	double rate_buy;
	double before;
	double now;
	double buy_now;
	double after;
	Node next;
	Node previous;
	public Node(double before, double now,double buy_now, double after, Node next,Node previous) {
		super();
		this.before = before;
		this.now = now;
		this.buy_now=buy_now;
		this.after = after;
		this.next = next;
		this.previous=previous;
		rate_compare=this.now/this.before*10000;
		rate_buy=this.after/this.buy_now*10000;
	}

}