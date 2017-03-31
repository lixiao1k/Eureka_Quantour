package data.fetchdataimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import data.common.FileMethod;
import data.common.WebMethod;
import data.parse.ParseStockName;
import data.parse.Translate;
import exception.InternetdisconnectException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

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
	public static void main(String[] args){
		new StockSetFetchByWeb();
	}
	public StockSetFetchByWeb(){
		SHA="config/stock/stockset/SHA";
		SHB="config/stock/stockset/SHB";
		SZA="config/stock/stockset/SZA";
		SZB="config/stock/stockset/SZB";
		CYB="config/stock/stockset/CYB";
		ZXB="config/stock/stockset/ZXB";
		HS300="config/stock/stockset/HS300";
		filemethod=FileMethod.getInstance();
		webmethod=WebMethod.getInstance();
		filemethod.makepath("config/parse");
		filemethod.makepath("config/backups");
		filemethod.makepath("config/stock/stockset");
		filemethod.makepath(SHA);
		filemethod.makepath(SHB);
		filemethod.makepath(SZA);
		filemethod.makepath(SZB);
		filemethod.makepath(HS300);
		filemethod.makepath(CYB);
		filemethod.makepath(ZXB);
	}
	/**
	 * 从网上获取所有股票的名字与编号
	 * @throws InternetdisconnectException 当无法从网络获取信息时抛出该异常
	 */
	public void getAllStockName() throws InternetdisconnectException{
		try{
			File backups=new File("config/backups/StringNameList_Backups");
			
			if(!backups.exists()){
				backups.createNewFile();
				try {
					webmethod.testInternet();
					String str=webmethod.saveToFile_ByBufferedReader("http://quote.stockstar.com/stock/stock_index.htm"
							,"config/backups/StringNameList_Backups");
					dealStringList(webmethod.match("<li>.*?</li>",str));
					System.out.println("update by web");
				} catch (InternetdisconnectException e) {
					throw e;
				}
			}
			else{
				BufferedReader br=new BufferedReader(new FileReader(backups));
				String total="";
				while(br.ready()){
					total=total+br.readLine();
				}
				br.close();
				dealStringList(webmethod.match("<li>.*?</li>",total));
				System.out.println("update by backups");
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
			File backups_HS300=new File("config/backups/HS300List");
			if(!backups_HS300.exists()){
				backups_HS300.createNewFile();
				try {
					webmethod.testInternet();
					webmethod.saveToFile("115.29.204.48","webdata","000300cons.xls","config/backups/HS300List.xls");
					processExcel("config/backups/HS300List.xls", HS300);
					System.out.println("update by web");
				} catch (InternetdisconnectException e) {
					throw e;
				}
			}
			else{
				BufferedReader br=new BufferedReader(new FileReader(backups_HS300));
				filemethod.dealdir(new File(HS300));
				while(br.ready()){
					addstock(br.readLine().substring(0, 6),HS300);
				}
				br.close();
				System.out.println("update by backups");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
//	/**
//	 * 从网上获取中小板的列表
//	 * @throws InternetdisconnectException
//	 */
//	public void getZXBList() throws InternetdisconnectException{
//		try{
//			File backups_ZXB=new File("config/backups/ZXBList");
//			if(!backups_ZXB.exists()){
//				backups_ZXB.createNewFile();
//				try {
//					webmethod.testInternet();
//					webmethod.saveToFile_ByInputStream("http://www.szse.cn/szseWeb/ShowReport.szse?SHOWTYPE=xlsx&CATALOGID=1747&ZSDM=399005&tab1PAGENUM=1&ENCODE=1&TABKEY=tab1"
//							,"config/backups/ZXBList.xlsx");
//					processExcel("config/backups/ZXBList.xlsx", ZXB);
//					System.out.println("update by web");
//				} catch (InternetdisconnectException e) {
//					throw e;
//				}
//			}
//			else{
//				BufferedReader br=new BufferedReader(new FileReader(backups_ZXB));
//				filemethod.dealdir(new File(ZXB));
//				while(br.ready()){
//					addstock(br.readLine().substring(0, 6),ZXB);
//				}
//				br.close();
//				System.out.println("update by backups");
//			}
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//	}
	
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
			while(it.hasNext()){
				str=it.next();
				code=str.substring(0,6);
				name=ParseStockName.getInstance().chkHalf(str.substring(6));
				if(name.equals(code)){
					continue;
				}
				generateStockSet(code);
				nameTocode_pro.setProperty(name, code);
				codeToname_pro.setProperty(code, name);
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
	 * 处理excel文件
	 * @param localpath 本地excel文件地址
	 * @param sectionpath 将要输入到的股票池
	 * @return
	 */
	private String processExcel(String localpath,String sectionpath){
		String buffer = "";
        try {  
               File file = new File(localpath);
               // 设置读文件编码
               WorkbookSettings setEncode = new WorkbookSettings();
               setEncode.setEncoding("GB2312");
               // 从文件流中获取Excel工作区对象（WorkBook）
               Workbook wb = Workbook.getWorkbook(file,setEncode); 
               Sheet sheet = wb.getSheet(0); 
               for (int i = 1; i < 301; i++) {  
            		 Cell cell = sheet.getCell(0, i);   
            		 buffer += cell.getContents(); 
            		 Cell cell1 = sheet.getCell(1, i); 
            		 buffer += ParseStockName.getInstance().chkHalf(cell1.getContents());
            		 Translate t=Translate.getInstance();
            		 if(t.trans_codeToname(cell.getContents())==null){
            			 System.out.println(cell.getContents());
            		 }
            		 buffer +="\n";
            		 addstock(cell.getContents(),sectionpath);
               }  
        } catch (BiffException e) {  
        	e.printStackTrace();  
        } catch (IOException e) {  
        	e.printStackTrace();  
        }   
        //write the string into the file
        String savePath = localpath.substring(0,localpath.length()-4);
        File saveCSV = new File(savePath);
        try {   
        	if(!saveCSV.exists())
        		saveCSV.createNewFile();
        	BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
        	writer.write(buffer);
        	writer.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return buffer;
	}
}
