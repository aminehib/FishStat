package tests.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import exceptions.InvalidFileFormat;
import model.DataFrame;
import model.Fish;
import traitements.LinearRegression;

public class IntegrationLinearRegressionTest {

    @Test
    void linearRegressionCompletesMissingRatesFromSize() throws InvalidFileFormat {
        DataFrame df = new DataFrame();
        df.readcsv("tests/resources/integration_regression.csv");

        LinearRegression regression = new LinearRegression();
        regression.complete(df.getData());

        ArrayList<Fish> data = df.getData();
        Fish missing = data.get(2);

        assertNotNull(missing.getInfestationRate());
        assertEquals(0.25, missing.getInfestationRate(), 1e-9);
    }
}