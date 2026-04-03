package tools;

import java.util.ArrayList;

public class LinearRegression {

    private Double a = null;
    private Double b = null ;

    public LinearRegression(ArrayList<Double> x , ArrayList<Double> y){

        if(x.size() != y.size())return ;

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        int n = 0;

        for(int i = 0; i < x.size(); i++){

            Double xi = x.get(i);
            Double yi = y.get(i);

            if(xi == null || yi == null) continue;

            sumX += xi;
            sumY += yi;
            sumXY += xi * yi;
            sumX2 += xi * xi;
            n++;
        }

        if(n < 2) return;

        double denom = n * sumX2 - sumX * sumX;
        if(denom == 0) return;

        a = (n * sumXY - sumX * sumY) / denom;
        b = (sumY - a * sumX) / n;

    }

    public ArrayList<Double> predict(ArrayList<Double> x){
        if(a == null || b == null)return null ;
        ArrayList<Double> y = new ArrayList<>() ;
        for(Double elm : x){
            if(elm != null)y.add(elm*a+b);
        }
        return y ;
    }

    public Double predict(Double x ){
        if(x == null || a == null || b == null)return null ;
        return  a*x + b ;
    }


    public Double getCoeff(){return this.a ;}

    public Double getIntercept(){return this.b ;}
    
}
