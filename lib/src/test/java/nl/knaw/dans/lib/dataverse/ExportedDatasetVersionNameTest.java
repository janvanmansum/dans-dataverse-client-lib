/*
 * Copyright (C) 2021 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
