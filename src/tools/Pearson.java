package tools;

import java.util.ArrayList;

/**
 * Outil utilitaire de calcul du coefficient de corrélation
 * de Pearson entre deux séries.
 */
public class Pearson {

    /**
     * Calcule le coefficient de Pearson entre {@code x} et {@code y}.
     *
     * @param x première série
     * @param y deuxième série
     * @return le coefficient dans [-1, 1], ou {@code null} si
     *         indéfini (covariance ou écart-type nul/invalide)
     */
    public static Double pearson(ArrayList<Double> x , ArrayList<Double> y){
        Double cov = Covariance.covariance(x, y) ;
        Double stdx = StandatrdDeviation.std(x) ;
        Double stdy = StandatrdDeviation.std(y) ;

        if(cov == null || stdx == null || stdy ==null)return null ;
        if(stdx*stdy == 0)return null ;
        return cov / (stdx*stdy) ;
    }

}
