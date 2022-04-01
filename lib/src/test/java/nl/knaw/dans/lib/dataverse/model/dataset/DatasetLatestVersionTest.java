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

class DatasetLatestVersionTest extends ModelDatasetMapperFixture {
    private static final Class<DatasetLatestVersion> classUnderTest = DatasetLatestVersion.class;

    @Test
    public void canDeserialize() throws Exception {
        DatasetLatestVersion latestVersion = mapper.readValue(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, latestVersion.getClass());
        assertEquals("file://10.5072/DAR/LDIU4X", latestVersion.getStorageIdentifier());
        assertEquals("DANS Archaeology Data Station (dev)", latestVersion.getPublisher());
        assertEquals("2022-03-24", latestVersion.getPublicationDate());
        assertEquals(1, latestVersion.getLatestVersion().getFiles().size());
    }

    @Test
    public void roundTrip() throws Exception {
        DatasetLatestVersion latestVersion = roundTrip(getTestJsonFileFor(classUnderTest), classUnderTest);
        assertEquals(classUnderTest, latestVersion.getClass());
    }
}