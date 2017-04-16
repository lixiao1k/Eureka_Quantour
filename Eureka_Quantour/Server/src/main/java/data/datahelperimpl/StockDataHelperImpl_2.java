package data.datahelperimpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import data.common.DateTrie;
import data.common.IndexTree;
import data.datahelperservice.IStockDataHelper_2;
import data.fetchdataimpl.StockDataFetchImpl;
import data.fetchdataservice.IStockDataFetch;
import data.parse.Parse;
import exception.DateOverException;
import exception.InternetdisconnectException;
import exception.NullDateException;
import exception.StockHaltingException;
/**
 * 股票模块数据的数据处理接口
 * @author 刘宇翔
 *
 */
public class StockDataHelperImpl_2 implements IStockDataHelper_2{
	private static int BUFFER_SIZE=1024*1024*2;
	
	private File infopath;//数据的地址
	private IStockDataFetch fetch;//爬取数据的接口
	private MappedByteBuffer mbb_data;//全部数据的内存映射
	private MappedByteBuffer mbb_position;//全部索引位置的内存映射
	private Parse parse;//一些数据转换的方法
	private byte[] receive;//接受数据的容器（一次一行）
	private byte[] doublereceive;//接受数据的容器（一次两行）
	
	private List<Integer> pointerToposition;
	private HashMap<Integer,Integer> dateIndex;
	private List<Integer> datesort;
	private int datesize;
	
	private DateTrie datetree;//以日期为主键的树
	private int datalength;
	private int positionlength;
	
	private byte[] dst_BUFFERSIZE;
	
	private String stockInfo;
	
	
	private StockIndexBuffer indexBuffer;
	private StockIndexBuffer dataBuffer;
	private DataBuffer_ByInputStream dataBuffer_is;
	private DataBuffer_ByInputStream indexBuffer_is;
	private byte[] dst_MainIndex;
	
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
		loadData2();
		long t2=System.currentTimeMillis();
		System.out.println("映射到内存的时间"+(t2-t1));
//		check2();
//		check();
//	    
//   	test();
	}
	/**
	 * 初始化装载数据的容器
	 */
	private void initContainer(){
		ie=InitEnvironment.getInstance();
		
		indexBuffer=new StockIndexBuffer(3300);
		dataBuffer=new StockIndexBuffer(100);
		indexBuffer_is=new DataBuffer_ByInputStream(3300);
		dataBuffer_is=new DataBuffer_ByInputStream(100);
		
		stockInfo=ie.getPath("stockinfo");
		
		dst_BUFFERSIZE=new byte[BUFFER_SIZE];
		
		dst_MainIndex=new byte[8];
		
		receive=new byte[16];
		doublereceive=new byte[17];
		
		datetree=new DateTrie();
		
		pointerToposition=new ArrayList<Integer>();
		dateIndex=new HashMap<Integer,Integer>();
		datesort=new ArrayList<Integer>();
		datesize=0;
	}
	/**
	 * 初始化爬取数据的通道
	 */
	private void initFetchChannel(){
		fetch=StockDataFetchImpl.getInstance();
		parse=Parse.getInstance();
		infopath=new File(ie.getPath("stockinfo"));
		try {
			fetch.fetchAllStockInfo();
		} catch (InternetdisconnectException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	 * 程序启动时记载数据
	 */
	private void loadData2(){
		try {
			//客户端中的算法
			FileInputStream is2=new FileInputStream("config/resources/date/mainPosition");
			FileChannel fc2=is2.getChannel();
			mbb_position=fc2.map(MapMode.READ_ONLY, 0, fc2.size());
			byte[] dst1=new byte[BUFFER_SIZE];
			byte[] dst2=new byte[(int) (fc2.size()%BUFFER_SIZE)];
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
			is2.close();
			BufferedReader br=new BufferedReader(new FileReader("config/resources/date/totalCalendar"));
			int count=0;
			while(br.ready()){
				String out=br.readLine();
				int cal=Integer.valueOf(out.substring(0, 8));
				int row=Integer.parseInt(out.substring(8));
				pointerToposition.add(row);
				dateIndex.put(cal, count);
				datesort.add(cal);
				datesize++;
				count++;
			}
			File path=new File("config/stock/info");
			String[] t=path.list();
			for(String code:t){
				try{
					Integer.valueOf(code);
				}catch(NumberFormatException e){
					continue;
				}
				try {
					getSingleInfo(20170328, code);
				} catch (StockHaltingException | NullDateException e) {
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
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
		return read(c,b,mbb_data);
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
		return read(c,b,mbb_data);
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
		return read(c,b,mbb_data);
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
			BufferedReader br1=new BufferedReader(new FileReader("config/resources/date/totalCalendar"));
			BufferedReader br2=new BufferedReader(new FileReader("config/resources/date/mainPosition"));
			int count =0;
			int total = 0;
			while(br1.ready()){
				String str=br1.readLine();
				int cal=Integer.valueOf(str.substring(0, 8));
				total=0;
				BufferedReader br3=new BufferedReader(new FileReader("config/resources/date/calendarDate/"+cal));
				while(br3.ready()){
					String info=br3.readLine();
					str=br2.readLine();
					String code=str.substring(0,6);
					int length=Integer.valueOf(str.substring(6,9));
					int total2=Integer.valueOf(str.substring(9));
					int length2=info.length();
					if(length!=length2){
						System.out.println(code+""+cal+""+info);
						System.exit(0);
					}
					if(total!=total2){
						System.out.println(code+""+cal+""+info);
						System.exit(0);
					}
					total=total+length+1;
				}
				br3.close();
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
						{
							String str1="";
							try {
								str1 = getSingleInfo(a, stock);
							} catch (StockHaltingException | NullDateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(str1==null){
								System.out.println(cal+":"+stock+":"+row+"\nnull!!!!!");
								System.exit(0);
							}
							if(!str1.equals(str)){
								System.out.println(cal+":"+stock+":"+row);
								System.exit(0);
							}
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
			int k=100000;
			File path=new File("config/stock/info");
			String[] t=path.list();
			long ttt1=System.currentTimeMillis();
			
			for(int cal :datesort){
				for(int i=0;i<100;i++)
					try {
						getSingleInfo(cal, out[1]);
					} catch (StockHaltingException | NullDateException e) {
					}
			}
			Runtime.getRuntime().gc();
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
	
//	/**
//	 * 判断是否是交易日
//	 * @param day 需要判断的日期
//	 * @return	是交易日则返回true，否则返回false
//	 */
//	public boolean isMarketDay(int day){
//		return datetree.get(day).isLeaf();
//	}
//	/**
//	 * 将日期map停留在日期date上
//	 * @param date 日期
//	 * @throws NullDateException 缺少该天信息
//	 */
//	public DateLeaf remain_forAllinfo(int date) throws NullDateException{
//		DateLeaf leaf_forDatetree=datetree.get(date);	
//		if(!leaf_forDatetree.isLeaf()){
//			throw new NullDateException(date);
//		}
//		else{
//			return leaf_forDatetree;
//		}
//	}
//	/**
//	 * 将日期往后推一天（针对于汇总信息的表）
//	 * @throws NullDateException 如果已是最后一天抛出该异常
//	 */
//	public DateLeaf nextDay_forAllinfo(DateLeaf leaf) throws NullDateException{
//		DateLeaf leaf_forDatetree=(DateLeaf) leaf.getNext();
//		if(leaf_forDatetree==null){
//			throw new NullDateException(leaf.getCal());
//		}
//		else{
//			return leaf_forDatetree;
//		}
//	}
//	/**
//	 * 将日期往前推一天（针对于汇总信息的表）
//	 * @throws NullDateException 如果已是第一天抛出该异常
//	 */
//	public DateLeaf previousDay_forAllinfo(DateLeaf leaf) throws NullDateException{
//		DateLeaf leaf_forDatetree=(DateLeaf) leaf.getPrevious();
//		if(leaf_forDatetree==null){
//			throw new NullDateException(leaf.getCal());
//		}
//		else{
//			return leaf_forDatetree;
//		}
//	}
//	/**
//	 * 获取某个软件自带的股票池的股票的某天信息
//	 * @param set 股票池名称
//	 * @param date 日期
//	 * @return StockSetInfoPO 股票池信息的po
//	 * @throws StockHaltingException 停牌时抛出该异常
//	 */
//	public String getStockInfoinSet_throughRemain(int code,DateLeaf leaf) throws StockHaltingException{	
//		try{
//			int row;
//			row=leaf.getDateinfo().get(code);
//			return readPosition(row);
//		}catch(Exception e){
//			throw new StockHaltingException(code);
//		}	
//	}

//	/**
//	 * 将日期map停留在日期date上
//	 * @param date 日期
//	 * @throws NullDateException 缺少该天信息
//	 */
//	public StockLeaf remain_forSingleinfo(int code,int date) throws NullDateException{
////		StockLeaf leaf_forStocktree=datetree.get(date).getDateinfo().get(code);
////		if(!leaf_forStocktree.isLeaf()){
////			throw new NullDateException(date);
////		}
////		else{
////			return leaf_forStocktree;
////		}
//		return null;
//	}
//	/**
//	 * 将日期往后推一天（针对于汇总信息的表）
//	 * @throws NullDateException 如果已是最后一天抛出该异常
//	 */
//	public StockLeaf nextDay_forSingleinfo(StockLeaf leaf) throws NullDateException{
//		StockLeaf leaf_forStocktree=(StockLeaf) leaf.getNext();
//		if(leaf_forStocktree==null){
//			throw new NullDateException(((DateLeaf)(leaf.getParent())).getCal());
//		}
//		else{
//			return leaf_forStocktree;
//		}
//	}
//	/**
//	 * 获取某个的股票的某天信息（根据停留指针）
//	 * @return String 股票池信息的po
//	 * @throws StockHaltingException 停牌时抛出该异常
//	 */
//	public	String getSingleInfo_throughRemain(StockLeaf leaf){
//		int row=leaf.getDateinfo();
//		return readPosition(row);
//	}
	
	
	
	
//-------------------------------new method-------------------------------------//
	/**
	 * 获取某只股票最早的一天
	 * @param code 股票编号
	 * @return 日期
	 * @throws IOException 
	 */
	public LocalDate getMinDay(String code) throws IOException{
		String path=stockInfo+"/"+code+"/config.properties";
		Properties pro=new Properties();
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(path));
		pro.load(bis);
		bis.close();
		LocalDate cal=LocalDate.parse(pro.getProperty("first_day"));
		return cal;
	}
	/**
	 * 获取某只股票最晚的一天
	 * @param code 股票编号
	 * @return 日期
	 */
	public LocalDate getMaxDay(String code) throws IOException{
		String path=stockInfo+"/"+code+"/config.properties";
		Properties pro=new Properties();
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(path));
		pro.load(bis);
		bis.close();
		LocalDate cal=LocalDate.parse(pro.getProperty("last_day"));
		return cal;
	}
	/**
	 * 获取指定股票指定日期的信息
	 * @param cal 日期，形如XXXXXXXX,20170328
	 * @param code 股票编号，形如000001,1
	 * @return 股票信息，不存在时抛出异常
	 * @throws StockHaltingException 不存在股票数据时抛出该异常
	 * @throws NullDateException 不存在该日期时抛出该异常
	 */
	public String getSingleInfo(int cal,String code) throws StockHaltingException, NullDateException{
		int index=dateIndex.getOrDefault(cal, -1);
		if(index<0){
			throw new NullDateException(cal);
		}
		else{
			int pointer=pointerToposition.get(index);
			MappedByteBuffer mbb=indexBuffer.getMbb(Integer.valueOf(code), stockInfo+"/"+code+"/mainIndex");
			MappedByteBuffer mbb_data=dataBuffer.getMbb(cal, "config/resources/date/calendarDate/"+cal);
			String str=getIndexByMbb(mbb,index);
			int relative=Integer.valueOf(str.substring(0,4));
			if(relative==9999){
				throw new NullDateException(cal);
			}
//			MappedByteBuffer mbb_main=null;
//			try {
//				mbb_main = loadStockDateIndex("config/resources/mainData");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			MappedByteBuffer mbb_position=null;
//			try {
//				mbb_position = loadStockDateIndex("config/resources/date/mainPosition");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return readPosition(pointer+relative, mbb_position, mbb_data);
		}
	}
	
	/**
	 * 获取指定股票指定日期的信息
	 * @param cal 日期，形如XXXXXXXX,20170328
	 * @param code 股票编号，形如000001,1
	 * @return 股票信息，不存在时抛出异常
	 * @throws StockHaltingException 不存在股票数据时抛出该异常
	 * @throws NullDateException 不存在该日期时抛出该异常
	 */
	public String getSingleInfo2(int cal,String code) throws StockHaltingException, NullDateException{
		int index=dateIndex.getOrDefault(cal, -1);
		if(index<0){
			throw new NullDateException(cal);
		}
		else{
			int pointer=pointerToposition.get(index);
			MappedByteBuffer mbb=indexBuffer.getMbb(Integer.valueOf(code), stockInfo+"/"+code+"/mainIndex");
			byte[] mbb_data=dataBuffer_is.getMbb(cal, "config/resources/date/calendarDate/"+cal);
			String str=getIndexByMbb(mbb,index);
			int relative=Integer.valueOf(str.substring(0,4));
			if(relative==9999){
				throw new NullDateException(cal);
			}
			return readPosition(pointer+relative, mbb_position, mbb_data);
		}
	}
	/**
	 * 获取某一天起之后last个交易日之后的天数
	 * @param date 起始日期
	 * @param last 推移的天数
	 * @return 交易日日期
	 * @throws DateOverException
	 * @throws NullDateException 
	 */
	public LocalDate addDays(int date,int last) throws DateOverException, NullDateException{
		int index=dateIndex.getOrDefault(date, -1);
		if(index<0){
			throw new NullDateException(date);
		}
		else{
			index=index+last;
			if(index>=0&&index<datesize){
				return parse.getlocalDate(datesort.get(index));
			}
			else{
				throw new DateOverException(date, last);
			}
		}
	}
	/**
	 * 读取数据文件
	 * @param start 数据的起始位置
	 * @param size 数据的字节
	 * @return 需要读的数据
	 */
	private String read(int start,int size,MappedByteBuffer mbb_data){
		byte[] dst=new byte [size];
		mbb_data.position(start);
		mbb_data.get(dst);
		String t=new String(dst);
		return t;
	}
	/**
	 * 读取数据文件
	 * @param start 数据的起始位置
	 * @param size 数据的字节
	 * @return 需要读的数据
	 */
	private String read(int start,int size,byte[] mbb_data){
		byte[] dst=new byte [size];
		System.arraycopy(dst, 0, mbb_data, start, size);
		String t=new String(dst);
		return t;
	}
	/**
	 * 读取索引文件(常规方式)
	 * @param row 索引所处的行数
	 * @return 数据文件的索引
	 */
	private String readPosition(int row,MappedByteBuffer mbb_position,MappedByteBuffer mbb_data){
		int position=row*16;
		mbb_position.position(position);
		try{
		mbb_position.get(receive);
		}catch(BufferUnderflowException e){
			System.out.println(position);
		}
		int b=Integer.parseInt(new String(receive,6,3));
		int c=Integer.parseInt(new String(receive,9,6));
		return read(c,b,mbb_data);
	}
	/**
	 * 读取索引文件(常规方式)
	 * @param row 索引所处的行数
	 * @return 数据文件的索引
	 */
	private String readPosition(int row,MappedByteBuffer mbb_position,byte[] mbb_data){
		int position=row*16;
		mbb_position.position(position);
		try{
		mbb_position.get(receive);
		}catch(BufferUnderflowException e){
			System.out.println(position);
		}
		int b=Integer.parseInt(new String(receive,6,3));
		int c=Integer.parseInt(new String(receive,9,6));
		return read(c,b,mbb_data);
	}
	private String getIndexByMbb(MappedByteBuffer mbb,int Index){
		int position=Index*9;
		mbb.position(position);
		mbb.get(dst_MainIndex);
		return new String(dst_MainIndex);
	}
	private MappedByteBuffer loadStockDateIndex(String path) throws IOException{
		FileInputStream is=new FileInputStream(path);
		FileChannel fc=is.getChannel();
		MappedByteBuffer mbb=fc.map(MapMode.READ_ONLY, 0, fc.size());
//		byte[] tempdst=new byte[(int) (fc.size()%BUFFER_SIZE)];
//		for(int i=0;i<fc.size();i+=BUFFER_SIZE){
//			if(fc.size()-i>BUFFER_SIZE){
//				mbb.get(dst_BUFFERSIZE);
//			}
//			else{
//				mbb.get(tempdst);
//			}
//		}
		is.close();
		return mbb;
	}
}
