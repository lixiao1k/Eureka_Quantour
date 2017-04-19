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
    private List<Double> jizhunfudu=new ArrayList<>();
    private List<Double> celuefudu=new ArrayList<>();

    private List<Double> jizhunshouyilv;
    private List<Double> celueshouyilv;


    public Return(List<String> stockcode, LocalDate begin, LocalDate end, SaleVO salevo, StrategyConditionVO strategyConditionVO) {
        this.stockcode = stockcode;
        this.begin = begin;
        this.end = end;
        this.salevo = salevo;
        this.strategyConditionVO = strategyConditionVO;
        this.days=salevo.getTiaocangqi();
        this.timelist=new ArrayList<>();
        String type=strategyConditionVO.getName();
        if (type.equals("动量策略")) comparator=new dongliangcelue(strategyConditionVO.getExtra());
        if (type.equals("均值策略")) comparator=new junzhicelue(strategyConditionVO.getExtra());

    }

    public Double getAlpha(){
        double rp=gerYearReturn();
        double rm=gerBasicYearReturn();
        double rf=0.04;
        return rp-(rf+(rm-rf)*getBeta());


    }
    public Double getBeta(){

        return utility.getCorvariance(jizhunfudu,celuefudu)/utility.getCorvariance(jizhunfudu,jizhunfudu);
    }

    public Double gerYearReturn(){


        return getYear(celueshouyilv);
    }
    public Double gerBasicYearReturn(){

        return getYear(jizhunshouyilv);
    }

    private Double getYear(List<Double> list){
        double shuzi=list.get(list.size()-1);
        int i=0;
        LocalDate itr=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());
        for (;itr.compareTo(end)<=0;itr=itr.plusDays(1)){
            i++;
        }

        double a=1+shuzi;
        double b=365.0/i;
        return Math.pow(a,b)-1;
    }


    public Double getSharpe(){
        double ri=gerYearReturn();
        double rf=0.04;
        double seta=utility.getCorvariance(celueshouyilv,celueshouyilv);
        return (ri-rf)/seta;
    }


    public List<LocalDate> getTimelist() {
        return timelist;
    }

    public List<Double> getBasicReturn ()
            throws PriceTypeException, NullStockIDException {
    	if(jizhunshouyilv==null){
    		 List<Double> list=new ArrayList<>();


    	        LocalDate iter=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());

    	        double init=100.0;
    	        try {
    	            for (;
    	                 iter.compareTo(end)<=0;
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

    	                    if(zheci==0) {
    	                        continue;
    	                    }
    	                    timelist.add(iter);
    	                jizhunfudu.add(shangci/zheci);
    	               System.out.println(zheci+"  "+shangci);
    	                init=init*(shangci/zheci);
    	                double rate=(init-100)/100;
    	                list.add(rate);

    	            }
    	        } catch (DateOverException e) {
    	            e.printStackTrace();
    	        }
    	        jizhunshouyilv=list;

    	        return jizhunshouyilv;
    	}
    	else{
    		return jizhunshouyilv;
    	}
    }




    public List<Double> getStragetyReturn ( ) throws  NullStockIDException, PriceTypeException {
        if(celueshouyilv==null){
        	double init=100.0;
            List<Double> list=new ArrayList<>();
            LocalDate iter=LocalDate.of(begin.getYear(),begin.getMonth(),begin.getDayOfMonth());
            try {
                for (;
                     iter.compareTo(end)<=0;
                     iter=idi.addDays(iter,days))
                {
                    double zheci=0;
                    double shangci=0;
                    List<SingleStockInfoPO> polist=new ArrayList<>();

                    for(String name:stockcode){
                        try {

                            polist.add(idi.getSingleStockInfo(name,iter));
                        } catch (NullDateException e) {
                            continue;
                        }
                    }
                    if(polist.size()==0) continue;

                    Collections.sort(polist,comparator);
//                    for (int as=0;as<polist.size();as++)
//                    System.out.println(polist.get(as).getName());

                    List<String> jilu=new ArrayList<>();



//                    System.out.println("assd");
                    int i=0;
                    int j=0;
                    while (j<Math.min(strategyConditionVO.getNums(),polist.size())&&i<Math.min(strategyConditionVO.getNums(),polist.size())){
//                        System.out.print(getjiage(polist.get(i)));
                        
                        SingleStockInfoPO pozheci=polist.get(i);
                        i++;
                        SingleStockInfoPO poshangci = null;

                        try {
                            poshangci = idi.getSingleStockInfo(pozheci.getCode(),idi.addDays(iter,days));
                        } catch (NullDateException e) {
//                            System.out.println("as");
                            continue;
                        }
                        zheci=zheci+getjiage(pozheci);
                        shangci+=getjiage(poshangci);
                        
                        j++;
                    }




                    
                    if(zheci==0) System.exit(0);
                    celuefudu.add(shangci/zheci);
                    init=init*(shangci/zheci);

                    double rate=(init-100)/100;
                    list.add(rate);


                }
            } catch (DateOverException e) {
//                e.printStackTrace();
            }
            celueshouyilv=list;
            return list;
        }
        else{
        	return celueshouyilv;
        }
    }




    private double getjiage(SingleStockInfoPO po) throws PriceTypeException {
        String type=salevo.getTiaocangjiage();
        if (type.equals("收盘价")) return po.getAftClose();
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

            if (junzhi1==Integer.MAX_VALUE && junzhi2==Integer.MAX_VALUE)
            {
                return 0;
            }
            if (junzhi1== Integer.MAX_VALUE){
                return Integer.MAX_VALUE;
            }
            if (junzhi2== Integer.MAX_VALUE){
                return -Integer.MAX_VALUE;
            }
            try {
                rate1=(getjiage(o1)-junzhi1)/junzhi1;
                rate2=(getjiage(o2)-junzhi2)/junzhi2;
            } catch (PriceTypeException e) {
                e.printStackTrace();
            }

            int p=(int)(rate1*10000);
            int q=(int)(rate2*10000);


            return q-p;

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
            }

//            System.out.println(before);
            SingleStockInfoPO o3=null;
            SingleStockInfoPO o4=null;
            double rate1=0;
            double rate2=0;
            try {
                o3 = idi.getSingleStockInfo(name1,before);


            } catch (NullStockIDException e) {
                e.printStackTrace();
            } catch (NullDateException e) {
//                e.printStackTrace();
            }

            try {
                o4 = idi.getSingleStockInfo(name2,before);
            } catch (NullStockIDException e) {
                e.printStackTrace();
            } catch (NullDateException e) {
//                e.printStackTrace();
            }

            if ((o3==null)&&(o4==null))
                return 0;

            if (o3==null)
                return Integer.MAX_VALUE;
            if (o4==null)
                return -Integer.MAX_VALUE;

            rate1=(o1.getAftClose()-o3.getAftClose())/o3.getAftClose();
            rate2=(o2.getAftClose()-o4.getAftClose())/o4.getAftClose();
            int p=(int)(rate1*10000);
            int q=(int)(rate2*10000);



            return q-p;

        }
    }







}
