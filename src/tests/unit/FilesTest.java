package tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import files.Files;

public class FilesTest {

    @Test
    void getExtensionReturnsCsvForLowercase() {
        assertEquals("csv", Files.getExtension("data.csv"));
    }

    @Test
    void getExtensionReturnsEmptyWhenMissing() {
        assertEquals("", Files.getExtension("data"));
    }

    @Test
    void getExtensionReturnsEmptyWhenTrailingDot() {
        assertEquals("", Files.getExtension("data."));
    }
}
