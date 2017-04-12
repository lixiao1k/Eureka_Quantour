package logic.utility;

import data.service.IDataInterface;
import data.serviceimpl.DataInterfaceImpl;
import exception.BeginInvalidException;
import exception.DateInvalidException;
import exception.EndInvalidException;
import exception.NullStockIDException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Calendar;

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



    public void ifDateValid(LocalDate begin, LocalDate end,String stockcode) throws BeginInvalidException, DateInvalidException, EndInvalidException, NullStockIDException {

        LocalDate start= idi.getMinDay(stockcode);
        LocalDate jieshu= idi.getMinDay(stockcode);
        if (end.compareTo(start)<0)throw new EndInvalidException();
        if (begin.compareTo(jieshu)<0)throw new EndInvalidException();
        if (begin.compareTo(end)>0)
            throw new DateInvalidException();
    }

//    public double calVariance(double[] num){
//        int length = num.length;
//        if( length==0 )
//            return 0.0;
//        double result = 0.0;
//        double tempD1 = 0.0;
//        double tempD2 = 0.0;
//        for(int i=0;i<length;i++){
//            tempD1 += Math.pow(num[i], 2);
//            tempD2 += num[i];
//        }
//        tempD2 = Math.pow(tempD2, 2);
//        result = (tempD1 - tempD2/length) / length;
//        return formatDoubleSaveFive( result );
//    }
//
//    public double formatDoubleSaveFive(double d){
//        DecimalFormat df = new DecimalFormat("#0.00000");
//        return Double.parseDouble( df.format(d) );
//    }
//
//    public double formatDoubleSaveTwo(double d){
//        DecimalFormat df = new DecimalFormat("#0.00");
//        return Double.parseDouble( df.format(d) );
//    }
//
//    public boolean ifDoubleEqual(double d1, double d2){
//        String s1 = String.valueOf(formatDoubleSaveTwo(d1));
//        String s2 = String.valueOf(formatDoubleSaveTwo(d2));
//        return s1.equals(s2);
//    }
//
//    public void calendarAdvance(Calendar cal){
//        if( cal.get(Calendar.DAY_OF_WEEK) == 2 )
//            cal.add(Calendar.DAY_OF_MONTH, -3); // 星期一
//        else if( cal.get(Calendar.DAY_OF_WEEK) == 1 )
//            cal.add(Calendar.DAY_OF_MONTH, -2); //星期天
//        else
//            cal.add(Calendar.DAY_OF_MONTH, -1);
//    }
//    public void calendarAfter(Calendar cal){
//        if( cal.get(Calendar.DAY_OF_WEEK) == 6 )
//            cal.add(Calendar.DAY_OF_MONTH, 3);
//        else if( cal.get(Calendar.DAY_OF_WEEK) == 7 )
//            cal.add(Calendar.DAY_OF_MONTH, 2);
//        else
//            cal.add(Calendar.DAY_OF_MONTH, 1);
//    }
//    public void calendarAfter(Calendar cal,int days){
//        for (int i=0;i<days;i++){
//            calendarAfter(cal);
//        }
//    }
//    public void calendarAdvance(Calendar cal,int days){
//        for (int i=0;i<days;i++){
//            calendarAdvance(cal);
//        }
//    }

}