package data.fetchdataimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.common.FileMethod;
import data.common.WebMethod;
import data.datahelperimpl.InitEnvironment;
import data.parse.Parse;
import data.parse.Translate;
import exception.InternetdisconnectException;

/**
 * 为软件扩充股票池和股票
 * @author 刘宇翔
 *
 */
public class StockSetFetchByWeb{
	private String SHA;//沪A
	private String SZA;//深A
	private String SHB;//沪B
	private String SZB;//深B
	private String CYB;//创业板
	private String HS300;//沪深300
	private String ZXB;//中小板
	private FileMethod filemethod;
	private WebMethod webmethod;
	private InitEnvironment ie;
	public StockSetFetchByWeb(){
		init();
	}
	//初始化
	private void init(){
		ie=InitEnvironment.getInstance();
		SHA=ie.getPath("SHA");
		SHB=ie.getPath("SHB");
		SZA=ie.getPath("SZA");
		SZB=ie.getPath("SZB");
		CYB=ie.getPath("CYB");
		ZXB=ie.getPath("ZXB");
		HS300=ie.getPath("HS300");
		filemethod=FileMethod.getInstance();
		webmethod=WebMethod.getInstance();	
	}
	/**
	 * 从网上获取所有股票的名字与编号
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	public void getAllStockName() throws InternetdisconnectException{
		try{
			File backups=new File(ie.getPath("backups")+"/StringNameList_Backups");
			try {
				System.out.println("get stock list");
				webmethod.testInternet();
				String str=webmethod.saveToFile_ByBufferedReader("http://"+ie.getPath("StockNameListHtml")
						,ie.getPath("backups")+"/StringNameList_Backups");
				dealStringList(webmethod.match("<li>.*?</li>",str));
				System.out.println("update by web");
			} catch (InternetdisconnectException e) {
				if(backups.exists()){
					BufferedReader br=new BufferedReader(new FileReader(backups));
					String total="";
					while(br.ready()){
						total=total+br.readLine();
					}
					br.close();
					dealStringList(webmethod.match("<li>.*?</li>",total));
					System.out.println("update by backups");
				}
				else{
					throw e;
				}
			}		
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 从网上获取沪深300的列表
	 * @throws InternetdisconnectException
	 */
	public void getHS300List() throws InternetdisconnectException{
		try{
			File backups_HS300=new File(ie.getPath("backups")+"/HS300List");
			try {
				System.out.println("get HS300 list");
				webmethod.testInternet();
				webmethod.saveToFile("115.29.204.48","webdata","000300cons.xls",ie.getPath("backups")+"/HS300List.xls");
				filemethod.dealdir(new File(HS300));
				readXLS("config/backups/HS300List.xls", HS300);
				System.out.println("update by web");
			} catch (InternetdisconnectException e) {
					if(backups_HS300.exists()){
						BufferedReader br=new BufferedReader(new FileReader(backups_HS300));
						filemethod.dealdir(new File(HS300));
						while(br.ready()){
							addstock(br.readLine().substring(0, 6),HS300);
						}
						br.close();
						System.out.println("update by backups");
					}
					else{
						throw e;
					}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 从网上获取中小板的列表
	 * @throws InternetdisconnectException
	 */
	public void getZXBList() throws InternetdisconnectException{
		try{
			File backups_ZXB=new File("config/backups/ZXBList");
			try {
				System.out.println("get ZXB list");
				webmethod.testInternet();
				webmethod.saveToFile_ByInputStream("http://www.szse.cn/szseWeb/ShowReport.szse?SHOWTYPE=xlsx&CATALOGID=1747&ZSDM=399005&tab1PAGENUM=1&ENCODE=1&TABKEY=tab1"
						,"config/backups/ZXBList.xlsx");
				filemethod.dealdir(new File(ZXB));
				readXLSX("config/backups/ZXBList.xlsx", ZXB);
				System.out.println("update by web");
			} catch (InternetdisconnectException e) {
					if(backups_ZXB.exists()){
						BufferedReader br=new BufferedReader(new FileReader(backups_ZXB));
						filemethod.dealdir(new File(ZXB));
						while(br.ready()){
							addstock(br.readLine().substring(0, 6),ZXB);
						}
						br.close();
						System.out.println("update by backups");
					}
					else{
						throw e;
					}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理爬取到的股票名字
	 * @param list
	 */
	private void dealStringList(List<String> list){
		try{
			Iterator<String> it=list.iterator();
			String str="";
			String code="";
			String name="";
			File nameTocode=new File("config/parse/nameTocode.properties");
			File codeToname=new File("config/parse/codeToname.properties");
			if(!nameTocode.exists()||!codeToname.exists()){
				nameTocode.createNewFile();
				codeToname.createNewFile();
			}
			else{
				nameTocode.delete();
				codeToname.delete();
			}
			File stockset=new File("config/stock/stockset");
			File[] setlist=stockset.listFiles();
			for(File file:setlist){
				filemethod.dealdir(file);
			}
			Properties nameTocode_pro=new Properties();
			Properties codeToname_pro=new Properties();
			OutputStream nameTocode_out=new FileOutputStream("config/parse/nameTocode.properties");
			OutputStream codeToname_out=new FileOutputStream("config/parse/codeToname.properties");
			int i=0;
			while(it.hasNext()){
				str=it.next();
				code=str.substring(0,6);
				name=Parse.getInstance().chkHalf(str.substring(6));
				if(name.equals(code)){
					continue;
				}
				generateStockInfo(code);
				generateStockSet(code);
				nameTocode_pro.setProperty(name, code);
				codeToname_pro.setProperty(code, name);
				i++;
				System.out.println("正在处理第"+i+"个");
			}
			nameTocode_pro.store(nameTocode_out, "update");
			codeToname_pro.store(codeToname_out, "update");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void generateStockSet(String code){
		switch(code.charAt(0)){
		case '0':
			addstock(code,SZA);
			break;
		case '2':
			addstock(code,SZB);
			break;
		case '3':
			addstock(code,CYB);
			addstock(code,SZA);
			break;
		case '6':
			addstock(code,SHA);
			break;
		case '9':
			addstock(code,SHB);
			break;
		default:
			System.out.println(code);
		}
	}
	
	private void addstock(String code,String set){
		File file=new File(set+"/"+code);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理XLS文件
	 * @param localpath 本地excel文件地址
	 * @param sectionpath 将要输入到的股票池
	 * @return
	 */
	private String readXLS(String localpath,String sectionpath){
		String str = "";
        HSSFWorkbook xwb=null;
		try {
			xwb = new HSSFWorkbook(new FileInputStream(localpath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		// 读取第一章表格内容  
		HSSFSheet sheet = xwb.getSheetAt(0);  
		// 定义 row、cell  
		HSSFRow row;  
		String cell; 
		String cell1;
		Translate t=Translate.getInstance();
		// 循环输出表格中的内容  
		for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {  
			row = sheet.getRow(i);  
			// 通过 row.getCell(j).toString() 获取单元格内容，  
			cell = row.getCell(0).toString();  
			cell1= Parse.getInstance().chkHalf(row.getCell(1).toString());
			str	= str + cell +cell1 +"\n";  
			if(t.trans_codeToname(cell)==null){
   			 System.out.println(cell);
			}
			addstock(cell,sectionpath);
		} 
		String savePath = localpath.substring(0,localpath.length()-4);
		File saveCSV = new File(savePath);
		try {   
			if(!saveCSV.exists())
				saveCSV.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 处理XLSX文件
	 * @param localpath 本地excel文件地址
	 * @param sectionpath 将要输入到的股票池
	 * @return
	 */
	private String readXLSX(String localpath,String sectionpath) {
        String str = "";
        XSSFWorkbook xwb=null;
		try {
			xwb = new XSSFWorkbook(localpath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		// 读取第一章表格内容  
		XSSFSheet sheet = xwb.getSheetAt(0);  
		// 定义 row、cell  
		XSSFRow row;  
		String cell; 
		String cell1;
		Translate t=Translate.getInstance();
		// 循环输出表格中的内容  
		for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {  
			row = sheet.getRow(i);  
			// 通过 row.getCell(j).toString() 获取单元格内容，  
			cell = row.getCell(0).toString();  
			cell1= Parse.getInstance().chkHalf(row.getCell(1).toString());
			str	= str + cell +cell1 +"\n";  
			if(t.trans_codeToname(cell)==null){
   			 System.out.println(cell);
			}
			addstock(cell,sectionpath);
		} 
		String savePath = localpath.substring(0,localpath.length()-4);
		File saveCSV = new File(savePath);
		try {   
			if(!saveCSV.exists())
				saveCSV.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	private void generateStockInfo(String code){
		String path="config/stock/info"+"/"+code;
		filemethod.makepath(path);
		File data=new File(path+"/data");
		File properties=new File(path+"/config.properties");
		try {
			if(!data.exists())
				data.createNewFile();
			if(!properties.exists())
				properties.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties pro=new Properties();
		try {
			InputStream in = new FileInputStream(path+"/config.properties");
			pro.load(in);
			in.close();
			if(!pro.containsKey("update_day")){
				OutputStream out=new FileOutputStream(path+"/config.properties");
				pro.setProperty("first_day", "2005-02-01");
				pro.store(out, "init");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
