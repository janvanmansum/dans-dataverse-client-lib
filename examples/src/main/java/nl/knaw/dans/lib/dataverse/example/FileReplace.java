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

import javax.swing.text.html.Option;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileReplace extends ExampleBase {

    private static final Logger log = LoggerFactory.getLogger(FileReplace.class);

    public static void main(String[] args) throws Exception {
        int databaseId = Integer.parseInt(args[0]);
        Path newFile = Paths.get(args[1]);

        var meta = new FileMeta();
        meta.setLabel("New_label");
        var metaStr = mapper.writeValueAsString(meta);

        DataverseResponse<FileList> r = client.file(databaseId).replaceFileItem(Optional.of(newFile.toFile()), Optional.of(metaStr));
        log.info("Response message: {}", r.getEnvelopeAsString());
    }
}
