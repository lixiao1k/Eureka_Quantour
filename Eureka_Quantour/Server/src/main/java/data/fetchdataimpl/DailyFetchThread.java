package data.fetchdataimpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import data.fetchdataservice.IStockDataFetch;
import data.parse.Parse;
import exception.InternetdisconnectException;

public class DailyFetchThread {
	private IStockDataFetch datafetch;
	public DailyFetchThread(){
		datafetch=StockDataFetchImpl.getInstance();
	}
	public void start(int deviant) {
		Properties pro=new Properties();
		try{	
			InputStream is=new FileInputStream("config/stock/dataconfig.properties");
			pro.load(is);
			is.close();
		}catch(IOException e){
		}
		String lastday=pro.getProperty("lastday");
		int day=Parse.getInstance().getIntDate(lastday);
		Runnable runnable = new Runnable() {  
        public void run() {  
                try {
					datafetch.fetchAllStockInfo();
				} catch (InternetdisconnectException e) {
					System.out.println(e.toString());
				}
            }  
        };  
        int now=Parse.getInstance().getIntDate(LocalDate.now());
        long min=Duration.between( LocalDateTime.now(),LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0, 0))).toMinutes();
        if(now==day){
        	min=min+1440;
        }
        ScheduledExecutorService service = Executors  
                .newSingleThreadScheduledExecutor();  
        service.scheduleAtFixedRate(runnable, min+deviant, 1440, TimeUnit.MINUTES); 
	}
	public void waitoneday(){
        start(1440);
	}
}
