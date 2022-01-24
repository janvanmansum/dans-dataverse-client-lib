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
package nl.knaw.dans.lib.dataverse.model.dataset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DatasetVersionTest extends ModelDatasetMapperFixture {
    private static final Class<DatasetVersion> classUnderTest = DatasetVersion.class;

    @Test
    public void canDeserialize() throws Throwable {
        DatasetVersion dsv = mapper.readValue(getTestJsonFileFor(classUnderTest, "no-files"), classUnderTest);
        assertEquals(classUnderTest, dsv.getClass());
        assertEquals(7, dsv.getId());
        assertEquals("file://10.5072/FK2/U6AEZM", dsv.getStorageIdentifier());
        assertEquals("CC0", dsv.getLicense().getName());
        MetadataField title = dsv.getMetadataBlocks()
            .get("citation")
            .getFields()
            .stream()
            .filter(f -> "title".equals(f.getTypeName()) )
            .findFirst().orElseThrow(() -> fail("No title found"));
        assertEquals("Test", ((PrimitiveSingleValueField) title).getValue());
    }

    @Test
    public void roundTrip() throws Exception {
        DatasetVersion f = roundTrip(getTestJsonFileFor(classUnderTest, "no-files"), DatasetVersion.class);
        assertEquals(classUnderTest, f.getClass());
    }
}