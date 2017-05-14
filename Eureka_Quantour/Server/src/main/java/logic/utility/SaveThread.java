package logic.utility;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.NullStockIDException;
import exception.PriceTypeException;
import vo.SaleVO;
import vo.StrategyConditionVO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by huihantao on 2017/5/13.
 */
public class SaveThread extends Thread{
    private Return stragety;
    private IDataInterface idi = new DataInterfaceImpl();
    private String username;
    private String name;
    public SaveThread(StrategyConditionVO strategyConditionVO, SaleVO saleVO,String username,String name){
        this.username=username;
        this.name=name;
        List<String> stocklistname=idi.getStockSetInfo("HS300",null);
        LocalDate now=LocalDate.now();
        LocalDate start=now.minusDays(100);
        stragety=new Return(stocklistname,start,now,saleVO,strategyConditionVO);

    }

    public void run(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stragety.getTimelist();
        stragety.getAlpha();
        stragety.getBeta();
        stragety.getSharpe();
        stragety.gerYearReturn();
        try {
            stragety.getBasicReturn();
            stragety.getStragetyReturn();
        } catch (NullStockIDException e) {
            e.printStackTrace();
        } catch (PriceTypeException e) {
            e.printStackTrace();
        }

        //idi.save(username,name,timelist, basicreturn ,stragetyreturn yearreturn,zuidahuiche);


    }

}
