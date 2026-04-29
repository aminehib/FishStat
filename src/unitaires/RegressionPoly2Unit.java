package unitaires;

import java.util.ArrayList;
import java.util.Arrays;
import tools.RegressionPoly2;

public class RegressionPoly2Unit {

    public static void main(String[] args) {
        System.out.println("===== Test RegressionPoly2 =====");
        int ok = 0, ko = 0;

        // Test 1 : y = x^2 + 2x + 3
        // Pour x = -2,-1,0,1,2,3 -> y = 3,2,3,6,11,18
        ArrayList<Double> x1 = new ArrayList<>(Arrays.asList(-2.0, -1.0, 0.0, 1.0, 2.0, 3.0));
        ArrayList<Double> y1 = new ArrayList<>(Arrays.asList(3.0, 2.0, 3.0, 6.0, 11.0, 18.0));
        RegressionPoly2 r1 = new RegressionPoly2(x1, y1);

        Double[] coeffs = r1.getCoeffs();
        System.out.println("Test 1 : coeff a (x^2) attendu 1.0  obtenu " + coeffs[0]);
        if (coeffs[0] != null && Math.abs(coeffs[0] - 1.0) < 1e-6) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("Test 2 : coeff b (x) attendu 2.0  obtenu " + coeffs[1]);
        if (coeffs[1] != null && Math.abs(coeffs[1] - 2.0) < 1e-6) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("Test 3 : intercept c attendu 3.0  obtenu " + r1.getIntercept());
        if (r1.getIntercept() != null && Math.abs(r1.getIntercept() - 3.0) < 1e-6) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 4 : predict(4) = 16+8+3 = 27
        Double yp = r1.predict(4.0);
        System.out.println("Test 4 : predict(4) attendu 27.0  obtenu " + yp);
        if (yp != null && Math.abs(yp - 27.0) < 1e-6) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 5 : predict(liste)
        ArrayList<Double> xtest = new ArrayList<>(Arrays.asList(0.0, 4.0));
        ArrayList<Double> ytest = r1.predict(xtest);
        System.out.println("Test 5 : predict([0,4]) attendu [3.0, 27.0]  obtenu " + ytest);
        if (ytest != null && ytest.size() == 2
                && Math.abs(ytest.get(0) - 3.0) < 1e-6
                && Math.abs(ytest.get(1) - 27.0) < 1e-6) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 6 : predict(null) -> null
        Double yp2 = r1.predict((Double) null);
        System.out.println("Test 6 : predict(null) attendu null  obtenu " + yp2);
        if (yp2 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan RegressionPoly2 : " + ok + " OK / " + (ok + ko));
    }
}
