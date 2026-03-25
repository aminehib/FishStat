package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.Fish;
import traitements.MeanValue;

public class TraitementCleanTest {

    @Test
    void cleanSetsOutliersToNull() {
        ArrayList<Fish> data = new ArrayList<>();
        data.add(fish(2.0));
        data.add(fish(2.0));
        data.add(fish(2.0));
        data.add(fish(2.0));
        data.add(fish(2.0));
        data.add(fish(2.0));
        data.add(fish(2.0));
        data.add(fish(50.0));

        MeanValue traitement = new MeanValue();
        traitement.clean(data);

        assertEquals(2.0, data.get(0).getInfestationRate(), 1e-9);
        assertNull(data.get(7).getInfestationRate());
    }

    private Fish fish(Double rate) {
        Fish f = new Fish("s", null, null, null);
        f.setInfestationRate(rate);
        return f;
    }
}