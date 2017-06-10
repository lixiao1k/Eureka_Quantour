package logic.utility;

import data.service.ICompanyDataInterface;
import data.service.IDataInterface;
import data.service.IStockDataInterface;
import data.service.IStockSetInterface;
import data.service.IStrategyDataInterface;
import data.serviceimpl.CompanyDataController;
import data.serviceimpl.DataInterfaceImpl;
import data.serviceimpl.StockDataController_2;
import data.serviceimpl.StockSetDataController;
import data.serviceimpl.StrategyDataController;
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
	private IStockSetInterface set = StockSetDataController.getInstance();
	private IStrategyDataInterface strategy = StrategyDataController.getInstance();
    private String username;
    private String name;
    public SaveThread(StrategyConditionVO strategyConditionVO, SaleVO saleVO,String username,String name){
        this.username=username;
        this.name=name;
        List<String> stocklistname=set.getStockSetInfo("HS300",null);
        LocalDate now=LocalDate.of(2016,1,1);
        LocalDate start=now.minusDays(100);
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

        StrategyShowPO po=new StrategyShowPO(timelist,basicreturn,strategylist,alpha,beta,sharp,zuidahuiche,yearreturn,jizhunyearreturn);
        strategy.addStrategyShow(username,name,po);
    }

}
