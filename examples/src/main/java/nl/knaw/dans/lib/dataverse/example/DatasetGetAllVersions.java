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
package nl.knaw.dans.lib.dataverse.example;

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetVersion;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class DatasetGetAllVersions extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        DataverseResponse<List<DatasetVersion>> r = client.dataset(persistentId).getAllVersions();
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());

        if (r.getData().size() > 0) {
            DatasetVersion firstVersion = r.getData().get(0);
            log.info("First Version Create Time: {}", firstVersion.getCreateTime());
            log.info("First Version State: {}", firstVersion.getVersionState());
            for (FileMeta fm : firstVersion.getFiles()) {
                log.info("File Label: {}", fm.getLabel());
                log.info("File UNF (if present): {}", fm.getDataFile().getUnf());
            }
        }
    }
}
