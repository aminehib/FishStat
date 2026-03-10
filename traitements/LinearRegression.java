package traitements;

import java.util.ArrayList;
import model.Fish;

public class LinearRegression extends Traitement {

    private Double a = null;
    private Double b = null ;


    @Override
    public void complete(ArrayList<Fish> poissons) {
        ArrayList<Fish> Known = getKnownValues(poissons);
        if(Known.size() < 2)return ;
        ArrayList<Double> x = new ArrayList<>() ;
        ArrayList<Double> y = new ArrayList<>() ;
        for(int i = 0 ; i < Known.size() ; i++){
            if(  Known.get(i).getSize() != null ){
                x.add(Known.get(i).getSize());
                y.add(Known.get(i).getInfestationRate());
            }
        }
        if(x.size() < 2)return ;
        linearRegression(x, y);
        ArrayList<Fish>Unknown = getUnknownValues(poissons);
        for(int i = 0 ; i < Unknown.size() ; i++){
            Unknown.get(i).setInfestationRate(a * Unknown.get(i).getSize() + b);
        }
    }


    private void linearRegression(ArrayList<Double> x , ArrayList<Double> y){

        double sumX  = 0 ;
        double sumY = 0 ;
        double sumXY = 0 ;
        double sumX2 = 0 ;
        int n = x.size();

        for(int i = 0 ; i < n; i++){
            sumX += x.get(i);
            sumY += y.get(i);
            sumXY += x.get(i) * y.get(i);
            sumX2 += x.get(i) * x.get(i);
        }

        a = ( n * sumXY - sumX * sumY) / ( n * sumX2 - sumX * sumX);

        b = (sumY - a * sumX) / n ;

    }

    public double getCoeff(){return this.a ;}

    public double getIntercept(){return this.b ;}

    
}
