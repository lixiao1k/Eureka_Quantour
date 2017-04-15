package logic.serviceimpl;

import logic.utility.Utility;

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
        LocalDate now =LocalDate.of(2017,03,28);
        List<Double> A=new ArrayList<>();
        A.add(Math.random()*100);
        A.add(Math.random()*100);
        A.add(Math.random()*100);
        A.add(Math.random()*100);
        A.add(Math.random()*100);
        A.add(Math.random()*100);
        A.add(Math.random()*100);
        A.add(Math.random()*100);
        List<Double> B=new ArrayList<>();
        B.add(Math.random()*100);
        B.add(Math.random()*100);
        B.add(Math.random()*100);
        B.add(Math.random()*100);
        B.add(Math.random()*100);
        B.add(Math.random()*100);
        B.add(Math.random()*100);
        B.add(Math.random()*100);
        for (int i=0;i<8;i++){
            System.out.print(A.get(i)+",");
        }
        System.out.println();
        for (int i=0;i<8;i++){
            System.out.print(B.get(i)+",");
        }
            System.out.println(utility.getCorvariance(A,B));



    }
}
