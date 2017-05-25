package logic.utility;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.NullStockIDException;
import exception.PriceTypeException;
import po.StrategyShowPO;
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
        LocalDate now=LocalDate.of(2016,1,1);
        LocalDate start=now.minusDays(100);
        System.out.println(strategyConditionVO.getName());
        stragety=new Return(stocklistname,start,now,saleVO,strategyConditionVO);

    }

    public void run(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Double> basicreturn=null;
        List<Double> strategylist=null;
        try {
            basicreturn =stragety.getBasicReturn();
            strategylist= stragety.getStragetyReturn();
        } catch (NullStockIDException e) {
//            e.printStackTrace();
        } catch (PriceTypeException e) {
//            e.printStackTrace();
        }
        List<LocalDate> timelist=stragety.getTimelist();
        double alpha=stragety.getAlpha();
        double beta=stragety.getBeta();
        double sharp=stragety.getSharpe();
        double yearreturn=stragety.gerYearReturn();
        double jizhunyearreturn=stragety.gerBasicYearReturn();
        double zuidahuiche=stragety.getzuidaguiceh();


        System.out.println("aes  asf "+yearreturn);

        StrategyShowPO po=new StrategyShowPO(timelist,basicreturn,strategylist,alpha,beta,sharp,zuidahuiche,yearreturn,jizhunyearreturn);
        idi.addStrategyShow(username,name,po);
    }

}
