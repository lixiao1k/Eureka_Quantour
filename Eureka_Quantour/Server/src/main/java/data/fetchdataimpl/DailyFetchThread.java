package data.fetchdataimpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import data.fetchdataservice.IStockDataFetch;
import exception.InternetdisconnectException;

public class DailyFetchThread {
	private IStockDataFetch datafetch;
	public DailyFetchThread(){
		datafetch=StockDataFetchImpl.getInstance();
	}
	public void start(int deviant){
		Runnable runnable = new Runnable() {  
            public void run() {  
                try {
					datafetch.fetchAllStockInfo();
				} catch (InternetdisconnectException e) {
					System.out.println(e.toString());
				}
            }  
        };  
        long min=Duration.between( LocalDateTime.now(),LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0, 0))).toMinutes();
        if(min<0){
        	min=1440+min;
        }
        ScheduledExecutorService service = Executors  
                .newSingleThreadScheduledExecutor();  
        service.scheduleAtFixedRate(runnable, min+deviant, 1440, TimeUnit.MINUTES); 
	}
	public void waitoneday(){
        start(1440);
	}
}
