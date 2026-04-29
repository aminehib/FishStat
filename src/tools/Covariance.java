package tools;

import java.util.ArrayList;

/**
 * Outil utilitaire de calcul de la covariance entre deux séries.
 */
public class Covariance {

/**
 * Calcule la covariance entre {@code x} et {@code y}.
 * Les paires contenant un {@code null} sont ignorées.
 *
 * @param x première série
 * @param y deuxième série (même taille que {@code x})
 * @return la covariance, ou {@code null} si les listes sont
 *         invalides ou de taille &lt; 2
 */
public static Double covariance(ArrayList<Double> x ,ArrayList<Double> y){

    if(x == null || y == null || x.size() != y.size()) return null;

    ArrayList<Double> xf = new ArrayList<>();
    ArrayList<Double> yf = new ArrayList<>();

    for(int i = 0; i < x.size(); i++){
        if(x.get(i) != null && y.get(i) != null){
            xf.add(x.get(i));
            yf.add(y.get(i));
        }
    }

    int n = xf.size();
    if(n < 2) return null;

    double mx = new MeanValue(xf).getMean();
    double my = new MeanValue(yf).getMean();

    double cov = 0;

    for(int i = 0; i < n; i++){
        cov += (xf.get(i) - mx) * (yf.get(i) - my);
    }

    return cov / n;
}

}
