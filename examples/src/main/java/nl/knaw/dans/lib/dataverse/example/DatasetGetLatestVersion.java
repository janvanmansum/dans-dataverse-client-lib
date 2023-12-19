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
import nl.knaw.dans.lib.dataverse.model.dataset.DatasetLatestVersion;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class DatasetGetLatestVersion extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        DataverseResponse<DatasetLatestVersion> r = client.dataset(persistentId).getLatestVersion();
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());

        DatasetLatestVersion datasetLatestVersion = r.getData();
        log.info("Publication Date: {}", datasetLatestVersion.getPublicationDate());
        log.info("Publisher: {}", datasetLatestVersion.getPublisher());
        if (datasetLatestVersion.getLatestVersion().getVersionState().equals("RELEASED")) {
            log.info("Version: {}.{}", datasetLatestVersion.getLatestVersion().getVersionNumber(), datasetLatestVersion.getLatestVersion().getVersionMinorNumber());
        } else {
            log.info("DRAFT version");
        }

        for (FileMeta fm : datasetLatestVersion.getLatestVersion().getFiles()) {
            log.info("File Label: {}", fm.getLabel());
            log.info("File UNF (if present): {}", fm.getDataFile().getUnf());
        }
    }
}
