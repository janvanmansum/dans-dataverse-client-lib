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

import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.dataverse.Dataverse;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class DataverseResponseTest extends MapperFixture {
    private static final Class<?> classUnderTest = DataverseResponse.class;

    protected DataverseResponseTest() {
        super("");
    }

    @Test
    public void simpleDataverseViewResponseCanBeDeserialized() throws Exception {
        DataverseResponse<Dataverse> r =
            new DataverseResponse<>(FileUtils.readFileToString(getTestJsonFileFor(classUnderTest), StandardCharsets.UTF_8),
                mapper, Dataverse.class);
        Assertions.assertEquals("root", r.getData().getAlias());
        Assertions.assertEquals("Dataverse Name", r.getData().getName());
    }

    @Test
    public void datasetVersionWithMD5InFilesResponseCanBeDeserialized() throws Exception {
        DataverseResponse<DatasetVersion> r =
                new DataverseResponse<>(FileUtils.readFileToString(getTestJsonFileFor(classUnderTest, 
                        "DatasetVersionWithMD5InFiles"), StandardCharsets.UTF_8),
                        mapper, DatasetVersion.class);
        // The "md5" property should be ignored preventing failure during object mapping. 
        // We can not check md5 is not there, it is simply not a property of the DataFile POJO. 
        // Some simple assertion anyway. 
        Assertions.assertEquals(2, r.getData().getDatasetId());
        Assertions.assertEquals(3, r.getData().getVersionNumber());
        Assertions.assertEquals(5, r.getData().getFiles().size());
    }

    @Test
    public void datasetVersionWithAddedPropsUpTov5_14ResponseCanBeDeserialized() throws Exception {
        DataverseResponse<DatasetVersion> r =
            new DataverseResponse<>(FileUtils.readFileToString(getTestJsonFileFor(classUnderTest,
                "DatasetVersionWithAddedPropsUpTov5_14"), StandardCharsets.UTF_8),
                mapper, DatasetVersion.class);
        // check some standard properties
        Assertions.assertEquals(2, r.getData().getDatasetId());
        Assertions.assertEquals(3, r.getData().getVersionNumber());
        Assertions.assertEquals(5, r.getData().getFiles().size());
        // check added properties
        Assertions.assertEquals(new URI("https://licensebuttons.net/l/zero/1.0/88x31.png"), r.getData().getLicense().getIconUri());
        Assertions.assertEquals("2023-01-10", r.getData().getPublicationDate());
        Assertions.assertEquals("2023-01-10", r.getData().getCitationDate());
        Assertions.assertEquals("hdl:10695/test-12345", r.getData().getAlternativePersistentId());
        Assertions.assertEquals("Documentation", r.getData().getFiles().get(4).getDataFile().getCategories().get(0));
    }

}
