package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import files.Files;

public class FilesTest {

    @TestPopulation1
    void getExtensionReturnsCsvForLowercase() {
        assertEquals("csv", Files.getExtension("data.csv"));
    }

    @TestPopulation1
    void getExtensionReturnsEmptyWhenMissing() {
        assertEquals("", Files.getExtension("data"));
    }

    @TestPopulation1
    void getExtensionReturnsEmptyWhenTrailingDot() {
        assertEquals("", Files.getExtension("data."));
    }
}
