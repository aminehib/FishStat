package unitaires;

import java.util.ArrayList;
import java.util.Arrays;
import tools.Coords;

public class CoordsUnit {

    public static void main(String[] args) {
        System.out.println("===== Test Coords =====");
        int ok = 0, ko = 0;

        // Test 1 : getX, getY
        Coords c1 = new Coords(3.0, 4.0);
        System.out.println("Test 1 : getX attendu 3.0  obtenu " + c1.getX());
        if (Math.abs(c1.getX() - 3.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("Test 2 : getY attendu 4.0  obtenu " + c1.getY());
        if (Math.abs(c1.getY() - 4.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 3 : distance (0,0) - (3,4) = 5
        Coords origine = new Coords(0.0, 0.0);
        Coords p = new Coords(3.0, 4.0);
        Double d = Coords.distance(origine, p);
        System.out.println("Test 3 : distance((0,0),(3,4)) attendu 5.0  obtenu " + d);
        if (Math.abs(d - 5.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 4 : distance d'un point avec lui-meme = 0
        Double d2 = Coords.distance(p, p);
        System.out.println("Test 4 : distance(p,p) attendu 0.0  obtenu " + d2);
        if (Math.abs(d2) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 5 : distance avec null -> Double.MAX_VALUE
        Double d3 = Coords.distance(null, p);
        System.out.println("Test 5 : distance(null, p) attendu MAX_VALUE  obtenu " + d3);
        if (d3 == Double.MAX_VALUE) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 6 : init_Coords
        ArrayList<Double> X = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0));
        ArrayList<Double> Y = new ArrayList<>(Arrays.asList(4.0, 5.0, 6.0));
        Coords[] tab = Coords.init_Coords(X, Y);
        System.out.println("Test 6 : init_Coords longueur attendu 3  obtenu " + (tab == null ? "null" : tab.length));
        if (tab != null && tab.length == 3) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 7 : init_Coords valeur 1ere coord
        if (tab != null) {
            System.out.println("Test 7 : tab[0] = (1,4) -> getX " + tab[0].getX() + " getY " + tab[0].getY());
            if (Math.abs(tab[0].getX() - 1.0) < 1e-9 && Math.abs(tab[0].getY() - 4.0) < 1e-9) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }
        }

        // Test 8 : init_Coords tailles differentes -> null
        ArrayList<Double> X2 = new ArrayList<>(Arrays.asList(1.0, 2.0));
        ArrayList<Double> Y2 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0));
        Coords[] tab2 = Coords.init_Coords(X2, Y2);
        System.out.println("Test 8 : init_Coords tailles differentes attendu null  obtenu " + (tab2 == null ? "null" : "non-null"));
        if (tab2 == null) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        // Test 9 : null en parametre du constructeur -> MAX_VALUE
        Coords c9 = new Coords(null, null);
        System.out.println("Test 9 : Coords(null,null).getX attendu MAX_VALUE  obtenu " + c9.getX());
        if (c9.getX() == Double.MAX_VALUE) { System.out.println("  [OK]"); ok++; } else { System.out.println("  [KO]"); ko++; }

        System.out.println("\nBilan Coords : " + ok + " OK / " + (ok + ko));
    }
}
