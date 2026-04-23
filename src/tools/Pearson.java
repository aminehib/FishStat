package tools;

import java.util.ArrayList;

public class Pearson {

    public static Double pearson(ArrayList<Double> x , ArrayList<Double> y){
        return Covariance.covariance(x, y) / (StandatrdDeviation.std(x) * StandatrdDeviation.std(y)) ;
    }
    
}
