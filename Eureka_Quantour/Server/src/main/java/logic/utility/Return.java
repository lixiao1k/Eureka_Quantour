package logic.utility;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.*;
import po.SingleStockInfoPO;
import vo.SaleVO;
import vo.StrategyConditionVO;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by huihantao on 2017/4/6.
 */
public class Return {
    private Utility utility=Utility.getInstance();
    private IDataInterface idi = new DataInterfaceImpl();


    private List<String> stockcode;
    private LocalDate begin;
    private LocalDate end;
    private SaleVO salevo;
    private StrategyConditionVO strategyConditionVO;
    private int days;
    private Comparator<SingleStockInfoPO> comparator;
    private List<LocalDate> timelist;

    public Return(List<String> stockcode, LocalDate begin, LocalDate end, SaleVO salevo, StrategyConditionVO strategyConditionVO) {
        this.stockcode = stockcode;
        this.begin = begin;
        this.end = end;
        this.salevo = salevo;
        this.strategyConditionVO = strategyConditionVO;
        this.days=salevo.getTiaocangqi();
        String type=strategyConditionVO.getName();
        if (type.equals("动量策略")) comparator=new dongliangcelue(strategyConditionVO.getExtra());
        if (type.equals("均值策略")) comparator=new junzhicelue(strategyConditionVO.getExtra());
        LocalDate iter=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());

        try {
            for (;
                 iter.compareTo(end)<0;
                 iter=idi.addDays(iter,days)){
                timelist.add(LocalDate.of(iter.getYear(),iter.getMonth(),iter.getDayOfMonth()));
            }
        } catch (DateOverException e) {
            e.printStackTrace();
        }
    }

    public Double getAlpha(){
        return 0.0;
    }
    public Double getBeta(){
        return 0.0;
    }

    public List<LocalDate> getTimelist() {
        return timelist;
    }

    public List<Double> getBasicReturn ()
            throws PriceTypeException, NullStockIDException
    {

        List<Double> list=new ArrayList<>();


        LocalDate iter=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());

        double init=100.0;
        try {
            for (;
                 iter.compareTo(end)<0;
                 iter=idi.addDays(iter,days))
            {
                double zheci=0;
                double shangci=0;

                    for (String name : stockcode) {
                        try {
                        SingleStockInfoPO po1 = idi.getSingleStockInfo(name, iter);
                        SingleStockInfoPO po2 = idi.getSingleStockInfo(name, idi.addDays(iter, days));
                        zheci = zheci + getjiage(po1);
                        shangci = shangci + getjiage(po2);
                        }
                        catch (NullDateException e) {
                            continue;
                        }
                    }
                init=init*(zheci/shangci);
                double rate=(init-100)/100;
                list.add(rate);

            }
        } catch (DateOverException e) {
            e.printStackTrace();
        }

        return list;
    }




    public List<Double> getStragetyReturn ( ) throws  NullStockIDException, NullDateException, PriceTypeException {
        double init=100.0;
        List<Double> list=new ArrayList<>();
        LocalDate iter=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());


        try {
            for (;
                 iter.compareTo(end)<0;
                 iter=idi.addDays(iter,days))
            {
                double zheci=0;
                double shangci=0;

                List<SingleStockInfoPO> polist=new ArrayList<>();
                for(String name:stockcode){
                    polist.add(idi.getSingleStockInfo(name,iter));
                }
                Collections.sort(polist,comparator);
                List<String> jilu=new ArrayList<>();
                for (int i=0;i<strategyConditionVO.getNums();i++){
                    zheci=zheci+getjiage(polist.get(i));
                    jilu.add(polist.get(i).getCode());
                }


                for(String name:jilu){
                    SingleStockInfoPO po=idi.getSingleStockInfo(name,idi.addDays(iter,-days));
                    shangci+=getjiage(po);

                }


                init=init*(zheci/shangci);
                double rate=(init-100)/100;
                list.add(rate);


            }
        } catch (DateOverException e) {
            e.printStackTrace();
        }

        return list;
    }




        private double getjiage(SingleStockInfoPO po) throws PriceTypeException {
        String type=salevo.getTiaocangjiage();
        if (type.equals("收盘价")) return po.getClose();
        if (type.equals("开盘价")) return po.getOpen();
        throw new PriceTypeException();
    }


    private  class junzhicelue implements Comparator<SingleStockInfoPO> {

        private int days;
        private junzhicelue(List<Object> objects){
            this.days=(Integer) objects.get(0);
        }


        @Override
        public int compare(SingleStockInfoPO o1, SingleStockInfoPO o2) {
            String name1=o1.getCode();
            String name2=o2.getCode();
            LocalDate now=o1.getDate();
            double junzhi1=0.0;
            double junzhi2=0.0;
            try {
                junzhi1=utility.getEMA(name1,now,days);
                junzhi2=utility.getEMA(name2,now,days);
            } catch (DateOverException e) {
                e.printStackTrace();
            } catch (NullStockIDException e) {
                e.printStackTrace();
            }
            double rate1=0.0;
            double rate2=0.0;
            try {
                rate1=(getjiage(o1)-junzhi1)/junzhi1;
                rate2=(getjiage(o1)-junzhi1)/junzhi1;
            } catch (PriceTypeException e) {
                e.printStackTrace();
            }

            return (int) (rate1-rate2)*100;
        }
    }





    private class dongliangcelue implements Comparator<SingleStockInfoPO> {

        private int days;
        private dongliangcelue(List<Object> objects){
            this.days=(Integer) objects.get(0);
        }


        @Override
        public int compare(SingleStockInfoPO o1, SingleStockInfoPO o2) {

            String name1=o1.getCode();
            String name2=o2.getCode();

            LocalDate now=o1.getDate();
            LocalDate before=now.minusDays(days);
            try {
                before = idi.addDays(now,-days);
            } catch (DateOverException e) {
                e.printStackTrace();
            }

            SingleStockInfoPO o3;
            SingleStockInfoPO o4;
            double rate1=0;
            double rate2=0;
            try {
                o3 = idi.getSingleStockInfo(name1,before);
                o4 = idi.getSingleStockInfo(name2,before);
                rate1=(o3.getAftClose()-o1.getAftClose())/o3.getAftClose();
                rate2=(o4.getAftClose()-o2.getAftClose())/o4.getAftClose();
            } catch (NullStockIDException e) {
                e.printStackTrace();
            } catch (NullDateException e) {
                e.printStackTrace();
            }



            return (int) (rate1-rate2)*100;

        }
    }







}
