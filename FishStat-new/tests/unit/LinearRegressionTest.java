package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.Fish;
import traitements.LinearRegression;

public class LinearRegressionTest {

    @Test
    void predictsMissingRateFromSize() {
        ArrayList<Fish> data = new ArrayList<>();
        data.add(fish(10.0, 0.20));
        data.add(fish(20.0, 0.40));
        data.add(fish(15.0, null));

        LinearRegression regression = new LinearRegression();
        regression.complete(data);

        assertEquals(0.30, data.get(2).getInfestationRate(), 1e-9);
    }

    @Test
    void doesNothingWhenRegressionImpossible() {
        ArrayList<Fish> data = new ArrayList<>();
        data.add(fish(10.0, 0.20));
        data.add(fish(10.0, 0.30));
        data.add(fish(10.0, null));

        LinearRegression regression = new LinearRegression();
        regression.complete(data);

        assertNull(data.get(2).getInfestationRate());
    }

    private Fish fish(Double size, Double rate) {
        Fish f = new Fish("s", null, null, null);
        f.setSize(size);
        f.setInfestationRate(rate);
        return f;
    }
}