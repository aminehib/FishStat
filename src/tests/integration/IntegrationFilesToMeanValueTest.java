package tests.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import files.Files;
import model.DataFrame;
import model.Fish;
import traitements.MeanValue;

public class IntegrationFilesToMeanValueTest {

    @Test
    void csvToFilesToFishToDataFrameToMeanValue() throws IOException {
        ArrayList<String> lines = Files.getFile("tests/resources/integration_files_to_mean.csv");
        ArrayList<Fish> poissons = TestCsvParser.parseLines(lines);

        DataFrame df = new DataFrame(poissons);
        MeanValue mean = new MeanValue();
        mean.complete(df.getData());

        Fish missing = df.getData().get(1);
        assertNotNull(missing.getInfestationRate());
        assertEquals(0.18, missing.getInfestationRate(), 1e-9);
    }

}
