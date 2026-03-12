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
            Double size = Known.get(i).getSize();
            Double rate = Known.get(i).getInfestationRate();
            if(size != null && rate != null){
                x.add(size);
                y.add(rate);
            }
        }
        if(x.size() < 2)return ;
        linearRegression(x, y);
        if(a == null || b == null){
            // Pourquoi: la régression n'a pas pu être calculée (ex: division par zéro).
            return;
        }
        ArrayList<Fish>Unknown = getUnknownValues(poissons);
        for(int i = 0 ; i < Unknown.size() ; i++){
            Double size = Unknown.get(i).getSize();
            if(size == null) {
                // Pourquoi: impossible de prédire sans taille.
                continue;
            }
            Unknown.get(i).setInfestationRate(a * size + b);
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

        double denom = ( n * sumX2 - sumX * sumX);
        if(denom == 0){
            // Pourquoi: éviter une division par zéro si toutes les tailles sont identiques.
            return;
        }
        a = ( n * sumXY - sumX * sumY) / denom;

        b = (sumY - a * sumX) / n ;

    }

    public double getCoeff(){return this.a ;}

    public double getIntercept(){return this.b ;}

    
}
