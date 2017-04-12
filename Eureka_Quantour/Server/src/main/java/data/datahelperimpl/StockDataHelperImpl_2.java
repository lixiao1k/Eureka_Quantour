package data.datahelperimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Scanner;

import data.common.DateTrie;
import data.common.IndexTree;
import data.common.StockHashTree;
import data.common.StockLeaf;
import data.common.DateLeaf;
import data.datahelperservice.IStockDataHelper_2;
import data.fetchdataimpl.StockDataFetchImpl;
import data.fetchdataservice.IStockDataFetch;
import data.parse.Parse;
import exception.InternetdisconnectException;
import exception.NullDateException;
import exception.StockHaltingException;
/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public class StockDataHelperImpl_2 implements IStockDataHelper_2{
	private File infopath;//数据的地址
	private IStockDataFetch fetch;//爬取数据的接口
	private MappedByteBuffer mbb_data;//全部数据的内存映射
	private MappedByteBuffer mbb_position;//全部索引位置的内存映射
	private Parse parse;//一些数据转换的方法
	private byte[] receive;//接受数据的容器（一次一行）
	private byte[] doublereceive;//接受数据的容器（一次两行）
	
	private DateTrie datetree;//以日期为主键的树
	private int datalength;
	private int positionlength;
	
	private InitEnvironment ie;
	public static void main(String[] args){
		new StockDataHelperImpl_2();
	}
	
	/**
	 * 初始化
	 */
	public StockDataHelperImpl_2(){
		initContainer();
		initFetchChannel();
		
		long t1=System.currentTimeMillis();
		loadData();
		long t2=System.currentTimeMillis();
		System.out.println("映射到内存的时间"+(t2-t1));
//	    
		test();
	}
	/**
	 * 初始化装载数据的容器
	 */
	private void initContainer(){
		ie=InitEnvironment.getInstance();
		
		receive=new byte[13];
		doublereceive=new byte[17];
		
		datetree=new DateTrie();
	}
	/**
	 * 初始化爬取数据的通道
	 */
	private void initFetchChannel(){
		fetch=StockDataFetchImpl.getInstance();
		parse=Parse.getInstance();
		infopath=new File(ie.getPath("stockinfo"));
//		DailyFetchThread thread=new DailyFetchThread();
		if(!infopath.exists()&&!infopath.isDirectory()){
			try {
				fetch.fetchAllStockSet();
				fetch.fetchAllStockInfo();
			} catch (InternetdisconnectException e) {
				System.out.println(e.toString());
			}
//			thread.waitoneday();
		}
		else{
//			thread.start();
		}
	}
	/**
	 * 程序启动时记载数据
	 */
	private void loadData(){
		try {
			
			//服务器中的算法
			int BUFFER_SIZE=1024*1024*8;
			long ttt1=System.currentTimeMillis();										
			FileInputStream is=new FileInputStream("config/resources/mainData");
			FileChannel fc=is.getChannel();
			System.out.println(fc.size());
			mbb_data=fc.map(MapMode.READ_ONLY, 0, fc.size());
			
			byte[] dst1=new byte[BUFFER_SIZE];
			byte[] dst2=new byte[(int) (fc.size()%BUFFER_SIZE)];
			for(int i=0;i<fc.size();i+=BUFFER_SIZE){
				if(fc.size()-i>BUFFER_SIZE){
					mbb_data.get(dst1);
				}
				else{
					mbb_data.get(dst2);
				}
			}
			dst1=null;
			dst2=null;	
			long ttt2=System.currentTimeMillis();
			System.out.println("获取数据时间"+(ttt2-ttt1));	
			is.close();
			FileInputStream is2=new FileInputStream("config/resources/mainPosition");
			FileChannel fc2=is2.getChannel();
			System.out.println(fc2.size());
			
			mbb_position=fc2.map(MapMode.READ_ONLY, 0, fc2.size());
			
			dst1=new byte[BUFFER_SIZE];
			dst2=new byte[(int) (fc2.size()%BUFFER_SIZE)];
			for(int i=0;i<fc2.size();i+=BUFFER_SIZE){
				if(fc2.size()-i>BUFFER_SIZE){
					mbb_position.get(dst1);
				}
				else{
					mbb_position.get(dst2);
				}
			}
			dst1=null;
			dst2=null;
			
			long tttt2=System.currentTimeMillis();
			System.out.println("获取数据时间"+(tttt2-ttt1));	
			is2.close();					
			BufferedReader br=new BufferedReader(new FileReader("config/resources/mainIndex"));
			int count=0;
			IndexTree it=new IndexTree();
			while(br.ready()){
				String out=br.readLine();
				int cal=parse.getIntDate(out.substring(0, 10));
				int code=Integer.parseInt(out.substring(11));
				int year= cal / 10000;
				int month= (cal -year * 10000 ) / 100;
				int day=cal - year * 10000 - month * 100;
				it.add(year, month, day, code, count);
//				datetree.add(year, month, day, code, count);
				count++;
			}
			it.end();
//			datetree.clear();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * 读取索引文件(常规方式)
	 * @param row 索引所处的行数
	 * @return 数据文件的索引
	 */
	private String readPosition(int row){
		int position=row*14;
		mbb_position.position(position);
		mbb_position.get(receive);
		int b=Integer.parseInt(new String(receive,0,3));
		int c=Integer.parseInt(new String(receive,4,9));
		return read(c,b);
	}
	/**
	 * 读取索引文件(双倍内容)
	 * @param row 索引所处的行数
	 * @return 数据文件的索引
	 */
	private String readDoublePosition(int row){
		int position=row*14;
		mbb_position.position(position);
		mbb_position.get(receive);
		int b=Integer.parseInt(new String(receive,0,3));
		int c=Integer.parseInt(new String(receive,4,9));
		int position1=row*14+14;
		mbb_position.position(position1);
		mbb_position.get(receive);
		b=Integer.parseInt(new String(receive,0,3))+b+1;
		return read(c,b);
	}
	/**
	 * 读取索引文件(四倍内容)
	 * @param row 索引所处的行数
	 * @return 数据文件的索引
	 */
	private String readQuaPosition(int row){
		int position=row*14;
		mbb_position.position(position);
		mbb_position.get(receive);
		int b=Integer.parseInt(new String(receive,0,3));
		int c=Integer.parseInt(new String(receive,4,9));
		position=row*14+14;
		mbb_position.position(position);
		mbb_position.get(receive);
		b=Integer.parseInt(new String(receive,0,3))+b+1;
		position=row*14+14+14;
		mbb_position.position(position);
		mbb_position.get(receive);
		b=Integer.parseInt(new String(receive,0,3))+b+1;
		position=row*14+14+14;
		mbb_position.position(position);
		mbb_position.get(receive);
		b=Integer.parseInt(new String(receive,0,3))+b+1;
		return read(c,b);
	}
	/**
	 * 读取索引文件(双倍容器)
	 * @param row 索引所处的行数
	 * @return 数据文件的索引
	 */
	private String readDoubleSizePosition(int row){
		int position=row*14;
		mbb_position.position(position);
		mbb_position.get(doublereceive);
		int b=Integer.parseInt(new String(doublereceive,0,3))+1+Integer.parseInt(new String(doublereceive,14,3));
		int c=Integer.parseInt(new String(doublereceive,4,9));
		return read(c,b);
	}
	/**
	 * 读取数据文件
	 * @param start 数据的起始位置
	 * @param size 数据的字节
	 * @return 需要读的数据
	 */
	private String read(int start,int size){
		byte[] dst=new byte [size];
		mbb_data.position(start);
		mbb_data.get(dst);
		String t=new String(dst);
		return t;
	}
	/**
	 * 生成一个在2005-02-01到2017-03-28的随机日期
	 * @return 日期的String
	 */
	public String randomDateBetweenMinAndMax(){  
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	       Calendar calendar = Calendar.getInstance();  
	       //注意月份要减去1  
	       calendar.set(2015,1,1);  
	       calendar.getTime().getTime();  
	       //根据需求，这里要将时分秒设置为0  
	       calendar.set(Calendar.HOUR_OF_DAY, 0);  
	       calendar.set(Calendar.MINUTE, 0);  
	       calendar.set(Calendar.SECOND,0);  
	       long min = calendar.getTime().getTime();;  
	       calendar.set(2017,2,29);  
	       calendar.set(Calendar.HOUR_OF_DAY,0);  
	       calendar.set(Calendar.MINUTE,0);  
	       calendar.set(Calendar.SECOND,0);  
	       calendar.getTime().getTime();  
	       long max = calendar.getTime().getTime();  
	       //得到大于等于min小于max的double值  
	       double randomDate = Math.random()*(max-min)+min;  
	       //将double值舍入为整数，转化成long类型  
	       calendar.setTimeInMillis(Math.round(randomDate));  
	       return sdf.format(calendar.getTime());  
	}  
	/**
	 * 检测数据完整性测试——part1
	 */
	public void check(){
		try{
			BufferedReader br1=new BufferedReader(new FileReader("config/resources/mainData"));
			BufferedReader br2=new BufferedReader(new FileReader("config/resources/mainPosition"));
			int count =0;
			int total = 0;
			while(br1.ready()){
				count++;
				String t1=br1.readLine();
				int size1=t1.length();
				String[] t2=br2.readLine().split(",");
				int size2=Integer.parseInt(t2[0]);
				int t=Integer.parseInt(t2[1]);
				if(size1!=size2){
					System.out.println(count+":   size1="+size1+",size2="+(size2)+"\n"+t1);
					System.exit(0);
				}
				if(t!=total){
					System.out.println(count+"::"+total+t1);
					System.exit(0);
				}
				if(!read(total,size2).equals(t1)){
					System.out.println(count+":"+total+t1);
					System.exit(0);
				}
				total=total+size1+1;
			}
			br1.close();
			br2.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 检测数据完整性测试——part2
	 */
	public void check2(){
		try {
			String[] filelist=infopath.list();
			String str="";
			String cal="";
			if(true){
				int count=0;
				long total=0;
				for(String stock:filelist){		
					count++;
					System.out.println(3269-count);
					BufferedReader br=new BufferedReader(new FileReader("config/stock/info/"+stock+"/data"));
					BufferedReader br_adj=new BufferedReader(new FileReader("config/stock/info/"+stock+"/subscription"));	
					BufferedReader br_adj1=new BufferedReader(new FileReader("config/stock/info/"+stock+"/afterscription"));	
					while(br.ready()){
						str=br.readLine()+","+br_adj.readLine()+","+br_adj1.readLine();
						cal=str.substring(0, 10);
						int a=parse.getIntDate(cal);
						str=str.substring(11);
						int row=0;
						try{
							row=datetree.get(a).getDateinfo().get(Integer.parseInt(stock));
							String str1=readPosition(row);
							if(str1==null){
								System.out.println(cal+":"+stock+":"+row+"\nnull!!!!!");
								System.exit(0);
							}
							if(!str1.equals(str)){
								System.out.println(cal+":"+stock+":"+row);
								System.exit(0);
							}
						}catch(Exception e){
								System.out.println(cal+":"+stock+":"+row+"\nnull!!!!!");
								System.exit(0);
							
						}
						
						
					}	
					br_adj.close();
					br.close();
					br_adj1.close();
				}
				System.out.println(total);
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	/**
	 * 检测datetrie中数据完整性和正确性
	 */
	public void check3(){
		try{
		BufferedReader br=new BufferedReader(new FileReader("config/resources/mainIndex"));
		int count=0;
		int day5=0;
		int day6=0;
		int day7=0;
		while(br.ready()){
			String out=br.readLine();
			int cal=parse.getIntDate(out.substring(0, 10));
			int code=Integer.parseInt(out.substring(11));
			int year= cal / 10000;
			int month= (cal -year * 10000 ) / 100;
			int day=cal - year * 10000 - month * 100;
			if(cal==20170405){
				day5++;
			}
			if(cal==20170406){
				day6++;
			}
			if(cal==20170407){
				day7++;
			}
			if(datetree.get(year, month, day).getDateinfo().get(code)!=count){
				System.out.println(cal+":"+code);
				System.exit(0);
			}
			count++;
		}
		br.close();
		System.out.println("2017-04-05:"+day5+
				"2017-04-06:"+day6+
				"2017-04-07:"+day7);
		System.out.println("success");
		}catch(IOException e){
			
		}
	}
	private void test(){
		Scanner sc=new Scanner(System.in);
		String str="";
		System.out.println("done");
		while(!(str=sc.nextLine()).equals("esc")){
			String[] out=str.split(",");
			int b1=parse.getIntDate(out[0]);
			int year= b1 / 10000;
			int month= (b1 -year * 10000 ) / 100;
			int k=10000000;
			int day=b1 - year * 10000 - month * 100;
			int b2=Integer.parseInt(out[1]);
			long ttt1=System.currentTimeMillis();
//			Collection<StockLeaf> it=datetree.get(b1).values();
//			for(StockLeaf i:it){
//				readPosition(i.getDateinfo());
//			}
			long ttt2=System.currentTimeMillis();
			System.out.println("读取时间"+(ttt2-ttt1));
			
//			long m1=System.currentTimeMillis();
//			for(int i=0;i<k;i++) stocktree.get(b2);
//			long m2=System.currentTimeMillis();
//			System.out.println("读取时间"+(m2-m1));

			//int count=0;
//			Random random=new Random();
//			for(int i=0;i<1;i++) {
//				if(true){
//					count=0;
//						StockNode it=map.getMarket(20170328).getMin();
//						while(it.getNext()!=null){
//							for(Entry<Integer,Integer> e:it.getIndex()){
//								readPosition(e.getValue());
//								count++;
//								System.out.println(e.getKey());
//							}
//							tree2.get("2017-03-28").toString(tree2.get("2017-03-28").getRoot());
//							it=it.getNext();
//						}	
//				}
//			}
//			System.out.println(count);
			
//			System.out.println(readPosition(5638085));
//			System.out.println(readPosition(5638085));
//			for(int j=0;j<5;j++){
//				for(int i=0;i<2500000;i++){
//					readDoublePosition(i);	
//				}
//			}
//			
//			long a1=System.currentTimeMillis();
//			for(int j=0;j<5;j++){
//				for(int i=0;i<1250000;i++){
//					readQuaPosition(i);	
//				}
//			}
//			long a2=System.currentTimeMillis();
//			System.out.println("读取时间"+(a2-a1));
		}
		sc.close();
	}
	
	/**
	 * 判断是否是交易日
	 * @param day 需要判断的日期
	 * @return	是交易日则返回true，否则返回false
	 */
	public boolean isMarketDay(int day){
		return datetree.get(day).isLeaf();
	}
	/**
	 * 获取指定股票指定日期的信息
	 * @param cal 日期，形如XXXXXXXX,20170328
	 * @param code 股票编号，形如000001,1
	 * @return 股票信息，不存在时抛出异常
	 * @throws StockHaltingException 不存在股票数据时抛出该异常
	 * @throws NullDateException 不存在该日期时抛出该异常
	 */
	public String getSingleInfo(int a,int b) throws StockHaltingException, NullDateException{
		int row;
		try{
			row=datetree.get(a).getDateinfo().get(b);
		}catch(NullPointerException e){
			throw new NullDateException(a);
		}
		try{
			return readPosition(row);
		}catch(Exception e){
			throw new StockHaltingException(a,b);
		}
	}
	/**
	 * 将日期map停留在日期date上
	 * @param date 日期
	 * @throws NullDateException 缺少该天信息
	 */
	public DateLeaf remain_forAllinfo(int date) throws NullDateException{
		DateLeaf leaf_forDatetree=datetree.get(date);	
		if(!leaf_forDatetree.isLeaf()){
			throw new NullDateException(date);
		}
		else{
			return leaf_forDatetree;
		}
	}
	/**
	 * 将日期往后推一天（针对于汇总信息的表）
	 * @throws NullDateException 如果已是最后一天抛出该异常
	 */
	public DateLeaf nextDay_forAllinfo(DateLeaf leaf) throws NullDateException{
		DateLeaf leaf_forDatetree=(DateLeaf) leaf.getNext();
		if(leaf_forDatetree==null){
			throw new NullDateException(leaf.getCal());
		}
		else{
			return leaf_forDatetree;
		}
	}
	/**
	 * 将日期往前推一天（针对于汇总信息的表）
	 * @throws NullDateException 如果已是第一天抛出该异常
	 */
	public DateLeaf previousDay_forAllinfo(DateLeaf leaf) throws NullDateException{
		DateLeaf leaf_forDatetree=(DateLeaf) leaf.getPrevious();
		if(leaf_forDatetree==null){
			throw new NullDateException(leaf.getCal());
		}
		else{
			return leaf_forDatetree;
		}
	}
	/**
	 * 获取某个软件自带的股票池的股票的某天信息
	 * @param set 股票池名称
	 * @param date 日期
	 * @return StockSetInfoPO 股票池信息的po
	 * @throws StockHaltingException 停牌时抛出该异常
	 */
	public String getStockInfoinSet_throughRemain(int code,DateLeaf leaf) throws StockHaltingException{	
		try{
			int row;
			row=leaf.getDateinfo().get(code);
			return readPosition(row);
		}catch(Exception e){
			throw new StockHaltingException(code);
		}	
	}

	/**
	 * 将日期map停留在日期date上
	 * @param date 日期
	 * @throws NullDateException 缺少该天信息
	 */
	public StockLeaf remain_forSingleinfo(int code,int date) throws NullDateException{
//		StockLeaf leaf_forStocktree=datetree.get(date).getDateinfo().get(code);
//		if(!leaf_forStocktree.isLeaf()){
//			throw new NullDateException(date);
//		}
//		else{
//			return leaf_forStocktree;
//		}
		return null;
	}
	/**
	 * 将日期往后推一天（针对于汇总信息的表）
	 * @throws NullDateException 如果已是最后一天抛出该异常
	 */
	public StockLeaf nextDay_forSingleinfo(StockLeaf leaf) throws NullDateException{
		StockLeaf leaf_forStocktree=(StockLeaf) leaf.getNext();
		if(leaf_forStocktree==null){
			throw new NullDateException(((DateLeaf)(leaf.getParent())).getCal());
		}
		else{
			return leaf_forStocktree;
		}
	}
	/**
	 * 获取某个的股票的某天信息（根据停留指针）
	 * @return String 股票池信息的po
	 * @throws StockHaltingException 停牌时抛出该异常
	 */
	public	String getSingleInfo_throughRemain(StockLeaf leaf){
		int row=leaf.getDateinfo();
		return readPosition(row);
	}
	/**
	 * 获取某只股票最早的一天
	 * @param code 股票编号
	 * @return 日期
	 */
	public int getMinDay(int code){
//		int index=datetree.getStockindex().get(code);
//		return ((DateLeaf)datetree.getStockmin().get(index).getParent()).getCal();
		return 0 ;
	}
	/**
	 * 获取某只股票最晚的一天
	 * @param code 股票编号
	 * @return 日期
	 */
	public int getMaxDay(int code){
//		int index=datetree.getStockindex().get(code);
//		return ((DateLeaf)datetree.getStockmax().get(index).getParent()).getCal();
		return 0;
	}
}