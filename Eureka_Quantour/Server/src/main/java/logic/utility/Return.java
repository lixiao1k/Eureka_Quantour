package logic.utility;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.PriceTypeException;
import po.SingleStockInfoPO;
import vo.SaleVO;
import vo.StrategyConditionVO;

import java.util.*;

/**
 * Created by huihantao on 2017/4/6.
 */
public class Return {
    private Utility utility=Utility.getInstance();
    private IDataInterface idi = new DataInterfaceImpl();


    private List<String> stockcode;
    private Calendar begin;
    private Calendar end;
    private SaleVO salevo;
    private StrategyConditionVO strategyConditionVO;
    private int days;
    private Comparator<SingleStockInfoPO> comparator;

    public Return(List<String> stockcode, Calendar begin, Calendar end, SaleVO salevo, StrategyConditionVO strategyConditionVO) {
        this.stockcode = stockcode;
        this.begin = begin;
        this.end = end;
        this.salevo = salevo;
        this.strategyConditionVO = strategyConditionVO;
        this.days=salevo.getTiaocangqi();
        String type=strategyConditionVO.getName();
        if (type.equals("动量策略")) comparator=new dongliangcelue(strategyConditionVO.getExtra());
        if (type.equals("均值策略")) comparator=new junzhicelue(strategyConditionVO.getExtra());
    }

    public List<Double> getBasicReturn () throws PriceTypeException {

        List<Double> list=new ArrayList<>();



        double init=100.0;
        for (Calendar beginclone=(Calendar) begin.clone();
             beginclone.compareTo(end)<0;
             utility.calendarAfter(beginclone,days))
        {
            double zheci=0;
            double shangci=0;

            for (String name:stockcode) {
                SingleStockInfoPO po1 = idi.getSingleStockInfo(name, beginclone);
                Calendar shangciriqi=(Calendar) beginclone.clone();
                utility.calendarAdvance(shangciriqi,days);
                SingleStockInfoPO po2 = idi.getSingleStockInfo(name, shangciriqi);
                zheci=zheci+getjiage(po1);
                shangci=shangci+getjiage(po2);

            }
            init=init*(zheci/shangci);
            double rate=(init-100)/100;
            list.add(rate);

        }

        return list;
    }

    public List<Double> getStragetyReturn ( )  {
        double init=100.0;
        List<Double> list=new ArrayList<>();


        for (Calendar beginclone=(Calendar) begin.clone();
             beginclone.compareTo(end)<0;
             utility.calendarAfter(beginclone,days))
        {
            double zheci=0;
            double shangci=0;

            List<SingleStockInfoPO> polist=new ArrayList<>();
            List<SingleStockInfoPO> celuepolist=new ArrayList<>();
            for(String name:stockcode){
                polist.add(idi.getSingleStockInfo(name,beginclone));
            }
            Collections.sort(polist,comparator);
            for (int i=0;i<strategyConditionVO.getNums();i++){
                zheci=zheci+polist.get(i).getClose();
            }

            Calendar shangciriqi=(Calendar) beginclone.clone();
            utility.calendarAdvance(shangciriqi,days);
            for(String name:stockcode){
                polist.add(idi.getSingleStockInfo(name,beginclone));
            }
            Collections.sort(polist,comparator);
            for (int i=0;i<strategyConditionVO.getNums();i++){
                shangci=shangci+polist.get(i).getClose();
            }

            init=init*(zheci/shangci);
            double rate=(init-100)/100;
            list.add(rate);


        }

        return list;
    }




        private double getjiage(SingleStockInfoPO po) throws PriceTypeException {
        String type=salevo.getTiaocangjiage();
        if (type.equals("收盘价")) return po.getClose();
        if (type.equals("开盘价")) return po.getOpen();
        throw new PriceTypeException();
    }


    private static class junzhicelue implements Comparator<SingleStockInfoPO> {

        private int days;
        private junzhicelue(List<Object> objects){
            this.days=(Integer) objects.get(0);
        }


        @Override
        public int compare(SingleStockInfoPO o1, SingleStockInfoPO o2) {


            


            return 0;
        }
    }





    private class dongliangcelue implements Comparator<SingleStockInfoPO> {

        private int days;
        private dongliangcelue(List<Object> objects){
            this.days=(Integer) objects.get(0);
        }


        @Override
        public int compare(SingleStockInfoPO o1, SingleStockInfoPO o2) {

            String name1=o1.getName();
            String name2=o2.getName();

            Calendar now=o1.getDate();
            Calendar before=(Calendar) now.clone();
            utility.calendarAdvance(before,days);

            SingleStockInfoPO o3=idi.getSingleStockInfo(name1,before);
            SingleStockInfoPO o4=idi.getSingleStockInfo(name2,before);

            double rate1=(o3.getAdjclose()-o1.getAdjclose())/o3.getAdjclose();
            double rate2=(o4.getAdjclose()-o2.getAdjclose())/o4.getAdjclose();

            return (int) (rate2-rate1)*100;

        }
    }





}
