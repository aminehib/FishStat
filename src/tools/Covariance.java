package tools;

import java.util.ArrayList;

public class Covariance {

public static Double covariance(ArrayList<Double> x ,ArrayList<Double> y){

    if(x == null || y == null || x.size() != y.size()) return null;

    ArrayList<Double> xf = new ArrayList<>();
    ArrayList<Double> yf = new ArrayList<>();

    for(int i = 0; i < x.size(); i++){
        if(x.get(i) != null && y.get(i) != null){
            xf.add(x.get(i));
            yf.add(y.get(i));
        }
    }

    int n = xf.size();
    if(n < 2) return null;

    double mx = new MeanValue(xf).getMean();
    double my = new MeanValue(yf).getMean();

    double cov = 0;

    for(int i = 0; i < n; i++){
        cov += (xf.get(i) - mx) * (yf.get(i) - my);
    }

    return cov / n;
}
    
}
