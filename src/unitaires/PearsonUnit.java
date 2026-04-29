package unitaires;

import java.util.ArrayList;
import java.util.Arrays;
import tools.Pearson;

public class PearsonUnit {

    public static void main(String[] args) {
        System.out.println("===== Test Pearson =====");
        int ok = 0, ko = 0;

        // Test 1 : correlation parfaite positive y = 2x  -> 1.0
        ArrayList<Double> x1 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        ArrayList<Double> y1 = new ArrayList<>(Arrays.asList(2.0, 4.0, 6.0, 8.0, 10.0));
        Double p1 = Pearson.pearson(x1, y1);
        System.out.println("Test 1 : pearson(x, 2x)  attendu 1.0  obtenu " + p1);
        if (p1 != null && Math.abs(p1 - 1.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 2 : correlation parfaite negative y = -x  -> -1.0
        ArrayList<Double> x2 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        ArrayList<Double> y2 = new ArrayList<>(Arrays.asList(-1.0, -2.0, -3.0, -4.0, -5.0));
        Double p2 = Pearson.pearson(x2, y2);
        System.out.println("Test 2 : pearson(x, -x)  attendu -1.0  obtenu " + p2);
        if (p2 != null && Math.abs(p2 - (-1.0)) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 3 : y constante (std = 0)  -> null
        ArrayList<Double> x3 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        ArrayList<Double> y3 = new ArrayList<>(Arrays.asList(7.0, 7.0, 7.0, 7.0, 7.0));
        Double p3 = Pearson.pearson(x3, y3);
        System.out.println("Test 3 : pearson(x, constante)  attendu null  obtenu " + p3);
        if (p3 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 4 : decorrele
        // x = [1,2,3,4,5,6], y = [3,1,4,1,5,9]  -> faible correlation
        ArrayList<Double> x4 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0));
        ArrayList<Double> y4 = new ArrayList<>(Arrays.asList(3.0, 1.0, 4.0, 1.0, 5.0, 9.0));
        Double p4 = Pearson.pearson(x4, y4);
        System.out.println("Test 4 : pearson decorrele  obtenu " + p4 + "  (entre -1 et 1)");
        if (p4 != null && p4 >= -1.0 && p4 <= 1.0) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan Pearson : " + ok + " OK / " + (ok + ko));
    }
}
