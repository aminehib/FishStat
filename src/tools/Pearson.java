package tools;

import java.util.ArrayList;

public class Pearson {

    public static Double pearson(ArrayList<Double> x , ArrayList<Double> y){
        Double cov = Covariance.covariance(x, y) ;
        Double stdx = StandatrdDeviation.std(x) ;
        Double stdy = StandatrdDeviation.std(y) ;
        
        if(cov == null || stdx == null || stdy ==null)return null ;
        if(stdx*stdy == 0)return null ;
        return cov / (stdx*stdy) ;
    }
    
}
