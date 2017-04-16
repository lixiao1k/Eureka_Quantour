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
        System.out.println(s.getStockSetSortedInfo("SHA",LocalDate.now().plusDays(-1),null));
//        s.setStrategy(stragetyConditionVo,saleVO,LocalDate.of(2013,01,01),LocalDate.of(2016,11,01),"SHA",null);
//        System.out.print(s.getYieldChartData().getJizhunlist().size());
        System.out.println();
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
