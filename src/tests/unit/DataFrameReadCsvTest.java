package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import exceptions.InvalidFileFormat;
import files.CsvReader;
import model.DataFrame;
import model.Fish;
import model.* ;

public class DataFrameReadCsvTest {

    @TestPopulation1
    void readCsvParsesValidFile() throws InvalidFileFormat {
        DataFrame df = new DataFrame();
        df.setData(CsvReader.readCsv("tests/resources/valid.csv", null, null, 0, null, null));

        ArrayList<Data> data = df.getData();
        assertEquals(2, data.size());

        Data first = data.get(0);
        assertEquals("maquereau", first.getSpecies());
        assertEquals(65.2, first.getLength());
        assertEquals(3.8, first.getWeight());
        assertEquals(70.0, first.getSize());
        assertEquals(0.12, first.getInfestationRate());
        assertEquals(2, first.getContent().size());
        assertEquals("larvae", first.getContent().toArray()[0]);
        assertEquals("algae", first.getContent().toArray()[1]);

        Data second = data.get(1);
        assertEquals("sole", second.getSpecies());
        assertNull(second.getLength());
        assertEquals(1.2, second.getWeight());
        assertEquals(55.0, second.getSize());
        assertNull(second.getInfestationRate());
        assertEquals(1, second.getContent().size());
        assertEquals("parasite", second.getContent().toArray()[0]);
    }

    @TestPopulation1
    void readCsvRejectsInvalidExtension() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/upper_ext.CSV"));
    }

    @TestPopulation1
    void readCsvRejectsInvalidColumnCount() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/invalid_columns.csv"));
    }

    @TestPopulation1
    void readCsvRejectsInvalidNumber() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/invalid_number.csv"));
    }

    @TestPopulation1
    void readCsvRejectsMissingFile() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/missing.csv"));
    }

    @TestPopulation1
    void readCsvRejectsEmptyLines() {
        DataFrame df = new DataFrame();
        assertThrows(InvalidFileFormat.class, () -> df.readcsv("tests/resources/empty_lines.csv"));
    }
}
