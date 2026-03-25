package tests.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import exceptions.InvalidFileFormat;
import model.DataFrame;
import model.Fish;
import traitements.MeanValue;

public class IntegrationMeanValueTest {

    @Test
    void meanValueCompletesMissingRatesBySpecies() throws InvalidFileFormat {
        DataFrame df = new DataFrame();
        df.readcsv("tests/resources/integration_mean.csv");

        MeanValue mean = new MeanValue();
        mean.complete(df.getData());

        ArrayList<Fish> data = df.getData();
        Fish salmonMissing = data.get(2);
        Fish troutMissing = data.get(4);

        assertNotNull(salmonMissing.getInfestationRate());
        assertNotNull(troutMissing.getInfestationRate());
        assertEquals(0.30, salmonMissing.getInfestationRate(), 1e-9);
        assertEquals(0.30, troutMissing.getInfestationRate(), 1e-9);
    }
}