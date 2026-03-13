package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.Fish;
import traitements.MeanValue;

public class MeanValueTest {

    @Test
    void completesMissingRatesPerSpecies() {
        ArrayList<Fish> data = new ArrayList<>();
        data.add(fish("a", 0.10));
        data.add(fish("a", null));
        data.add(fish("b", null));

        MeanValue mean = new MeanValue();
        mean.complete(data);

        assertEquals(0.10, data.get(1).getInfestationRate(), 1e-9);
        assertNull(data.get(2).getInfestationRate());
    }

    private Fish fish(String species, Double rate) {
        Fish f = new Fish(species, null, null, null);
        f.setInfestationRate(rate);
        return f;
    }
}
