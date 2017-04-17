package logic.serviceimpl;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.*;
import logic.utility.Utility;
import po.SingleStockInfoPO;
import vo.SaleVO;
import vo.SingleStockInfoVO;
import vo.StrategyConditionVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huihantao on 2017/4/15.
 */
public class Main {
    public static void main(String [] args){
        StockLogicImpl s =new StockLogicImpl();
        Utility utility=Utility.getInstance();
        StrategyConditionVO stragetyConditionVo=new StrategyConditionVO();
        stragetyConditionVo.setName("动量策略");
        List<Object> list=new ArrayList<>();
        list.add(new Integer(20));
        stragetyConditionVo.setNums(10);
        stragetyConditionVo.setExtra(list);
        SaleVO saleVO=new SaleVO();
        saleVO.setTiaocangqi(10);


        try {
            SingleStockInfoVO vo=s.getStockBasicInfo("603726",LocalDate.of(2016,04,26));
            System.out.println(vo.getOpen());
        } catch (NullStockIDException e) {
            e.printStackTrace();
        } catch (NullDateException e) {
            e.printStackTrace();
        }
//        try {
//            List<SingleStockInfoVO> p=s.getStockSetSortedInfo("SHA",LocalDate.now().of(2016,3,6),null);
//        } catch (NullMarketException e) {
//            e.printStackTrace();
//        }
//        SingleStockInfoVO vo= null;
//        try {
//            vo = s.getStockBasicInfo("000900", LocalDate.of(2016,03,8));
//        } catch (NullStockIDException e) {
//            e.printStackTrace();
//        } catch (NullDateException e) {
//            e.printStackTrace();
//        }
//        System.out.println(vo.getFudu());
//        for (SingleStockInfoVO vo:p){
//            System.out.println(vo.getClose()+"  "+vo.getFudu()+" "+vo.getOpen());
//        }
//        s.setStrategy(stragetyConditionVo,saleVO,LocalDate.of(2016,01,01),LocalDate.of(2017,1,1),"SHA",null);
//        s.getYieldChartData().getBeta();
//        System.out.print(s.getYieldChartData().getSharpe());
//        System.out.println();
//        System.out.print(s.getYieldChartData().getCeluelist().size());
//        IDataInterface s=new DataInterfaceImpl();
//        LocalDate p=LocalDate.of(2015,01,01);
//
//        try {
//            p=s.addDays(p,10);
//            System.out.println(p);
//        } catch (DateOverException e) {
//            e.printStackTrace();
//        }


    }
}
