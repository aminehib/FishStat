package unitaires;

import java.util.ArrayList;
import java.util.Arrays;
import tools.LinearRegression;

public class LinearRegressionUnit {

    public static void main(String[] args) {
        System.out.println("===== Test LinearRegression =====");
        int ok = 0, ko = 0;

        // Test 1 : y = 3x + 1 (parfaitement lineaire)
        ArrayList<Double> x1 = new ArrayList<>(Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0));
        ArrayList<Double> y1 = new ArrayList<>(Arrays.asList(1.0, 4.0, 7.0, 10.0, 13.0, 16.0));
        LinearRegression r1 = new LinearRegression(x1, y1);
        System.out.println("Test 1 : coeff a (y=3x+1) attendu 3.0  obtenu " + r1.getCoeff());
        if (r1.getCoeff() != null && Math.abs(r1.getCoeff() - 3.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("Test 2 : intercept b attendu 1.0  obtenu " + r1.getIntercept());
        if (r1.getIntercept() != null && Math.abs(r1.getIntercept() - 1.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 3 : predict(10) = 31
        Double yp = r1.predict(10.0);
        System.out.println("Test 3 : predict(10) attendu 31.0  obtenu " + yp);
        if (yp != null && Math.abs(yp - 31.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 4 : predict(liste)
        ArrayList<Double> xtest = new ArrayList<>(Arrays.asList(0.0, 10.0));
        ArrayList<Double> ytest = r1.predict(xtest);
        System.out.println("Test 4 : predict([0,10]) attendu [1.0, 31.0]  obtenu " + ytest);
        if (ytest != null && ytest.size() == 2
                && Math.abs(ytest.get(0) - 1.0) < 1e-9
                && Math.abs(ytest.get(1) - 31.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 5 : pente negative y = -2x + 5
        ArrayList<Double> x2 = new ArrayList<>(Arrays.asList(0.0, 1.0, 2.0, 3.0));
        ArrayList<Double> y2 = new ArrayList<>(Arrays.asList(5.0, 3.0, 1.0, -1.0));
        LinearRegression r2 = new LinearRegression(x2, y2);
        System.out.println("Test 5 : coeff a (y=-2x+5) attendu -2.0  obtenu " + r2.getCoeff());
        if (r2.getCoeff() != null && Math.abs(r2.getCoeff() - (-2.0)) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 6 : ignore les null
        ArrayList<Double> x3 = new ArrayList<>(Arrays.asList(0.0, 1.0, null, 3.0, 4.0));
        ArrayList<Double> y3 = new ArrayList<>(Arrays.asList(1.0, 4.0, null, 10.0, 13.0));
        LinearRegression r3 = new LinearRegression(x3, y3);
        System.out.println("Test 6 : coeff avec null  attendu 3.0  obtenu " + r3.getCoeff());
        if (r3.getCoeff() != null && Math.abs(r3.getCoeff() - 3.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 7 : moins de 2 points -> coeffs null
        ArrayList<Double> x4 = new ArrayList<>(Arrays.asList(1.0));
        ArrayList<Double> y4 = new ArrayList<>(Arrays.asList(2.0));
        LinearRegression r4 = new LinearRegression(x4, y4);
        System.out.println("Test 7 : 1 seul point -> a null  obtenu " + r4.getCoeff());
        if (r4.getCoeff() == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 8 : predict avec modele non-entrainable
        Double yp4 = r4.predict(5.0);
        System.out.println("Test 8 : predict avec modele null  attendu null  obtenu " + yp4);
        if (yp4 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan LinearRegression : " + ok + " OK / " + (ok + ko));
    }
}
