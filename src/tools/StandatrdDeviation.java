package tools;

import java.util.ArrayList;

public class StandatrdDeviation {

    public static Double std(ArrayList<Double> x){

        if(x.size() == 0)return  null ;

        Double mx = new MeanValue(x).getMean() ;

        double s = 0 ;


        for(Double v : x){
            if(v != null){
                s += v - mx*mx ;
            }
        }
        return Math.sqrt(s  / x.size()) ;
    }

    
    
}
