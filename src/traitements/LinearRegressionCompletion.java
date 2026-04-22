package traitements;

import java.util.ArrayList;

import model.*;
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
            //poisson.setInfestationRate(model.predict(size));
        }
    }

    public  void Complete(DataFrame<Fish> fish , String X ,String Y){

        if(X.equals(Y) || fish.getData().size() == 0)return ;

        ArrayList<Double> x = null ;
        ArrayList<Double> y = null ;
        ArrayList<Fish> unknown = new ArrayList<>() ;

        switch(X){
            case "Length":
                x = fish.getLengths() ;
                break ;
            
             case "Weight":
                x = fish.getWeights() ;
                break ;

             case "Size":
                x = fish.getSizes() ;
                break ;

            case "Parasites":
                x = fish.getParasites();
                break ;
            
             case "InfestationRate":
                x = fish.getInfestationRates() ;
                break ;
            
        }

        switch(Y){
            case "Length":
                y = fish.getLengths() ;
                break ;
            
             case "Weight":
                y = fish.getWeights() ;
                break ;

             case "Size":
                y = fish.getSizes() ;
                break ;
            
            case  "Parasites":
                y = fish.getParasites() ;
            
             case "InfestationRate":
                y = fish.getInfestationRates() ;
                break ;
            
        }

        for(int i = 0 ; i < y.size() ; i++){
            if(y.get(i) == null)
                unknown.add(fish.getData().get(i));
        }

        if(unknown.size() == 0){
            return ;
        } ;
        
        LinearRegression model= new LinearRegression(x, y) ;

        if(model.getCoeff() == null || model.getIntercept() == null){
            return ;
        } ;

        y = model.predict(x) ;


        switch(Y){
            case "Length":
                for(int i = 0 ; i < unknown.size() ;i++){
                    unknown.get(i).setLength(y.get(i));
                }
                break ;
            
             case "Weight":
                for(int i = 0 ; i < unknown.size() ;i++){
                    unknown.get(i).setWeight(y.get(i));
                }
                break ;
            
            case "Parasites":
                for(int i = 0 ; i < unknown.size() ;i++){
                    unknown.get(i).setParasites(y.get(i).intValue());
                }
                break ;

             case "Size":
                for(int i = 0 ; i < unknown.size() ;i++){
                    unknown.get(i).setSize(y.get(i));
                }
                break ;
            
             case "InfestationRate":
                for(int i = 0 ; i < unknown.size() ;i++){
                    unknown.get(i).setInfestationRate(y.get(i));
                }
                break ;
            
        }
        

    }







   

    
}
