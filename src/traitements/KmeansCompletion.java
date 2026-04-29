package traitements;

import java.util.*;

import model.DataFrame;
import model.Fish;
import tools.Coords;
import tools.KMeans;
import tools.MeanValue;
import tools.StandatrdDeviation;

/**
 * Stratégie de complétion par K-Means : sélectionne le couple
 * de colonnes minimisant l'inertie, regroupe les poissons en
 * clusters, puis remplace les valeurs manquantes par la moyenne
 * du cluster d'appartenance.
 */
public class KmeansCompletion extends Traitement {

    private static String[] cols = {"Length","Weight","Size","Parasites","InfestationRate"};

    /**
     * Complète les valeurs manquantes en utilisant un clustering
     * K-Means sur le couple de colonnes le plus discriminant.
     *
     * @param fish le DataFrame à compléter
     */
    @Override
    public void complete(DataFrame<Fish> fish){

        ArrayList<Fish> poissons = new ArrayList<>(fish.getData());
        LinkedHashMap<String , ArrayList<Fish>> Species = getSpecies(poissons);

        int bestC1 = 0;
        int bestC2 = 1;
        double minInertia = Double.MAX_VALUE;

        for(int i = 0; i < cols.length; i++){
            for(int j = i + 1; j < cols.length; j++){

                ArrayList<Double> x = fish.getColumn(cols[i]);
                ArrayList<Double> y = fish.getColumn(cols[j]);

                normalize(x);
                normalize(y);

                Coords[] centers = new Coords[Species.size()];
                int c = 0;

                for(String species : Species.keySet()){
                    DataFrame<Fish> dfSpecies = new DataFrame<>(Species.get(species));

                    Double mx = new MeanValue(dfSpecies.getColumn(cols[i])).getMean();
                    Double my = new MeanValue(dfSpecies.getColumn(cols[j])).getMean();

                    if(mx == null || my == null) continue;

                    centers[c] = new Coords(mx, my);
                    c++;
                }

                ArrayList<Fish> validFish = new ArrayList<>();

                for(int k = 0; k < poissons.size(); k++){
                    if(x.get(k) != null && y.get(k) != null){
                        validFish.add(poissons.get(k));
                    }
                }

                if(validFish.isEmpty()) continue;

                DataFrame<Fish> dfValid = new DataFrame<>(validFish);

                ArrayList<Integer> labels = KMeans.Kmeans(
                        centers, 300, 0.005, dfValid, cols[i], cols[j]);

                double inertia = inertie(
                        Coords.init_Coords(dfValid.getColumn(cols[i]), dfValid.getColumn(cols[j])),
                        centers,
                        labels
                );

                if(inertia < minInertia){
                    minInertia = inertia;
                    bestC1 = i;
                    bestC2 = j;
                }
            }
        }

        Coords[] bestCenters = new Coords[Species.size()];
        int c = 0;

        for(String species : Species.keySet()){
            DataFrame<Fish> dfSpecies = new DataFrame<>(Species.get(species));

            Double mx = new MeanValue(dfSpecies.getColumn(cols[bestC1])).getMean();
            Double my = new MeanValue(dfSpecies.getColumn(cols[bestC2])).getMean();

            bestCenters[c] = new Coords(mx, my);
            c++;
        }

        ArrayList<Integer> labels = KMeans.Kmeans(
                bestCenters, 300, 0.005, fish, cols[bestC1], cols[bestC2]);

        LinkedHashMap<Integer , ArrayList<Fish>> clusters = cluster(fish, labels);

        for(ArrayList<Fish> group : clusters.values()){

            DataFrame<Fish> dfGroup = new DataFrame<>(group);

            for(int r = 0 ; r < cols.length ; r++){

                ArrayList<Double> col = dfGroup.getColumn(cols[r]);
                Double mean = new MeanValue(col).getMean();

                if(mean == null) continue;

                for(int k = 0; k < group.size(); k++){

                    if(col.get(k) == null){
                        Fish f = group.get(k);

                        switch (cols[r]) {
                            case "InfestationRate":
                                f.setInfestationRate(mean);
                                break;
                            case "Parasites":
                                f.setParasites(mean.intValue());
                                break;
                            case "Weight":
                                f.setWeight(mean);
                                break;
                            case "Size":
                                f.setSize(mean);
                                break;
                            case "Length":
                                f.setLength(mean);
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Calcule l'inertie totale d'un partitionnement (somme des
     * carrés des distances au centre du cluster d'appartenance).
     *
     * @param coords  les points
     * @param centers les centres des clusters
     * @param labels  l'étiquette de cluster de chaque point
     * @return l'inertie totale
     */
    public static double inertie(Coords[] coords, Coords[] centers, ArrayList<Integer> labels) {
        double inertie = 0.0;

        for (int i = 0; i < coords.length; i++) {
            int k = labels.get(i);
            double d = Coords.distance(coords[i], centers[k]);
            inertie += d * d;
        }

        return inertie;
    }

    /**
     * Regroupe les poissons par cluster selon les étiquettes.
     *
     * @param df     le DataFrame source
     * @param labels les étiquettes (même taille que les données)
     * @return une map ordonnée cluster → liste de poissons
     */
    public LinkedHashMap<Integer , ArrayList<Fish>> cluster(DataFrame<Fish> df , ArrayList<Integer> labels){
        ArrayList<Fish> poissons = df.getData();
        LinkedHashMap<Integer , ArrayList<Fish>> cl = new LinkedHashMap<>();

        for(int i = 0; i < labels.size(); i++){
            int l = labels.get(i);
            cl.putIfAbsent(l, new ArrayList<>());
            cl.get(l).add(poissons.get(i));
        }

        return cl;
    }

    private static void normalize(ArrayList<Double> x){

        Double moy = new MeanValue(x).getMean();
        Double std = StandatrdDeviation.std(x);

        if(moy == null || std == null || std == 0) return;

        for(int i = 0; i < x.size(); i++){
            Double val = x.get(i);
            if(val != null){
                x.set(i, (val - moy) / std);
            }
        }
    }
}
