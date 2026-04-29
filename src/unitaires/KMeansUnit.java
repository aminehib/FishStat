package unitaires;

import java.util.ArrayList;
import model.DataFrame;
import model.Fish;
import tools.Coords;
import tools.KMeans;

public class KMeansUnit {

    public static void main(String[] args) {
        System.out.println("===== Test KMeans =====");
        int ok = 0, ko = 0;

        // On construit un DataFrame<Fish> avec 2 clusters bien separes :
        //   cluster A autour de (Length=10, Weight=10)  : 3 poissons
        //   cluster B autour de (Length=100, Weight=100): 3 poissons
        ArrayList<Fish> poissons = new ArrayList<>();
        poissons.add(makeFish(10.0, 10.0));
        poissons.add(makeFish(11.0, 9.0));
        poissons.add(makeFish(9.0, 11.0));
        poissons.add(makeFish(100.0, 100.0));
        poissons.add(makeFish(101.0, 99.0));
        poissons.add(makeFish(99.0, 101.0));
        DataFrame<Fish> df = new DataFrame<>(poissons);

        Coords[] centers = new Coords[]{ new Coords(10.0, 10.0), new Coords(100.0, 100.0) };
        ArrayList<Integer> labels = KMeans.Kmeans(centers, 100, 0.001, df, "Length", "Weight");

        System.out.println("Test 1 : labels non-null  obtenu " + labels);
        if (labels != null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 2 : 6 labels
        if (labels != null) {
            System.out.println("Test 2 : nb labels attendu 6  obtenu " + labels.size());
            if (labels.size() == 6) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

            // Test 3 : les 3 premiers ont le meme label, idem pour les 3 derniers
            boolean clusterAouB = labels.get(0).equals(labels.get(1)) && labels.get(1).equals(labels.get(2));
            boolean clusterCouD = labels.get(3).equals(labels.get(4)) && labels.get(4).equals(labels.get(5));
            boolean different   = !labels.get(0).equals(labels.get(3));
            System.out.println("Test 3 : 3 premiers meme cluster ? " + clusterAouB
                    + " | 3 derniers meme cluster ? " + clusterCouD
                    + " | clusters differents ? " + different);
            if (clusterAouB && clusterCouD && different) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }
        }

        // Test 4 : centres null -> null
        ArrayList<Integer> labels2 = KMeans.Kmeans(null, 100, 0.001, df, "Length", "Weight");
        System.out.println("Test 4 : centres null -> labels null  obtenu " + labels2);
        if (labels2 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 5 : centres vide -> null
        ArrayList<Integer> labels3 = KMeans.Kmeans(new Coords[0], 100, 0.001, df, "Length", "Weight");
        System.out.println("Test 5 : centres vide -> labels null  obtenu " + labels3);
        if (labels3 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan KMeans : " + ok + " OK / " + (ok + ko));
    }

    private static Fish makeFish(Double length, Double weight) {
        Fish f = new Fish("test", length, weight, 0.0, 0.0);
        return f;
    }
}
