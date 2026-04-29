package tools;

import java.util.ArrayList;

/**
 * Régression linéaire simple {@code y = a*x + b} ajustée par les
 * moindres carrés. Les paires contenant un {@code null} sont ignorées.
 */
public class LinearRegression {

    private Double a = null;
    private Double b = null ;

    /**
     * Calcule les coefficients de la droite de régression.
     *
     * @param x la série des abscisses
     * @param y la série des ordonnées (même taille)
     */
    public LinearRegression(ArrayList<Double> x , ArrayList<Double> y){

        if(x.size() != y.size())return ;

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        int n = 0;

        for(int i = 0; i < x.size(); i++){

            Double xi = x.get(i);
            Double yi = y.get(i);

            if(xi == null || yi == null) continue;

            sumX += xi;
            sumY += yi;
            sumXY += xi * yi;
            sumX2 += xi * xi;
            n++;
        }

        if(n < 2) return;

        double denom = n * sumX2 - sumX * sumX;
        if(denom == 0) return;

        a = (n * sumXY - sumX * sumY) / denom;
        b = (sumY - a * sumX) / n;

    }

    /**
     * Prédit les ordonnées pour une liste d'abscisses.
     *
     * @param x la liste d'abscisses
     * @return les prédictions, ou {@code null} si le modèle est invalide
     */
    public ArrayList<Double> predict(ArrayList<Double> x){
        if(a == null || b == null)return null ;
        ArrayList<Double> y = new ArrayList<>() ;
        for(Double elm : x){
            if(elm != null)y.add(elm*a+b);
        }
        return y ;
    }

    /**
     * Prédit l'ordonnée pour une abscisse unique.
     *
     * @param x une abscisse
     * @return la prédiction, ou {@code null} si entrée ou modèle invalide
     */
    public Double predict(Double x ){
        if(x == null || a == null || b == null)return null ;
        return  a*x + b ;
    }

    /** @return le coefficient directeur {@code a} */
    public Double getCoeff(){return this.a ;}

    /** @return l'ordonnée à l'origine {@code b} */
    public Double getIntercept(){return this.b ;}

}
