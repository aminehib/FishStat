package tools;

import java.util.*;
import model.*;

/**
 * Algorithme de clustering K-Means en 2D appliqué à deux colonnes
 * d'un {@link DataFrame} de poissons.
 */
public class KMeans {

    /**
     * Exécute l'algorithme K-Means.
     *
     * @param init_centers centres initiaux (modifiés en place)
     * @param max_iter     nombre maximum d'itérations
     * @param epsilon      seuil de convergence sur le déplacement des centres
     * @param df           DataFrame source
     * @param X            nom de la colonne servant d'abscisse
     * @param Y            nom de la colonne servant d'ordonnée
     * @return les étiquettes de cluster pour chaque point, ou {@code null} si invalide
     */
    public static ArrayList<Integer> Kmeans(Coords[] init_centers, int max_iter, double epsilon, DataFrame<Fish> df, String X, String Y) {

        if (init_centers == null || init_centers.length == 0) return null;

        ArrayList<Double> x = df.getColumn(X);
        ArrayList<Double> y = df.getColumn(Y) ;


        if (x == null || y == null) return null;

        int nPoints = x.size();
        ArrayList<Integer> labels = new ArrayList<>();
        ArrayList<Double> dist = new ArrayList<>();

        for (int i = 0; i < nPoints; i++) {
            labels.add(-1);
            dist.add(Double.MAX_VALUE);
        }

        Coords[] coords = Coords.init_Coords(x, y);


        Coords[] previous_centers = new Coords[init_centers.length];
        for (int i = 0; i < init_centers.length; i++) {
            previous_centers[i] = new Coords(init_centers[i].getX(), init_centers[i].getY());
        }

        int iter = 0;

        while (iter < max_iter) {
            iter++;


            for (int i = 0; i < dist.size(); i++) {
                dist.set(i, Double.MAX_VALUE);
            }


            for (int i = 0; i < init_centers.length; i++) {
                previous_centers[i] = new Coords(init_centers[i].getX(), init_centers[i].getY());
            }

            for (int k = 0; k < init_centers.length; k++) {
                Coords center = init_centers[k];

                for (int i = 0; i < coords.length; i++) {
                    double d = Coords.distance(center, coords[i]);

                    if (d < dist.get(i)) {
                        dist.set(i, d);
                        labels.set(i, k);
                    }
                }
            }

            for (int k = 0; k < init_centers.length; k++) {
                double mx = 0;
                double my = 0;
                int count = 0;

                for (int i = 0; i < coords.length; i++) {
                    if (labels.get(i) == k) {
                        mx += coords[i].getX();
                        my += coords[i].getY();
                        count++;
                    }
                }

                if (count > 0) {
                    init_centers[k].setX(mx / count);
                    init_centers[k].setY(my / count);
                }
            }


            if (stop(init_centers, previous_centers, epsilon)) {
                break;
            }
        }

        return labels;
    }

    private static boolean stop(Coords[] current, Coords[] previous, double epsilon) {
        for (int i = 0; i < current.length; i++) {
            if (Coords.distance(current[i], previous[i]) > epsilon) {
                return false;
            }
        }
        return true;
    }
}
