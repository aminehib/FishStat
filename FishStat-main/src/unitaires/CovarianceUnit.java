package unitaires;

import java.util.ArrayList;
import java.util.Arrays;
import tools.Covariance;

public class CovarianceUnit {

    public static void main(String[] args) {
        System.out.println("===== Test Covariance =====");
        int ok = 0, ko = 0;

        // Test 1 : x = y = [1,2,3,4,5]
        // moyenne = 3, somme(xi-3)^2 = 4+1+0+1+4 = 10, cov = 10/5 = 2.0
        ArrayList<Double> x1 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        ArrayList<Double> y1 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        Double c1 = Covariance.covariance(x1, y1);
        System.out.println("Test 1 : cov(x,x)  attendu 2.0  obtenu " + c1);
        if (c1 != null && Math.abs(c1 - 2.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 2 : y = -x  -> cov negative = -2.0
        ArrayList<Double> x2 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        ArrayList<Double> y2 = new ArrayList<>(Arrays.asList(-1.0, -2.0, -3.0, -4.0, -5.0));
        Double c2 = Covariance.covariance(x2, y2);
        System.out.println("Test 2 : cov(x,-x)  attendu -2.0  obtenu " + c2);
        if (c2 != null && Math.abs(c2 - (-2.0)) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 3 : y constante  -> cov = 0
        ArrayList<Double> x3 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        ArrayList<Double> y3 = new ArrayList<>(Arrays.asList(7.0, 7.0, 7.0, 7.0, 7.0));
        Double c3 = Covariance.covariance(x3, y3);
        System.out.println("Test 3 : cov(x, constante)  attendu 0.0  obtenu " + c3);
        if (c3 != null && Math.abs(c3) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 4 : tailles differentes -> null
        ArrayList<Double> x4 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0));
        ArrayList<Double> y4 = new ArrayList<>(Arrays.asList(1.0, 2.0));
        Double c4 = Covariance.covariance(x4, y4);
        System.out.println("Test 4 : tailles differentes  attendu null  obtenu " + c4);
        if (c4 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 5 : null en entree -> null
        Double c5 = Covariance.covariance(null, x1);
        System.out.println("Test 5 : cov(null, x)  attendu null  obtenu " + c5);
        if (c5 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 6 : moins de 2 valeurs valides -> null
        ArrayList<Double> x6 = new ArrayList<>(Arrays.asList(1.0, null, null));
        ArrayList<Double> y6 = new ArrayList<>(Arrays.asList(2.0, null, null));
        Double c6 = Covariance.covariance(x6, y6);
        System.out.println("Test 6 : 1 paire valide  attendu null  obtenu " + c6);
        if (c6 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan Covariance : " + ok + " OK / " + (ok + ko));
    }
}
