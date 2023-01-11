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
    public void DatasetVersionWithMD5InFilesResponseCanBeDeserialized() throws Exception {
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

//    @Test
//    public void nestedTypeParametersCanBeDeserialized() throws Exception {
//        DataverseResponse<List<DatasetVersion>> r = new DataverseResponse<>(FileUtils.readFileToString(new File("src/test/resources/dataverse-response/test2.json"), StandardCharsets.UTF_8),
//            mapper, List.class, DatasetVersion.class);
//        // TODO: REPLACE WITH ASSERTION
//        System.out.println(r.getData().get(0).getCreateTime());
//    }

}
