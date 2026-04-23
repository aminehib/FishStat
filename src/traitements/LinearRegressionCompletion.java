package traitements;

import java.util.ArrayList;

import model.*;
import tools.*;

public class LinearRegressionCompletion extends Traitement {

    
    @Override
    public void complete(DataFrame<Fish> fish) {
        double c = 0 ;
        int k  = -1 ;
        for(int i = 0 ; i < 5 ; i++){
            c = 0 ;
            k = -1 ; 
            for(int j = 0 ; j < 5 ; j++){
                if( i == j )continue ;
                if(c < corr(fish, i, j) ) {
                    c = corr(fish, i, j) ;
                    k = j ;
                }
            }
            if(k != -1 && c >= 0.5){
                completion(fish, i, k);
            }
        }
        
    }

    private Double corr(DataFrame<Fish> fish ,int i , int j){

        ArrayList<Double> x = null ;
        ArrayList<Double> y = null ;

        switch(i){
            case 0:
                x = fish.getLengths();
                break ;
            case 1:
                x = fish.getSizes() ;
                break ;
            case 2 :
                x = fish.getWeights();
                break ;
            case 3 :
                x = fish.getParasites();
                break ;
            case 4 :
                x = fish.getInfestationRates();
                break ;
        }

        switch(j){
            case 0:
                y = fish.getLengths();
                break ;
            case 1:
                y = fish.getSizes() ;
                break ;
            case 2 :
                y = fish.getWeights();
                break ;
            case 3 :
                y = fish.getParasites();
                break ;
            case 4 :
                y = fish.getInfestationRates();
                break ;
        }
        return Pearson.pearson(x, y) ;

    }

    private void completion(DataFrame<Fish> fish ,int i , int j){

        String x = null ;
        String y = null ;

        switch(i){
            case 0:
                x = "Length" ;
                break ;
            case 1:
                x = "Size";
                break ;
            case 2 :
                x = "Weight";
                break ;
            case 3 :
                x = "Parasites";
                break ;
            case 4 :
                x = "InfestationRate";
                break ;
        }

       switch(i){
            case 0:
                y = "Length" ;
                break ;
            case 1:
                y = "Size";
                break ;
            case 2 :
                y = "Weight";
                break ;
            case 3 :
                y = "Parasites";
                break ;
            case 4 :
                y = "InfestationRate";
                break ;
        }
        
        Complete(fish, x, y);
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
