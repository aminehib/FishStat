package tools;

import java.util.ArrayList;

/**
 * Outil utilitaire de calcul de l'écart-type d'une liste de
 * valeurs (les {@code null} sont ignorés).
 */
public class StandatrdDeviation {

    /**
     * Calcule l'écart-type de la liste fournie.
     *
     * @param x la liste de valeurs
     * @return l'écart-type, ou {@code null} si la liste est vide ou nulle
     */
    public static Double std(ArrayList<Double> x){

    if(x == null || x.size() == 0) return null;

    ArrayList<Double> xf = new ArrayList<>();

    for(Double v : x){
        if(v != null){
            xf.add(v);
        }
    }

    int n = xf.size();
    if(n == 0) return null;

    double mean = new MeanValue(xf).getMean();

    double sum = 0;

    for(Double v : xf){
        sum += (v - mean) * (v - mean);
    }

    return Math.sqrt(sum / n);
}



}
