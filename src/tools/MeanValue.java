package tools;

import java.util.ArrayList;

/**
 * Calcule la moyenne arithmétique d'une liste de valeurs
 * en ignorant les entrées {@code null}.
 */
public class MeanValue {

    private Double mean = null;

    /**
     * Construit le calculateur et calcule immédiatement la moyenne.
     *
     * @param values la liste de valeurs (les {@code null} sont ignorées)
     */
    public MeanValue(ArrayList<Double> values){
        double sum = 0 ;
        int taille  = 0 ;
        for(Double v : values){
            if(v !=null){
                sum += v.doubleValue() ;
                taille++ ;
            }
        }
        if(taille == 0)return ;
        mean =  sum / taille ;
    }

    /** @return la moyenne, ou {@code null} si aucune valeur valide */
    public Double getMean(){
        return mean ;
    }

}
