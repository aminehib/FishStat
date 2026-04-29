package unitaires;

import java.util.ArrayList;
import java.util.Arrays;
import tools.StandatrdDeviation;

public class StandardDeviationUnit {

    public static void main(String[] args) {
        System.out.println("===== Test StandatrdDeviation =====");
        int ok = 0, ko = 0;

        // Test 1 : ecart-type [2,4,4,4,5,5,7,9] -> 2.0
        ArrayList<Double> v1 = new ArrayList<>(Arrays.asList(2.0, 4.0, 4.0, 4.0, 5.0, 5.0, 7.0, 9.0));
        Double s1 = StandatrdDeviation.std(v1);
        System.out.println("Test 1 : std [2,4,4,4,5,5,7,9] attendu 2.0  obtenu " + s1);
        if (s1 != null && Math.abs(s1 - 2.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 2 : valeurs identiques -> 0
        ArrayList<Double> v2 = new ArrayList<>(Arrays.asList(5.0, 5.0, 5.0, 5.0));
        Double s2 = StandatrdDeviation.std(v2);
        System.out.println("Test 2 : std [5,5,5,5] attendu 0.0  obtenu " + s2);
        if (s2 != null && Math.abs(s2) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 3 : ignore les null
        ArrayList<Double> v3 = new ArrayList<>(Arrays.asList(2.0, null, 4.0, 4.0, null, 4.0, 5.0, 5.0, 7.0, 9.0));
        Double s3 = StandatrdDeviation.std(v3);
        System.out.println("Test 3 : std avec null  attendu 2.0  obtenu " + s3);
        if (s3 != null && Math.abs(s3 - 2.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 4 : liste vide -> null
        Double s4 = StandatrdDeviation.std(new ArrayList<>());
        System.out.println("Test 4 : std []  attendu null  obtenu " + s4);
        if (s4 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 5 : null en entree -> null
        Double s5 = StandatrdDeviation.std(null);
        System.out.println("Test 5 : std(null)  attendu null  obtenu " + s5);
        if (s5 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan StandardDeviation : " + ok + " OK / " + (ok + ko));
    }
}
