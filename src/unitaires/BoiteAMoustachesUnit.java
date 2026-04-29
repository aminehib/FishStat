package unitaires;

import java.util.ArrayList;
import java.util.Arrays;
import tools.BoiteAMoustaches;

public class BoiteAMoustachesUnit {

    public static void main(String[] args) {
        System.out.println("===== Test BoiteAMoustaches =====");
        int ok = 0, ko = 0;

        // Test 1 : [1,2,3,4,5,6,7,8,9]  Q1=2.5, Q2=5, Q3=7.5
        ArrayList<Double> v1 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0));
        BoiteAMoustaches b1 = new BoiteAMoustaches(v1);
        System.out.println("Test 1 : Q1 attendu 2.5  obtenu " + b1.getPremierQuantile());
        if (Math.abs(b1.getPremierQuantile() - 2.5) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("Test 2 : Q2 (mediane) attendu 5.0  obtenu " + b1.getMediane());
        if (Math.abs(b1.getMediane() - 5.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("Test 3 : Q3 attendu 7.5  obtenu " + b1.getDernierQuantile());
        if (Math.abs(b1.getDernierQuantile() - 7.5) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Moustaches : IQR = 5, MoustacheInf = 2.5 - 7.5 = -5, MoustacheSup = 7.5 + 7.5 = 15
        System.out.println("Test 4 : MoustacheInf attendu -5.0  obtenu " + b1.getMoustacheInf());
        if (Math.abs(b1.getMoustacheInf() - (-5.0)) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("Test 5 : MoustacheSup attendu 15.0  obtenu " + b1.getMoustacheSup());
        if (Math.abs(b1.getMoustacheSup() - 15.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 6 : ignore les null
        ArrayList<Double> v2 = new ArrayList<>(Arrays.asList(1.0, 2.0, null, 3.0, 4.0, null, 5.0, 6.0, 7.0, 8.0, 9.0));
        BoiteAMoustaches b2 = new BoiteAMoustaches(v2);
        System.out.println("Test 6 : mediane avec null attendu 5.0  obtenu " + b2.getMediane());
        if (Math.abs(b2.getMediane() - 5.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 7 : liste vide -> tout null
        BoiteAMoustaches b3 = new BoiteAMoustaches(new ArrayList<>());
        System.out.println("Test 7 : liste vide -> mediane null  obtenu " + b3.getMediane());
        if (b3.getMediane() == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 8 : liste paire [1,2,3,4]  Q2 = (2+3)/2 = 2.5
        ArrayList<Double> v4 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0));
        BoiteAMoustaches b4 = new BoiteAMoustaches(v4);
        System.out.println("Test 8 : mediane [1,2,3,4] attendu 2.5  obtenu " + b4.getMediane());
        if (Math.abs(b4.getMediane() - 2.5) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan BoiteAMoustaches : " + ok + " OK / " + (ok + ko));
    }
}
