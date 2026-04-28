package traitements;

import java.util.ArrayList;

import model.DataFrame;
import model.Fish;
import tools.LinearRegression;
import tools.Pearson;
import tools.RegressionPoly2;

public class RegressionCompletion extends Traitement {

    private static String[] cols = {"Length","Weight","Size","Total_parasites" ,"InfestationRate"};

    
    @Override
    public void complete(DataFrame<Fish> fish) {
        double c = 0 ;
        int k  = -1 ;
        for(int i = 0 ; i < 5 ; i++){
            c = 0 ;
            k = -1 ; 
            for(int j = 0 ; j < 5 ; j++){
                if( i == j )continue ;
                if(c < corr(fish, cols[i], cols[j]) ) {
                    c = corr(fish, cols[i], cols[j]) ;
                    k = j ;
                }
            }
            if(k != -1 && c >= 0.5){
                Complete(fish, cols[i], cols[k]);
            }
        }
        
    }

    private Double corr(DataFrame<Fish> fish ,String i , String j){

        ArrayList<Double> x = fish.getColumn(i) ;
        ArrayList<Double> y = fish.getColumn(j) ;
            
        return Pearson.pearson(x, y) ;

    }


    public  void Complete(DataFrame<Fish> fish , String X ,String Y){

        if(X.equals(Y) || fish.getData().size() == 0)return ;

        ArrayList<Double> x = fish.getColumn(X) ;
        ArrayList<Double> y = fish.getColumn(Y) ;
        ArrayList<Fish> unknown = new ArrayList<>() ;

        for(int i = 0 ; i < y.size() ; i++){
            if(y.get(i) == null)
                unknown.add(fish.getData().get(i));
        }

        if(unknown.size() == 0){
            return ;
        } ;
        
        RegressionPoly2 model= new RegressionPoly2(x, y) ;

        if(model.getCoeffs()[0] == null || model.getCoeffs()[1] == null || model.getIntercept() == null){
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
