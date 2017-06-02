package logic.utility;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import po.SingleStockInfoPO;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

/**
 * Created by huihantao on 2017/4/4.
 */
public class Utility {
    private IDataInterface idi=new DataInterfaceImpl();
    private static Utility utility=new Utility();
    private Utility(){

    }
    public static Utility getInstance(){
        return utility;
    }


    public void ifExpDateValid(LocalDate begin, LocalDate end,String name) throws BeginInvalidException, DateInvalidException, EndInvalidException, NullStockIDException {

        LocalDate start= idi.getExponentMinDay(name);
        LocalDate jieshu= idi.getExponentMaxDay(name);
        if (end.compareTo(start)<0)throw new EndInvalidException();
        if (begin.compareTo(jieshu)>0)throw new EndInvalidException();
        if (begin.compareTo(end)>0)
            throw new DateInvalidException();
    }
    public void ifDateValid(LocalDate begin, LocalDate end,String stockcode) throws BeginInvalidException, DateInvalidException, EndInvalidException, NullStockIDException {

        LocalDate start= idi.getMinDay(stockcode);
        LocalDate jieshu= idi.getMaxDay(stockcode);
        if (end.compareTo(start)<0)throw new EndInvalidException();
        if (begin.compareTo(jieshu)>0)throw new EndInvalidException();
        if (begin.compareTo(end)>0)
            throw new DateInvalidException();
    }

    public double getEMA(String stockCode,LocalDate time,int days) throws DateOverException, NullStockIDException {
        double jieguo=0.0;
        int p=0;
        for (int i=1;i<=days;i++)
        {
            try {
                SingleStockInfoPO po = idi.getSingleStockInfo(stockCode, idi.addDays(time, -i));
                jieguo+=po.getAftClose();
            }
            catch (NullDateException e) {
                p++;
                continue;
            }

        }
        if (p==days) {
            return Integer.MAX_VALUE;
        }
        return  jieguo/(days-p);
    }

    public double getCorvariance(List<Double> A,List<Double> B){
        double avejizhun=0;
        double avecelue=0;
        double p=0;
        for (int i=0;i<A.size();i++){
            avejizhun+=A.get(i);
        }
        avejizhun=avejizhun/A.size();

        for (int i=0;i<B.size();i++){
            avecelue+=B.get(i);
        }
        avecelue=avecelue/B.size();

        for (int i=0;i<B.size();i++){
            p+=A.get(i)*B.get(i);
        }
        p=p/A.size();
        return p-avecelue*avejizhun;
    }

    public double getAverage(List<Double> A){
        double zonghe=0;
        for (int i=0;i<A.size();i++){
            zonghe+=A.get(i);
        }
        return  zonghe/A.size();
    }

}
