package unitaires;

import java.util.ArrayList;
import java.util.Arrays;
import tools.MeanValue;

public class MeanValueUnit {

    public static void main(String[] args) {
        System.out.println("===== Test MeanValue =====");
        int ok = 0, ko = 0;

        // Test 1 : moyenne simple [1,2,3,4,5] -> 3.0
        ArrayList<Double> v1 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        Double m1 = new MeanValue(v1).getMean();
        System.out.println("Test 1 : moyenne [1,2,3,4,5] attendu 3.0  obtenu " + m1);
        if (m1 != null && Math.abs(m1 - 3.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 2 : ignore les null
        ArrayList<Double> v2 = new ArrayList<>(Arrays.asList(1.0, null, 3.0, null, 5.0));
        Double m2 = new MeanValue(v2).getMean();
        System.out.println("Test 2 : moyenne [1,null,3,null,5] attendu 3.0  obtenu " + m2);
        if (m2 != null && Math.abs(m2 - 3.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 3 : liste vide -> null
        Double m3 = new MeanValue(new ArrayList<>()).getMean();
        System.out.println("Test 3 : moyenne []  attendu null  obtenu " + m3);
        if (m3 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 4 : que des null -> null
        ArrayList<Double> v4 = new ArrayList<>(Arrays.asList(null, null, null));
        Double m4 = new MeanValue(v4).getMean();
        System.out.println("Test 4 : moyenne [null,null,null] attendu null  obtenu " + m4);
        if (m4 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 5 : une seule valeur
        Double m5 = new MeanValue(new ArrayList<>(Arrays.asList(42.0))).getMean();
        System.out.println("Test 5 : moyenne [42]  attendu 42.0  obtenu " + m5);
        if (m5 != null && Math.abs(m5 - 42.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 6 : valeurs negatives
        ArrayList<Double> v6 = new ArrayList<>(Arrays.asList(-1.0, -2.0, -3.0));
        Double m6 = new MeanValue(v6).getMean();
        System.out.println("Test 6 : moyenne [-1,-2,-3]  attendu -2.0  obtenu " + m6);
        if (m6 != null && Math.abs(m6 - (-2.0)) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan MeanValue : " + ok + " OK / " + (ok + ko));
    }
}
