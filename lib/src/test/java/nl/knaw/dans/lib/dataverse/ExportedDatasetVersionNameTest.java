package nl.knaw.dans.lib.dataverse;

import nl.knaw.dans.lib.dataverse.model.ExportedDatasetVersionName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExportedDatasetVersionNameTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/correct-dve-names.csv", numLinesToSkip = 1)
    public void correctNamesShouldBeParsed(String input, String expSpaceName, String expSchema, int expMajor, int expMinor, String expExtension) {
        System.out.println(input);
        ExportedDatasetVersionName n = new ExportedDatasetVersionName(input);
        assertEquals(expSpaceName, n.getSpaceName());
        assertEquals(expSchema, n.getSchema());
        assertEquals(expMajor, n.getMajorVersion());
        assertEquals(expMinor, n.getMinorVersion());
        assertEquals(expExtension, n.getExtension());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "", // empty
        "illegal:charsv1.2.zip",
        "not-a-valid-extensionv1.2.pdf"
    })
    public void incorrectNamesShouldBeRejected(String input) {
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            new ExportedDatasetVersionName(input);
        });
        assertEquals(String.format("Name does not conform to dataset version export naming pattern: %s", input), thrown.getMessage());
    }

}
