package tools;

import java.util.ArrayList;
import model.Fish;

public class BoiteAMoustaches {

    private double Q1 ;
    private double Q2 ;
    private double Q3 ;
    private double moustacheInf ;
    private double moustacheSup ;

    public BoiteAMoustaches(ArrayList<Fish> poissons){
        // grader les poissont avec un taux d'infestation connu
        poissons.removeIf( arg->{
            return (arg.getInfestationRate() == null ) ;
        });

        poissons.sort((f1, f2) -> {
        Double t1 = f1.getInfestationRate() ;
        Double t2 = f2.getInfestationRate() ;
        // tri croissant
        return t1.compareTo(t2);
        });

        int n = poissons.size();
        int milieu = n / 2 ;
        int quart = milieu / 2 ;
        if( n % 2 == 0){
            Q2 = ( poissons.get( milieu - 1).getInfestationRate() + poissons.get( milieu).getInfestationRate() ) / 2.0 ;
            if(  milieu % 2 == 0){
                Q1 = ( poissons.get( quart - 1).getInfestationRate() + poissons.get( quart).getInfestationRate() ) / 2.0 ;
                Q3 = ( poissons.get( milieu + quart - 1).getInfestationRate() + poissons.get( milieu + quart).getInfestationRate() ) / 2.0 ;
            }else{
                Q1 = poissons.get(quart).getInfestationRate();
                Q3 = poissons.get(quart + milieu).getInfestationRate();
            }
        }else{
            Q2 = poissons.get(milieu).getInfestationRate();
            if(milieu % 2 == 0){
                Q1 = ( poissons.get( quart - 1).getInfestationRate() + poissons.get( quart).getInfestationRate() ) / 2.0 ;
                Q3 = ( poissons.get( milieu + quart - 1).getInfestationRate() + poissons.get( milieu + quart).getInfestationRate() ) / 2.0 ;
            }else{
                Q1 = poissons.get(quart).getInfestationRate();
                Q3 = poissons.get(quart + milieu).getInfestationRate();
            }
        }
        double ecartInterquantile = Q3 - Q1 ;
        moustacheInf =  Q1 -  (1.5 * ecartInterquantile) ;
        moustacheSup =  Q3 +  (1.5 * ecartInterquantile );
        
    }

    public double getPremierQuantile(){
        return Q1 ;
    }
    public double getDernierQuantile(){
        return Q3;
    }
    public double getMediane(){
        return Q2 ;
    }

    public double getMoustacheSup(){
        return moustacheSup ;
    }

    public double getMoustacheInf(){
        return moustacheInf ;
    }


    
    
}
