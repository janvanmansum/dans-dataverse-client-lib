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

import nl.knaw.dans.lib.dataverse.DataverseResponse;
import nl.knaw.dans.lib.dataverse.ExampleBase;
import nl.knaw.dans.lib.dataverse.model.dataset.FileList;
import nl.knaw.dans.lib.dataverse.model.file.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DatasetAddFile extends ExampleBase {

    private static final Logger log = LoggerFactory.getLogger(DatasetAddFile.class);

    public static void main(String[] args) throws Exception {
        String persistentId = args[0];
        Path file = Paths.get(args[1]);
        FileMeta fileMeta = new FileMeta();
        fileMeta.setLabel("Alternative_label");
        fileMeta.setRestricted(true);
        DataverseResponse<FileList> r = client.dataset(persistentId).addFile(file, fileMeta);
        log.info("Response message: {}", r.getEnvelopeAsJson().toPrettyString());
        log.info("File: {}", r.getData().getFiles().get(0));
    }
}
