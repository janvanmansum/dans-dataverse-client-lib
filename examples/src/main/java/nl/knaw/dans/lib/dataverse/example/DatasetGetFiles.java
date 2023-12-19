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
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class DatasetGetFiles extends ExampleBase {

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        String version = args[1];
        DataverseResponse<List<FileMeta>> r = client.dataset(persistentId).getFiles(version);
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());

        if (r.getData().isEmpty()) {
            log.info("No files found");
        }
        else {
            FileMeta firstFile = r.getData().get(0);
            log.info("First File Label: {}", firstFile.getLabel());
            log.info("First File DirectoryLabel: {}", r.getData().get(0).getDirectoryLabel());
            log.info("First File DataFile StorageIdentifier: {}", r.getData().get(0).getDataFile().getStorageIdentifier());
            log.info("First File DataFile Checksum Value: {}", r.getData().get(0).getDataFile().getChecksum().getValue());
        }
    }
}
