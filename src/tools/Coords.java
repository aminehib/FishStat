package tools;

import java.util.ArrayList;

/**
 * Représente un point 2D ({@code x}, {@code y}) avec utilitaires
 * de construction depuis des listes et de calcul de distance.
 * Les valeurs {@code null} sont remplacées par {@link Double#MAX_VALUE}.
 */
public class Coords {

    private Double x = Double.MAX_VALUE ;
    private Double y = Double.MAX_VALUE ;

    /**
     * Construit un point.
     *
     * @param x abscisse (ou {@code null} → {@link Double#MAX_VALUE})
     * @param y ordonnée (ou {@code null} → {@link Double#MAX_VALUE})
     */
    public Coords(Double x , Double y){
        this.x = (x == null)? Double.MAX_VALUE : x;
        this.y = (y ==null)? Double.MAX_VALUE : y ;
    }

    /**
     * Construit un tableau de points à partir de deux listes parallèles.
     *
     * @param X la liste des abscisses
     * @param Y la liste des ordonnées (même taille que {@code X})
     * @return un tableau de {@code Coords}, ou {@code null} si invalide
     */
    public static Coords[] init_Coords(ArrayList<Double> X , ArrayList<Double> Y ){

        if(X == null || Y == null || X.size() != Y.size())return null ;

        Coords[] coords = new Coords[X.size()] ;

        for(int i = 0 ;  i <  X.size() ; i++){
            coords[i] = new Coords(X.get(i), Y.get(i)) ;
        }
        return coords ;
    }

    /**
     * Calcule la distance euclidienne entre deux points.
     *
     * @param x1 premier point
     * @param x2 second point
     * @return la distance, ou {@link Double#MAX_VALUE} si l'un des points est {@code null}
     */
    public static Double distance(Coords x1 , Coords x2){
        if(x1 == null || x2 == null ) return Double.MAX_VALUE ;
        return Math.sqrt(Math.pow(x1.getX() - x2.getX() , 2) + Math.pow(x1.getY() - x2.getY(), 2));
    }

    /** @return l'abscisse */
    public Double getX(){
        return this.x == null ? Double.MAX_VALUE : this.x ;
    }
    /** @return l'ordonnée */
    public Double getY(){
        return this.y == null ? Double.MAX_VALUE : this.y  ;
    }

    /** @param x la nouvelle abscisse */
    public void setX(Double x){
        this.x  = x;
        if(x == null) x = Double.MAX_VALUE ;
    }
    /** @param y la nouvelle ordonnée */
    public void setY(Double y){
        this.y = x ;
        if(y == null) y = Double.MAX_VALUE ;
    }

}
