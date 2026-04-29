package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.DataFrame;
import model.Fish;
import traitements.MeanValue;
import traitements.MeanValueCompletion;

public class MeanValueTest {

    @TestPopulation1
    void completesMissingRatesPerSpecies() {
        ArrayList<Fish> data = new ArrayList<>();
        data.add(fish("a", 0.10));
        data.add(fish("a", null));
        data.add(fish("b", null));

        MeanValueCompletion mean = new MeanValueCompletion();
        mean.complete(new DataFrame<>(data));

        assertEquals(0.10, data.get(1).getInfestationRate(), 1e-9);
        assertNull(data.get(2).getInfestationRate());
    }

    private Fish fish(String species, Double rate) {
        Fish f = new Fish(species, null, null, null);
        f.setInfestationRate(rate);
        return f;
    }
}
