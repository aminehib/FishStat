package tools;

import java.util.ArrayList;

public class StandatrdDeviation {

    public static Double std(ArrayList<Double> x){

    if(x == null || x.size() == 0) return null;

    ArrayList<Double> xf = new ArrayList<>();

    for(Double v : x){
        if(v != null){
            xf.add(v);
        }
    }

    int n = xf.size();
    if(n == 0) return null;

    double mean = new MeanValue(xf).getMean();

    double sum = 0;

    for(Double v : xf){
        sum += (v - mean) * (v - mean);
    }

    return Math.sqrt(sum / n);
}

    
    
}
