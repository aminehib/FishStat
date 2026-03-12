package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import exceptions.InvalidFileFormat;
import model.DataFrame;
import model.Fish;

public class DataFrameReadCsvTest {

    @Test
    void readCsvParsesValidFile() throws InvalidFileFormat {
        DataFrame df = new DataFrame();
        df.readcsv("tests/resources/valid.csv");

        ArrayList<Fish> data = df.getData();
        assertEquals(2, data.size());

        Fish first = data.get(0);
        assertEquals("maquereau", first.getSpecies());
        assertEquals(65.2, first.getLength());
        assertEquals(3.8, first.getWeight());
        assertEquals(70.0, first.getSize());
        assertEquals(0.12, first.getInfestationRate());
        assertEquals(2, first.getContent().size());
        assertEquals("larvae", first.getContent().get(0));
        assertEquals("algae", first.getContent().get(1));

        Fish second = data.get(1);
        assertEquals("sole", second.getSpecies());
        assertNull(second.getLength());
        assertEquals(1.2, second.getWeight());
        assertEquals(55.0, second.getSize());
        assertNull(second.getInfestationRate());
        assertEquals(1, second.getContent().size());
        assertEquals("parasite", second.getContent().get(0));
    }

    @Test
    void readCsvRejectsInvalidExtension() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/upper_ext.CSV"));
    }

    @Test
    void readCsvRejectsInvalidColumnCount() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/invalid_columns.csv"));
    }

    @Test
    void readCsvRejectsInvalidNumber() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/invalid_number.csv"));
    }

    @Test
    void readCsvRejectsMissingFile() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/missing.csv"));
    }

    @Test
    void readCsvRejectsEmptyLines() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/empty_lines.csv"));
    }
}
