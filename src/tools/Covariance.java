package tools;

import java.util.ArrayList;

public class Covariance {

    public static Double covariance(ArrayList<Double> x ,ArrayList<Double> y){

        if(x.size() != y.size())return null ;

        Double mx = new MeanValue(x).getMean();
        Double my = new MeanValue(y).getMean();

        int N = x.size() ;

        double Cov = 0  ;
        for(int i = 0 ; i < N ; i++){
            Cov += (x.get(i)-mx)*(y.get(i)-my);
        }
        return Cov / N ;

    }
    
}
