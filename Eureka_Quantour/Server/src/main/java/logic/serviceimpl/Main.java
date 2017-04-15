package logic.serviceimpl;

import exception.NullDateException;
import exception.NullStockIDException;

import java.time.LocalDate;

/**
 * Created by huihantao on 2017/4/15.
 */
public class Main {
    public static void main(String [] args){
        StockLogicImpl s =new StockLogicImpl();
        LocalDate now =LocalDate.of(2017,03,28);
        try {
            System.out.println("sefgrd");
            System.out.println(s.getStockBasicInfo("1",now).getClose());
        } catch (NullStockIDException e) {
            e.printStackTrace();
        } catch (NullDateException e) {
            e.printStackTrace();
        }


    }
}
