package traitements;

import java.util.ArrayList;

import model.DataFrame;
import model.Fish;
import tools.*;

public class LinearRegressionCompletion extends Traitement {


    @Override
    public void complete(DataFrame<Fish> fish) {
        ArrayList<Fish> poissons = new ArrayList<>(fish.getData());
        DataFrame<Fish> Known = new DataFrame(getKnownValues(poissons));
        if(Known.getData().size() < 2)return ;
        ArrayList<Double> x = Known.getSizes() ;
        ArrayList<Double> y = Known.getInfestationRates() ;
       
        LinearRegression model = new LinearRegression(x, y);
        Double a = model.getCoeff();
        Double b = model.getIntercept() ;
        if(a == null || b == null){
            // Pourquoi: la régression n'a pas pu être calculée (ex: division par zéro).
            return;
        }
        
        DataFrame<Fish> Unknown = new DataFrame(getUnknownValues(poissons));

        for(Fish poisson : Unknown.getData()){
            Double size =  poisson.getSize();
            Double infestationRate = poisson.getInfestationRate();
            if(size == null || infestationRate != null) continue;
            poisson.setInfestationRate(model.predict(size));
        }
    }


   

    
}
