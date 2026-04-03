package tests.integration;

import java.util.ArrayList;

import model.Fish;

public final class TestCsvParser {

    private TestCsvParser() {}

    public static ArrayList<Fish> parseLines(ArrayList<String> lines) {
        ArrayList<Fish> poissons = new ArrayList<>();
        for (String line : lines) {
            String[] data = line.split(",", -1);
            Fish fish = new Fish(null, null, null, null);
            fish.setSpecies(data[0].equals("") ? null : data[0]);
            fish.setLength(parseDouble(data[1]));
            fish.setWeight(parseDouble(data[2]));
            fish.setSize(parseDouble(data[3]));
            fish.setInfestationRate(parseDouble(data[4]));
            String[] contenu = data[5].split(";;");
            for (String cont : contenu) {
                if (!cont.equals("")) fish.addContent(cont);
            }
            poissons.add(fish);
        }
        return poissons;
    }

    private static Double parseDouble(String value) {
        if (value == null || value.equals("")) return null;
        return Double.parseDouble(value);
    }
}
